package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentStrategyExecuteStatusEnum {
    WAIT_EXECUTE(1, "待执行"),
    EXECUTING(2, "执行中"),
    EXECUTE_SUCCESS(3, "执行成功"),
    EXECUTE_FAILED(4, "执行失败"),
    SKIPPED(5, "已跳过");

    private final Integer code;

    private final String msg;

    DocumentStrategyExecuteStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentStrategyExecuteStatusEnum getRc(Integer code) {
        for (DocumentStrategyExecuteStatusEnum item : DocumentStrategyExecuteStatusEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
