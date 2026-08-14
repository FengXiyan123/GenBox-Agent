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
public class DocumentParentBlockItemVo {

    private Long parentBlockId;

    private Integer parentBlockNo;

    private String sectionPath;

    private Integer sourceType;

    private String sourceTypeName;

    private Integer charCount;

    private Integer tokenCount;

    private Integer childCount;

    private Integer startChunkNo;

    private Integer endChunkNo;

    private String parentText;
}
