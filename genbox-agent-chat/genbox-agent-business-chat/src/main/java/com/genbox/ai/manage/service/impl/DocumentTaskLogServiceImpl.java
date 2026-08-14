package com.genbox.ai.manage.service.impl;

import lombok.AllArgsConstructor;
import com.baidu.fsg.uid.UidGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genbox.ai.manage.data.GenBoxAgentDocumentTaskLog;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentTaskLogMapper;
import com.genbox.ai.manage.service.DocumentTaskLogService;
import com.genbox.enums.BusinessStatus;
import org.springframework.stereotype.Service;

/**
 * 服务实现层。
 */
@AllArgsConstructor
@Service
public class DocumentTaskLogServiceImpl implements DocumentTaskLogService {

    private final GenBoxAgentDocumentTaskLogMapper taskLogMapper;
    private final ObjectMapper objectMapper;
    private final UidGenerator uidGenerator;

    @Override
    public void saveLog(Long taskId,
                        Long documentId,
                        Integer stageType,
                        Integer eventType,
                        Integer logLevel,
                        Integer operatorType,
                        Long operatorId,
                        String content,
                        Object detail) {
        GenBoxAgentDocumentTaskLog log = new GenBoxAgentDocumentTaskLog();
        log.setId(uidGenerator.getUid());
        log.setTaskId(taskId);
        log.setDocumentId(documentId);
        log.setStageType(stageType);
        log.setEventType(eventType);
        log.setLogLevel(logLevel);
        log.setOperatorType(operatorType);
        log.setOperatorId(operatorId);
        log.setContent(content);
        log.setDetailJson(toJson(detail));
        log.setStatus(BusinessStatus.YES.getCode());
        taskLogMapper.insert(log);
    }

    private String toJson(Object detail) {
        if (detail == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(detail);
        }
        catch (JsonProcessingException exception) {
            return String.valueOf(detail);
        }
    }
}
