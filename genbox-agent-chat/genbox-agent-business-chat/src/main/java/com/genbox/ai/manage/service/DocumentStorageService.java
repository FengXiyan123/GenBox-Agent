package com.genbox.ai.manage.service;

import com.genbox.ai.manage.support.StoredObjectInfo;

import java.util.List;

/**
 * 服务层。
 */
public interface DocumentStorageService {

    StoredObjectInfo uploadOriginalFile(Long documentId, String originalFileName, byte[] bytes, String contentType);

    String uploadParsedText(Long documentId, String parsedText);

    byte[] downloadObject(String objectName);

    String downloadText(String objectName);

    void deleteObjects(List<String> objectNameList);
}
