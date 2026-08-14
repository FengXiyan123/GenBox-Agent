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
public class DocumentPageQueryVo {

    private Integer pageNo;

    private Integer pageSize;

    private Long total;

    private List<DocumentListItemVo> records;
}
