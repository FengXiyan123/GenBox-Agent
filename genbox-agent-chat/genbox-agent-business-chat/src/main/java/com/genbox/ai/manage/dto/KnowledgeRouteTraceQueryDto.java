package com.genbox.ai.manage.dto;

import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class KnowledgeRouteTraceQueryDto {

    private String conversationId;

    private String mode;

    private String routeStatus;

    private String pageNo;

    private String pageSize;
}
