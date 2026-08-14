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

/**
 * 数据实体。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("genbox_agent_chat_exchange")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentChatExchange extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField("dialogue_code")
    private String conversationId;

    @TableField("user_prompt")
    private String question;

    @TableField("reply_content")
    private String answer;

    @TableField("reasoning_note_list")
    private String thinkingSteps;

    @TableField("source_snapshot_list")
    private String referenceList;

    @TableField("followup_suggestion_list")
    private String recommendationList;

    @TableField("tool_trace_list")
    private String usedToolList;

    @TableField("debug_trace_json")
    private String debugTraceJson;

    @TableField("exchange_state")
    private Integer turnStatus;

    @TableField("finish_note")
    private String errorMessage;

    @TableField("first_token_latency_ms")
    private Long firstResponseTimeMs;

    @TableField("total_latency_ms")
    private Long totalResponseTimeMs;
}
