package com.genbox.ai.manage.dto;

import lombok.Data;

import java.util.List;

/**
 * 数据传输对象。
 */
@Data
public class DocumentProfileBatchRegenerateDto {

    private List<String> documentIds;

    private String operatorId;
}
