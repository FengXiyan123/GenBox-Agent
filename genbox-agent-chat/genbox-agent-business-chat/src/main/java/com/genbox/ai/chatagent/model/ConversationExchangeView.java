package com.genbox.ai.chatagent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.genbox.ai.chatagent.model.debug.ChatDebugTrace;
import com.genbox.enums.ChatTurnStatus;

import java.util.Date;
import java.util.List;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationExchangeView {

    private long exchangeId;
    private String question;
    private String answer;
    private List<String> thinkingSteps;
    private List<SearchReference> references;
    private List<String> recommendations;
    private List<String> usedTools;
    private ChatDebugTrace debugTrace;
    private ChatTurnStatus status;
    private String errorMessage;
    private Long firstResponseTimeMs;
    private Long totalResponseTimeMs;
    private Date createTime;
    private Date editTime;
}
