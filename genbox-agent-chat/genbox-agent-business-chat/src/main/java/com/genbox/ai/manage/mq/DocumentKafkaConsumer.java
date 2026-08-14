package com.genbox.ai.manage.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.genbox.ai.manage.mq.message.DocumentIndexBuildMessage;
import com.genbox.ai.manage.mq.message.DocumentParseRouteMessage;
import com.genbox.ai.manage.service.DocumentAsyncProcessService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.genbox.constant.Constant.SPRING_INJECT_PREFIX_DISTINCTION_NAME;

/**
 * 消息组件。
 */
@Slf4j
@Component
public class DocumentKafkaConsumer {

    private final DocumentAsyncProcessService asyncProcessService;

    private final ObjectMapper objectMapper;

    public DocumentKafkaConsumer(DocumentAsyncProcessService asyncProcessService,
                                 ObjectMapper objectMapper) {
        this.asyncProcessService = asyncProcessService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = SPRING_INJECT_PREFIX_DISTINCTION_NAME+"-"+"${app.manage.kafka.parse-topic}", groupId = "${app.manage.kafka.group-id}-parse")
    public void consumeParseRoute(String payload) {
        try {

            DocumentParseRouteMessage message = objectMapper.readValue(payload, DocumentParseRouteMessage.class);

            asyncProcessService.handleParseRoute(message.getDocumentId(), message.getTaskId());
        }
        catch (Exception exception) {

            log.error("消费解析路由消息失败，payload={}", payload, exception);
        }
    }

    @KafkaListener(topics = SPRING_INJECT_PREFIX_DISTINCTION_NAME+"-"+"${app.manage.kafka.index-topic}", groupId = "${app.manage.kafka.group-id}-index")
    public void consumeIndexBuild(String payload) {
        try {

            DocumentIndexBuildMessage message = objectMapper.readValue(payload, DocumentIndexBuildMessage.class);

            asyncProcessService.handleIndexBuild(message.getDocumentId(), message.getTaskId(), message.getPlanId());
        }
        catch (Exception exception) {
            log.error("消费索引构建消息失败，payload={}", payload, exception);
        }
    }
}
