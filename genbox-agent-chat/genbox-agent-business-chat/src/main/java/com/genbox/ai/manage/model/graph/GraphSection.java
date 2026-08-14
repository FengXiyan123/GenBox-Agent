package com.genbox.ai.manage.model.graph;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结构图里的章节节点视图。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphSection {

    private Long nodeId;

    private Long documentId;

    private Long parseTaskId;

    private Integer nodeNo;

    private Integer depth;

    private Long parentNodeId;

    private Long prevSiblingNodeId;

    private Long nextSiblingNodeId;

    private String nodeCode;

    private String title;

    private String anchorText;

    private String sectionPath;

    private String canonicalPath;

    private String contentText;

    public String displayTitle() {
        if (StrUtil.isNotBlank(sectionPath)) {
            return sectionPath.trim();
        }
        if (StrUtil.isNotBlank(nodeCode) && StrUtil.isNotBlank(title)) {
            return (nodeCode + " " + title).trim();
        }
        return StrUtil.blankToDefault(title, "");
    }
}
