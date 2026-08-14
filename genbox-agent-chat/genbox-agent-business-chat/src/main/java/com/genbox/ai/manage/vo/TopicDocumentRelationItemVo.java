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
public class TopicDocumentRelationItemVo {

    private String topicCode;

    private String documentId;

    private String documentName;

    private String knowledgeScopeCode;

    private String knowledgeScopeName;

    private String businessCategory;

    private String documentTags;

    private String relationScore;

    private String relationSource;

    private String reason;
}
