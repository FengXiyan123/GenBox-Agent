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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据实体。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("genbox_agent_chat_channel_execution")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentChatChannelExecution extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField("dialogue_code")
    private String conversationId;

    @TableField("exchange_id")
    private Long exchangeId;

    @TableField("trace_id")
    private String traceId;

    @TableField("sub_question_index")
    private Integer subQuestionIndex;

    @TableField("sub_question")
    private String subQuestion;

    @TableField("channel_type")
    private String channelType;

    @TableField("execution_state")
    private Integer executionState;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;

    @TableField("duration_ms")
    private Long durationMs;

    @TableField("recalled_count")
    private Integer recalledCount;

    @TableField("accepted_count")
    private Integer acceptedCount;

    @TableField("final_selected_count")
    private Integer finalSelectedCount;

    @TableField("avg_score")
    private BigDecimal avgScore;

    @TableField("max_score")
    private BigDecimal maxScore;

    @TableField("min_score")
    private BigDecimal minScore;

    @TableField("config_snapshot")
    private String configSnapshot;

    @TableField("error_message")
    private String errorMessage;
}
