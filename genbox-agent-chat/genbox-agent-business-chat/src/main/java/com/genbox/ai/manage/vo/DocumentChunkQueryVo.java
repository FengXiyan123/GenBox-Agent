package com.genbox.ai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChunkQueryVo {

    private Long documentId;

    private Long taskId;

    private Long planId;

    private Integer pageNo;

    private Integer pageSize;

    private Long total;

    private List<DocumentChunkItemVo> records;
}
