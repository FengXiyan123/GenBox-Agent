package com.genbox.ai.chatagent.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.genbox.database.data.BaseTableData;

import java.util.Date;

/**
 * 数据实体。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("genbox_agent_chat_exchange_trace_stage")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentChatExchangeTraceStage extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField("dialogue_code")
    private String conversationId;

    @TableField("exchange_id")
    private Long exchangeId;

    @TableField("trace_id")
    private String traceId;

    @TableField("stage_code")
    private String stageCode;

    @TableField("stage_name")
    private String stageName;

    @TableField("stage_order")
    private Integer stageOrder;

    @TableField("stage_level")
    private Integer stageLevel;

    @TableField("parent_stage_id")
    private Long parentStageId;

    @TableField("execution_mode")
    private String executionMode;

    @TableField("stage_state")
    private Integer stageState;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;

    @TableField("duration_ms")
    private Long durationMs;

    @TableField("summary_text")
    private String summaryText;

    @TableField("error_message")
    private String errorMessage;

    @TableField("snapshot_json")
    private String snapshotJson;
}
