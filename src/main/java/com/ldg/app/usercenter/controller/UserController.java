package com.ldg.app.usercenter.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.ldg.app.dto.JwtTokenRespDto;
import com.ldg.app.dto.LoginRespDto;
import com.ldg.app.dto.UserLoginDto;
import com.ldg.app.dto.UserRespDto;
import com.ldg.app.entity.User;
import com.ldg.app.enums.ReslutCode;
import com.ldg.app.response.ReslutDto;
import com.ldg.app.usercenter.jjwt.JwtOperator;
import com.ldg.app.usercenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 分享(User)表控制层
 *
 * @author makejava
 * @since 2021-01-20 16:50:19
 */
@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    /**
     * 服务对象
     */
    private final UserService userService;
    /**
     * 微信提供的服务
     */
    private final WxMaService wxMaService;

    /**
     * token服务
     */
    private final JwtOperator jwtOperator;

    /**
     * 通过主键查询单条数据
     * 为啥必须要写出这样ResponseEnity<ReslutDto>才能被Feign调用??
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<ReslutDto> selectOne(@PathVariable Integer id) {
        log.info("我被请求了");
        User user = this.userService.queryById(id);
        if (user == null) {
            throw new NullArgumentException("user");
        } else {
            return new ResponseEntity<>(
                    ReslutDto.builder()
                            .code(ReslutCode.Ok.getCode())
                            .data(user)
                            .msg(ReslutCode.Ok.getMsg())
                            .build(), HttpStatus.OK
            );

        }
    }

    /**
     * 用户登录
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public LoginRespDto login(@RequestBody UserLoginDto loginDto) throws WxErrorException {
        //1.用code请求微信API,验证是否已登录微信小程序
        //2.如果用户已注册,则颁发token,并返回用户信息;如果未注册则注册,颁发token,并返回用户信息
        //微信小程序校验是否已经登录的结果
        log.info("wx:{}", wxMaService);
        WxMaJscode2SessionResult result = wxMaService
                .getUserService()
                .getSessionInfo(loginDto.getCode());
        //用户在微信上的唯一标识
        String openId = result.getOpenid();
        User user = userService.login(loginDto, openId);
        //颁发token
        Map<String, Object> userInfo = new HashMap<>(3);
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("role", user.getRoles());
        String token = jwtOperator.generateToken(userInfo);
        Date expirationDateFromToken = jwtOperator.getExpirationDateFromToken(token);
        log.info("用户:{} 登录成功!,生成的Token:{},有效期到:{}", user.getWxNickname(), token, expirationDateFromToken);

        LoginRespDto loginRespDto = LoginRespDto.builder()
                .user(UserRespDto.builder()
                        .avatarUrl(user.getAvatarUrl())
                        .bonus(user.getBonus())
                        .wxNickname(user.getWxNickname())
                        .id(user.getId())
                        .build())
                .token(JwtTokenRespDto.builder()
                        .token(token)
                        .expirationTime(expirationDateFromToken.getTime())
                        .build())
                .build();
        log.info("登录响应:{}", loginRespDto);
        return loginRespDto;

    }

}