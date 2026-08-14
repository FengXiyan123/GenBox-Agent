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
@TableName("genbox_agent_document")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentDocument extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String documentName;

    private String originalFileName;

    private Integer fileType;

    private String mimeType;

    private Long fileSize;

    private Integer storageType;

    private String bucketName;

    private String objectName;

    private String objectUrl;

    private Integer parseStatus;

    private Integer strategyStatus;

    private Integer indexStatus;

    private Integer charCount;

    private Integer tokenCount;

    private Integer structureLevel;

    private Integer contentQualityLevel;

    private String parseTextPath;

    private String parseErrorMsg;

    private String knowledgeScopeCode;

    private String knowledgeScopeName;

    private String businessCategory;

    private String documentTags;

    private Long currentPlanId;

    private Long lastParseTaskId;

    private Integer structureNodeCount;

    private Long lastIndexTaskId;
}
