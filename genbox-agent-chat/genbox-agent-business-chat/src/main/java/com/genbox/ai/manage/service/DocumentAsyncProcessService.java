package com.genbox.ai.manage.service;

/**
 * 服务层。
 */
public interface DocumentAsyncProcessService {

    void handleParseRoute(Long documentId, Long taskId);

    void handleIndexBuild(Long documentId, Long taskId, Long planId);
}
