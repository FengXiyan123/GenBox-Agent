package com.genbox.ai.manage.dto;

import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class KnowledgeScopeSaveDto {

    private String id;

    private String scopeCode;

    private String scopeName;

    private String parentScopeCode;

    private String description;

    private String aliases;

    private String examples;

    private String sortOrder;

    private String operatorId;
}
