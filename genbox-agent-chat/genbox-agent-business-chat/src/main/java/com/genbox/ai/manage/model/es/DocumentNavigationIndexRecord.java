package com.genbox.ai.manage.model.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文档导航索引记录。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentNavigationIndexRecord {

    private Long nodeId;

    private Long documentId;

    private Long parseTaskId;

    private String nodeType;

    private String nodeCode;

    private Integer nodeNo;

    private Integer depth;

    private Long parentNodeId;

    private String title;

    private String anchorText;

    private String sectionPath;

    private String canonicalPath;

    private String contentText;

    private Integer itemIndex;
}
