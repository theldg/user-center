package com.ldg.app.usercenter.controller.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldg.app.enums.ReslutCode;
import com.ldg.app.response.ReslutDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author ldg
 * 统一的响应处理
 */
@RestControllerAdvice(basePackages = {"com.ldg.app.usercenter.controller"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //response是ResponseEntity类型或者注释了@NotControllerRes
        return !methodParameter.getParameterType().isAssignableFrom(ResponseEntity.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // String类型不能直接包装(内部会有问题)？？
        if (methodParameter.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            // 将数据包装在ResultVo里后转换为json串进行返回
            try {
                return objectMapper.writeValueAsString(new ResponseEntity<>(
                        ReslutDto.builder().data(data).code(ReslutCode.Ok.getCode()).msg(ReslutCode.Ok.getMsg()).build()
                        , HttpStatus.OK));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
        return new ResponseEntity<>(
                ReslutDto.builder().data(data).code(ReslutCode.Ok.getCode()).msg(ReslutCode.Ok.getMsg()).build()
                , HttpStatus.OK);
    }
}
