package com.genbox.ai.manage.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 支撑组件。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentBlockCandidate {

    private String sectionPath;

    private Long structureNodeId;

    private Integer structureNodeType;

    private String canonicalPath;

    private Integer itemIndex;

    private String text;

    private Integer sourceType;

    private List<ChunkCandidate> childChunks = new ArrayList<>();

    public ParentBlockCandidate(String sectionPath,
                                String text,
                                Integer sourceType,
                                List<ChunkCandidate> childChunks) {
        this(sectionPath, null, null, "", null, text, sourceType, childChunks);
    }
}
