package com.genbox.ai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTaskLogQueryVo {

    private Long taskId;

    private Long documentId;

    private Integer taskType;

    private String taskTypeName;

    private Integer taskStatus;

    private String taskStatusName;

    private Integer currentStage;

    private String currentStageName;

    private Date startTime;

    private Date finishTime;

    private Long costMillis;

    private String errorCode;

    private String errorMsg;

    private Long total;

    private List<DocumentTaskLogVo> logs;
}
