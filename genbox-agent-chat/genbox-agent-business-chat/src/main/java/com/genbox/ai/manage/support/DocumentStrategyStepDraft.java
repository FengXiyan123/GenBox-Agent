package com.genbox.ai.manage.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支撑组件。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentStrategyStepDraft {

    private String pipelineType;

    private Integer strategyType;

    private Integer strategyRole;

    private Integer sourceType;

    private String recommendReason;
}
