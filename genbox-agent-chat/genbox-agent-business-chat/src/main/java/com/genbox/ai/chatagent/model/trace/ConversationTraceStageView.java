package com.genbox.ai.chatagent.model.trace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationTraceStageView {

    private long stageId;

    private String traceId;

    private String stageCode;

    private String stageName;

    private Integer stageOrder;

    private Integer stageLevel;

    private Long parentStageId;

    private String executionMode;

    private String stageState;

    private Instant startTime;

    private Instant endTime;

    private Long durationMs;

    private String summaryText;

    private String errorMessage;

    private Map<String, Object> snapshot;
}
