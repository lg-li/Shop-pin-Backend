package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProductAttributeValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinProductAttributeValueMapperTest {

    @Autowired
    private PinProductAttributeValueMapper pinProductAttributeValueMapper;

    @Test
    public void testInsertProduct() {
        PinProductAttributeValue p = new PinProductAttributeValue();
        p.setProductId(2);
        p.setSku("aa");
        p.setStock(2);
        p.setPrice(BigDecimal.valueOf(2.00));
        p.setImageUrl("http://b.c/");
        p.setCost(BigDecimal.valueOf(5.5));
        System.out.println(pinProductAttributeValueMapper.insert(p));
//        pinProductAttributeValueMapper.insertProductAttributeValue(2, "aa",
//                2, BigDecimal.valueOf(2.00), "http://a/", BigDecimal.valueOf(5.5)
//        );
    }
}
