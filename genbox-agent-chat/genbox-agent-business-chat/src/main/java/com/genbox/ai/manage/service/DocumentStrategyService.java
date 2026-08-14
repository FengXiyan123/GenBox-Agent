package com.genbox.ai.manage.service;

import com.genbox.ai.manage.data.GenBoxAgentDocument;
import com.genbox.ai.manage.data.GenBoxAgentDocumentStrategyPlan;
import com.genbox.ai.manage.data.GenBoxAgentDocumentStrategyStep;
import com.genbox.ai.manage.support.DocumentAnalysisResult;
import com.genbox.ai.manage.support.DocumentStrategyPlanDraft;
import com.genbox.ai.manage.support.ParentBlockCandidate;

import java.util.List;

/**
 * 服务层。
 */
public interface DocumentStrategyService {

    DocumentStrategyPlanDraft recommendStrategy(GenBoxAgentDocument document, DocumentAnalysisResult analysisResult);

    List<GenBoxAgentDocumentStrategyStep> normalizeSteps(GenBoxAgentDocumentStrategyPlan basePlan,
                                                        List<GenBoxAgentDocumentStrategyStep> baseSteps,
                                                        List<Integer> requestParentStrategyTypes,
                                                        List<Integer> requestChildStrategyTypes,
                                                        Long documentId);

    List<ParentBlockCandidate> buildParentBlocks(GenBoxAgentDocument document,
                                                 GenBoxAgentDocumentStrategyPlan plan,
                                                 List<GenBoxAgentDocumentStrategyStep> steps,
                                                 String parsedText);
}
