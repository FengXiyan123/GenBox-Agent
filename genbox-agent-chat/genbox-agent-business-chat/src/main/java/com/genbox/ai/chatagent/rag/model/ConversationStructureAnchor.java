package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话结构锚点。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationStructureAnchor {

    private String rootSectionCode;

    private String rootSectionTitle;

    private String targetSectionHint;

    private Long structureNodeId;

    private String canonicalPath;

    private String scopeMode;

    public boolean isEmpty() {
        return (rootSectionCode == null || rootSectionCode.isBlank())
            && (rootSectionTitle == null || rootSectionTitle.isBlank())
            && (targetSectionHint == null || targetSectionHint.isBlank())
            && structureNodeId == null
            && (canonicalPath == null || canonicalPath.isBlank());
    }
}
