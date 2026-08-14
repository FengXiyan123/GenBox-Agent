package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 检索阶段真正执行的问题计划。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrievalQuestionPlan {

    private String retrievalQuestion;

    private List<String> subQuestions;
}
