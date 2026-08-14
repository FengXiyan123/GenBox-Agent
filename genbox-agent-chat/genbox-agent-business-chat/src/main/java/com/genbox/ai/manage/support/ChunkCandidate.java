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
public class ChunkCandidate {

    private String sectionPath;

    private Long structureNodeId;

    private Integer structureNodeType;

    private String canonicalPath;

    private Integer itemIndex;

    private String text;

    private Integer sourceType;

    public ChunkCandidate(String sectionPath, String text, Integer sourceType) {
        this(sectionPath, null, null, "", null, text, sourceType);
    }
}
