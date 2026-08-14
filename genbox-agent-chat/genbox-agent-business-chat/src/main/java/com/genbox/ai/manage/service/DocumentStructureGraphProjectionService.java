package com.genbox.ai.manage.service;

import java.util.List;

/**
 * 服务层。
 */
public interface DocumentStructureGraphProjectionService {

    boolean enabled();

    void projectToGraph(Long documentId, Long parseTaskId);

    void deleteByDocumentId(Long documentId);

    default List<String> statusNotes() {
        return List.of();
    }
}
