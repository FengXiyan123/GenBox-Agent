package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话编号项锚点。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationItemAnchor {

    private Integer itemIndex;

    private String itemText;

    private Long structureNodeId;

    private String canonicalPath;

    public boolean isEmpty() {
        return itemIndex == null
            && (itemText == null || itemText.isBlank())
            && structureNodeId == null
            && (canonicalPath == null || canonicalPath.isBlank());
    }
}
