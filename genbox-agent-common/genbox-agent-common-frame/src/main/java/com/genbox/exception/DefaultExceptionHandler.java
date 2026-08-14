package com.genbox.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.genbox.common.ApiResponse;
import com.genbox.enums.BaseCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常类。
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = GenBoxAgentFrameException.class)
    public ApiResponse<String> toolkitExceptionHandler(HttpServletRequest request, GenBoxAgentFrameException genBoxAgentFrameException) {
        log.error("业务异常 错误信息 : {} method : {} url : {} query : {} ", genBoxAgentFrameException.getMessage(), request.getMethod(), getRequestUrl(request), getRequestQuery(request), genBoxAgentFrameException);
        return ApiResponse.error(genBoxAgentFrameException.getCode(), genBoxAgentFrameException.getMessage());
    }

    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse<List<ArgumentError>> validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error("参数验证异常 错误信息 : {} method : {} url : {} query : {} ", ex.getMessage(), request.getMethod(), getRequestUrl(request), getRequestQuery(request), ex);
        BindingResult bindingResult = ex.getBindingResult();
        List<ArgumentError> argumentErrorList =
                bindingResult.getFieldErrors()
                        .stream()
                        .map(fieldError -> {
                            ArgumentError argumentError = new ArgumentError();
                            argumentError.setArgumentName(fieldError.getField());
                            argumentError.setMessage(fieldError.getDefaultMessage());
                            return argumentError;
                        }).collect(Collectors.toList());
        return ApiResponse.error(BaseCode.PARAMETER_ERROR.getCode(),argumentErrorList);
    }

    @ExceptionHandler(value = Throwable.class)
    public ApiResponse<String> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        log.error("全局异常 错误信息 : {} method : {} url : {} query : {} ", throwable.getMessage(), request.getMethod(), getRequestUrl(request), getRequestQuery(request), throwable);
        return ApiResponse.error();
    }

    private String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    private String getRequestQuery(HttpServletRequest request){
        return request.getQueryString();
    }
}
