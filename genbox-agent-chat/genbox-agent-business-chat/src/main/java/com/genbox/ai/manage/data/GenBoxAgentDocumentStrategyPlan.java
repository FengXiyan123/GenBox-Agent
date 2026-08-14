package com.genbox.ai.manage.data;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("genbox_agent_document_strategy_plan")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentDocumentStrategyPlan extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private Long documentId;

    private Integer planVersion;

    private Integer planSource;

    private Integer planStatus;

    private Integer strategyCount;

    private String strategySnapshot;

    private String recommendReason;

    private String adjustNote;

    private Long confirmUserId;

    private Date confirmTime;
}
