package com.genbox.ai.manage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class DocumentDeleteDto {

    @NotBlank(message = "文档id不能为空")
    private String documentId;
}
