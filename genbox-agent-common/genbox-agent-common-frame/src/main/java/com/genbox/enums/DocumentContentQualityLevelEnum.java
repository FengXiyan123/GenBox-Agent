package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentContentQualityLevelEnum {
    UNKNOWN(0, "未知"),
    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高");

    private final Integer code;

    private final String msg;

    DocumentContentQualityLevelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentContentQualityLevelEnum getRc(Integer code) {
        for (DocumentContentQualityLevelEnum item : DocumentContentQualityLevelEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
