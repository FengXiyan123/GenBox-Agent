package com.baidu.fsg.uid.exception;

/**
 * 异常类。
 */
public class UidGenerateException extends RuntimeException {

    private static final long serialVersionUID = -27048199131316992L;

    public UidGenerateException() {
        super();
    }

    public UidGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UidGenerateException(String message) {
        super(message);
    }

    public UidGenerateException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    public UidGenerateException(Throwable cause) {
        super(cause);
    }

}
