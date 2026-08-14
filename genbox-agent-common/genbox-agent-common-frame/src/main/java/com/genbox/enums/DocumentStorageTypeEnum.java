package com.genbox.enums;

/**
 * 枚举定义。
 */
public enum DocumentStorageTypeEnum {
    MINIO(1, "MinIO");

    private final Integer code;

    private final String msg;

    DocumentStorageTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public static DocumentStorageTypeEnum getRc(Integer code) {
        for (DocumentStorageTypeEnum item : DocumentStorageTypeEnum.values()) {
            if (item.code.intValue() == code.intValue()) {
                return item;
            }
        }
        return null;
    }
}
