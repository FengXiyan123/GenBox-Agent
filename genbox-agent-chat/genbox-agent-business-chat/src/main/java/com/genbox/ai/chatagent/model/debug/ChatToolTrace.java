package com.genbox.ai.chatagent.model.debug;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单次工具调用观测快照。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatToolTrace {

    private String toolName;

    private String status;

    private String inputSummary;

    private String effectiveInput;

    private String outputSummary;

    private String errorMessage;

    private Integer referenceCount;

    private String topic;

    private Long durationMs;
}
