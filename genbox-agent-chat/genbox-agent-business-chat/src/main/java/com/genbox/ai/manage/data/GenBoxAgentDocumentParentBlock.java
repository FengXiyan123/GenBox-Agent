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
@TableName("genbox_agent_document_parent_block")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentDocumentParentBlock extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private Long documentId;

    private Long taskId;

    private Long planId;

    private Integer parentNo;

    private Integer sourceType;

    private String sectionPath;

    private Long structureNodeId;

    private Integer structureNodeType;

    private String canonicalPath;

    private Integer itemIndex;

    private String parentText;

    private Integer charCount;

    private Integer tokenCount;

    private Integer childCount;

    private Integer startChunkNo;

    private Integer endChunkNo;
}
