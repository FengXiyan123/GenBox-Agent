package com.genbox.ai.manage.dto;

import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class KnowledgeTopicSaveDto {

    private String id;

    private String topicCode;

    private String topicName;

    private String scopeCode;

    private String description;

    private String aliases;

    private String examples;

    private String answerShape;

    private String executionPreference;

    private String sortOrder;

    private String operatorId;
}
