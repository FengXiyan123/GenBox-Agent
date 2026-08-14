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
public class KnowledgeRouteTracePageVo {

    private String pageNo;

    private String pageSize;

    private String totalSize;

    private String totalPages;

    private List<KnowledgeRouteTraceItemVo> records;
}
