package com.ldg.app.usercenter.controller;

import com.ldg.app.entity.BonusEventLog;
import com.ldg.app.usercenter.service.BonusEventLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 积分变更记录表(BonusEventLog)表控制层
 *
 * @author makejava
 * @since 2021-01-20 16:51:03
 */
@RestController
@RequestMapping("bonusEventLog")
public class BonusEventLogController {
    /**
     * 服务对象
     */
    @Resource
    private BonusEventLogService bonusEventLogService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public BonusEventLog selectOne(Integer id) {
        return this.bonusEventLogService.queryById(id);
    }

}