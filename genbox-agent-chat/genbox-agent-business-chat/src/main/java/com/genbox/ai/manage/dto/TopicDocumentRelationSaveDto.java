package com.genbox.ai.manage.dto;

import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class TopicDocumentRelationSaveDto {

    private String topicCode;

    private String documentId;

    private String relationScore;

    private String relationSource;

    private String reason;

    private String operatorId;
}
