package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum BaseCode {

    SUCCESS(0, "OK"),

    SYSTEM_ERROR(-1,"系统异常，请稍后重试"),

    UID_WORK_ID_ERROR(500,"uid_work_id设置失败"),

    PARAMETER_ERROR(10054,"参数验证异常"),
    ;

    private final Integer code;

    private String msg = "";

    BaseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg == null ? "" : this.msg;
    }

    public static String getMsg(Integer code) {
        for (BaseCode re : BaseCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re.msg;
            }
        }
        return "";
    }

    public static BaseCode getRc(Integer code) {
        for (BaseCode re : BaseCode.values()) {
            if (re.code.intValue() == code.intValue()) {
                return re;
            }
        }
        return null;
    }
}
