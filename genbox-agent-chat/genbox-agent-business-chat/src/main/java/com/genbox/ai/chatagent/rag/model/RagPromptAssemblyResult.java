package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * RAG Prompt 装配结果。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RagPromptAssemblyResult {

    private String systemPrompt;

    private String userPrompt;

    private int totalBudget;

    private int perSubQuestionBudget;

    private int renderedReferenceCount;

    private int omittedReferenceCount;

    private List<String> renderedReferenceDetails;

    private List<String> omittedReferenceDetails;
}
