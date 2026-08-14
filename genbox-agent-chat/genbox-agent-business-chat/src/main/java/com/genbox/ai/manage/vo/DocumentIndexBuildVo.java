package com.genbox.ai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentIndexBuildVo {

    private Long documentId;

    private Long taskId;

    private Integer taskType;

    private String taskTypeName;

    private Integer taskStatus;

    private String taskStatusName;

    private Integer indexStatus;

    private String indexStatusName;
}
