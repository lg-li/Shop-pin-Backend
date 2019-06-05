package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.PinApplication;
import cn.edu.neu.shop.pin.customer.service.ProductService;
import cn.edu.neu.shop.pin.model.PinProduct;
import java.util.List;

import cn.edu.neu.shop.pin.model.PinProductAttributeDefinition;
import cn.edu.neu.shop.pin.model.PinProductAttributeValue;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes= PinApplication.class)
//@EnableAutoConfiguration

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinProductMapperTest {

    @Autowired
    private PinProductMapper pinProductMapper;

    @Autowired
    private ProductService productService;

//    @Autowired
//    private ProductService productService;

    @Test
    public void testPage() {
        PageHelper.startPage(1, 10);
        List<PinProduct> list = pinProductMapper.getHotProducts();
        System.out.println("Size: " + list.size());
        for(PinProduct p : list) {
            System.out.println();
            System.out.println(p);
            System.out.println();
        }
    }

    @Test
    public void testGetProductById() {
        PinProduct p = productService.getProductById(1);
        System.out.println("p: " + p);
        System.out.println("###########################");
        System.out.println("");

        System.out.println("Product: " + p.getName());
        List<PinProductAttributeDefinition> defList = p.getPinProductAttributeDefinitions();
        System.out.println("def: " + defList.size());
        for(PinProductAttributeDefinition pp : defList) {
            System.out.println(pp.getAttributeValues());
        }

        List<PinProductAttributeValue> valList = p.getPinProductAttributeValues();
        System.out.println("val: " + valList.size());
        for(PinProductAttributeValue pp : valList) {
            System.out.println(pp.getSku());
        }

        System.out.println("");
        System.out.println("###########################");
    }
}
