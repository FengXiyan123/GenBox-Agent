package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 问题改写结果。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RagRewriteResult {

    private String rewrittenQuestion;

    private List<String> subQuestions;

    private String rawModelOutput;

    public RagRewriteResult(String rewrittenQuestion, List<String> subQuestions) {
        this.rewrittenQuestion = rewrittenQuestion;
        this.subQuestions = subQuestions;
    }
}
