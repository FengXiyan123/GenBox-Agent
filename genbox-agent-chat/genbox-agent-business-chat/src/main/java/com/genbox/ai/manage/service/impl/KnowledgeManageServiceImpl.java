package com.genbox.ai.manage.service.impl;

import lombok.AllArgsConstructor;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import cn.hutool.core.util.StrUtil;
import com.genbox.ai.manage.data.GenBoxAgentDocument;
import com.genbox.ai.manage.data.GenBoxAgentDocumentProfile;
import com.genbox.ai.manage.data.GenBoxAgentKnowledgeScopeNode;
import com.genbox.ai.manage.data.GenBoxAgentKnowledgeTopicNode;
import com.genbox.ai.manage.data.GenBoxAgentTopicDocumentRelation;
import com.genbox.ai.manage.dto.DocumentProfileBatchRegenerateDto;
import com.genbox.ai.manage.dto.DocumentProfileDetailQueryDto;
import com.genbox.ai.manage.dto.DocumentProfileRegenerateDto;
import com.genbox.ai.manage.dto.KnowledgeRouteTraceQueryDto;
import com.genbox.ai.manage.dto.KnowledgeScopeDeleteDto;
import com.genbox.ai.manage.dto.KnowledgeScopeSaveDto;
import com.genbox.ai.manage.dto.KnowledgeTopicDeleteDto;
import com.genbox.ai.manage.dto.KnowledgeTopicQueryDto;
import com.genbox.ai.manage.dto.KnowledgeTopicSaveDto;
import com.genbox.ai.manage.dto.TopicDocumentRelationListQueryDto;
import com.genbox.ai.manage.dto.TopicDocumentRelationRemoveDto;
import com.genbox.ai.manage.dto.TopicDocumentRelationSaveDto;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentKnowledgeScopeNodeMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentKnowledgeTopicNodeMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentKnowledgeRouteTraceMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentTopicDocumentRelationMapper;
import com.genbox.ai.manage.service.DocumentProfileService;
import com.genbox.ai.manage.service.KnowledgeManageService;
import com.genbox.ai.manage.vo.DocumentProfileVo;
import com.genbox.ai.manage.vo.KnowledgeRouteTraceItemVo;
import com.genbox.ai.manage.vo.KnowledgeRouteTracePageVo;
import com.genbox.ai.manage.vo.KnowledgeScopeItemVo;
import com.genbox.ai.manage.vo.KnowledgeTopicItemVo;
import com.genbox.ai.manage.vo.TopicDocumentRelationItemVo;
import com.genbox.enums.BaseCode;
import com.genbox.enums.BusinessStatus;
import com.genbox.exception.GenBoxAgentFrameException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 服务实现层。
 */
@AllArgsConstructor
@Service
public class KnowledgeManageServiceImpl implements KnowledgeManageService {

    private final GenBoxAgentKnowledgeScopeNodeMapper scopeNodeMapper;
    private final GenBoxAgentKnowledgeTopicNodeMapper topicNodeMapper;
    private final GenBoxAgentTopicDocumentRelationMapper topicDocumentRelationMapper;
    private final GenBoxAgentKnowledgeRouteTraceMapper knowledgeRouteTraceMapper;
    private final GenBoxAgentDocumentMapper documentMapper;
    private final DocumentProfileService documentProfileService;
    private final UidGenerator uidGenerator;

    @Override
    public KnowledgeScopeItemVo saveScope(KnowledgeScopeSaveDto dto) {
        validateScope(dto);
        GenBoxAgentKnowledgeScopeNode entity = scopeNodeMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentKnowledgeScopeNode>()
            .eq(GenBoxAgentKnowledgeScopeNode::getScopeCode, dto.getScopeCode().trim())
            .eq(GenBoxAgentKnowledgeScopeNode::getStatus, BusinessStatus.YES.getCode())
            .last("LIMIT 1"));
        if (entity == null) {
            entity = new GenBoxAgentKnowledgeScopeNode();
            entity.setId(uidGenerator.getUid());
            entity.setStatus(BusinessStatus.YES.getCode());
            entity.setScopeCode(dto.getScopeCode().trim());
        }
        entity.setScopeName(safeText(dto.getScopeName()));
        entity.setParentScopeCode(safeText(dto.getParentScopeCode()));
        entity.setDescription(safeText(dto.getDescription()));
        entity.setAliases(safeText(dto.getAliases()));
        entity.setExamples(safeText(dto.getExamples()));
        entity.setSortOrder(parseInteger(dto.getSortOrder(), 0));
        if (entity.getCreateTime() == null) {
            scopeNodeMapper.insert(entity);
        }
        else {
            scopeNodeMapper.updateById(entity);
        }
        return toScopeVo(entity);
    }

    @Override
    public boolean deleteScope(KnowledgeScopeDeleteDto dto) {
        String scopeCode = safeText(dto.getScopeCode());
        if (scopeCode.isBlank()) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), "scopeCode 不能为空。");
        }
        return scopeNodeMapper.update(null, new LambdaUpdateWrapper<GenBoxAgentKnowledgeScopeNode>()
            .eq(GenBoxAgentKnowledgeScopeNode::getScopeCode, scopeCode)
            .eq(GenBoxAgentKnowledgeScopeNode::getStatus, BusinessStatus.YES.getCode())
            .set(GenBoxAgentKnowledgeScopeNode::getStatus, BusinessStatus.NO.getCode())) > 0;
    }

    @Override
    public List<KnowledgeScopeItemVo> listScopes() {
        return scopeNodeMapper.selectList(new LambdaQueryWrapper<GenBoxAgentKnowledgeScopeNode>()
                .eq(GenBoxAgentKnowledgeScopeNode::getStatus, BusinessStatus.YES.getCode())
                .orderByAsc(GenBoxAgentKnowledgeScopeNode::getSortOrder, GenBoxAgentKnowledgeScopeNode::getId))
            .stream()
            .map(this::toScopeVo)
            .toList();
    }

    @Override
    public KnowledgeTopicItemVo saveTopic(KnowledgeTopicSaveDto dto) {
        validateTopic(dto);
        GenBoxAgentKnowledgeTopicNode entity = topicNodeMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentKnowledgeTopicNode>()
            .eq(GenBoxAgentKnowledgeTopicNode::getTopicCode, dto.getTopicCode().trim())
            .eq(GenBoxAgentKnowledgeTopicNode::getStatus, BusinessStatus.YES.getCode())
            .last("LIMIT 1"));
        if (entity == null) {
            entity = new GenBoxAgentKnowledgeTopicNode();
            entity.setId(uidGenerator.getUid());
            entity.setStatus(BusinessStatus.YES.getCode());
            entity.setTopicCode(dto.getTopicCode().trim());
        }
        entity.setTopicName(safeText(dto.getTopicName()));
        entity.setScopeCode(safeText(dto.getScopeCode()));
        entity.setDescription(safeText(dto.getDescription()));
        entity.setAliases(safeText(dto.getAliases()));
        entity.setExamples(safeText(dto.getExamples()));
        entity.setAnswerShape(safeText(dto.getAnswerShape()));
        entity.setExecutionPreference(safeText(dto.getExecutionPreference()));
        entity.setSortOrder(parseInteger(dto.getSortOrder(), 0));
        if (entity.getCreateTime() == null) {
            topicNodeMapper.insert(entity);
        }
        else {
            topicNodeMapper.updateById(entity);
        }
        return toTopicVo(entity);
    }

    @Override
    public boolean deleteTopic(KnowledgeTopicDeleteDto dto) {
        String topicCode = safeText(dto.getTopicCode());
        if (topicCode.isBlank()) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), "topicCode 不能为空。");
        }
        return topicNodeMapper.update(null, new LambdaUpdateWrapper<GenBoxAgentKnowledgeTopicNode>()
            .eq(GenBoxAgentKnowledgeTopicNode::getTopicCode, topicCode)
            .eq(GenBoxAgentKnowledgeTopicNode::getStatus, BusinessStatus.YES.getCode())
            .set(GenBoxAgentKnowledgeTopicNode::getStatus, BusinessStatus.NO.getCode())) > 0;
    }

    @Override
    public List<KnowledgeTopicItemVo> listTopics(KnowledgeTopicQueryDto dto) {
        String scopeCode = dto == null ? "" : safeText(dto.getScopeCode());
        LambdaQueryWrapper<GenBoxAgentKnowledgeTopicNode> wrapper = new LambdaQueryWrapper<GenBoxAgentKnowledgeTopicNode>()
            .eq(GenBoxAgentKnowledgeTopicNode::getStatus, BusinessStatus.YES.getCode())
            .orderByAsc(GenBoxAgentKnowledgeTopicNode::getSortOrder, GenBoxAgentKnowledgeTopicNode::getId);
        if (scopeCode != null && !scopeCode.isBlank()) {
            wrapper.eq(GenBoxAgentKnowledgeTopicNode::getScopeCode, scopeCode);
        }
        return topicNodeMapper.selectList(wrapper).stream().map(this::toTopicVo).toList();
    }

    @Override
    public DocumentProfileVo queryProfile(DocumentProfileDetailQueryDto dto) {
        Long documentId = parseRequiredLong(dto == null ? null : dto.getDocumentId(), "documentId");
        GenBoxAgentDocumentProfile profile = documentProfileService.getByDocumentId(documentId)
            .orElseThrow(() -> new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), "文档画像不存在。"));
        return toProfileVo(profile);
    }

    @Override
    public DocumentProfileVo regenerateProfile(DocumentProfileRegenerateDto dto) {
        Long documentId = parseRequiredLong(dto == null ? null : dto.getDocumentId(), "documentId");
        return toProfileVo(documentProfileService.regenerateProfile(documentId));
    }

    @Override
    public List<DocumentProfileVo> batchRegenerateProfiles(DocumentProfileBatchRegenerateDto dto) {
        List<Long> documentIds = dto == null || dto.getDocumentIds() == null
            ? List.of()
            : dto.getDocumentIds().stream().map(value -> parseRequiredLong(value, "documentId")).toList();
        return documentProfileService.batchRegenerateProfiles(documentIds).stream()
            .map(this::toProfileVo)
            .toList();
    }

    @Override
    public List<TopicDocumentRelationItemVo> listTopicDocuments(TopicDocumentRelationListQueryDto dto) {
        String topicCode = dto == null ? "" : safeText(dto.getTopicCode());
        LambdaQueryWrapper<GenBoxAgentTopicDocumentRelation> wrapper = new LambdaQueryWrapper<GenBoxAgentTopicDocumentRelation>()
            .eq(GenBoxAgentTopicDocumentRelation::getStatus, BusinessStatus.YES.getCode())
            .orderByDesc(GenBoxAgentTopicDocumentRelation::getRelationScore, GenBoxAgentTopicDocumentRelation::getId);
        if (topicCode != null && !topicCode.isBlank()) {
            wrapper.eq(GenBoxAgentTopicDocumentRelation::getTopicCode, topicCode);
        }
        return topicDocumentRelationMapper.selectList(wrapper).stream()
            .map(this::toRelationVo)
            .toList();
    }

    @Override
    public TopicDocumentRelationItemVo saveTopicDocumentRelation(TopicDocumentRelationSaveDto dto) {
        String topicCode = safeText(dto.getTopicCode());
        Long documentId = parseRequiredLong(dto.getDocumentId(), "documentId");
        if (topicCode.isBlank()) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), "topicCode 不能为空。");
        }
        GenBoxAgentTopicDocumentRelation relation = topicDocumentRelationMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentTopicDocumentRelation>()
            .eq(GenBoxAgentTopicDocumentRelation::getTopicCode, topicCode)
            .eq(GenBoxAgentTopicDocumentRelation::getDocumentId, documentId)
            .eq(GenBoxAgentTopicDocumentRelation::getStatus, BusinessStatus.YES.getCode())
            .last("LIMIT 1"));
        if (relation == null) {
            relation = new GenBoxAgentTopicDocumentRelation();
            relation.setId(uidGenerator.getUid());
            relation.setTopicCode(topicCode);
            relation.setDocumentId(documentId);
            relation.setStatus(BusinessStatus.YES.getCode());
        }
        relation.setRelationScore(parseDecimal(dto.getRelationScore(), BigDecimal.ZERO));
        relation.setRelationSource(firstNonBlank(dto.getRelationSource(), "manual"));
        relation.setReason(safeText(dto.getReason()));
        if (relation.getCreateTime() == null) {
            topicDocumentRelationMapper.insert(relation);
        }
        else {
            topicDocumentRelationMapper.updateById(relation);
        }
        return toRelationVo(relation);
    }

    @Override
    public boolean removeTopicDocumentRelation(TopicDocumentRelationRemoveDto dto) {
        String topicCode = safeText(dto.getTopicCode());
        Long documentId = parseRequiredLong(dto.getDocumentId(), "documentId");
        if (topicCode.isBlank()) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), "topicCode 不能为空。");
        }
        return topicDocumentRelationMapper.update(null, new LambdaUpdateWrapper<GenBoxAgentTopicDocumentRelation>()
            .eq(GenBoxAgentTopicDocumentRelation::getTopicCode, topicCode)
            .eq(GenBoxAgentTopicDocumentRelation::getDocumentId, documentId)
            .eq(GenBoxAgentTopicDocumentRelation::getStatus, BusinessStatus.YES.getCode())
            .set(GenBoxAgentTopicDocumentRelation::getStatus, BusinessStatus.NO.getCode())) > 0;
    }

    @Override
    public KnowledgeRouteTracePageVo queryRouteTracePage(KnowledgeRouteTraceQueryDto dto) {
        int pageNo = parseInteger(dto == null ? null : dto.getPageNo(), 1);
        int pageSize = parseInteger(dto == null ? null : dto.getPageSize(), 20);
        String conversationId = dto == null ? "" : safeText(dto.getConversationId());
        String mode = dto == null ? "" : safeText(dto.getMode());
        String routeStatus = dto == null ? "" : safeText(dto.getRouteStatus());
        LambdaQueryWrapper<com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace> wrapper =
            new LambdaQueryWrapper<com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace>()
                .eq(com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace::getStatus, BusinessStatus.YES.getCode())
                .orderByDesc(com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace::getCreateTime,
                    com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace::getId);
        if (StrUtil.isNotBlank(conversationId)) {
            wrapper.eq(com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace::getConversationId, conversationId);
        }
        if (StrUtil.isNotBlank(mode)) {
            wrapper.eq(com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace::getMode, mode);
        }
        if (StrUtil.isNotBlank(routeStatus)) {
            Integer parsedStatus = parseInteger(routeStatus, -1);
            if (parsedStatus > 0) {
                wrapper.eq(com.genbox.ai.manage.data.GenBoxAgentKnowledgeRouteTrace::getRouteStatus, parsedStatus);
            }
        }
        long total = knowledgeRouteTraceMapper.selectCount(wrapper);
        List<KnowledgeRouteTraceItemVo> records = knowledgeRouteTraceMapper.selectList(wrapper.last("LIMIT " + ((long) (pageNo - 1) * pageSize) + "," + pageSize))
            .stream()
            .map(item -> new KnowledgeRouteTraceItemVo(
                String.valueOf(item.getId()),
                safeText(item.getConversationId()),
                item.getExchangeId() == null ? "" : String.valueOf(item.getExchangeId()),
                safeText(item.getQuestion()),
                safeText(item.getRewriteQuestion()),
                safeText(item.getMode()),
                safeText(item.getTopScopesJson()),
                safeText(item.getTopTopicsJson()),
                safeText(item.getTopDocumentsJson()),
                item.getSelectedDocumentId() == null ? "" : String.valueOf(item.getSelectedDocumentId()),
                item.getHitSelectedDocument() == null ? "" : String.valueOf(item.getHitSelectedDocument()),
                item.getConfidence() == null ? "0.0000" : item.getConfidence().toPlainString(),
                item.getRouteStatus() == null ? "" : String.valueOf(item.getRouteStatus()),
                safeText(item.getErrorMsg()),
                item.getCreateTime() == null ? "" : String.valueOf(item.getCreateTime().getTime())
            ))
            .toList();
        long totalPages = total <= 0 ? 0 : (total + pageSize - 1) / pageSize;
        return new KnowledgeRouteTracePageVo(
            String.valueOf(pageNo),
            String.valueOf(pageSize),
            String.valueOf(total),
            String.valueOf(totalPages),
            records
        );
    }

    private void validateScope(KnowledgeScopeSaveDto dto) {
        if (dto == null || safeText(dto.getScopeCode()).isBlank() || safeText(dto.getScopeName()).isBlank()) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), "scopeCode 和 scopeName 不能为空。");
        }
    }

    private void validateTopic(KnowledgeTopicSaveDto dto) {
        if (dto == null || safeText(dto.getTopicCode()).isBlank() || safeText(dto.getTopicName()).isBlank() || safeText(dto.getScopeCode()).isBlank()) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), "topicCode、topicName、scopeCode 不能为空。");
        }
    }

    private KnowledgeScopeItemVo toScopeVo(GenBoxAgentKnowledgeScopeNode node) {
        return new KnowledgeScopeItemVo(
            String.valueOf(node.getId()),
            safeText(node.getScopeCode()),
            safeText(node.getScopeName()),
            safeText(node.getParentScopeCode()),
            safeText(node.getDescription()),
            safeText(node.getAliases()),
            safeText(node.getExamples()),
            String.valueOf(Optional.ofNullable(node.getSortOrder()).orElse(0))
        );
    }

    private KnowledgeTopicItemVo toTopicVo(GenBoxAgentKnowledgeTopicNode node) {
        return new KnowledgeTopicItemVo(
            String.valueOf(node.getId()),
            safeText(node.getTopicCode()),
            safeText(node.getTopicName()),
            safeText(node.getScopeCode()),
            safeText(node.getDescription()),
            safeText(node.getAliases()),
            safeText(node.getExamples()),
            safeText(node.getAnswerShape()),
            safeText(node.getExecutionPreference()),
            String.valueOf(Optional.ofNullable(node.getSortOrder()).orElse(0))
        );
    }

    private DocumentProfileVo toProfileVo(GenBoxAgentDocumentProfile profile) {
        return new DocumentProfileVo(
            String.valueOf(profile.getDocumentId()),
            safeText(profile.getDocumentSummary()),
            safeText(profile.getDocumentType()),
            safeText(profile.getCoreTopics()),
            safeText(profile.getExampleQuestions()),
            String.valueOf(Optional.ofNullable(profile.getGraphFriendly()).orElse(0)),
            String.valueOf(Optional.ofNullable(profile.getSupportsGraphOutline()).orElse(0)),
            String.valueOf(Optional.ofNullable(profile.getSupportsItemLookup()).orElse(0)),
            String.valueOf(Optional.ofNullable(profile.getSupportsGraphAssist()).orElse(0)),
            safeText(profile.getProfileSource()),
            String.valueOf(Optional.ofNullable(profile.getProfileStatus()).orElse(0)),
            safeText(profile.getErrorMsg())
        );
    }

    private TopicDocumentRelationItemVo toRelationVo(GenBoxAgentTopicDocumentRelation relation) {
        GenBoxAgentDocument document = documentMapper.selectById(relation.getDocumentId());
        return new TopicDocumentRelationItemVo(
            safeText(relation.getTopicCode()),
            String.valueOf(relation.getDocumentId()),
            document == null ? "" : safeText(document.getDocumentName()),
            document == null ? "" : safeText(document.getKnowledgeScopeCode()),
            document == null ? "" : safeText(document.getKnowledgeScopeName()),
            document == null ? "" : safeText(document.getBusinessCategory()),
            document == null ? "" : safeText(document.getDocumentTags()),
            relation.getRelationScore() == null ? "0.0000" : relation.getRelationScore().toPlainString(),
            safeText(relation.getRelationSource()),
            safeText(relation.getReason())
        );
    }

    private Long parseRequiredLong(String rawValue, String fieldName) {
        if (StrUtil.isBlank(rawValue)) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), fieldName + "不能为空。");
        }
        try {
            Long value = Long.valueOf(rawValue.trim());
            if (value <= 0) {
                throw new NumberFormatException("must be positive");
            }
            return value;
        }
        catch (NumberFormatException exception) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), fieldName + "格式非法。");
        }
    }

    private Integer parseInteger(String rawValue, Integer fallback) {
        if (StrUtil.isBlank(rawValue)) {
            return fallback;
        }
        try {
            return Integer.valueOf(rawValue.trim());
        }
        catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private BigDecimal parseDecimal(String rawValue, BigDecimal fallback) {
        if (StrUtil.isBlank(rawValue)) {
            return fallback;
        }
        try {
            return new BigDecimal(rawValue.trim());
        }
        catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private String safeText(String text) {
        return text == null ? "" : text.trim();
    }

    private String firstNonBlank(String primary, String fallback) {
        if (StrUtil.isNotBlank(primary)) {
            return primary.trim();
        }
        return StrUtil.blankToDefault(fallback, "");
    }
}
