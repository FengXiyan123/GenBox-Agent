package com.genbox.ai.manage.model.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 章节及其直接子章节。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphSectionWithChildren {

    private GraphSection section;

    @Builder.Default
    private List<GraphSection> children = new ArrayList<>();
}
