package com.genbox.ai.manage.dto;

import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class DocumentPageQueryDto {

    private Integer pageNo;

    private Integer pageSize;

    private String keyword;
}
