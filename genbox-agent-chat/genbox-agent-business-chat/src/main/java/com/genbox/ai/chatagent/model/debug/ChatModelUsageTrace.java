package com.genbox.ai.chatagent.model.debug;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单次模型调用的使用量轨迹。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatModelUsageTrace {

    private String stageName;

    private String provider;

    private String model;

    private Integer promptTokens;

    private Integer completionTokens;

    private Integer totalTokens;

    private Double estimatedCost;

    private Long durationMs;

    private String status;
}
