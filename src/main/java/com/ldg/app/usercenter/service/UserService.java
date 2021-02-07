package com.ldg.app.usercenter.service;

import com.ldg.app.dto.UserLoginDto;
import com.ldg.app.entity.User;

import java.util.List;

/**
 * 分享(User)表服务接口
 *
 * @author makejava
 * @since 2021-01-20 16:50:17
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 判读用户是已经否注册
     *
     * @param loginDto
     * @param openId
     * @return
     */
    User login(UserLoginDto loginDto, String openId);
}