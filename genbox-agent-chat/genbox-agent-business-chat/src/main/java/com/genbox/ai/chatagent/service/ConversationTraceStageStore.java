package com.genbox.ai.chatagent.service;

import com.genbox.ai.chatagent.model.trace.ConversationTraceStageCode;
import com.genbox.ai.chatagent.model.trace.ConversationTraceStageState;
import com.genbox.ai.chatagent.model.trace.ConversationTraceStageView;

import java.util.List;

/**
 * 服务层。
 */
public interface ConversationTraceStageStore {

    long startStage(String conversationId,
                    long exchangeId,
                    String traceId,
                    ConversationTraceStageCode stageCode,
                    int stageLevel,
                    Long parentStageId,
                    String executionMode,
                    String summaryText,
                    Object snapshot);

    void finishStage(long stageId,
                     ConversationTraceStageState stageState,
                     String summaryText,
                     String errorMessage,
                     Object snapshot,
                     long durationMs);

    List<ConversationTraceStageView> listStageViews(String conversationId, long exchangeId);

    void deleteStages(String conversationId);
}
