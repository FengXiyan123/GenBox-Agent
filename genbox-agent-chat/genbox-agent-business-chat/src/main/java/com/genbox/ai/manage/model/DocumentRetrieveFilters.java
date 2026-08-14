package com.genbox.ai.manage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档检索过滤提示。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRetrieveFilters {

    @Builder.Default
    private List<String> documentNameHints = new ArrayList<>();

    @Builder.Default
    private List<String> businessCategoryHints = new ArrayList<>();

    @Builder.Default
    private List<String> documentTagHints = new ArrayList<>();

    @Builder.Default
    private List<String> sectionPathHints = new ArrayList<>();

    @Builder.Default
    private List<String> canonicalPathHints = new ArrayList<>();

    @Builder.Default
    private List<Long> structureNodeIdHints = new ArrayList<>();

    @Builder.Default
    private List<Integer> itemIndexHints = new ArrayList<>();

    @Builder.Default
    private List<String> yearHints = new ArrayList<>();

    public boolean isEmpty() {
        return documentNameHints.isEmpty()
            && businessCategoryHints.isEmpty()
            && documentTagHints.isEmpty()
            && sectionPathHints.isEmpty()
            && canonicalPathHints.isEmpty()
            && structureNodeIdHints.isEmpty()
            && itemIndexHints.isEmpty()
            && yearHints.isEmpty();
    }
}
