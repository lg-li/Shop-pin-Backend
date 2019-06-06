package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserCreditRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinUserCreditRecordMapperTest {

    @Autowired
    PinUserCreditRecordMapper pinUserCreditRecordMapper;

    @Test
    public void testGetCheckInDaysNum() {
        System.out.println("Days: " + pinUserCreditRecordMapper.getCheckInDaysNum(1));
    }

    @Test
    public void testGetCheckInDaysInfo() {
        List<PinUserCreditRecord> list = pinUserCreditRecordMapper.getCheckInDaysInfo(1);
        for(PinUserCreditRecord p : list) {
            System.out.println("id: " + p.getId());
            System.out.println("userId: " + p.getUserId());
            System.out.println("valueChange: " + p.getValueChange());
            System.out.println("type: " + p.getType());
            System.out.println("createTime: " + p.getCreateTime());
            System.out.println("note: " + p.getNote());
        }
    }
}
