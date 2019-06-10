package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.service.UserCreditRecordService;
import cn.edu.neu.shop.pin.service.security.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinUserCreditRecordServiceTest {

    @Autowired
    private UserCreditRecordService userCreditRecordService;

    @Autowired
    private UserService userService;

    @Test
    public void testCheckIn() throws Exception {
        System.out.println(userCreditRecordService.getContinuousCheckInDaysNum(8));
        userCreditRecordService.dailyCheckIn(8);
        System.out.println(userCreditRecordService.getUserCreditData(8));

    }
}
