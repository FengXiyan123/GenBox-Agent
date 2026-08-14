package com.genbox.ai.chatagent.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.genbox.ai.chatagent.model.ConversationSessionView;

import java.util.List;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSessionListVo {

    private long pageNo;

    private long pageSize;

    private long totalSize;

    private long totalPages;

    private List<ConversationSessionView> sessions;
}
