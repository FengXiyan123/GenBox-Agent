package com.genbox.ai.manage.model.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识路由索引记录。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeRouteIndexRecord {

    private String routeId;

    private String entityType;

    private String entityCode;

    private Long documentId;

    private String scopeCode;

    private String scopeName;

    private String topicCode;

    private String topicName;

    private String documentName;

    private String businessCategory;

    private String displayName;

    private String descriptionText;

    private String aliasesText;

    private String examplesText;

    private String summaryText;

    private String routeText;

    @Builder.Default
    private List<String> entityTerms = new ArrayList<>();

    @Builder.Default
    private List<String> tags = new ArrayList<>();
}
