package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回答阶段最终使用的历史上下文。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerHistoryContext {

    private String renderedText;

    private String structuredContext;

    private String recentContext;

    private boolean followUpQuestion;

    private Integer totalBudget;

    private Integer recentBudget;

    private Integer structuredBudget;

    public boolean isEmpty() {
        return renderedText == null || renderedText.isBlank();
    }
}
