package com.genbox.ai.chatagent.rag.model;

/**
 * 文档导航动作。
 */
public enum DocumentNavigationAction {
    TOPIC_CONTINUE,
    TOPIC_SWITCH,
    FRESH_TOPIC,
    SIBLING_SECTION_SWITCH,
    CHILD_SECTION_DESCEND,
    ANCESTOR_SECTION_RETURN,
    ITEM_REFERENCE,
    SECTION_ADJACENCY_LOOKUP
}
