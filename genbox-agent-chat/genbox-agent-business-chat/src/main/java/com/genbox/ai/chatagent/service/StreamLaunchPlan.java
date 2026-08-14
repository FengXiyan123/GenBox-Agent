package com.genbox.ai.chatagent.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.genbox.enums.ChatQueryMode;

import java.time.LocalDate;

/**
 * 服务层。
 */
@Data
@AllArgsConstructor
public class StreamLaunchPlan {

    private final String question;

    private final String conversationId;

    private final ChatQueryMode chatMode;

    private final Long selectedDocumentId;

    private final String selectedDocumentName;

    private final Long selectedTaskId;

    private final String leaseKey;

    private final String leaseOwnerToken;

    private final LocalDate currentDate;

    private final String currentDateText;
}
