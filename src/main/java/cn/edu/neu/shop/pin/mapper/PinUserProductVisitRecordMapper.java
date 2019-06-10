package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUserProductVisitRecord;
import cn.edu.neu.shop.pin.util.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface PinUserProductVisitRecordMapper extends BaseMapper<PinUserProductVisitRecord> {

    Integer getNumberOfVisitRecord(Date fromTime, Date toTime, Integer storeId);
}