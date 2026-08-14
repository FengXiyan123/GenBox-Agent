package com.genbox.ai.manage.service;

/**
 * 服务层。
 */
public interface DocumentTaskLogService {

    void saveLog(Long taskId,
                 Long documentId,
                 Integer stageType,
                 Integer eventType,
                 Integer logLevel,
                 Integer operatorType,
                 Long operatorId,
                 String content,
                 Object detail);
}
