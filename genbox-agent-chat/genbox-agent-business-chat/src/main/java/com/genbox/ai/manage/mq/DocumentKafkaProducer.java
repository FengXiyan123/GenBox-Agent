package com.genbox.ai.manage.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import com.genbox.ai.manage.config.DocumentManageProperties;
import com.genbox.ai.manage.mq.message.DocumentIndexBuildMessage;
import com.genbox.ai.manage.mq.message.DocumentParseRouteMessage;
import com.genbox.core.SpringUtil;
import com.genbox.enums.DocumentManageCode;
import com.genbox.exception.GenBoxAgentFrameException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息组件。
 */
@AllArgsConstructor
@Component
public class DocumentKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final DocumentManageProperties properties;

    public void sendParseRoute(DocumentParseRouteMessage message) {

        send(SpringUtil.getPrefixDistinctionName() + "-" + properties.getKafka().getParseTopic(), String.valueOf(message.getDocumentId()), message);
    }

    public void sendIndexBuild(DocumentIndexBuildMessage message) {

        send(SpringUtil.getPrefixDistinctionName() + "-" + properties.getKafka().getIndexTopic(), String.valueOf(message.getDocumentId()), message);
    }

    private void send(String topic, String key, Object message) {
        try {

            String payload = objectMapper.writeValueAsString(message);

            kafkaTemplate.send(topic, key, payload).get();
        } catch (Exception exception) {
            throw new GenBoxAgentFrameException(DocumentManageCode.KAFKA_SEND_FAILED.getCode(),
                "Kafka 消息发送失败: " + exception.getMessage(), exception);
        }
    }
}
