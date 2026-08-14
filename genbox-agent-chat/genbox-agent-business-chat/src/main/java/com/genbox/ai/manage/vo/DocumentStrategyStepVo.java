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
public class DocumentStrategyStepVo {

    private Integer stepNo;

    private String pipelineType;

    private String pipelineTypeName;

    private Integer strategyType;

    private String strategyName;

    private Integer strategyRole;

    private String strategyRoleName;

    private Integer sourceType;

    private String sourceTypeName;

    private Integer executeStatus;

    private String executeStatusName;

    private String recommendReason;
}
