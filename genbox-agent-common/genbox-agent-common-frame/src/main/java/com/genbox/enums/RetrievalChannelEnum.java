package com.genbox.enums;

import lombok.Getter;

/**
 * 枚举定义。
 */
public enum RetrievalChannelEnum {

    KEYWORD(1, "keyword","关键词检索"),

    VECTOR(2, "vector","向量检索"),

    RERANK(3,"rerank","重排序");

    @Getter
    private final int code;
    @Getter
    private final String name;
    @Getter
    private final String desc;

    RetrievalChannelEnum(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public static RetrievalChannelEnum fromCode(Integer code) {
        if (code == null) {
            throw new IllegalArgumentException("检索通道类型 code 不能为空");
        }
        for (RetrievalChannelEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的检索通道类型 code: " + code);
    }
}
