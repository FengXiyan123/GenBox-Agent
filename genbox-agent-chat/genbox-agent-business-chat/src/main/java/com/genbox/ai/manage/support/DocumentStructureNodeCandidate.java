package com.genbox.ai.manage.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支撑组件。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentStructureNodeCandidate {

    private Integer nodeNo;

    private Integer nodeType;

    private Integer parentNodeNo;

    private Integer prevSiblingNodeNo;

    private Integer nextSiblingNodeNo;

    private Integer depth;

    private String nodeCode;

    private String title;

    private String anchorText;

    private String canonicalPath;

    private String sectionPath;

    private String contentText;

    private Integer itemIndex;
}
