package com.genbox.ai.chatagent.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据传输对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {

    @NotBlank(message = "question 不能为空")
    private String question;
    private String conversationId;

    @NotBlank(message = "chatMode 不能为空")
    private String chatMode;

    private String selectedDocumentId;
}
