package com.ldg.app.usercenter.controller.aop;

import com.ldg.app.enums.ReslutCode;
import com.ldg.app.exception.UnauthenticationException;
import com.ldg.app.exception.UnauthorizationException;
import com.ldg.app.response.ReslutDto;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ldg
 * 统一的异常处理
 */
@RestControllerAdvice
public class ControllerExceptionAdvice {
    /**
     * 处理授权异常
     * 403 没有权限
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizationException.class)
    public ResponseEntity<ReslutDto> unauthorizationExceptionHandler(UnauthorizationException e) {
        return new ResponseEntity<>(
                ReslutDto.builder()
                        .msg(e.getMessage())
                        .code(ReslutCode.Forbidden.getCode())
                        .data(ReslutCode.Forbidden.getMsg())
                        .build()
                , HttpStatus.FORBIDDEN
        );
    }


    /**
     * 处理认证异常
     * 401 没有认证
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthenticationException.class)
    public ResponseEntity<ReslutDto> Handler(UnauthenticationException e) {
        return new ResponseEntity<>(
                ReslutDto.builder()
                        .msg(e.getMessage())
                        .code(ReslutCode.Unauthorized.getCode())
                        .data(ReslutCode.Unauthorized.getMsg())
                        .build()
                , HttpStatus.UNAUTHORIZED
        );
    }


    /**
     * 处理空参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NullArgumentException.class)
    public ResponseEntity<ReslutDto> nullArgumentExceptionHandler(NullArgumentException e) {
        return new ResponseEntity<>(
                ReslutDto.builder()
                        .msg(e.getMessage())
                        .code(ReslutCode.NotFound.getCode())
                        .data(ReslutCode.NotFound.getMsg())
                        .build()
                , HttpStatus.NOT_FOUND
        );
    }


}
