package com.ldg.app.usercenter.service.impl;

import com.ldg.app.entity.BonusEventLog;
import com.ldg.app.usercenter.mapper.BonusMapper;
import com.ldg.app.usercenter.service.BonusEventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ldg
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BonusEventLogServiceImp implements BonusEventLogService {

    private final BonusMapper bonusMapper;

    @Override
    public BonusEventLog queryById(Integer id) {
        return bonusMapper.selectById(id);
    }

    @Override
    public List<BonusEventLog> queryAllByLimit(int offset, int limit) {
        return null;
    }

    @Override
    public BonusEventLog insert(BonusEventLog bonusEventLog) {
        return null;
    }

    @Override
    public BonusEventLog update(BonusEventLog bonusEventLog) {
        return null;
    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }
}
