package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentPlanSourceEnum {
    SYSTEM_RECOMMEND(1, "系统推荐"),
    USER_ADJUST(2, "用户调整");

    private final Integer code;

    private final String msg;

    DocumentPlanSourceEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentPlanSourceEnum getRc(Integer code) {
        for (DocumentPlanSourceEnum item : DocumentPlanSourceEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
