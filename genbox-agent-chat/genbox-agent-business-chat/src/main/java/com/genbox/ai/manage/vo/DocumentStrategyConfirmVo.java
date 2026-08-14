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
public class DocumentStrategyConfirmVo {

    private Long documentId;

    private Long planId;

    private Integer planVersion;

    private Integer strategyStatus;

    private String strategyStatusName;

    private Boolean normalized;

    private DocumentStrategyPipelineVo parentPipeline;

    private DocumentStrategyPipelineVo childPipeline;
}
