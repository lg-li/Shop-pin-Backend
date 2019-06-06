package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserCreditRecord;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PinUserCreditRecordMapper extends BaseMapper<PinUserCreditRecord> {

    Integer getCheckInDaysNum(Integer userId);

    List<PinUserCreditRecord> getCheckInDaysInfo(Integer userId);
}