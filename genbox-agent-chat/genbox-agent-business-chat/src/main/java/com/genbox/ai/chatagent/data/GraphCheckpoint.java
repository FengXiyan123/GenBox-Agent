package com.genbox.ai.chatagent.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据实体。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("GRAPH_CHECKPOINT")
public class GraphCheckpoint {

    @TableId(value = "checkpoint_id", type = IdType.INPUT)
    private String checkpointId;

    private String threadId;

    private String nodeId;

    private String nextNodeId;

    private String stateData;

    private LocalDateTime savedAt;
}
