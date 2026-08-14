package com.genbox.ai.chatagent.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 数据传输对象。
 */
@Data
public class ConversationIdentityDto {

    @NotBlank(message = "conversationId 不能为空")
    private String conversationId;
}
