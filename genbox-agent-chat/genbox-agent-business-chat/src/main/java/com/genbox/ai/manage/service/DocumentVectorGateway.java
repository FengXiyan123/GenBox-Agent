package com.genbox.ai.manage.service;

import com.genbox.ai.manage.data.GenBoxAgentDocumentChunk;

import java.util.List;

/**
 * 服务层。
 */
public interface DocumentVectorGateway {

    void vectorize(List<GenBoxAgentDocumentChunk> chunkList);

    void deleteByDocumentId(Long documentId);
}
