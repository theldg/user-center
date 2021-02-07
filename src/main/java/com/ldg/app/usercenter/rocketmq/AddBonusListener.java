package com.ldg.app.usercenter.rocketmq;

import com.ldg.app.dto.UserAddBonusMsgDto;
import com.ldg.app.entity.BonusEventLog;
import com.ldg.app.entity.User;
import com.ldg.app.usercenter.mapper.BonusMapper;
import com.ldg.app.usercenter.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 监听MQ消息
 *
 * @author ldg
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusListener {
    private final UserMapper userMapper;
    private final BonusMapper bonusMapper;

    @StreamListener(Sink.INPUT)
    public void onMessage(UserAddBonusMsgDto userAddBonusMsgDto) {
        //1.为用户加积分
        Integer userId = userAddBonusMsgDto.getUserId();
        log.info("userId:{}", userId);
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("MQ中userId参数异常");
        }
        Integer bonus = userAddBonusMsgDto.getBonus();
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateById(user);
        //2.记录日志到bonus_event_log表里
        bonusMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event("投稿")
                        .createTime(new Date())
                        .description("投稿增加积分")
                        .build());

        log.info("投稿积分添加完成");
    }
}
