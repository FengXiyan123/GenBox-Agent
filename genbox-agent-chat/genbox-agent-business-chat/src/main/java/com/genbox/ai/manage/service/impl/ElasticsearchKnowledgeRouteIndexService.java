package com.genbox.ai.manage.service.impl;

import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.genbox.ai.manage.config.DocumentManageProperties;
import com.genbox.ai.manage.data.GenBoxAgentDocument;
import com.genbox.ai.manage.data.GenBoxAgentDocumentProfile;
import com.genbox.ai.manage.data.GenBoxAgentKnowledgeScopeNode;
import com.genbox.ai.manage.data.GenBoxAgentKnowledgeTopicNode;
import com.genbox.ai.manage.data.GenBoxAgentTopicDocumentRelation;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentProfileMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentKnowledgeScopeNodeMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentKnowledgeTopicNodeMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentTopicDocumentRelationMapper;
import com.genbox.ai.manage.model.es.KnowledgeRouteIndexRecord;
import com.genbox.ai.manage.service.KnowledgeRouteIndexService;
import com.genbox.enums.BusinessStatus;
import com.genbox.enums.DocumentIndexStatusEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 服务实现层。
 */
@Slf4j
@AllArgsConstructor
@Service
@ConditionalOnProperty(prefix = "app.manage.elasticsearch", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ElasticsearchKnowledgeRouteIndexService implements KnowledgeRouteIndexService {

    private static final Duration REFRESH_INTERVAL = Duration.ofSeconds(5);
    private static final AtomicLong LAST_REFRESH_TIME = new AtomicLong(0L);

    @Qualifier("documentManageElasticsearchClient")
    private final ElasticsearchClient elasticsearchClient;
    private final DocumentManageProperties properties;
    private final GenBoxAgentKnowledgeScopeNodeMapper scopeNodeMapper;
    private final GenBoxAgentKnowledgeTopicNodeMapper topicNodeMapper;
    private final GenBoxAgentDocumentMapper documentMapper;
    private final GenBoxAgentDocumentProfileMapper documentProfileMapper;
    private final GenBoxAgentTopicDocumentRelationMapper topicDocumentRelationMapper;
    

    @Override
    public void refreshIfNeeded() {
        long now = System.currentTimeMillis();
        long last = LAST_REFRESH_TIME.get();
        if (now - last < REFRESH_INTERVAL.toMillis()) {
            return;
        }
        if (!LAST_REFRESH_TIME.compareAndSet(last, now)) {
            return;
        }
        try {
            refreshAll();
        }
        catch (Exception exception) {
            LAST_REFRESH_TIME.set(0L);
            log.warn("刷新知识路由索引失败，将在下次查询时重试。", exception);
        }
    }

    @Override
    public List<RouteLexicalHit> search(String routingText, String entityType, int size) {
        if (StrUtil.isBlank(routingText) || StrUtil.isBlank(entityType)) {
            return List.of();
        }
        refreshIfNeeded();
        List<String> entityTerms = extractEntityTerms(routingText);
        try {
            SearchResponse<KnowledgeRouteIndexRecord> response = elasticsearchClient.search(search -> search
                    .index(properties.getElasticsearch().getRouteIndexName())
                    .size(Math.max(1, Math.min(size, 10)))
                    .query(query -> query.bool(bool -> {
                        bool.filter(filter -> filter.term(term -> term.field("entityType").value(entityType)));
                        bool.should(should -> should.matchPhrase(matchPhrase -> matchPhrase
                            .field("displayName")
                            .query(routingText)
                            .boost(12.0f)
                        ));
                        bool.should(should -> should.multiMatch(multiMatch -> multiMatch
                            .query(routingText)
                            .fields("displayName^10", "aliasesText^8", "examplesText^6", "summaryText^5", "routeText^4", "descriptionText^3")
                            .type(TextQueryType.BestFields)
                        ));
                        for (String entityTerm : entityTerms) {
                            bool.should(should -> should.term(term -> term
                                .field("entityTerms")
                                .value(entityTerm)
                                .boost(9.0f)
                            ));
                        }
                        bool.minimumShouldMatch("1");
                        return bool;
                    })),
                KnowledgeRouteIndexRecord.class);
            List<RouteLexicalHit> hits = new ArrayList<>();
            for (Hit<KnowledgeRouteIndexRecord> hit : response.hits().hits()) {
                KnowledgeRouteIndexRecord source = hit.source();
                if (source == null) {
                    continue;
                }
                hits.add(new RouteLexicalHit(
                    source.getRouteId(),
                    source.getEntityCode(),
                    source.getEntityType(),
                    source.getDocumentId(),
                    source.getScopeCode(),
                    source.getTopicCode(),
                    source.getDocumentName(),
                    hit.score() == null ? 0D : hit.score()
                ));
            }
            return hits;
        }
        catch (IOException exception) {
            log.warn("知识路由 ES lexical 检索失败，退回语义匹配: entityType={}, query='{}'", entityType, StrUtil.maxLength(routingText, 120), exception);
            return List.of();
        }
    }

    @Override
    public void deleteDocumentRoute(Long documentId) {
        if (documentId == null) {
            return;
        }
        try {
            elasticsearchClient.deleteByQuery(delete -> delete
                .index(properties.getElasticsearch().getRouteIndexName())
                .refresh(true)
                .query(query -> query.bool(bool -> bool
                    .filter(filter -> filter.term(term -> term
                        .field("entityType")
                        .value("document")
                    ))
                    .filter(filter -> filter.term(term -> term
                        .field("documentId")
                        .value(documentId)
                    ))
                ))
            );
            log.info("知识路由索引中的文档路由快照已删除: documentId={}, index={}",
                documentId, properties.getElasticsearch().getRouteIndexName());
        }
        catch (IOException exception) {
            throw new IllegalStateException("删除知识路由索引中的文档路由快照失败", exception);
        }
    }

    private void refreshAll() throws IOException {
        List<KnowledgeRouteIndexRecord> records = buildIndexRecords();
        String indexName = properties.getElasticsearch().getRouteIndexName();
        elasticsearchClient.deleteByQuery(delete -> delete
            .index(indexName)
            .refresh(true)
            .query(query -> query.matchAll(matchAll -> matchAll))
        );
        if (records.isEmpty()) {
            log.info("知识路由索引刷新完成，但当前没有可写入的路由快照。");
            return;
        }
        BulkRequest.Builder bulkBuilder = new BulkRequest.Builder()
            .index(indexName)
            .refresh(Refresh.WaitFor);
        for (KnowledgeRouteIndexRecord record : records) {
            bulkBuilder.operations(operation -> operation.index(index -> index
                .id(record.getRouteId())
                .document(record)
            ));
        }
        BulkResponse response = elasticsearchClient.bulk(bulkBuilder.build());
        if (response.errors()) {
            String errorMessage = response.items().stream()
                .filter(item -> item.error() != null)
                .map(item -> item.id() + ":" + item.error().reason())
                .collect(Collectors.joining("; "));
            throw new IllegalStateException("批量写入知识路由索引失败: " + errorMessage);
        }
        log.info("知识路由索引刷新完成: recordCount={}, index={}", records.size(), indexName);
    }

    private List<KnowledgeRouteIndexRecord> buildIndexRecords() {
        List<KnowledgeRouteIndexRecord> records = new ArrayList<>();
        List<GenBoxAgentKnowledgeScopeNode> scopes = scopeNodeMapper.selectList(new LambdaQueryWrapper<GenBoxAgentKnowledgeScopeNode>()
            .eq(GenBoxAgentKnowledgeScopeNode::getStatus, BusinessStatus.YES.getCode()));
        List<GenBoxAgentKnowledgeTopicNode> topics = topicNodeMapper.selectList(new LambdaQueryWrapper<GenBoxAgentKnowledgeTopicNode>()
            .eq(GenBoxAgentKnowledgeTopicNode::getStatus, BusinessStatus.YES.getCode()));
        List<GenBoxAgentDocument> documents = documentMapper.selectList(new LambdaQueryWrapper<GenBoxAgentDocument>()
            .eq(GenBoxAgentDocument::getStatus, BusinessStatus.YES.getCode())
            .eq(GenBoxAgentDocument::getIndexStatus, DocumentIndexStatusEnum.BUILD_SUCCESS.getCode())
            .isNotNull(GenBoxAgentDocument::getLastIndexTaskId));
        Map<Long, GenBoxAgentDocumentProfile> profileMap = documentProfileMapper.selectList(new LambdaQueryWrapper<GenBoxAgentDocumentProfile>()
                .eq(GenBoxAgentDocumentProfile::getStatus, BusinessStatus.YES.getCode())
                .eq(GenBoxAgentDocumentProfile::getProfileStatus, 2))
            .stream()
            .collect(Collectors.toMap(GenBoxAgentDocumentProfile::getDocumentId, item -> item, (left, right) -> right));
        Map<String, List<GenBoxAgentKnowledgeTopicNode>> topicByScope = topics.stream()
            .collect(Collectors.groupingBy(GenBoxAgentKnowledgeTopicNode::getScopeCode));
        Map<String, List<GenBoxAgentTopicDocumentRelation>> relationByTopic = topicDocumentRelationMapper.selectList(
                new LambdaQueryWrapper<GenBoxAgentTopicDocumentRelation>()
                    .eq(GenBoxAgentTopicDocumentRelation::getStatus, BusinessStatus.YES.getCode()))
            .stream()
            .collect(Collectors.groupingBy(GenBoxAgentTopicDocumentRelation::getTopicCode));

        for (GenBoxAgentKnowledgeScopeNode scope : scopes) {
            List<String> scopeTags = new ArrayList<>();
            topicByScope.getOrDefault(scope.getScopeCode(), List.of()).forEach(topic -> {
                addUnique(scopeTags, topic.getTopicName());
                parseCommaText(topic.getAliases()).forEach(item -> addUnique(scopeTags, item));
            });
            records.add(KnowledgeRouteIndexRecord.builder()
                .routeId("scope:" + scope.getScopeCode())
                .entityType("scope")
                .entityCode(scope.getScopeCode())
                .scopeCode(scope.getScopeCode())
                .scopeName(scope.getScopeName())
                .displayName(safeText(scope.getScopeName()))
                .descriptionText(safeText(scope.getDescription()))
                .aliasesText(safeText(scope.getAliases()))
                .examplesText(safeText(scope.getExamples()))
                .summaryText(safeText(scope.getDescription()))
                .routeText(join(scope.getScopeName(), scope.getDescription(), scope.getAliases(), scope.getExamples()))
                .entityTerms(extractEntityTerms(join(scope.getScopeCode(), scope.getScopeName(), scope.getAliases())))
                .tags(scopeTags)
                .build());
        }

        for (GenBoxAgentKnowledgeTopicNode topic : topics) {
            List<String> tags = new ArrayList<>();
            parseJsonArray(topic.getExamples()).forEach(item -> addUnique(tags, item));
            parseCommaText(topic.getAliases()).forEach(item -> addUnique(tags, item));
            records.add(KnowledgeRouteIndexRecord.builder()
                .routeId("topic:" + topic.getTopicCode())
                .entityType("topic")
                .entityCode(topic.getTopicCode())
                .scopeCode(topic.getScopeCode())
                .topicCode(topic.getTopicCode())
                .topicName(topic.getTopicName())
                .displayName(safeText(topic.getTopicName()))
                .descriptionText(safeText(topic.getDescription()))
                .aliasesText(safeText(topic.getAliases()))
                .examplesText(safeText(topic.getExamples()))
                .summaryText(join(topic.getAnswerShape(), topic.getExecutionPreference()))
                .routeText(join(
                    topic.getTopicCode(),
                    topic.getTopicName(),
                    topic.getDescription(),
                    topic.getAliases(),
                    topic.getExamples(),
                    topic.getAnswerShape(),
                    topic.getExecutionPreference()))
                .entityTerms(extractEntityTerms(join(topic.getTopicCode(), topic.getTopicName(), topic.getAliases())))
                .tags(tags)
                .build());
        }

        Map<Long, GenBoxAgentKnowledgeTopicNode> topicDocumentMap = new LinkedHashMap<>();
        for (GenBoxAgentKnowledgeTopicNode topic : topics) {
            for (GenBoxAgentTopicDocumentRelation relation : relationByTopic.getOrDefault(topic.getTopicCode(), List.of())) {
                topicDocumentMap.put(relation.getDocumentId(), topic);
            }
        }

        for (GenBoxAgentDocument document : documents) {
            GenBoxAgentDocumentProfile profile = profileMap.get(document.getId());
            List<String> tags = new ArrayList<>();
            parseCommaText(document.getDocumentTags()).forEach(item -> addUnique(tags, item));
            if (profile != null) {
                parseJsonArray(profile.getCoreTopics()).forEach(item -> addUnique(tags, item));
                parseJsonArray(profile.getExampleQuestions()).forEach(item -> addUnique(tags, item));
            }
            relationByTopic.forEach((topicCode, relations) -> relations.stream()
                .filter(relation -> document.getId().equals(relation.getDocumentId()))
                .findFirst()
                .ifPresent(relation -> {
                    GenBoxAgentKnowledgeTopicNode topic = topics.stream()
                        .filter(item -> topicCode.equals(item.getTopicCode()))
                        .findFirst()
                        .orElse(null);
                    if (topic != null) {
                        addUnique(tags, topic.getTopicName());
                        parseCommaText(topic.getAliases()).forEach(item -> addUnique(tags, item));
                    }
                }));
            records.add(KnowledgeRouteIndexRecord.builder()
                .routeId("document:" + document.getId())
                .entityType("document")
                .entityCode(String.valueOf(document.getId()))
                .documentId(document.getId())
                .scopeCode(safeText(document.getKnowledgeScopeCode()))
                .scopeName(safeText(document.getKnowledgeScopeName()))
                .documentName(safeText(document.getDocumentName()))
                .businessCategory(safeText(document.getBusinessCategory()))
                .displayName(safeText(document.getDocumentName()))
                .descriptionText(profile == null ? "" : safeText(profile.getDocumentType()))
                .aliasesText("")
                .examplesText(profile == null ? "" : joinJsonLike(parseJsonArray(profile.getExampleQuestions())))
                .summaryText(profile == null ? "" : safeText(profile.getDocumentSummary()))
                .routeText(join(
                    document.getDocumentName(),
                    document.getKnowledgeScopeCode(),
                    document.getKnowledgeScopeName(),
                    document.getBusinessCategory(),
                    document.getDocumentTags(),
                    profile == null ? "" : profile.getDocumentSummary(),
                    profile == null ? "" : profile.getCoreTopics(),
                    profile == null ? "" : profile.getExampleQuestions(),
                    profile == null ? "" : profile.getDocumentType()
                ))
                .entityTerms(extractEntityTerms(join(document.getDocumentName(), document.getDocumentTags(), document.getKnowledgeScopeName())))
                .tags(tags)
                .build());
        }
        return records;
    }

    private List<String> extractEntityTerms(String text) {
        if (StrUtil.isBlank(text)) {
            return List.of();
        }
        LinkedHashSet<String> terms = new LinkedHashSet<>();
        String normalized = text.trim();
        for (String part : normalized.split("[\\s、，,；;：:（）()]+")) {
            String trimmed = part.trim();
            if (trimmed.length() < 2) {
                continue;
            }
            if (trimmed.matches(".*[A-Za-z].*") || trimmed.matches(".*\\d.*")) {
                terms.add(trimmed);
                terms.add(trimmed.toUpperCase(Locale.ROOT));
                terms.add(trimmed.toLowerCase(Locale.ROOT));
            }
        }
        return new ArrayList<>(terms).stream().limit(20).toList();
    }

    private List<String> parseJsonArray(String raw) {
        String normalized = StrUtil.blankToDefault(raw, "").trim();
        if (normalized.isBlank() || "[]".equals(normalized)) {
            return List.of();
        }
        String body = normalized.replace("[", "").replace("]", "");
        if (body.isBlank()) {
            return List.of();
        }
        return List.of(body.split(",")).stream()
            .map(item -> item.replace("\"", "").trim())
            .filter(StrUtil::isNotBlank)
            .toList();
    }

    private List<String> parseCommaText(String raw) {
        String normalized = StrUtil.blankToDefault(raw, "").trim();
        if (normalized.isBlank()) {
            return List.of();
        }
        return List.of(normalized.split(",")).stream()
            .map(String::trim)
            .filter(StrUtil::isNotBlank)
            .toList();
    }

    private String joinJsonLike(List<String> values) {
        return values == null || values.isEmpty() ? "" : String.join(" ", values);
    }

    private String join(String... values) {
        return Arrays.stream(values)
            .filter(StrUtil::isNotBlank)
            .collect(Collectors.joining(" "));
    }

    private void addUnique(List<String> values, String value) {
        if (StrUtil.isBlank(value)) {
            return;
        }
        if (!values.contains(value.trim())) {
            values.add(value.trim());
        }
    }

    private String safeText(String text) {
        return text == null ? "" : text.trim();
    }
}
