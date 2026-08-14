package com.genbox.ai.manage.service;

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
import com.genbox.ai.manage.vo.DocumentProfileVo;
import com.genbox.ai.manage.vo.KnowledgeRouteTracePageVo;
import com.genbox.ai.manage.vo.KnowledgeScopeItemVo;
import com.genbox.ai.manage.vo.KnowledgeTopicItemVo;
import com.genbox.ai.manage.vo.TopicDocumentRelationItemVo;

import java.util.List;

/**
 * 服务层。
 */
public interface KnowledgeManageService {

    KnowledgeScopeItemVo saveScope(KnowledgeScopeSaveDto dto);

    boolean deleteScope(KnowledgeScopeDeleteDto dto);

    List<KnowledgeScopeItemVo> listScopes();

    KnowledgeTopicItemVo saveTopic(KnowledgeTopicSaveDto dto);

    boolean deleteTopic(KnowledgeTopicDeleteDto dto);

    List<KnowledgeTopicItemVo> listTopics(KnowledgeTopicQueryDto dto);

    DocumentProfileVo queryProfile(DocumentProfileDetailQueryDto dto);

    DocumentProfileVo regenerateProfile(DocumentProfileRegenerateDto dto);

    List<DocumentProfileVo> batchRegenerateProfiles(DocumentProfileBatchRegenerateDto dto);

    List<TopicDocumentRelationItemVo> listTopicDocuments(TopicDocumentRelationListQueryDto dto);

    TopicDocumentRelationItemVo saveTopicDocumentRelation(TopicDocumentRelationSaveDto dto);

    boolean removeTopicDocumentRelation(TopicDocumentRelationRemoveDto dto);

    KnowledgeRouteTracePageVo queryRouteTracePage(KnowledgeRouteTraceQueryDto dto);
}
