package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinOrderIndividualMapperTest {

    @Autowired
    PinOrderIndividualMapper pinOrderIndividualMapper;

    @Test
    public void testGetAllWithProductsByKeyWord() {
        System.out.println(new Date());
        List<PinOrderIndividual> list = pinOrderIndividualMapper.getAllWithProductsByKeyWord("", 1);
        System.out.println(new Date());
        for(PinOrderIndividual orderIndividual : list) {
            System.out.println(orderIndividual.getUserId());
        }
    }

    @Test
    public void selectByOrderGroupId() {
        System.out.println(pinOrderIndividualMapper.selectByOrderGroupId(1));
        System.out.println(pinOrderIndividualMapper.selectByOrderGroupId(1).size());
    }

    @Test
    public void testGetRecentThreeMonthsOrderIndividuals() {
        List<PinOrderIndividual> list = pinOrderIndividualMapper.getRecentThreeMonthsOrderIndividuals(1);
        for(PinOrderIndividual p : list) {
            System.out.println(p.getPayTime());
        }
    }

    @Test
    public void getOrdersByKeyWord(){
        List<PinOrderIndividual> list = pinOrderIndividualMapper.getAllWithProductsByKeyWord("沈阳",1);
        System.out.println("pause");
    }
}