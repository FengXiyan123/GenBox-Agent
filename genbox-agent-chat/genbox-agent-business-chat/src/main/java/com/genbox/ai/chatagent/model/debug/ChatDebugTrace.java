package com.genbox.ai.chatagent.model.debug;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.genbox.ai.chatagent.rag.model.DocumentNavigationDecision;
import com.genbox.enums.ChatQueryMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 单轮对话调试轨迹。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDebugTrace {

    private String executionMode;

    private ChatQueryMode chatMode;

    private String originalQuestion;

    private String rewriteQuestion;

    @Builder.Default
    private List<String> rewriteSubQuestions = new ArrayList<>();

    @JsonAlias("rewrittenQuestion")
    private String retrievalQuestion;

    private String agentQuestion;

    private DocumentNavigationDecision navigationDecision;

    private String historySummary;

    private String longTermSummary;

    private String recentHistoryTranscript;

    private String answerRecentTranscript;

    private String answerHistoryContext;

    private boolean answerHistoryFollowUpQuestion;

    private boolean historyCompressionApplied;

    private Long historyCoveredExchangeId;

    private Integer historyCoveredExchangeCount;

    private Integer historyCompressionCount;

    private String currentDateText;

    private boolean requiresFreshSearch;

    private boolean requiresCurrentDateAnchoring;

    @JsonAlias("subQuestions")
    @Builder.Default
    private List<String> retrievalSubQuestions = new ArrayList<>();

    private Long selectedDocumentId;

    private Long selectedTaskId;

    @Builder.Default
    private List<String> retrievalNotes = new ArrayList<>();

    @Builder.Default
    private List<String> usedChannels = new ArrayList<>();

    @Builder.Default
    private List<ChatToolTrace> toolTraces = new ArrayList<>();

    @Builder.Default
    private List<ChatModelUsageTrace> modelUsageTraces = new ArrayList<>();

    private ChatLimitStats limitStats;

    private String ragSystemPrompt;

    private String ragUserPrompt;

    private String noEvidenceReply;
}
