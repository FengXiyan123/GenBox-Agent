package com.genbox.ai.manage.service.keyword;

import com.genbox.ai.manage.data.GenBoxAgentDocumentChunk;
import com.genbox.ai.manage.model.DocumentRetrieveRequest;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * 服务层。
 */
public interface DocumentKeywordSearchGateway {

    void indexChunks(List<GenBoxAgentDocumentChunk> chunkList);

    List<Document> search(DocumentRetrieveRequest request);

    void deleteByDocumentId(Long documentId);
}
