package com.genbox.ai.manage.service;

import com.genbox.enums.DocumentFileTypeEnum;
import com.genbox.ai.manage.support.DocumentAnalysisResult;

/**
 * 服务层。
 */
public interface DocumentParserService {

    DocumentAnalysisResult parse(byte[] bytes, String originalFileName, String mimeType, DocumentFileTypeEnum fileType);
}
