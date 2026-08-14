package com.genbox.ai.manage.model.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 模型对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeRouteDecision {

    private List<ScopeRouteCandidate> scopes = new ArrayList<>();

    private List<TopicRouteCandidate> topics = new ArrayList<>();

    private List<DocumentRouteCandidate> documents = new ArrayList<>();

    private BigDecimal confidence = BigDecimal.ZERO;

    private String routeStatus = "SUCCESS";

    private String reason = "";

    public DocumentRouteCandidate topDocument() {
        return documents == null || documents.isEmpty() ? null : documents.get(0);
    }
}
