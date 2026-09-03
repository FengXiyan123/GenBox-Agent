# GenBox-Agent 只读展示部署设计

## 目标

在现有 2 核 4G 阿里云 ECS 上，以严格只读展示模式部署 GenBox-Agent，保留现有会话、知识路由、RAG 向量、文档对象和索引数据，不允许线上发起新对话或修改知识资产。

## 已确认约束

- ECS：x86_64、2 vCPU、约 3.4 GiB 可用内存、40 GiB 系统盘。
- 宿主机已安装 Docker、Docker Compose、Java 21 和宝塔 Nginx。
- Nginx 已占用宿主机 80 端口，宝塔面板使用 8888 端口。
- 前端构建产物使用 Vite `dist`，后端使用 Spring Boot jar，后端端口为 9082。
- 用户不允许访客发起新对话；当前代码的预览拦截器继续拦截 `/api/chat/stream`。
- 线上只展示已有会话、知识路由、路由追踪、文档和检索结果。
- 不在生产环境执行上传、删除、切块、建索引、画像生成、主题关联、会话重置等写操作。
- 不覆盖宝塔现有 Nginx 配置，不删除本机或服务器上的 Docker Volume。

## 方案

### 运行拓扑

宿主机 Nginx 提供前端静态资源，并把 `/api`、`/manage`、`/admin/auth` 转发到宿主机 Java 进程的 9082 端口。数据库和基础设施通过 Docker Compose 管理，所有数据库端口只绑定到 127.0.0.1，不能从公网访问。

```text
Nginx 80/443
├── vue/dist 静态文件
└── proxy → Java Spring Boot :9082

Docker Compose 核心服务
├── MySQL 8.0
├── PostgreSQL + pgvector 17
├── Redis 6.0.8
├── MinIO RELEASE.2024-01-01T16-36-33Z
└── Elasticsearch 8.5.2

可选服务
└── Neo4j 5.22-community
```

Kafka、Zookeeper 和 Kibana 默认不启动。严格只读环境通过 `spring.kafka.listener.auto-startup=false` 禁止异步文档消费者启动，并关闭 Kafka topic 自动创建；Kibana 不影响业务页面。Neo4j 保留为可选 profile，内存足够时再启用。

### Compose 配置

生产 Compose 文件放在 `deploy/docker-compose.readonly.yml`，仅包含部署编排，不写入密码或 API Key。服务使用固定镜像版本、显式命名 Volume、健康检查和有限内存。`.env.production.example` 只保存变量名和示例说明，真实 `.env` 只放在服务器 `/opt/genbox-agent/runtime/.env`，权限为 600。

核心 Volume：

- `genbox-mysql-data` → `/var/lib/mysql`
- `genbox-pgvector-data` → `/var/lib/postgresql/data`
- `genbox-minio-data` → `/data`
- `genbox-es-data` → `/usr/share/elasticsearch/data`
- `genbox-neo4j-data` → `/data`

容器端口只发布给宿主机回环地址；应用在宿主机运行时使用 `127.0.0.1` 访问这些端口。Elasticsearch 还必须配置持久化数据卷，避免当前本地 ES 无 Volume 的问题再次发生。

### 数据恢复

部署使用已经完成的备份，不重新切块或重新构建知识库：

1. MySQL 容器健康后导入 `genbox_agent.sql`。
2. pgvector 容器健康后使用 `pg_restore` 导入 `genbox_agent_pgvector.dump`。
3. MinIO 容器启动后恢复 `minio-data` 文件。
4. Elasticsearch 优先导入已保存的 `es-container.tar` 或使用等价的索引恢复方式；不能接受空 ES。
5. Neo4j profile 启用时，使用 `neo4j.dump` 恢复 `neo4j` 数据库。
6. Redis 创建空实例；不迁移缓存。
7. Kafka/Zookeeper 不恢复旧消息，避免重复执行历史切块任务。

每个阶段恢复后都执行健康检查、文件数量/大小检查和应用只读查询验证。任何校验失败都停止后续恢复，不删除原备份。

### 后端与前端

- 后端 jar 使用 Java 21 构建并部署到 `/opt/genbox-agent/app/genbox-agent-business-chat.jar`。
- 通过 systemd 启动后端，使用 `/opt/genbox-agent/runtime/.env` 注入数据库、模型、MinIO、ES、Neo4j、管理账号和预览模式配置。
- 设置 `GENBOX_AGENT_PREVIEW_MODE_ENABLED=true`。
- 设置 `SPRING_KAFKA_LISTENER_AUTO_STARTUP=false`，并将 `APP_MANAGE_KAFKA_AUTO_CREATE_TOPICS=false`。
- 前端版本目录放在 `/opt/genbox-agent/ui/releases/<release>/`，`current` 符号链接指向已验收版本。
- Nginx 使用 Vue history fallback；仅代理业务 API，不把 `/actuator` 暴露到公网。

### 资源策略

当前 ECS 先按低资源尝试：

- Java 后端最大堆约 1～1.2 GB。
- Elasticsearch 堆约 512 MB。
- MySQL 和 PostgreSQL 分别限制在约 512～768 MB。
- Redis、MinIO 和可选 Neo4j 使用较低内存上限。
- Kibana、Kafka、Zookeeper 默认不启动。

如果出现容器 OOM、Java `OutOfMemoryError`、频繁交换或服务不可用，再升级 ECS；升级不是本次部署的前置条件。

### 镜像来源

Compose 使用本机已确认的版本：

```text
mysql:8.0
pgvector/pgvector:pg17
redis:6.0.8
minio/minio:RELEASE.2024-01-01T16-36-33Z
elasticsearch:8.5.2
neo4j:5.22-community
```

若 ECS 访问 Docker Hub 继续超时，优先使用阿里云镜像仓库；仍不可用时，只对失败镜像使用本地 `docker save`、`scp`、服务器 `docker load`。不迁移无关镜像。

## 验收标准

- 备份校验文件全部通过，原始备份仍保留。
- MySQL 中历史会话、知识范围、主题、文档元数据数量与本地一致。
- pgvector 可检索已有文档向量。
- MinIO 可读取已有原始文档和解析文本。
- Elasticsearch 路由、关键词、导航索引非空且查询正常。
- Neo4j 启用时结构数据可查询；未启用时应用使用 MySQL 回退能力。
- `/chat`、`/admin/login`、知识路由、路由追踪、对话观测页面可访问。
- 新对话、文档上传、删除、切块、建索引、知识主题修改等请求均被预览模式拒绝。
- 80/443 对公网开放，22 和 8888 仅允许管理 IP；数据库端口不开放公网。
- Nginx 配置通过 `nginx -t`，后端通过 `/actuator/health`（仅本机）检查。
- 保留上一版 jar、前端 release 和恢复前数据，支持通过符号链接和 systemd 回滚。

## 不在本次范围内

- 不允许访客新建对话，因此不改造聊天持久化策略。
- 不新增业务功能或前端页面。
- 不重新设计知识路由算法。
- 不删除本地 Docker 容器、Volume 或备份文件。
- 不把数据库、ES、MinIO、Neo4j 端口暴露到互联网。
