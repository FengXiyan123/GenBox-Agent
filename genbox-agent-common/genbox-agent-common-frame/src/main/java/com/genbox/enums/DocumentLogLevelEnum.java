package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentLogLevelEnum {
    INFO(1, "INFO"),
    WARN(2, "WARN"),
    ERROR(3, "ERROR");

    private final Integer code;

    private final String msg;

    DocumentLogLevelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentLogLevelEnum getRc(Integer code) {
        for (DocumentLogLevelEnum item : DocumentLogLevelEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
