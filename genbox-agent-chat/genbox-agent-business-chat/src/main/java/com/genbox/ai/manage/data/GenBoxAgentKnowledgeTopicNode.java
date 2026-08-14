package com.genbox.ai.manage.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.genbox.database.data.BaseTableData;

/**
 * 数据实体。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("genbox_agent_knowledge_topic_node")
@EqualsAndHashCode(callSuper = true)
public class GenBoxAgentKnowledgeTopicNode extends BaseTableData {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String topicCode;

    private String topicName;

    private String scopeCode;

    private String description;

    private String aliases;

    private String examples;

    private String answerShape;

    private String executionPreference;

    private Integer sortOrder;
}
