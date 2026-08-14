package com.genbox.ai.chatagent.service;

import com.genbox.ai.chatagent.model.ChannelExecutionView;
import com.genbox.ai.chatagent.model.RetrievalResultView;

import java.util.List;

/**
 * 服务层。
 */
public interface RetrievalObserveStore {

    void batchSaveResults(String conversationId, long exchangeId, List<RetrievalResultView> results);

    void batchSaveChannelExecutions(String conversationId, long exchangeId, List<ChannelExecutionView> executions);

    List<RetrievalResultView> listResults(String conversationId, long exchangeId);

    List<ChannelExecutionView> listChannelExecutions(String conversationId, long exchangeId);

    void deleteByConversation(String conversationId);
}
