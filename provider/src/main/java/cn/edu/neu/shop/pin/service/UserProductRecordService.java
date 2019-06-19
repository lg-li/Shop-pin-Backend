package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserProductVisitRecordMapper;
import cn.edu.neu.shop.pin.model.PinUserProductVisitRecord;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductRecordService extends AbstractService<PinUserProductVisitRecord> {

    private final PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper;

    public UserProductRecordService(PinUserProductVisitRecordMapper pinUserProductVisitRecordMapper) {
        this.pinUserProductVisitRecordMapper = pinUserProductVisitRecordMapper;
    }

    /**
     * 根据userId获取商品浏览记录
     *
     * @param userId 用户ID
     * @return 用户访问记录List
     */
    public List<PinUserProductVisitRecord> getUserProductVisitRecord(Integer userId) {
        PinUserProductVisitRecord pinUserProductVisitRecord = new PinUserProductVisitRecord();
        pinUserProductVisitRecord.setId(userId);
        return pinUserProductVisitRecordMapper.select(pinUserProductVisitRecord);
    }
}
