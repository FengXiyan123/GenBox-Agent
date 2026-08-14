package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 编排阶段使用的结构化历史要点。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPlanningContext {

    private String conversationGoal;

    @Builder.Default
    private List<String> stableFacts = new ArrayList<>();

    @Builder.Default
    private List<String> pendingQuestions = new ArrayList<>();

    @Builder.Default
    private List<String> retrievalHints = new ArrayList<>();

    @Builder.Default
    private List<String> queryContextHints = new ArrayList<>();
}
