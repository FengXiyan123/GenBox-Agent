package com.genbox.ai.manage.model.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 章节及其父章节、相邻兄弟章节。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphSectionWithSiblings {

    private GraphSection section;

    private GraphSection parent;

    private GraphSection previousSibling;

    private GraphSection nextSibling;
}
