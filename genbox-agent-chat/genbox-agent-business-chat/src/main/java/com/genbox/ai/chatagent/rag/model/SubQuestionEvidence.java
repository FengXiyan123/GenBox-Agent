package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.genbox.ai.chatagent.model.SearchReference;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * 单个子问题的证据容器。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubQuestionEvidence {

    private int subQuestionIndex;

    private String subQuestion;

    private List<Document> documents;

    private List<SearchReference> references;

    private List<SubQuestionChannelTrace> channelTraces;

    private Integer fusedCandidateCount;

    private Integer parentCandidateCount;

    private Integer rerankedCandidateCount;
}
