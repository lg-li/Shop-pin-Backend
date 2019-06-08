package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductVisitRecordMapper;
import cn.edu.neu.shop.pin.model.PinUserProductVisitRecord;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductRecordService extends AbstractService<PinUserProductVisitRecord> {

    @Autowired
    private PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;

    /**
     * 根据userId获取商品浏览记录
     *
     * @param userId
     * @return
     */
    public List<PinUserProductVisitRecord> getUserProductVisitRecord(Integer userId) {
        PinUserProductVisitRecord pinUserProductVisitRecord = new PinUserProductVisitRecord();
        pinUserProductVisitRecord.setId(userId);
        return pinUserProductVisitRecordMapper.select(pinUserProductVisitRecord);
    }
}
