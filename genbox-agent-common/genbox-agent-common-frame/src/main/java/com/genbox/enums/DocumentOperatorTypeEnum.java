package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentOperatorTypeEnum {
    SYSTEM(1, "系统"),
    USER(2, "用户"),
    ADMIN(3, "管理员");

    private final Integer code;

    private final String msg;

    DocumentOperatorTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentOperatorTypeEnum getRc(Integer code) {
        for (DocumentOperatorTypeEnum item : DocumentOperatorTypeEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
