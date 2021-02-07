package com.ldg.app.usercenter.annotation;

import com.ldg.app.exception.UnauthenticationException;
import com.ldg.app.exception.UnauthorizationException;
import com.ldg.app.usercenter.jjwt.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author ldg
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckAuthorizationAspect {
    /**
     * jwt服务
     */
    private final JwtOperator jwtOperator;

    /**
     * 认证切面
     * 只切入controller包下的类
     * *    com.ldg.app.usercenter.controller.*.      *       (..)
     * 返回值     类(cotrller当前包下的类:不包括子包)     方法名   方法参数
     *
     * @param point
     * @return
     * @throws
     */
    @Around("@annotation(com.ldg.app.usercenter.annotation.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        checkToken();
        return point.proceed();

    }


    /**
     * 授权切面
     * 如果存在@checkAuthorization注解就会执行该判断方法
     *
     * @param point
     * @return
     */
    @Around("@annotation(com.ldg.app.usercenter.annotation.CheckAuthorization)")
    public Object checkAuthority(ProceedingJoinPoint point) throws Throwable {
        checkToken();
        HttpServletRequest request = getServletRequest();
        String role = (String) request.getAttribute("role");
        //拿到的其实时MethodSignature
        MethodSignature signature = (MethodSignature) point.getSignature();
        String value = signature.getMethod().getAnnotation(CheckAuthorization.class).value();
        if (!Objects.equals(value, role)) {
            throw new UnauthorizationException("授权失败");
        }
        return point.proceed();
    }

    private void checkToken() throws UnauthenticationException {
        //1.从header里面获取token
        //静态获取request
        //2.校验token是否合法,如果不合法,直接抛异常
        HttpServletRequest request = getServletRequest();
        String token = request.getHeader("X-Token");
        try {
            jwtOperator.validateToken(token);
            //如果校验成功就将用户信息设置到request中
            Claims claims = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id", claims.get("id"));
            request.setAttribute("role", claims.get("role"));
            request.setAttribute("wxNickname", claims.get("wxNickname"));

        } catch (Exception e) {
            throw new UnauthenticationException("认证失败");
        }
    }

    private HttpServletRequest getServletRequest() {
        //静态获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

}
