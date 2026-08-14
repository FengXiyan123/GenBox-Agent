package com.genbox.ai.chatagent.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单个子问题在某个检索通道上的执行痕迹。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubQuestionChannelTrace {

    private String channelName;

    private int recalledCount;

    private int acceptedCount;
}
