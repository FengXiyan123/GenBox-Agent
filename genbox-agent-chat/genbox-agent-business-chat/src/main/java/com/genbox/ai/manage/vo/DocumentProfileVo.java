package com.genbox.ai.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentProfileVo {

    private String documentId;

    private String documentSummary;

    private String documentType;

    private String coreTopics;

    private String exampleQuestions;

    private String graphFriendly;

    private String supportsGraphOutline;

    private String supportsItemLookup;

    private String supportsGraphAssist;

    private String profileSource;

    private String profileStatus;

    private String errorMsg;
}
