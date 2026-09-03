package com.genbox.ai.manage.service.impl;

import lombok.AllArgsConstructor;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import com.genbox.ai.manage.data.GenBoxAgentDocument;
import com.genbox.ai.manage.data.GenBoxAgentDocumentChunk;
import com.genbox.ai.manage.data.GenBoxAgentDocumentParentBlock;
import com.genbox.ai.manage.data.GenBoxAgentDocumentProfile;
import com.genbox.ai.manage.data.GenBoxAgentDocumentStrategyPlan;
import com.genbox.ai.manage.data.GenBoxAgentDocumentStrategyStep;
import com.genbox.ai.manage.data.GenBoxAgentDocumentTask;
import com.genbox.ai.manage.data.GenBoxAgentDocumentTaskLog;
import com.genbox.ai.manage.data.GenBoxAgentTopicDocumentRelation;
import com.genbox.ai.manage.dto.DocumentChunkQueryDto;
import com.genbox.ai.manage.dto.DocumentChunkDetailQueryDto;
import com.genbox.ai.manage.dto.DocumentDeleteDto;
import com.genbox.ai.manage.dto.DocumentDetailQueryDto;
import com.genbox.ai.manage.dto.DocumentIndexBuildDto;
import com.genbox.ai.manage.dto.DocumentPageQueryDto;
import com.genbox.ai.manage.dto.DocumentStrategyConfirmDto;
import com.genbox.ai.manage.dto.DocumentStrategyPlanQueryDto;
import com.genbox.ai.manage.dto.DocumentStrategyStepItemDto;
import com.genbox.ai.manage.dto.DocumentTaskLogQueryDto;
import com.genbox.ai.manage.dto.DocumentUploadDto;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentChunkMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentParentBlockMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentProfileMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentStrategyPlanMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentStrategyStepMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentTaskLogMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentDocumentTaskMapper;
import com.genbox.ai.manage.mapper.GenBoxAgentTopicDocumentRelationMapper;
import com.genbox.ai.manage.mq.DocumentKafkaProducer;
import com.genbox.ai.manage.mq.message.DocumentIndexBuildMessage;
import com.genbox.ai.manage.mq.message.DocumentParseRouteMessage;
import com.genbox.ai.manage.service.DocumentManageService;
import com.genbox.ai.manage.service.DocumentNavigationIndexService;
import com.genbox.ai.manage.service.DocumentStorageService;
import com.genbox.ai.manage.service.DocumentStructureGraphProjectionService;
import com.genbox.ai.manage.service.DocumentStructureNodeService;
import com.genbox.ai.manage.service.DocumentStrategyService;
import com.genbox.ai.manage.service.DocumentTaskLogService;
import com.genbox.ai.manage.service.DocumentVectorGateway;
import com.genbox.ai.manage.service.KnowledgeRouteIndexService;
import com.genbox.ai.manage.service.keyword.DocumentKeywordSearchGateway;
import com.genbox.ai.manage.support.StoredObjectInfo;
import com.genbox.ai.manage.vo.DocumentChunkItemVo;
import com.genbox.ai.manage.vo.DocumentChunkQueryVo;
import com.genbox.ai.manage.vo.DocumentChunkDetailVo;
import com.genbox.ai.manage.vo.DocumentDeleteVo;
import com.genbox.ai.manage.vo.DocumentIndexBuildVo;
import com.genbox.ai.manage.vo.DocumentListItemVo;
import com.genbox.ai.manage.vo.DocumentParentBlockItemVo;
import com.genbox.ai.manage.vo.DocumentPageQueryVo;
import com.genbox.ai.manage.vo.DocumentStrategyConfirmVo;
import com.genbox.ai.manage.vo.DocumentStrategyPipelineVo;
import com.genbox.ai.manage.vo.DocumentStrategyPlanQueryVo;
import com.genbox.ai.manage.vo.DocumentStrategyPlanVo;
import com.genbox.ai.manage.vo.DocumentStrategyStepVo;
import com.genbox.ai.manage.vo.DocumentTaskLogQueryVo;
import com.genbox.ai.manage.vo.DocumentTaskLogVo;
import com.genbox.ai.manage.vo.DocumentUploadVo;
import com.genbox.enums.BaseCode;
import com.genbox.enums.BusinessStatus;
import com.genbox.enums.DocumentChunkSourceTypeEnum;
import com.genbox.enums.DocumentFileTypeEnum;
import com.genbox.enums.DocumentIndexStatusEnum;
import com.genbox.enums.DocumentLogLevelEnum;
import com.genbox.enums.DocumentManageCode;
import com.genbox.enums.DocumentOperatorTypeEnum;
import com.genbox.enums.DocumentParseStatusEnum;
import com.genbox.enums.DocumentPlanSourceEnum;
import com.genbox.enums.DocumentPlanStatusEnum;
import com.genbox.enums.DocumentStorageTypeEnum;
import com.genbox.enums.DocumentStrategyExecuteStatusEnum;
import com.genbox.enums.DocumentStrategyPipelineTypeEnum;
import com.genbox.enums.DocumentStrategyRoleEnum;
import com.genbox.enums.DocumentStrategySourceTypeEnum;
import com.genbox.enums.DocumentStrategyStatusEnum;
import com.genbox.enums.DocumentStrategyTypeEnum;
import com.genbox.enums.DocumentTaskEventTypeEnum;
import com.genbox.enums.DocumentTaskStageEnum;
import com.genbox.enums.DocumentTaskStatusEnum;
import com.genbox.enums.DocumentTaskTypeEnum;
import com.genbox.enums.DocumentTriggerSourceEnum;
import com.genbox.enums.DocumentVectorStatusEnum;
import com.genbox.exception.GenBoxAgentFrameException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实现层。
 */
@Slf4j
@AllArgsConstructor
@Service
public class DocumentManageServiceImpl implements DocumentManageService {

    private final GenBoxAgentDocumentMapper documentMapper;

    private final GenBoxAgentDocumentStrategyPlanMapper planMapper;

    private final GenBoxAgentDocumentStrategyStepMapper stepMapper;

    private final GenBoxAgentDocumentTaskMapper taskMapper;

    private final GenBoxAgentDocumentTaskLogMapper taskLogMapper;

    private final GenBoxAgentDocumentChunkMapper chunkMapper;

    private final GenBoxAgentDocumentParentBlockMapper parentBlockMapper;

    private final GenBoxAgentDocumentProfileMapper documentProfileMapper;

    private final GenBoxAgentTopicDocumentRelationMapper topicDocumentRelationMapper;

    private final DocumentStorageService storageService;

    private final DocumentStructureNodeService structureNodeService;

    private final DocumentStrategyService strategyService;

    private final DocumentTaskLogService taskLogService;

    private final DocumentVectorGateway vectorGateway;

    private final ObjectProvider<DocumentKeywordSearchGateway> keywordSearchGatewayProvider;

    private final ObjectProvider<DocumentNavigationIndexService> navigationIndexServiceProvider;

    private final ObjectProvider<DocumentStructureGraphProjectionService> graphProjectionServiceProvider;

    private final ObjectProvider<KnowledgeRouteIndexService> knowledgeRouteIndexServiceProvider;

    private final DocumentKafkaProducer kafkaProducer;

    private final TransactionTemplate transactionTemplate;
    
    private final UidGenerator uidGenerator;

    @Override
    public DocumentUploadVo upload(MultipartFile file, DocumentUploadDto dto) {

        if (file == null || file.isEmpty()) {
            throw new GenBoxAgentFrameException(DocumentManageCode.EMPTY_FILE_CONTENT.getCode(),
                DocumentManageCode.EMPTY_FILE_CONTENT.getMsg());
        }

        String originalFileName = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFileName)) {
            throw new GenBoxAgentFrameException(DocumentManageCode.UNSUPPORTED_FILE_TYPE.getCode(),
                "上传文件缺少原始文件名，无法识别文件类型。");
        }

        DocumentFileTypeEnum fileType = DocumentFileTypeEnum.fromFileName(originalFileName);
        if (fileType == null) {
            throw new GenBoxAgentFrameException(DocumentManageCode.UNSUPPORTED_FILE_TYPE.getCode(),
                DocumentManageCode.UNSUPPORTED_FILE_TYPE.getMsg());
        }

        byte[] fileBytes = getFileBytes(file);
        Long documentId = uidGenerator.getUid();

        StoredObjectInfo storedObjectInfo = storageService.uploadOriginalFile(
                documentId, originalFileName, fileBytes, file.getContentType());

        GenBoxAgentDocument document = new GenBoxAgentDocument();
        document.setId(documentId);
        document.setDocumentName(StrUtil.isNotBlank(dto.getDocumentName()) ? dto.getDocumentName() : originalFileName);
        document.setOriginalFileName(originalFileName);
        document.setFileType(fileType.getCode());
        document.setMimeType(file.getContentType());
        document.setFileSize((long) fileBytes.length);
        document.setStorageType(DocumentStorageTypeEnum.MINIO.getCode());
        document.setBucketName(storedObjectInfo.getBucketName());
        document.setObjectName(storedObjectInfo.getObjectName());
        document.setObjectUrl(storedObjectInfo.getObjectUrl());
        document.setParseStatus(DocumentParseStatusEnum.PARSING.getCode());
        document.setStrategyStatus(DocumentStrategyStatusEnum.WAIT_RECOMMEND.getCode());
        document.setIndexStatus(DocumentIndexStatusEnum.WAIT_BUILD.getCode());
        document.setCharCount(0);
        document.setTokenCount(0);

        document.setKnowledgeScopeCode(StrUtil.trimToNull(dto.getKnowledgeScopeCode()));
        document.setKnowledgeScopeName(StrUtil.trimToNull(dto.getKnowledgeScopeName()));
        document.setBusinessCategory(StrUtil.trimToNull(dto.getBusinessCategory()));
        document.setDocumentTags(StrUtil.trimToNull(dto.getDocumentTags()));
        document.setStatus(BusinessStatus.YES.getCode());

        Long taskId = uidGenerator.getUid();
        GenBoxAgentDocumentTask task = new GenBoxAgentDocumentTask();
        task.setId(taskId);
        task.setDocumentId(documentId);
        task.setTaskType(DocumentTaskTypeEnum.PARSE_ROUTE.getCode());
        task.setTaskStatus(DocumentTaskStatusEnum.NEW.getCode());
        task.setCurrentStage(DocumentTaskStageEnum.FILE_UPLOAD.getCode());
        Long operatorId = parseOptionalLong(dto.getOperatorId());
        task.setTriggerSource(resolveTriggerSource(operatorId));
        task.setRetryCount(0);
        task.setStatus(BusinessStatus.YES.getCode());

        DocumentUploadVo uploadVo = transactionTemplate.execute(status -> {
            documentMapper.insert(document);
            taskMapper.insert(task);

            taskLogService.saveLog(taskId, documentId,
                DocumentTaskStageEnum.FILE_UPLOAD.getCode(),
                DocumentTaskEventTypeEnum.COMPLETE.getCode(),
                DocumentLogLevelEnum.INFO.getCode(),
                resolveOperatorType(operatorId),
                operatorId,
                "文件上传完成，已进入解析与策略推荐队列。",
                Map.of("originalFileName", originalFileName, "fileSize", fileBytes.length));

            return new DocumentUploadVo(documentId, taskId, document.getDocumentName(),
                document.getParseStatus(), document.getStrategyStatus(), document.getIndexStatus());
        });

        kafkaProducer.sendParseRoute(new DocumentParseRouteMessage(documentId, taskId));

        return uploadVo;
    }

    @Override
    public DocumentPageQueryVo queryDocumentPage(DocumentPageQueryDto dto) {

        int pageNo = dto.getPageNo() == null || dto.getPageNo() <= 0 ? 1 : dto.getPageNo();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() <= 0 ? 10 : dto.getPageSize();
        String keyword = StrUtil.isNotBlank(dto.getKeyword()) ? dto.getKeyword().trim() : null;

        Page<GenBoxAgentDocument> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<GenBoxAgentDocument> wrapper = new LambdaQueryWrapper<GenBoxAgentDocument>()
            .eq(GenBoxAgentDocument::getStatus, BusinessStatus.YES.getCode())
            .orderByDesc(GenBoxAgentDocument::getEditTime, GenBoxAgentDocument::getId);

        if (keyword != null) {
            wrapper.and(query -> query.like(GenBoxAgentDocument::getDocumentName, keyword)
                .or()
                .like(GenBoxAgentDocument::getOriginalFileName, keyword));
        }

        IPage<GenBoxAgentDocument> resultPage = documentMapper.selectPage(page, wrapper);
        List<GenBoxAgentDocument> documentList = resultPage.getRecords();
        Map<Long, GenBoxAgentDocumentTask> latestTaskMap = getLatestTaskMap(documentList);

        List<DocumentListItemVo> records = documentList.stream()
            .map(document -> toDocumentListItemVo(document, latestTaskMap.get(document.getId())))
            .toList();

        return new DocumentPageQueryVo(pageNo, pageSize, resultPage.getTotal(), records);
    }

    @Override
    public DocumentListItemVo queryDocumentDetail(DocumentDetailQueryDto dto) {
        GenBoxAgentDocument document = getDocumentOrThrow(dto.getDocumentId());
        GenBoxAgentDocumentTask latestTask = getLatestTask(document.getId());
        return toDocumentListItemVo(document, latestTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DocumentDeleteVo deleteDocument(DocumentDeleteDto dto) {
        Long documentId = parseRequiredLong(dto.getDocumentId(), "文档id");
        GenBoxAgentDocument document = getDocumentOrThrow(documentId);

        long activeTaskCount = taskMapper.selectCount(new LambdaQueryWrapper<GenBoxAgentDocumentTask>()
            .eq(GenBoxAgentDocumentTask::getDocumentId, documentId)
            .eq(GenBoxAgentDocumentTask::getStatus, BusinessStatus.YES.getCode())
            .in(GenBoxAgentDocumentTask::getTaskStatus, DocumentTaskStatusEnum.NEW.getCode(), DocumentTaskStatusEnum.RUNNING.getCode()));
        if (activeTaskCount > 0) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_STATUS_INVALID.getCode(),
                "当前文档存在进行中的任务，请等待任务结束后再删除。");
        }

        storageService.deleteObjects(List.of(document.getObjectName(), document.getParseTextPath()));
        vectorGateway.deleteByDocumentId(documentId);

        DocumentKeywordSearchGateway keywordSearchGateway = keywordSearchGatewayProvider.getIfAvailable();
        if (keywordSearchGateway != null) {
            log.info("删除文档关键词索引: documentId={}", documentId);
            keywordSearchGateway.deleteByDocumentId(documentId);
        }
        DocumentNavigationIndexService navigationIndexService = navigationIndexServiceProvider.getIfAvailable();
        if (navigationIndexService != null) {
            log.info("删除文档导航索引: documentId={}", documentId);
            navigationIndexService.deleteByDocumentId(documentId);
        }
        KnowledgeRouteIndexService knowledgeRouteIndexService = knowledgeRouteIndexServiceProvider.getIfAvailable();
        if (knowledgeRouteIndexService != null) {
            log.info("删除知识路由索引中的文档快照: documentId={}", documentId);
            knowledgeRouteIndexService.deleteDocumentRoute(documentId);
        }
        DocumentStructureGraphProjectionService graphProjectionService = graphProjectionServiceProvider.getIfAvailable();
        if (graphProjectionService != null && graphProjectionService.enabled()) {
            log.info("删除文档结构图投影: documentId={}", documentId);
            graphProjectionService.deleteByDocumentId(documentId);
        }

        documentProfileMapper.delete(new LambdaQueryWrapper<GenBoxAgentDocumentProfile>()
            .eq(GenBoxAgentDocumentProfile::getDocumentId, documentId));
        topicDocumentRelationMapper.delete(new LambdaQueryWrapper<GenBoxAgentTopicDocumentRelation>()
            .eq(GenBoxAgentTopicDocumentRelation::getDocumentId, documentId));
        parentBlockMapper.delete(new LambdaQueryWrapper<GenBoxAgentDocumentParentBlock>()
            .eq(GenBoxAgentDocumentParentBlock::getDocumentId, documentId));
        chunkMapper.delete(new LambdaQueryWrapper<GenBoxAgentDocumentChunk>()
            .eq(GenBoxAgentDocumentChunk::getDocumentId, documentId));
        structureNodeService.deleteByDocumentId(documentId);
        taskLogMapper.delete(new LambdaQueryWrapper<GenBoxAgentDocumentTaskLog>()
            .eq(GenBoxAgentDocumentTaskLog::getDocumentId, documentId));
        stepMapper.delete(new LambdaQueryWrapper<GenBoxAgentDocumentStrategyStep>()
            .eq(GenBoxAgentDocumentStrategyStep::getDocumentId, documentId));
        taskMapper.delete(new LambdaQueryWrapper<GenBoxAgentDocumentTask>()
            .eq(GenBoxAgentDocumentTask::getDocumentId, documentId));
        planMapper.delete(new LambdaQueryWrapper<GenBoxAgentDocumentStrategyPlan>()
            .eq(GenBoxAgentDocumentStrategyPlan::getDocumentId, documentId));
        documentMapper.deleteById(documentId);

        return new DocumentDeleteVo(documentId, document.getDocumentName());
    }

    @Override
    public DocumentStrategyPlanQueryVo queryStrategyPlan(DocumentStrategyPlanQueryDto dto) {

        GenBoxAgentDocument document = getDocumentOrThrow(dto.getDocumentId());
        DocumentStrategyPlanVo planVo = null;
        boolean planReady = false;

        if (document.getCurrentPlanId() != null) {
            GenBoxAgentDocumentStrategyPlan plan = planMapper.selectById(document.getCurrentPlanId());
            if (plan != null && Objects.equals(plan.getStatus(), BusinessStatus.YES.getCode())) {
                List<GenBoxAgentDocumentStrategyStep> stepList = listStepByPlanId(plan.getId());
                planVo = toPlanVo(plan, stepList);
                planReady = true;
            }
        }

        return new DocumentStrategyPlanQueryVo(
            document.getId(),
            document.getDocumentName(),
            document.getParseStatus(),
            enumMsg(DocumentParseStatusEnum.getRc(document.getParseStatus())),
            document.getStrategyStatus(),
            enumMsg(DocumentStrategyStatusEnum.getRc(document.getStrategyStatus())),
            document.getIndexStatus(),
            enumMsg(DocumentIndexStatusEnum.getRc(document.getIndexStatus())),
            document.getParseErrorMsg(),
            planReady,
            planVo
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DocumentStrategyConfirmVo confirmStrategy(DocumentStrategyConfirmDto dto) {

        GenBoxAgentDocument document = getDocumentOrThrow(dto.getDocumentId());
        if (!Objects.equals(document.getParseStatus(), DocumentParseStatusEnum.PARSE_SUCCESS.getCode())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_STATUS_INVALID.getCode(), "当前文档还未完成解析，不能确认策略。");
        }

        if (!Objects.equals(document.getCurrentPlanId(), dto.getBasePlanId())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.STRATEGY_PLAN_NOT_FOUND.getCode(), "当前文档的基础方案不存在或已切换。");
        }

        GenBoxAgentDocumentStrategyPlan basePlan = planMapper.selectById(dto.getBasePlanId());
        if (basePlan == null || !Objects.equals(basePlan.getStatus(), BusinessStatus.YES.getCode())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.STRATEGY_PLAN_NOT_FOUND.getCode(),
                DocumentManageCode.STRATEGY_PLAN_NOT_FOUND.getMsg());
        }

        List<GenBoxAgentDocumentStrategyStep> baseStepList = listStepByPlanId(basePlan.getId());
        List<Integer> requestParentTypeList = dto.getParentSteps().stream()
            .sorted(Comparator.comparing(item -> item.getStepNo() == null ? Integer.MAX_VALUE : item.getStepNo()))
            .map(DocumentStrategyStepItemDto::getStrategyType)
            .filter(Objects::nonNull)
            .toList();
        List<Integer> requestChildTypeList = dto.getChildSteps().stream()
            .sorted(Comparator.comparing(item -> item.getStepNo() == null ? Integer.MAX_VALUE : item.getStepNo()))
            .map(DocumentStrategyStepItemDto::getStrategyType)
            .filter(Objects::nonNull)
            .toList();

        List<GenBoxAgentDocumentStrategyStep> normalizedStepList = strategyService.normalizeSteps(
            basePlan, baseStepList, requestParentTypeList, requestChildTypeList, dto.getDocumentId());

        List<Integer> normalizedParentTypeList = extractPipelineTypes(normalizedStepList, DocumentStrategyPipelineTypeEnum.PARENT);
        List<Integer> normalizedChildTypeList = extractPipelineTypes(normalizedStepList, DocumentStrategyPipelineTypeEnum.CHILD);

        if (normalizedParentTypeList.isEmpty()) {
            throw new GenBoxAgentFrameException(DocumentManageCode.STRATEGY_STEP_EMPTY.getCode(), "父块流水线不能为空。");
        }
        if (normalizedChildTypeList.isEmpty()) {
            throw new GenBoxAgentFrameException(DocumentManageCode.STRATEGY_STEP_EMPTY.getCode(), "子块流水线不能为空。");
        }

        if (normalizedStepList.isEmpty()) {
            throw new GenBoxAgentFrameException(DocumentManageCode.STRATEGY_STEP_EMPTY.getCode(),
                DocumentManageCode.STRATEGY_STEP_EMPTY.getMsg());
        }

        List<Integer> baseParentTypeList = extractPipelineTypes(baseStepList, DocumentStrategyPipelineTypeEnum.PARENT);
        List<Integer> baseChildTypeList = extractPipelineTypes(baseStepList, DocumentStrategyPipelineTypeEnum.CHILD);
        List<Integer> requestDistinctParentTypeList = new LinkedHashSet<>(requestParentTypeList).stream().toList();
        List<Integer> requestDistinctChildTypeList = new LinkedHashSet<>(requestChildTypeList).stream().toList();

        boolean normalized = !requestDistinctParentTypeList.equals(normalizedParentTypeList)
            || !requestDistinctChildTypeList.equals(normalizedChildTypeList);

        boolean changed = !baseParentTypeList.equals(normalizedParentTypeList)
            || !baseChildTypeList.equals(normalizedChildTypeList);

        Long targetPlanId;
        Integer targetPlanVersion;
        List<GenBoxAgentDocumentStrategyStep> targetStepList;

        if (!changed) {

            basePlan.setPlanStatus(DocumentPlanStatusEnum.CONFIRMED.getCode());
            basePlan.setPlanSource(basePlan.getPlanSource() == null ? DocumentPlanSourceEnum.SYSTEM_RECOMMEND.getCode() : basePlan.getPlanSource());
            basePlan.setAdjustNote(dto.getAdjustNote());
            basePlan.setConfirmUserId(dto.getOperatorId());
            basePlan.setConfirmTime(new Date());
            planMapper.updateById(basePlan);
            targetPlanId = basePlan.getId();
            targetPlanVersion = basePlan.getPlanVersion();
            targetStepList = baseStepList;
        } else {

            basePlan.setPlanStatus(DocumentPlanStatusEnum.DISCARDED.getCode());
            planMapper.updateById(basePlan);

            Long newPlanId = uidGenerator.getUid();
            Integer newPlanVersion = getNextPlanVersion(document.getId());
            GenBoxAgentDocumentStrategyPlan newPlan = new GenBoxAgentDocumentStrategyPlan();
            newPlan.setId(newPlanId);
            newPlan.setDocumentId(document.getId());
            newPlan.setPlanVersion(newPlanVersion);

            newPlan.setPlanSource(DocumentPlanSourceEnum.USER_ADJUST.getCode());
            newPlan.setPlanStatus(DocumentPlanStatusEnum.CONFIRMED.getCode());
            newPlan.setStrategyCount(normalizedStepList.size());
            newPlan.setStrategySnapshot(buildStrategySnapshot(normalizedStepList));
            newPlan.setRecommendReason(basePlan.getRecommendReason());
            newPlan.setAdjustNote(dto.getAdjustNote());
            newPlan.setConfirmUserId(dto.getOperatorId());
            newPlan.setConfirmTime(new Date());
            newPlan.setStatus(BusinessStatus.YES.getCode());
            planMapper.insert(newPlan);

            for (GenBoxAgentDocumentStrategyStep step : normalizedStepList) {
                step.setId(uidGenerator.getUid());
                step.setPlanId(newPlanId);
                step.setStatus(BusinessStatus.YES.getCode());
                stepMapper.insert(step);
            }

            targetPlanId = newPlanId;
            targetPlanVersion = newPlanVersion;
            targetStepList = normalizedStepList;
        }

        document.setCurrentPlanId(targetPlanId);
        document.setStrategyStatus(DocumentStrategyStatusEnum.CONFIRMED.getCode());
        documentMapper.updateById(document);

        GenBoxAgentDocumentTask latestParseTask = getLatestTask(document.getId(), DocumentTaskTypeEnum.PARSE_ROUTE.getCode());
        if (latestParseTask != null) {

            latestParseTask.setCurrentStage(DocumentTaskStageEnum.STRATEGY_CONFIRM.getCode());
            taskMapper.updateById(latestParseTask);

            if (changed) {

                taskLogService.saveLog(latestParseTask.getId(), document.getId(),
                    DocumentTaskStageEnum.STRATEGY_CONFIRM.getCode(),
                    DocumentTaskEventTypeEnum.USER_ADJUST.getCode(),
                    DocumentLogLevelEnum.INFO.getCode(),
                    resolveOperatorType(parseOptionalLong(dto.getOperatorId())),
                    parseOptionalLong(dto.getOperatorId()),
                    "用户调整了系统推荐策略。",
                    detail("parentStrategyTypes", normalizedParentTypeList,
                        "childStrategyTypes", normalizedChildTypeList,
                        "adjustNote", dto.getAdjustNote()));
            }

            taskLogService.saveLog(latestParseTask.getId(), document.getId(),
                DocumentTaskStageEnum.STRATEGY_CONFIRM.getCode(),
                DocumentTaskEventTypeEnum.USER_CONFIRM.getCode(),
                DocumentLogLevelEnum.INFO.getCode(),
                    resolveOperatorType(parseOptionalLong(dto.getOperatorId())),
                    parseOptionalLong(dto.getOperatorId()),
                    "用户已确认最终策略方案。",
                Map.of("planId", targetPlanId,
                    "parentStrategyTypes", normalizedParentTypeList,
                    "childStrategyTypes", normalizedChildTypeList));
        }

        return new DocumentStrategyConfirmVo(
            document.getId(),
            targetPlanId,
            targetPlanVersion,
            document.getStrategyStatus(),
            enumMsg(DocumentStrategyStatusEnum.getRc(document.getStrategyStatus())),
            normalized,
            toPipelineVo(DocumentStrategyPipelineTypeEnum.PARENT, targetStepList),
            toPipelineVo(DocumentStrategyPipelineTypeEnum.CHILD, targetStepList)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DocumentIndexBuildVo buildIndex(DocumentIndexBuildDto dto) {

        GenBoxAgentDocument document = getDocumentOrThrow(dto.getDocumentId());
        if (!Objects.equals(document.getParseStatus(), DocumentParseStatusEnum.PARSE_SUCCESS.getCode())
            || !Objects.equals(document.getStrategyStatus(), DocumentStrategyStatusEnum.CONFIRMED.getCode())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_STATUS_INVALID.getCode(), "当前文档尚未完成“解析成功 + 策略确认”，不能构建索引。");
        }

        if (!Objects.equals(document.getCurrentPlanId(), dto.getPlanId())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.STRATEGY_PLAN_NOT_FOUND.getCode(), "当前文档的生效方案与请求方案不一致。");
        }

        long runningTaskCount = taskMapper.selectCount(new LambdaQueryWrapper<GenBoxAgentDocumentTask>()
            .eq(GenBoxAgentDocumentTask::getDocumentId, dto.getDocumentId())
            .eq(GenBoxAgentDocumentTask::getTaskType, DocumentTaskTypeEnum.BUILD_INDEX.getCode())
            .in(GenBoxAgentDocumentTask::getTaskStatus, DocumentTaskStatusEnum.NEW.getCode(), DocumentTaskStatusEnum.RUNNING.getCode())
            .eq(GenBoxAgentDocumentTask::getStatus, BusinessStatus.YES.getCode()));
        if (runningTaskCount > 0) {
            throw new GenBoxAgentFrameException(DocumentManageCode.INDEX_TASK_RUNNING.getCode(),
                DocumentManageCode.INDEX_TASK_RUNNING.getMsg());
        }

        GenBoxAgentDocumentStrategyPlan plan = planMapper.selectById(dto.getPlanId());
        if (plan == null || !Objects.equals(plan.getStatus(), BusinessStatus.YES.getCode())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.STRATEGY_PLAN_NOT_FOUND.getCode(),
                DocumentManageCode.STRATEGY_PLAN_NOT_FOUND.getMsg());
        }

        Long taskId = uidGenerator.getUid();
        GenBoxAgentDocumentTask task = new GenBoxAgentDocumentTask();
        task.setId(taskId);
        task.setDocumentId(document.getId());
        task.setPlanId(dto.getPlanId());
        task.setTaskType(DocumentTaskTypeEnum.BUILD_INDEX.getCode());
        task.setTaskStatus(DocumentTaskStatusEnum.NEW.getCode());
        task.setCurrentStage(DocumentTaskStageEnum.CHUNK_EXECUTE.getCode());
        Long operatorId = parseOptionalLong(dto.getOperatorId());
        task.setTriggerSource(resolveTriggerSource(operatorId));
        task.setStrategySnapshot(plan.getStrategySnapshot());
        task.setRetryCount(0);
        task.setStatus(BusinessStatus.YES.getCode());
        taskMapper.insert(task);

        document.setIndexStatus(DocumentIndexStatusEnum.BUILDING.getCode());
        documentMapper.updateById(document);

        taskLogService.saveLog(taskId, document.getId(),
            DocumentTaskStageEnum.CHUNK_EXECUTE.getCode(),
            DocumentTaskEventTypeEnum.START.getCode(),
            DocumentLogLevelEnum.INFO.getCode(),
            resolveOperatorType(operatorId),
            operatorId,
            "索引构建任务已创建，等待异步执行。",
            Map.of("planId", dto.getPlanId(), "strategySnapshot", plan.getStrategySnapshot()));

        // buildIndex runs in a transaction. Publish only after commit so the Kafka
        // consumer can see the task/document rows before it starts processing.
        DocumentIndexBuildMessage message = new DocumentIndexBuildMessage(document.getId(), taskId, dto.getPlanId());
        DocumentMessageDispatch.afterCommit(() -> kafkaProducer.sendIndexBuild(message));

        return new DocumentIndexBuildVo(
            document.getId(),
            taskId,
            task.getTaskType(),
            enumMsg(DocumentTaskTypeEnum.getRc(task.getTaskType())),
            task.getTaskStatus(),
            enumMsg(DocumentTaskStatusEnum.getRc(task.getTaskStatus())),
            document.getIndexStatus(),
            enumMsg(DocumentIndexStatusEnum.getRc(document.getIndexStatus()))
        );
    }

    @Override
    public DocumentTaskLogQueryVo queryTaskLogs(DocumentTaskLogQueryDto dto) {

        GenBoxAgentDocumentTask task = taskMapper.selectById(dto.getTaskId());
        if (task == null || !Objects.equals(task.getStatus(), BusinessStatus.YES.getCode())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_NOT_FOUND.getCode(), "任务不存在。");
        }

        int pageNo = dto.getPageNo() == null || dto.getPageNo() <= 0 ? 1 : dto.getPageNo();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() <= 0 ? 20 : dto.getPageSize();
        Page<GenBoxAgentDocumentTaskLog> page = new Page<>(pageNo, pageSize);

        IPage<GenBoxAgentDocumentTaskLog> resultPage = taskLogMapper.selectPage(page,
            new LambdaQueryWrapper<GenBoxAgentDocumentTaskLog>()
                .eq(GenBoxAgentDocumentTaskLog::getTaskId, dto.getTaskId())
                .eq(GenBoxAgentDocumentTaskLog::getStatus, BusinessStatus.YES.getCode())
                .orderByAsc(GenBoxAgentDocumentTaskLog::getCreateTime, GenBoxAgentDocumentTaskLog::getId));

        List<DocumentTaskLogVo> logVoList = resultPage.getRecords().stream()
            .map(this::toTaskLogVo)
            .toList();

        return new DocumentTaskLogQueryVo(
            task.getId(),
            task.getDocumentId(),
            task.getTaskType(),
            enumMsg(DocumentTaskTypeEnum.getRc(task.getTaskType())),
            task.getTaskStatus(),
            enumMsg(DocumentTaskStatusEnum.getRc(task.getTaskStatus())),
            task.getCurrentStage(),
            enumMsg(DocumentTaskStageEnum.getRc(task.getCurrentStage())),
            task.getStartTime(),
            task.getFinishTime(),
            task.getCostMillis(),
            task.getErrorCode(),
            task.getErrorMsg(),
            resultPage.getTotal(),
            logVoList
        );
    }

    @Override
    public DocumentChunkQueryVo queryDocumentChunks(DocumentChunkQueryDto dto) {
        GenBoxAgentDocument document = getDocumentOrThrow(dto.getDocumentId());
        int pageNo = dto.getPageNo() == null || dto.getPageNo() <= 0 ? 1 : dto.getPageNo();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() <= 0 ? 20 : dto.getPageSize();

        Long effectiveTaskId = resolveChunkTaskId(document, dto.getTaskId());
        if (effectiveTaskId == null) {
            return new DocumentChunkQueryVo(document.getId(), null, document.getCurrentPlanId(), pageNo, pageSize, 0L, List.of());
        }

        GenBoxAgentDocumentTask task = taskMapper.selectById(effectiveTaskId);
        if (task == null
            || !Objects.equals(task.getStatus(), BusinessStatus.YES.getCode())
            || !Objects.equals(task.getDocumentId(), document.getId())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_NOT_FOUND.getCode(), "切块任务不存在。");
        }

        Page<GenBoxAgentDocumentChunk> page = new Page<>(pageNo, pageSize);
        IPage<GenBoxAgentDocumentChunk> resultPage = chunkMapper.selectPage(page,
            new LambdaQueryWrapper<GenBoxAgentDocumentChunk>()
                .eq(GenBoxAgentDocumentChunk::getDocumentId, document.getId())
                .eq(GenBoxAgentDocumentChunk::getTaskId, effectiveTaskId)
                .eq(GenBoxAgentDocumentChunk::getStatus, BusinessStatus.YES.getCode())
                .orderByAsc(GenBoxAgentDocumentChunk::getChunkNo, GenBoxAgentDocumentChunk::getId));

        Map<Long, GenBoxAgentDocumentParentBlock> parentBlockMap = listParentBlockMap(
            resultPage.getRecords().stream()
                .map(GenBoxAgentDocumentChunk::getParentBlockId)
                .filter(Objects::nonNull)
                .toList()
        );

        List<DocumentChunkItemVo> records = resultPage.getRecords().stream()
            .map(chunk -> toDocumentChunkItemVo(chunk, parentBlockMap.get(chunk.getParentBlockId())))
            .toList();

        return new DocumentChunkQueryVo(
            document.getId(),
            effectiveTaskId,
            task.getPlanId(),
            pageNo,
            pageSize,
            resultPage.getTotal(),
            records
        );
    }

    @Override
    public DocumentChunkDetailVo queryDocumentChunkDetail(DocumentChunkDetailQueryDto dto) {
        GenBoxAgentDocument document = getDocumentOrThrow(dto.getDocumentId());
        Long effectiveTaskId = resolveChunkTaskId(document, dto.getTaskId());
        if (effectiveTaskId == null) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_NOT_FOUND.getCode(), "当前文档还没有可查看的 chunk 详情。");
        }

        GenBoxAgentDocumentTask task = taskMapper.selectById(effectiveTaskId);
        if (task == null
            || !Objects.equals(task.getStatus(), BusinessStatus.YES.getCode())
            || !Objects.equals(task.getDocumentId(), document.getId())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_NOT_FOUND.getCode(), "切块任务不存在。");
        }

        GenBoxAgentDocumentChunk chunk = chunkMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentDocumentChunk>()
            .eq(GenBoxAgentDocumentChunk::getId, dto.getChunkId())
            .eq(GenBoxAgentDocumentChunk::getDocumentId, document.getId())
            .eq(GenBoxAgentDocumentChunk::getTaskId, effectiveTaskId)
            .eq(GenBoxAgentDocumentChunk::getStatus, BusinessStatus.YES.getCode())
            .last("limit 1"));
        if (chunk == null) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_NOT_FOUND.getCode(), "chunk 详情不存在。");
        }

        GenBoxAgentDocumentParentBlock parentBlock = chunk.getParentBlockId() == null
            ? null
            : parentBlockMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentDocumentParentBlock>()
                .eq(GenBoxAgentDocumentParentBlock::getId, chunk.getParentBlockId())
                .eq(GenBoxAgentDocumentParentBlock::getDocumentId, document.getId())
                .eq(GenBoxAgentDocumentParentBlock::getTaskId, effectiveTaskId)
                .eq(GenBoxAgentDocumentParentBlock::getStatus, BusinessStatus.YES.getCode())
                .last("limit 1"));

        List<GenBoxAgentDocumentChunk> siblingChunkList = chunk.getParentBlockId() == null
            ? List.of(chunk)
            : chunkMapper.selectList(new LambdaQueryWrapper<GenBoxAgentDocumentChunk>()
                .eq(GenBoxAgentDocumentChunk::getDocumentId, document.getId())
                .eq(GenBoxAgentDocumentChunk::getTaskId, effectiveTaskId)
                .eq(GenBoxAgentDocumentChunk::getParentBlockId, chunk.getParentBlockId())
                .eq(GenBoxAgentDocumentChunk::getStatus, BusinessStatus.YES.getCode())
                .orderByAsc(GenBoxAgentDocumentChunk::getChunkNo, GenBoxAgentDocumentChunk::getId));

        return new DocumentChunkDetailVo(
            document.getId(),
            effectiveTaskId,
            task.getPlanId(),
            toDocumentChunkItemVo(chunk, parentBlock),
            toDocumentParentBlockItemVo(parentBlock),
            siblingChunkList.stream()
                .map(item -> toDocumentChunkItemVo(item, parentBlock))
                .toList()
        );
    }

    private GenBoxAgentDocument getDocumentOrThrow(Long documentId) {

        GenBoxAgentDocument document = documentMapper.selectById(documentId);
        if (document == null || !Objects.equals(document.getStatus(), BusinessStatus.YES.getCode())) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_NOT_FOUND.getCode(),
                DocumentManageCode.DOCUMENT_NOT_FOUND.getMsg());
        }
        return document;
    }

    private List<GenBoxAgentDocumentStrategyStep> listStepByPlanId(Long planId) {
        List<GenBoxAgentDocumentStrategyStep> stepList = stepMapper.selectList(new LambdaQueryWrapper<GenBoxAgentDocumentStrategyStep>()
            .eq(GenBoxAgentDocumentStrategyStep::getPlanId, planId)
            .eq(GenBoxAgentDocumentStrategyStep::getStatus, BusinessStatus.YES.getCode()));
        return stepList.stream()
            .sorted(Comparator
                .comparingInt((GenBoxAgentDocumentStrategyStep step) -> pipelineOrder(step.getPipelineType()))
                .thenComparing(GenBoxAgentDocumentStrategyStep::getStepNo)
                .thenComparing(GenBoxAgentDocumentStrategyStep::getId))
            .toList();
    }

    private Integer getNextPlanVersion(Long documentId) {

        GenBoxAgentDocumentStrategyPlan latestPlan = planMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentDocumentStrategyPlan>()
            .eq(GenBoxAgentDocumentStrategyPlan::getDocumentId, documentId)
            .eq(GenBoxAgentDocumentStrategyPlan::getStatus, BusinessStatus.YES.getCode())
            .orderByDesc(GenBoxAgentDocumentStrategyPlan::getPlanVersion)
            .last("limit 1"));
        return latestPlan == null ? 1 : latestPlan.getPlanVersion() + 1;
    }

    private GenBoxAgentDocumentTask getLatestTask(Long documentId, Integer taskType) {

        return taskMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentDocumentTask>()
            .eq(GenBoxAgentDocumentTask::getDocumentId, documentId)
            .eq(GenBoxAgentDocumentTask::getTaskType, taskType)
            .eq(GenBoxAgentDocumentTask::getStatus, BusinessStatus.YES.getCode())
            .orderByDesc(GenBoxAgentDocumentTask::getId)
            .last("limit 1"));
    }

    private GenBoxAgentDocumentTask getLatestTask(Long documentId) {
        return taskMapper.selectOne(new LambdaQueryWrapper<GenBoxAgentDocumentTask>()
            .eq(GenBoxAgentDocumentTask::getDocumentId, documentId)
            .eq(GenBoxAgentDocumentTask::getStatus, BusinessStatus.YES.getCode())
            .orderByDesc(GenBoxAgentDocumentTask::getId)
            .last("limit 1"));
    }

    private Map<Long, GenBoxAgentDocumentTask> getLatestTaskMap(List<GenBoxAgentDocument> documentList) {
        if (documentList == null || documentList.isEmpty()) {
            return Map.of();
        }

        Set<Long> documentIdSet = documentList.stream()
            .map(GenBoxAgentDocument::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (documentIdSet.isEmpty()) {
            return Map.of();
        }

        List<GenBoxAgentDocumentTask> taskList = taskMapper.selectList(new LambdaQueryWrapper<GenBoxAgentDocumentTask>()
            .in(GenBoxAgentDocumentTask::getDocumentId, documentIdSet)
            .eq(GenBoxAgentDocumentTask::getStatus, BusinessStatus.YES.getCode())
            .orderByDesc(GenBoxAgentDocumentTask::getId));

        Map<Long, GenBoxAgentDocumentTask> latestTaskMap = new LinkedHashMap<>();
        for (GenBoxAgentDocumentTask task : taskList) {
            latestTaskMap.putIfAbsent(task.getDocumentId(), task);
        }
        return latestTaskMap;
    }

    private Long resolveChunkTaskId(GenBoxAgentDocument document, Long requestedTaskId) {
        if (requestedTaskId != null) {
            return requestedTaskId;
        }
        if (document.getLastIndexTaskId() != null) {
            return document.getLastIndexTaskId();
        }
        GenBoxAgentDocumentTask latestBuildTask = getLatestTask(document.getId(), DocumentTaskTypeEnum.BUILD_INDEX.getCode());
        return latestBuildTask == null ? null : latestBuildTask.getId();
    }

    private DocumentListItemVo toDocumentListItemVo(GenBoxAgentDocument document, GenBoxAgentDocumentTask latestTask) {
        return new DocumentListItemVo(
            document.getId(),
            document.getDocumentName(),
            document.getOriginalFileName(),
            document.getFileType(),
            enumMsg(DocumentFileTypeEnum.getRc(document.getFileType())),
            document.getFileSize(),
            document.getCharCount(),
            document.getTokenCount(),
            document.getParseStatus(),
            enumMsg(DocumentParseStatusEnum.getRc(document.getParseStatus())),
            document.getStrategyStatus(),
            enumMsg(DocumentStrategyStatusEnum.getRc(document.getStrategyStatus())),
            document.getIndexStatus(),
            enumMsg(DocumentIndexStatusEnum.getRc(document.getIndexStatus())),
            document.getParseErrorMsg(),
            document.getKnowledgeScopeCode(),
            document.getKnowledgeScopeName(),
            document.getBusinessCategory(),
            document.getDocumentTags(),
            document.getCurrentPlanId(),
            document.getLastIndexTaskId(),
            latestTask == null ? null : latestTask.getId(),
            latestTask == null ? null : latestTask.getTaskType(),
            latestTask == null ? "" : enumMsg(DocumentTaskTypeEnum.getRc(latestTask.getTaskType())),
            latestTask == null ? null : latestTask.getTaskStatus(),
            latestTask == null ? "" : enumMsg(DocumentTaskStatusEnum.getRc(latestTask.getTaskStatus())),
            document.getCreateTime(),
            document.getEditTime()
        );
    }

    private DocumentChunkItemVo toDocumentChunkItemVo(GenBoxAgentDocumentChunk chunk,
                                                     GenBoxAgentDocumentParentBlock parentBlock) {
        return new DocumentChunkItemVo(
            chunk.getId(),
            chunk.getParentBlockId(),
            parentBlock == null ? null : parentBlock.getParentNo(),
            parentBlock == null ? null : parentBlock.getChildCount(),
            parentBlock == null ? null : parentBlock.getStartChunkNo(),
            parentBlock == null ? null : parentBlock.getEndChunkNo(),
            chunk.getChunkNo(),
            chunk.getSectionPath(),
            chunk.getSourceType(),
            enumMsg(DocumentChunkSourceTypeEnum.getRc(chunk.getSourceType())),
            chunk.getCharCount(),
            chunk.getTokenCount(),
            chunk.getVectorStatus(),
            enumMsg(DocumentVectorStatusEnum.getRc(chunk.getVectorStatus())),
            chunk.getChunkText()
        );
    }

    private DocumentParentBlockItemVo toDocumentParentBlockItemVo(GenBoxAgentDocumentParentBlock parentBlock) {
        if (parentBlock == null) {
            return null;
        }
        return new DocumentParentBlockItemVo(
            parentBlock.getId(),
            parentBlock.getParentNo(),
            parentBlock.getSectionPath(),
            parentBlock.getSourceType(),
            enumMsg(DocumentChunkSourceTypeEnum.getRc(parentBlock.getSourceType())),
            parentBlock.getCharCount(),
            parentBlock.getTokenCount(),
            parentBlock.getChildCount(),
            parentBlock.getStartChunkNo(),
            parentBlock.getEndChunkNo(),
            parentBlock.getParentText()
        );
    }

    private Map<Long, GenBoxAgentDocumentParentBlock> listParentBlockMap(List<Long> parentBlockIds) {
        if (parentBlockIds == null || parentBlockIds.isEmpty()) {
            return Map.of();
        }
        return parentBlockMapper.selectList(new LambdaQueryWrapper<GenBoxAgentDocumentParentBlock>()
                .in(GenBoxAgentDocumentParentBlock::getId, parentBlockIds)
                .eq(GenBoxAgentDocumentParentBlock::getStatus, BusinessStatus.YES.getCode()))
            .stream()
            .collect(Collectors.toMap(
                GenBoxAgentDocumentParentBlock::getId,
                item -> item,
                (left, right) -> left,
                LinkedHashMap::new
            ));
    }

    private DocumentStrategyPlanVo toPlanVo(GenBoxAgentDocumentStrategyPlan plan, List<GenBoxAgentDocumentStrategyStep> stepList) {
        return new DocumentStrategyPlanVo(
            plan.getId(),
            plan.getPlanVersion(),
            plan.getPlanSource(),
            enumMsg(DocumentPlanSourceEnum.getRc(plan.getPlanSource())),
            plan.getPlanStatus(),
            enumMsg(DocumentPlanStatusEnum.getRc(plan.getPlanStatus())),
            plan.getStrategySnapshot(),
            plan.getRecommendReason(),
            toPipelineVo(DocumentStrategyPipelineTypeEnum.PARENT, stepList),
            toPipelineVo(DocumentStrategyPipelineTypeEnum.CHILD, stepList)
        );
    }

    private List<DocumentStrategyStepVo> toStepVoList(List<GenBoxAgentDocumentStrategyStep> stepList) {

        return stepList.stream()
            .sorted(Comparator
                .comparingInt((GenBoxAgentDocumentStrategyStep step) -> pipelineOrder(step.getPipelineType()))
                .thenComparing(GenBoxAgentDocumentStrategyStep::getStepNo)
                .thenComparing(GenBoxAgentDocumentStrategyStep::getId))
            .map(step -> new DocumentStrategyStepVo(
                step.getStepNo(),
                step.getPipelineType(),
                enumMsg(DocumentStrategyPipelineTypeEnum.getRc(step.getPipelineType())),
                step.getStrategyType(),
                enumMsg(DocumentStrategyTypeEnum.getRc(step.getStrategyType())),
                step.getStrategyRole(),
                enumMsg(DocumentStrategyRoleEnum.getRc(step.getStrategyRole())),
                step.getSourceType(),
                enumMsg(DocumentStrategySourceTypeEnum.getRc(step.getSourceType())),
                step.getExecuteStatus(),
                enumMsg(DocumentStrategyExecuteStatusEnum.getRc(step.getExecuteStatus())),
                step.getRecommendReason()
            ))
            .toList();
    }

    private DocumentStrategyPipelineVo toPipelineVo(DocumentStrategyPipelineTypeEnum pipelineType,
                                                    List<GenBoxAgentDocumentStrategyStep> stepList) {
        List<GenBoxAgentDocumentStrategyStep> pipelineSteps = stepList.stream()
            .filter(step -> pipelineType.getCode().equalsIgnoreCase(
                StrUtil.blankToDefault(step.getPipelineType(), DocumentStrategyPipelineTypeEnum.CHILD.getCode())
            ))
            .sorted(Comparator.comparingInt(GenBoxAgentDocumentStrategyStep::getStepNo))
            .toList();
        return new DocumentStrategyPipelineVo(
            pipelineType.getCode(),
            pipelineType.getMsg(),
            pipelineSteps.stream().map(step -> String.valueOf(step.getStrategyType())).collect(Collectors.joining(",")),
            toStepVoList(pipelineSteps)
        );
    }

    private List<Integer> extractPipelineTypes(List<GenBoxAgentDocumentStrategyStep> stepList,
                                               DocumentStrategyPipelineTypeEnum pipelineType) {
        return stepList.stream()
            .filter(step -> pipelineType.getCode().equalsIgnoreCase(
                StrUtil.blankToDefault(step.getPipelineType(), DocumentStrategyPipelineTypeEnum.CHILD.getCode())
            ))
            .sorted(Comparator.comparingInt(GenBoxAgentDocumentStrategyStep::getStepNo))
            .map(GenBoxAgentDocumentStrategyStep::getStrategyType)
            .toList();
    }

    private String buildStrategySnapshot(List<GenBoxAgentDocumentStrategyStep> stepList) {
        return "PARENT:" + toPipelineVo(DocumentStrategyPipelineTypeEnum.PARENT, stepList).getStrategySnapshot()
            + ";CHILD:" + toPipelineVo(DocumentStrategyPipelineTypeEnum.CHILD, stepList).getStrategySnapshot();
    }

    private int pipelineOrder(String pipelineType) {
        return DocumentStrategyPipelineTypeEnum.PARENT.getCode().equalsIgnoreCase(
            StrUtil.blankToDefault(pipelineType, "")
        ) ? 0 : 1;
    }

    private DocumentTaskLogVo toTaskLogVo(GenBoxAgentDocumentTaskLog logRecord) {
        return new DocumentTaskLogVo(
            logRecord.getId(),
            logRecord.getStageType(),
            enumMsg(DocumentTaskStageEnum.getRc(logRecord.getStageType())),
            logRecord.getEventType(),
            enumMsg(DocumentTaskEventTypeEnum.getRc(logRecord.getEventType())),
            logRecord.getLogLevel(),
            enumMsg(DocumentLogLevelEnum.getRc(logRecord.getLogLevel())),
            logRecord.getContent(),
            logRecord.getDetailJson(),
            logRecord.getCreateTime()
        );
    }

    private Integer resolveOperatorType(Long operatorId) {

        return operatorId == null ? DocumentOperatorTypeEnum.SYSTEM.getCode() : DocumentOperatorTypeEnum.USER.getCode();
    }

    private Integer resolveTriggerSource(Long operatorId) {

        return operatorId == null ? DocumentTriggerSourceEnum.SYSTEM.getCode() : DocumentTriggerSourceEnum.USER.getCode();
    }

    private Long parseOptionalLong(String rawValue) {
        if (StrUtil.isBlank(rawValue)) {
            return null;
        }
        try {
            Long value = Long.valueOf(rawValue.trim());
            return value > 0 ? value : null;
        }
        catch (NumberFormatException exception) {
            return null;
        }
    }

    private Long parseOptionalLong(Long rawValue) {
        return rawValue == null || rawValue <= 0 ? null : rawValue;
    }

    private Long parseRequiredLong(String rawValue, String fieldName) {
        if (StrUtil.isBlank(rawValue)) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), fieldName + "不能为空。");
        }

        try {

            Long value = Long.valueOf(rawValue.trim());
            if (value <= 0) {
                throw new NumberFormatException("id must be positive");
            }
            return value;
        }
        catch (NumberFormatException exception) {
            throw new GenBoxAgentFrameException(BaseCode.PARAMETER_ERROR.getCode(), fieldName + "格式不正确。");
        }
    }

    private String enumMsg(Object enumObject) {
        if (enumObject == null) {
            return "";
        }
        if (enumObject instanceof DocumentParseStatusEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentFileTypeEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentStrategyStatusEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentIndexStatusEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentPlanSourceEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentPlanStatusEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentStrategyTypeEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentStrategyRoleEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentStrategySourceTypeEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentStrategyExecuteStatusEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentTaskTypeEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentTaskStatusEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentTaskStageEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentTaskEventTypeEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentLogLevelEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentChunkSourceTypeEnum value) {
            return value.getMsg();
        }
        if (enumObject instanceof DocumentVectorStatusEnum value) {
            return value.getMsg();
        }
        return "";
    }

    private byte[] getFileBytes(MultipartFile file) {
        try {

            return file.getBytes();
        }
        catch (IOException exception) {
            throw new GenBoxAgentFrameException(DocumentManageCode.DOCUMENT_STORAGE_FAILED.getCode(),
                "读取上传文件内容失败: " + exception.getMessage(), exception);
        }
    }

    private Map<String, Object> detail(Object... keyValues) {
        Map<String, Object> detailMap = new LinkedHashMap<>();

        for (int index = 0; index + 1 < keyValues.length; index += 2) {
            detailMap.put(String.valueOf(keyValues[index]), keyValues[index + 1]);
        }
        return detailMap;
    }
}
