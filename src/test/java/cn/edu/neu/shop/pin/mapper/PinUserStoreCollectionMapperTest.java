package cn.edu.neu.shop.pin.mapper;

import java.util.List;
import cn.edu.neu.shop.pin.model.PinUserStoreCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author flyhero
 */

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinUserStoreCollectionMapperTest {

    @Autowired
    PinUserStoreCollectionMapper pinUserStoreCollectionMapper;

    @Test
    public void testGetUserProductCollection() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
        List<PinUserStoreCollection> list = pinUserStoreCollectionMapper.getUserStoreCollection(2);
        for(PinUserStoreCollection p : list) {
            System.out.println("id: " + p.getId());
            System.out.println("userId: " + p.getUserId());
            System.out.println("storeId: " + p.getStoreId());
            System.out.println("store: ");
            System.out.println("--storeName: " + p.getStore().getName());
            System.out.println("--logoUrl: " + p.getStore().getLogoUrl());
            System.out.println("createTime: " + p.getCreateTime());
        }
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
    }

}