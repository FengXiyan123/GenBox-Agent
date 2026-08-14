package com.genbox.ai.manage.service;

import com.genbox.ai.manage.dto.DocumentIndexBuildDto;
import com.genbox.ai.manage.dto.DocumentChunkQueryDto;
import com.genbox.ai.manage.dto.DocumentChunkDetailQueryDto;
import com.genbox.ai.manage.dto.DocumentDetailQueryDto;
import com.genbox.ai.manage.dto.DocumentDeleteDto;
import com.genbox.ai.manage.dto.DocumentPageQueryDto;
import com.genbox.ai.manage.dto.DocumentStrategyConfirmDto;
import com.genbox.ai.manage.dto.DocumentStrategyPlanQueryDto;
import com.genbox.ai.manage.dto.DocumentTaskLogQueryDto;
import com.genbox.ai.manage.dto.DocumentUploadDto;
import com.genbox.ai.manage.vo.DocumentIndexBuildVo;
import com.genbox.ai.manage.vo.DocumentChunkQueryVo;
import com.genbox.ai.manage.vo.DocumentChunkDetailVo;
import com.genbox.ai.manage.vo.DocumentDeleteVo;
import com.genbox.ai.manage.vo.DocumentListItemVo;
import com.genbox.ai.manage.vo.DocumentPageQueryVo;
import com.genbox.ai.manage.vo.DocumentStrategyConfirmVo;
import com.genbox.ai.manage.vo.DocumentStrategyPlanQueryVo;
import com.genbox.ai.manage.vo.DocumentTaskLogQueryVo;
import com.genbox.ai.manage.vo.DocumentUploadVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 服务层。
 */
public interface DocumentManageService {

    DocumentUploadVo upload(MultipartFile file, DocumentUploadDto dto);

    DocumentPageQueryVo queryDocumentPage(DocumentPageQueryDto dto);

    DocumentListItemVo queryDocumentDetail(DocumentDetailQueryDto dto);

    DocumentDeleteVo deleteDocument(DocumentDeleteDto dto);

    DocumentStrategyPlanQueryVo queryStrategyPlan(DocumentStrategyPlanQueryDto dto);

    DocumentStrategyConfirmVo confirmStrategy(DocumentStrategyConfirmDto dto);

    DocumentIndexBuildVo buildIndex(DocumentIndexBuildDto dto);

    DocumentChunkQueryVo queryDocumentChunks(DocumentChunkQueryDto dto);

    DocumentChunkDetailVo queryDocumentChunkDetail(DocumentChunkDetailQueryDto dto);

    DocumentTaskLogQueryVo queryTaskLogs(DocumentTaskLogQueryDto dto);
}
