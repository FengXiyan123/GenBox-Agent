package com.genbox.ai.manage.service;

import com.genbox.ai.manage.data.GenBoxAgentDocumentStructureNode;
import com.genbox.ai.manage.support.DocumentStructureNodeCandidate;

import java.util.List;
import java.util.Map;

/**
 * 服务层。
 */
public interface DocumentStructureNodeService {

    List<GenBoxAgentDocumentStructureNode> replaceDocumentNodes(Long documentId,
                                                               Long parseTaskId,
                                                               List<DocumentStructureNodeCandidate> candidates);

    List<GenBoxAgentDocumentStructureNode> listDocumentNodes(Long documentId, Long parseTaskId);

    Map<Long, GenBoxAgentDocumentStructureNode> nodeMap(Long documentId, Long parseTaskId);

    void deleteByDocumentId(Long documentId);
}
