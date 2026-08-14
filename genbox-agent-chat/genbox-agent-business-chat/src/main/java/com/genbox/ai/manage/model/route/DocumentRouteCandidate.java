package com.genbox.ai.manage.model.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 模型对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRouteCandidate {

    private String documentId;

    private String documentName;

    private String lastIndexTaskId;

    private String knowledgeScopeCode;

    private String knowledgeScopeName;

    private String businessCategory;

    private String documentTags;

    private BigDecimal score;

    private String reason;
}
