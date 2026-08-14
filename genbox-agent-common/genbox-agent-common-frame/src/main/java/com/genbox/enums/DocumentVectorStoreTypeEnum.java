package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentVectorStoreTypeEnum {
    MILVUS(1, "Milvus"),
    PG_VECTOR(2, "PGVector"),
    ELASTICSEARCH(3, "Elasticsearch");

    private final Integer code;

    private final String msg;

    DocumentVectorStoreTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentVectorStoreTypeEnum getRc(Integer code) {
        for (DocumentVectorStoreTypeEnum item : DocumentVectorStoreTypeEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
