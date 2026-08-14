package com.genbox.ai.manage.model.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 模型对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicRouteCandidate {

    private String topicCode;

    private String topicName;

    private String scopeCode;

    private BigDecimal score;

    private String reason;
}
