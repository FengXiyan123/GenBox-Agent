package com.genbox.ai.chatagent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 视图对象。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrievalResultView {

    private Long id;
    private String traceId;
    private int subQuestionIndex;
    private String subQuestion;
    private String channelType;
    private Integer channelRank;
    private Integer rrfRank;
    private Integer finalRank;
    private BigDecimal originalScore;
    private BigDecimal rrfScore;
    private BigDecimal rerankScore;
    private boolean gatePassed;
    private boolean isElevated;
    private boolean isSelected;
    private String selectionReason;
    private Long documentId;
    private String documentName;
    private Long chunkId;
    private Integer chunkNo;
    private Long parentBlockId;
    private Integer parentBlockNo;
    private String sectionPath;
    private String chunkTextPreview;
    private Integer chunkCharCount;
    private Instant createTime;
}
