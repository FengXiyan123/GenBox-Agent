package com.genbox.ai.manage.model.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 目标编号项及其所在章节上下文。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphItemWithContext {

    private GraphSection section;

    private GraphItem item;

    @Builder.Default
    private List<GraphItem> siblingItems = new ArrayList<>();
}
