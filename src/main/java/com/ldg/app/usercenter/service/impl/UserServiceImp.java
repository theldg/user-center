package com.ldg.app.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.ldg.app.dto.UserLoginDto;
import com.ldg.app.entity.User;
import com.ldg.app.usercenter.mapper.UserMapper;
import com.ldg.app.usercenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
/**推荐使用这种方式来避免idea读取不到bean**/
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImp implements UserService {

    /**
     * @Autowired idea不能智能识别mybatis的注解认为bean为null, 而且spring不推荐直接在field中使用
     **/
    private final UserMapper userMapper;

    @Override
    public User queryById(Integer id) {
        if (id != null) {
            return userMapper.selectById(id);
        } else {
            return null;
        }
    }

    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return null;
    }

    @Override
    public User insert(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }


    @Override
    public User login(UserLoginDto loginDto, String openId) {

        User user = userMapper.selectOne(
                new Wrapper<User>() {
                    @Override
                    public User getEntity() {
                        return User.builder()
                                .wxId(openId)
                                .build();
                    }

                    @Override
                    public MergeSegments getExpression() {
                        return null;
                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public String getSqlSegment() {
                        return null;
                    }
                }
        );
        if (Objects.isNull(user)) {
            user = User.builder()
                    .wxId(openId)
                    .updateTime(new Date())
                    .createTime(new Date())
                    .wxNickname(loginDto.getWxNickname())
                    .roles("user")
                    .avatarUrl(loginDto.getAvatarUrl())
                    .bonus(300)
                    .build();
            userMapper.insert(user);
        }
        return user;
    }
}
