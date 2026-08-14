package com.genbox.ai.chatagent.rag.retrieve.channel;

import com.genbox.ai.chatagent.rag.config.ChatRagProperties;
import com.genbox.ai.chatagent.rag.model.ConversationExecutionPlan;
import com.genbox.ai.chatagent.rag.service.DocumentRetrieveRequestFactory;
import com.genbox.ai.manage.service.DocumentKnowledgeService;
import com.genbox.enums.RetrievalChannelEnum;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 向量检索通道。
 */
@Component
public class VectorRetrievalChannel implements RetrievalChannel {

    private final DocumentKnowledgeService documentKnowledgeService;
    private final ChatRagProperties properties;
    private final DocumentRetrieveRequestFactory documentRetrieveRequestFactory;

    public VectorRetrievalChannel(DocumentKnowledgeService documentKnowledgeService,
                                  ChatRagProperties properties,
                                  DocumentRetrieveRequestFactory documentRetrieveRequestFactory) {
        this.documentKnowledgeService = documentKnowledgeService;
        this.properties = properties;
        this.documentRetrieveRequestFactory = documentRetrieveRequestFactory;
    }

    @Override
    public String channelName() {
        return RetrievalChannelEnum.VECTOR.getName();
    }

    @Override
    public boolean supports(ConversationExecutionPlan plan) {

        return plan.getSelectedDocumentId() != null;
    }

    @Override
    public RetrievalChannelResult retrieve(String subQuestion, ConversationExecutionPlan plan) {

        List<Document> documentList = documentKnowledgeService.vectorSearch(
            documentRetrieveRequestFactory.build(subQuestion, plan, properties.getVectorTopK())
        );
        return new RetrievalChannelResult(
            channelName(), documentList
        );
    }
}
