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
@TableName("genbox_agent_document_structure_node")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentDocumentStructureNode extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private Long documentId;

    private Long parseTaskId;

    private Integer nodeNo;

    private Integer nodeType;

    private Long parentNodeId;

    private Long prevSiblingNodeId;

    private Long nextSiblingNodeId;

    private Integer depth;

    private String nodeCode;

    private String title;

    private String anchorText;

    private String canonicalPath;

    private String sectionPath;

    private String contentText;

    private Integer itemIndex;
}
