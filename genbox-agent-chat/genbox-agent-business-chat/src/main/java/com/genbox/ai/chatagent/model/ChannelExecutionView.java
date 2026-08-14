package com.genbox.ai.chatagent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelExecutionView {

    private Long id;
    private String traceId;
    private int subQuestionIndex;
    private String subQuestion;
    private String channelType;
    private int executionState;
    private Instant startTime;
    private Instant endTime;
    private Long durationMs;
    private int recalledCount;
    private int acceptedCount;
    private int finalSelectedCount;
    private BigDecimal avgScore;
    private BigDecimal maxScore;
    private BigDecimal minScore;
    private String errorMessage;
    private Instant createTime;
}
