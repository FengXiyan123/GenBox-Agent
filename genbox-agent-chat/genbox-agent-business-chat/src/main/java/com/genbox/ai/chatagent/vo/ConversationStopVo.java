package com.genbox.ai.chatagent.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationStopVo {

    private String conversationId;

    private boolean stopped;

    private String message;
}
