package com.genbox.ai.chatagent.support;

/**
 * 支撑组件。
 */
public record StreamEventMetadata(
    String conversationId,
    Long exchangeId
) {
}
