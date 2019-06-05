package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinProduct;
import cn.edu.neu.shop.pin.model.PinProductAttributeDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinProductAttributeValueMapperTest {

    @Autowired
    private PinProductAttributeValueMapper pinProductAttributeValueMapper;

    @Autowired
    private PinProductAttributeDefinitionMapper pinProductAttributeDefinitionMapper;

    @Autowired
    private PinProductMapper pinProductMapper;

    @Test
    @Rollback(false)//防止事务自动回滚
    public void testInsertProduct() {
        Set<Integer> ss = new HashSet<>();
        ss.add(1);ss.add(2);ss.add(4);ss.add(5);
        ss.add(14);ss.add(15);ss.add(16);ss.add(17);
        for(int i = 1; i <= 20; i++) {
            if(ss.contains(i)) continue;
            List<PinProductAttributeDefinition> list = getDefinitions(i);
            if(list.size() == 1) insertWithOneAttributes(i);
            else if(list.size() == 2) insertWithTwoAttributes(i);
            else if(list.size() == 3) insertWithThreeAttributes(i);
            else if(list.size() == 4) insertWithFourAttributes(i);
            else if(list.size() == 5) insertWithFiveAttributes(i);
        }
    }
    
    public void insertWithOneAttributes(Integer productId) {
        System.out.println("******* Inserting into proucts with 1 lines ********");
        System.out.println();

        PinProduct product = pinProductMapper.getProductById(productId);
        Integer stock = product.getStockCount();
        BigDecimal price = product.getPrice();
        String imageUrl = "xxx";
        BigDecimal cost = product.getCost();
        print(productId, stock, price, cost); // 显示结果
        List<String> list = getAttributes(productId); // 得到属性集
        String[] arr0 = list.get(0).split(";");
        for(int i = 0; i < arr0.length; i++) {
            String tmp = arr0[i];
            pinProductAttributeValueMapper.insertProductAttributeValue(
                    productId, tmp, stock, price, imageUrl, cost);
        }
        System.out.println();
        System.out.println("##############################################");
        System.out.println();
    }
    
    public void insertWithTwoAttributes(Integer productId) {
        System.out.println("******* Inserting into proucts with 2 lines ********");
        System.out.println();

        PinProduct product = pinProductMapper.getProductById(productId);
        Integer stock = product.getStockCount();
        BigDecimal price = product.getPrice();
        String imageUrl = "xxx";
        BigDecimal cost = product.getCost();
        print(productId, stock, price, cost); // 显示结果
        List<String> list = getAttributes(productId); // 得到属性集
        String[] arr0 = list.get(0).split(";");
        String[] arr1 = list.get(1).split(";");
        for(int i = 0; i < arr0.length; i++) {
            for(int j = 0; j < arr1.length; j++) {
                String tmp = arr0[i] + ";" + arr1[j];
                pinProductAttributeValueMapper.insertProductAttributeValue(
                        productId, tmp, stock, price, imageUrl, cost);
            }
        }
        System.out.println();
        System.out.println("##############################################");
        System.out.println();
    }

    public void insertWithThreeAttributes(Integer productId) {
        System.out.println("******* Inserting into proucts with 3 lines ********");
        System.out.println();

        PinProduct product = pinProductMapper.getProductById(productId);
        Integer stock = product.getStockCount();
        BigDecimal price = product.getPrice();
        String imageUrl = "xxx";
        BigDecimal cost = product.getCost();
        print(productId, stock, price, cost); // 显示结果
        List<String> list = getAttributes(productId); // 得到属性集
        String[] arr0 = list.get(0).split(";");
        String[] arr1 = list.get(1).split(";");
        String[] arr2 = list.get(2).split(";");
        for(int i = 0; i < arr0.length; i++) {
            for(int j = 0; j < arr1.length; j++) {
                for(int k = 0; k < arr2.length; k++) {
                    String tmp = arr0[i] + ";" + arr1[j] + ";" + arr2[k];
                    pinProductAttributeValueMapper.insertProductAttributeValue(
                            productId, tmp, stock, price, imageUrl, cost);
                }
            }
        }
        System.out.println();
        System.out.println("##############################################");
        System.out.println();
    }

    public void insertWithFourAttributes(Integer productId) {
        System.out.println("******* Inserting into proucts with 4 lines ********");
        System.out.println();

        PinProduct product = pinProductMapper.getProductById(productId);
        Integer stock = product.getStockCount();
        BigDecimal price = product.getPrice();
        String imageUrl = "xxx";
        BigDecimal cost = product.getCost();
        print(productId, stock, price, cost); // 显示结果
        List<String> list = getAttributes(productId); // 得到属性集
        String[] arr0 = list.get(0).split(";");
        String[] arr1 = list.get(1).split(";");
        String[] arr2 = list.get(2).split(";");
        String[] arr3 = list.get(3).split(";");
        for(int i = 0; i < arr0.length; i++) {
            for(int j = 0; j < arr1.length; j++) {
                for(int k = 0; k < arr2.length; k++) {
                    for(int l = 0; l < arr3.length; l++) {
                        String tmp = arr0[i] + ";" + arr1[j] + ";" + arr2[k] + ";" + arr3[l];
                        pinProductAttributeValueMapper.insertProductAttributeValue(
                                productId, tmp, stock, price, imageUrl, cost);
                    }
                }
            }
        }
        System.out.println();
        System.out.println("##############################################");
        System.out.println();
    }

    /**
     * 
     * @param productId
     */
    public void insertWithFiveAttributes(Integer productId) {
        System.out.println("******* Inserting into proucts with 5 lines ********");
        System.out.println();

        PinProduct product = pinProductMapper.getProductById(productId);
        Integer stock = product.getStockCount();
        BigDecimal price = product.getPrice();
        String imageUrl = "xxx";
        BigDecimal cost = product.getCost();
        print(productId, stock, price, cost); // 显示结果
        List<String> list = getAttributes(productId); // 得到属性集
        String[] arr0 = list.get(0).split(";");
        String[] arr1 = list.get(1).split(";");
        String[] arr2 = list.get(2).split(";");
        String[] arr3 = list.get(3).split(";");
        String[] arr4 = list.get(4).split(";");
        for(int i = 0; i < arr0.length; i++) {
            for(int j = 0; j < arr1.length; j++) {
                for(int k = 0; k < arr2.length; k++) {
                    for(int l = 0; l < arr3.length; l++) {
                        for(int m = 0; m < arr4.length; m++) {
                            String tmp = arr0[i]+";"+arr1[j]+";"+arr2[k]+";"+arr3[l]+";"+arr4[m];
                            pinProductAttributeValueMapper.insertProductAttributeValue(
                                    productId, tmp, stock, price, imageUrl, cost);
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println("##############################################");
        System.out.println();
    }
    
    public List<PinProductAttributeDefinition> getDefinitions(Integer productId) {
        PinProductAttributeDefinition pinProductAttributeDefinition = new PinProductAttributeDefinition();
        pinProductAttributeDefinition.setProductId(productId);
        List<PinProductAttributeDefinition> _list = pinProductAttributeDefinitionMapper.select(pinProductAttributeDefinition);
        return _list;
    }
    
    public List<String> getAttributes(Integer productId) {
        List<PinProductAttributeDefinition> _list = getDefinitions(productId);
        List<String> list = new ArrayList<>();
        for(PinProductAttributeDefinition _1 : _list) {
            list.add(_1.getAttributeValues());
        }
        return list;
    }

    public void print(Integer productId, Integer stock, BigDecimal price, BigDecimal cost) {
        System.out.println("To insert:");
        System.out.println("product_id: " + productId);
        System.out.println("stock: " + stock);
        System.out.println("price: " + price);
        System.out.println("cost: " + cost);
    }
}
