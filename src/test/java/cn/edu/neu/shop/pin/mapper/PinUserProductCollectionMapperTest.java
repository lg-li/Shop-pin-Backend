package cn.edu.neu.shop.pin.mapper;

import static org.junit.Assert.*;

import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinUserProductCollectionMapperTest {
    @Autowired
    PinUserProductCollectionMapper pinUserProductCollectionMapper;

    @Test
    public void testGetUserProductCollection() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
        List<PinUserProductCollection> list = pinUserProductCollectionMapper.getUserProductCollection(1);
        for(PinUserProductCollection p : list) {
            System.out.println("id: " + p.getId());
            System.out.println("userId: " + p.getUserId());
            System.out.println("productId: " + p.getProductId());
            System.out.println("product: ");
            System.out.println("--prouctName: " + p.getProduct().getName());
            System.out.println("--imageUrls: " + p.getProduct().getImageUrls());
            System.out.println("--price: " + p.getProduct().getPrice());
            System.out.println("storeId: " + p.getProduct().getStore().getId());
            System.out.println("store: ");
            System.out.println("--storeName: " + p.getProduct().getStore().getName());
            System.out.println("--logoUrl: " + p.getProduct().getStore().getLogoUrl());
            System.out.println("createTime: " + p.getCreateTime());
        }
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
    }

}