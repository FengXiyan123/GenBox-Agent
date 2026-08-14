package com.genbox.ai.manage.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息组件。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentIndexBuildMessage {

    private Long documentId;

    private Long taskId;

    private Long planId;
}
