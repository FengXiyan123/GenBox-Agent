package com.genbox.exception;

import com.genbox.common.ApiResponse;
import com.genbox.enums.BaseCode;
import lombok.Data;

/**
 * 异常类。
 */
@Data
public class GenBoxAgentFrameException extends BaseException {

	private Integer code;

	private String message;

	public GenBoxAgentFrameException() {
		super();
	}

	public GenBoxAgentFrameException(String message) {
		super(message);
	}

	public GenBoxAgentFrameException(String code, String message) {
		super(message);
		this.code = Integer.parseInt(code);
		this.message = message;
	}

	public GenBoxAgentFrameException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public GenBoxAgentFrameException(BaseCode baseCode) {
		super(baseCode.getMsg());
		this.code = baseCode.getCode();
		this.message = baseCode.getMsg();
	}

	public GenBoxAgentFrameException(ApiResponse apiResponse) {
		super(apiResponse.getMessage());
		this.code = apiResponse.getCode();
		this.message = apiResponse.getMessage();
	}

	public GenBoxAgentFrameException(Throwable cause) {
		super(cause);
	}

	public GenBoxAgentFrameException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public GenBoxAgentFrameException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.message = message;
	}
}
