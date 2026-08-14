package com.genbox.ai.chatagent.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据实体。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("GRAPH_THREAD")
public class GraphThread {

    @TableId(value = "thread_id", type = IdType.INPUT)
    private String threadId;

    private String threadName;

    @TableField("is_released")
    private Boolean released;
}
