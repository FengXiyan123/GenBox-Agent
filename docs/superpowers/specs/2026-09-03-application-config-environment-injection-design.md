# Application Configuration Environment Injection Design

## Goal

将 `genbox-agent-business-chat` 的外部服务连接信息与所有凭据从 `application.yaml` 中移除，统一改为必填环境变量；应用在任一必需变量缺失时拒绝启动。

## Scope

本次只调整连接信息和密钥，不修改端口、模型名称、连接池参数、业务开关、限流阈值、提示词、Kafka 主题或索引名称。

## Environment-variable Mapping

| Configuration area | Required environment variables |
| --- | --- |
| Redis | `GENBOX_REDIS_HOST`, `GENBOX_REDIS_PORT` |
| MySQL | `GENBOX_MYSQL_URL`, `GENBOX_MYSQL_USERNAME`, `GENBOX_MYSQL_PASSWORD` |
| Kafka | `GENBOX_KAFKA_BOOTSTRAP_SERVERS` |
| OpenAI chat | `GENBOX_OPENAI_BASE_URL`, `GENBOX_OPENAI_API_KEY` |
| OpenAI embedding | `GENBOX_OPENAI_EMBEDDING_BASE_URL`, `GENBOX_OPENAI_EMBEDDING_API_KEY` |
| Rerank | `GENBOX_RERANK_URL`, `GENBOX_RERANK_API_KEY` |
| Tavily | `GENBOX_TAVILY_BASE_URL`, `GENBOX_TAVILY_API_KEY` |
| MinIO | `GENBOX_MINIO_ENDPOINT`, `GENBOX_MINIO_ACCESS_KEY`, `GENBOX_MINIO_SECRET_KEY` |
| PGVector | `GENBOX_PGVECTOR_HOST`, `GENBOX_PGVECTOR_PORT`, `GENBOX_PGVECTOR_DATABASE`, `GENBOX_PGVECTOR_SCHEMA`, `GENBOX_PGVECTOR_USERNAME`, `GENBOX_PGVECTOR_PASSWORD` |
| Elasticsearch | `GENBOX_ELASTICSEARCH_URIS`, `GENBOX_ELASTICSEARCH_USERNAME`, `GENBOX_ELASTICSEARCH_PASSWORD` |
| Neo4j | `GENBOX_NEO4J_URI`, `GENBOX_NEO4J_USERNAME`, `GENBOX_NEO4J_PASSWORD`, `GENBOX_NEO4J_DATABASE` |
| Admin authentication | `GENBOX_AGENT_ADMIN_USERNAME`, `GENBOX_AGENT_ADMIN_PASSWORD`, `GENBOX_AGENT_ADMIN_TOKEN_SECRET` |

## Configuration Behavior

- Each mapped YAML value uses a placeholder without a default, for example `${GENBOX_MYSQL_URL}`.
- Spring resolves placeholders from the process environment. Missing variables cause configuration binding to fail during startup instead of silently falling back to development credentials.
- The Rerank API key does not fall back to the OpenAI API key; it has its own required environment variable whenever the configuration is loaded.
- The environment-variable template documents variable names and purpose only. It contains no hostnames, account names, passwords, tokens, or API keys.

## Files and Responsibilities

- `src/main/resources/application.yaml`: replace in-file connection values and credential defaults with required placeholders.
- `.env.example`: document all required variables using empty assignments and comments; it is not a source of runtime secrets.
- `src/test/java/.../ApplicationConfigurationTest.java`: load the YAML text and assert that sensitive literal values and default-placeholder syntax are absent from the external-service properties.

## Testing

1. Add a regression test that parses the application YAML as text and verifies that each mapped configuration key references its required environment variable without a `:` default.
2. Run the focused Maven test to demonstrate the test fails before the YAML change and passes after it.
3. Run the backend module test suite and package build with a complete set of temporary environment variables supplied only to the command process.

## Non-goals

- No deployment, container, CI, or secret-manager configuration is created.
- No values are copied from the existing configuration into the template or documentation.
- No frontend files or business logic are changed.
