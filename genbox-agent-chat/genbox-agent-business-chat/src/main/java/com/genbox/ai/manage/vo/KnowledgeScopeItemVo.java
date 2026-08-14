package com.genbox.ai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeScopeItemVo {

    private String id;

    private String scopeCode;

    private String scopeName;

    private String parentScopeCode;

    private String description;

    private String aliases;

    private String examples;

    private String sortOrder;
}
