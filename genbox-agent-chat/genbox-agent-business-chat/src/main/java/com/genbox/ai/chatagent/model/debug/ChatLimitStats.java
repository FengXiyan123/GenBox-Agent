package com.genbox.ai.chatagent.model.debug;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单轮对话的调用限制统计。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatLimitStats {

    private Integer modelCallsUsed;

    private Integer modelCallsRunLimit;

    private Integer modelCallsThreadLimit;

    private Integer toolCallsUsed;

    private Integer toolCallsRunLimit;

    private Integer toolCallsThreadLimit;

    private boolean limitTriggered;

    private String limitReason;
}
