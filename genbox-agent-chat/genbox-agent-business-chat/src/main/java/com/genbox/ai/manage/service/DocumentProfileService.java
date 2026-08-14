package com.genbox.ai.manage.service;

import com.genbox.ai.manage.data.GenBoxAgentDocumentProfile;
import com.genbox.ai.manage.data.GenBoxAgentDocumentStructureNode;
import com.genbox.ai.manage.support.DocumentAnalysisResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 服务层。
 */
public interface DocumentProfileService {

    GenBoxAgentDocumentProfile generateProfile(Long documentId,
                                              DocumentAnalysisResult analysisResult,
                                              List<GenBoxAgentDocumentStructureNode> structureNodes);

    GenBoxAgentDocumentProfile regenerateProfile(Long documentId);

    List<GenBoxAgentDocumentProfile> batchRegenerateProfiles(Collection<Long> documentIds);

    Optional<GenBoxAgentDocumentProfile> getByDocumentId(Long documentId);
}
