package com.genbox.database.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class BasePageDto {

    @Schema(name ="pageNumber", type ="Long", description ="页码",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer pageNumber;

    @Schema(name ="pageSize", type ="Long", description ="页大小",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer pageSize;
}
