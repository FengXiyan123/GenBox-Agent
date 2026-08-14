package com.genbox.exception;

import lombok.Data;

/**
 * 异常类。
 */
@Data
public class ArgumentError {

	private String argumentName;

	private String message;
}
