package com.genbox.ai.chatagent.model.memory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 长期会话摘要的结构化载体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummaryPayload {

    private String summary;

    private String conversationGoal;

    @Builder.Default
    private List<String> stableFacts = new ArrayList<>();

    @Builder.Default
    private List<String> userPreferences = new ArrayList<>();

    @Builder.Default
    private List<String> resolvedPoints = new ArrayList<>();

    @Builder.Default
    private List<String> pendingQuestions = new ArrayList<>();

    @Builder.Default
    private List<String> retrievalHints = new ArrayList<>();
}
