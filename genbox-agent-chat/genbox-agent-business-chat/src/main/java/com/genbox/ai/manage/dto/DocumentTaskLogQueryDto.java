package com.genbox.ai.manage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class DocumentTaskLogQueryDto {

    @NotNull(message = "任务id不能为空")
    private Long taskId;

    private Integer pageNo;

    private Integer pageSize;
}
