package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductVisitRecordMapper;
import cn.edu.neu.shop.pin.model.PinUserProductVisitRecord;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRecordService extends AbstractService<PinUserProductVisitRecord> {

    @Autowired
    private PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;

    public List<PinUserProductVisitRecord> getUserProductVisitRecord(Integer userId) {
        PinUserProductVisitRecord pinUserProductVisitRecord = new PinUserProductVisitRecord();
        pinUserProductVisitRecord.setId(userId);
        return pinUserProductVisitRecordMapper.select(pinUserProductVisitRecord);
    }
}
