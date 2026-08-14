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
@TableName("genbox_agent_chat_memory_summary")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentChatMemorySummary extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField("dialogue_code")
    private String conversationId;

    @TableField("covered_exchange_id")
    private Long coveredExchangeId;

    @TableField("covered_exchange_count")
    private Integer coveredExchangeCount;

    @TableField("compression_count")
    private Integer compressionCount;

    @TableField("summary_version")
    private Integer summaryVersion;

    @TableField("summary_text")
    private String summaryText;

    @TableField("summary_json")
    private String summaryJson;

    @TableField("last_source_edit_time")
    private Date lastSourceEditTime;
}
