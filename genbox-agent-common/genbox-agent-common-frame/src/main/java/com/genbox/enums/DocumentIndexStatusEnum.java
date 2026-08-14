package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentIndexStatusEnum {
    WAIT_BUILD(1, "待构建"),
    BUILDING(2, "构建中"),
    BUILD_SUCCESS(3, "构建成功"),
    BUILD_FAILED(4, "构建失败");

    private final Integer code;

    private final String msg;

    DocumentIndexStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentIndexStatusEnum getRc(Integer code) {
        for (DocumentIndexStatusEnum item : DocumentIndexStatusEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
