package com.genbox.ai.manage.data;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("genbox_agent_document_profile")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentDocumentProfile extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private Long documentId;

    private Integer profileVersion;

    private String documentSummary;

    private String documentType;

    private String coreTopics;

    private String exampleQuestions;

    private Integer graphFriendly;

    private Integer supportsGraphOutline;

    private Integer supportsItemLookup;

    private Integer supportsGraphAssist;

    private String profileSource;

    private Integer profileStatus;

    private String errorMsg;
}
