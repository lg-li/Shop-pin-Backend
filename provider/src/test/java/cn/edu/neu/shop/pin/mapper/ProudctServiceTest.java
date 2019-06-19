package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.service.ProductService;
import com.alibaba.fastjson.JSONObject;
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
public class ProudctServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testGetProductByIdWithOneComment() {
        JSONObject jsonObject = productService.getProductByIdWithOneComment(14);
        System.out.println(jsonObject.get("product"));
        System.out.println(jsonObject.get("comment"));
    }

}
