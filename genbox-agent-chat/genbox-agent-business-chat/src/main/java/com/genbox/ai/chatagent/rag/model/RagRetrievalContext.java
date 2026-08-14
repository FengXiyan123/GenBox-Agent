package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.genbox.ai.chatagent.model.SearchReference;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识检索上下文。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RagRetrievalContext {

    private String retrievalQuestion;

    private List<SubQuestionEvidence> subQuestionEvidenceList = new ArrayList<>();

    private List<String> retrievalNotes = new ArrayList<>();

    private List<String> usedChannels = new ArrayList<>();

    public boolean isEmpty() {
        return subQuestionEvidenceList == null
            || subQuestionEvidenceList.stream().allMatch(item -> item.getReferences() == null || item.getReferences().isEmpty());
    }

    public List<SearchReference> flattenReferences() {
        if (subQuestionEvidenceList == null || subQuestionEvidenceList.isEmpty()) {
            return List.of();
        }
        List<SearchReference> references = new ArrayList<>();
        for (SubQuestionEvidence item : subQuestionEvidenceList) {
            if (item.getReferences() == null || item.getReferences().isEmpty()) {
                continue;
            }
            references.addAll(item.getReferences());
        }
        return references;
    }
}
