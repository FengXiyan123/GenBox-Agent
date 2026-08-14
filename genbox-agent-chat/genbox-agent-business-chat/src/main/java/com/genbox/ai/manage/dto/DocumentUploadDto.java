package com.genbox.ai.manage.dto;

import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class DocumentUploadDto {

    private String documentName;

    private String operatorId;

    private String knowledgeScopeCode;

    private String knowledgeScopeName;

    private String businessCategory;

    private String documentTags;
}
