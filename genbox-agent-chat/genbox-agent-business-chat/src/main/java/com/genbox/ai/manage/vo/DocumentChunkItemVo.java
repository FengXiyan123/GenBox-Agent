package com.genbox.ai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChunkItemVo {

    private Long chunkId;

    private Long parentBlockId;

    private Integer parentBlockNo;

    private Integer parentChildCount;

    private Integer parentStartChunkNo;

    private Integer parentEndChunkNo;

    private Integer chunkNo;

    private String sectionPath;

    private Integer sourceType;

    private String sourceTypeName;

    private Integer charCount;

    private Integer tokenCount;

    private Integer vectorStatus;

    private String vectorStatusName;

    private String chunkText;
}
