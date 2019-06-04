package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProductAttributeValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinProductAttributeValueMapperTest {

    @Autowired
    private PinProductAttributeValueMapper pinProductAttributeValueMapper;

    @Autowired
    private PinProductAttributeDefinitionMapper pinProductAttributeDefinitionMapper;

    @Test
    @Rollback(false)//防止事务自动回滚
    public void testInsertProduct() {

        pinProductAttributeValueMapper.insertProductAttributeValue(2, "aa",
                2, BigDecimal.valueOf(2.00), "http://a/", BigDecimal.valueOf(5.5)
        );
    }
}
