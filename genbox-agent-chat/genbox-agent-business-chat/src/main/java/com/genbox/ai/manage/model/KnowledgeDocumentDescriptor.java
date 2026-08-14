package com.genbox.ai.manage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可参与知识检索的文档描述对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeDocumentDescriptor {

    private Long documentId;

    private String documentName;

    private Long lastIndexTaskId;

    private String knowledgeScopeCode;

    private String knowledgeScopeName;

    private String businessCategory;

    private String documentTags;
}
