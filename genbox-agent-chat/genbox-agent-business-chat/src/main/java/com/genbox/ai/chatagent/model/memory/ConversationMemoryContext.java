package com.genbox.ai.chatagent.model.memory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编排阶段真正使用的会话记忆上下文。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMemoryContext {

    private String assembledHistory;

    private String longTermSummary;

    private String recentTranscript;

    private String answerRecentTranscript;

    private ConversationSummaryPayload summaryPayload;

    private Long coveredExchangeId;

    private Integer coveredExchangeCount;

    private Integer compressionCount;

    private boolean compressionApplied;
}
