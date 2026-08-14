package com.genbox.ai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentListItemVo {

    private Long documentId;

    private String documentName;

    private String originalFileName;

    private Integer fileType;

    private String fileTypeName;

    private Long fileSize;

    private Integer charCount;

    private Integer tokenCount;

    private Integer parseStatus;

    private String parseStatusName;

    private Integer strategyStatus;

    private String strategyStatusName;

    private Integer indexStatus;

    private String indexStatusName;

    private String parseErrorMsg;

    private String knowledgeScopeCode;

    private String knowledgeScopeName;

    private String businessCategory;

    private String documentTags;

    private Long currentPlanId;

    private Long lastIndexTaskId;

    private Long latestTaskId;

    private Integer latestTaskType;

    private String latestTaskTypeName;

    private Integer latestTaskStatus;

    private String latestTaskStatusName;

    private Date createTime;

    private Date editTime;
}
