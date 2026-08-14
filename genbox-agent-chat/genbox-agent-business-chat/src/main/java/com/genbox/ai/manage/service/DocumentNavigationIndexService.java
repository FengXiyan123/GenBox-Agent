package com.genbox.ai.manage.service;

import com.genbox.ai.manage.data.GenBoxAgentDocumentStructureNode;

import java.util.List;

/**
 * 服务层。
 */
public interface DocumentNavigationIndexService {

    void reindexDocumentNodes(Long documentId, Long parseTaskId, List<GenBoxAgentDocumentStructureNode> nodes);

    void deleteByDocumentId(Long documentId);

    List<NavigationSectionHit> searchSections(Long documentId,
                                              String topic,
                                              String facet,
                                              String informationNeed,
                                              String question,
                                              int size);

    record NavigationSectionHit(
        Long nodeId,
        String nodeCode,
        String title,
        String sectionPath,
        String canonicalPath,
        double score
    ) {
    }
}
