package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentVectorStatusEnum {
    WAIT_VECTOR(1, "待向量化"),
    VECTORIZING(2, "向量化中"),
    VECTOR_SUCCESS(3, "向量化成功"),
    VECTOR_FAILED(4, "向量化失败");

    private final Integer code;

    private final String msg;

    DocumentVectorStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentVectorStatusEnum getRc(Integer code) {
        for (DocumentVectorStatusEnum item : DocumentVectorStatusEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
