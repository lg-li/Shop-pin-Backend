package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.PinApplication;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.mapper.PinUserProductCollectionMapper;
import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PinApplication.class)
@WebAppConfiguration
public class UserProductCollectionTest {

    @Autowired
    PinUserProductCollectionMapper pinUserProductCollectionMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testGetUserProductCollection() throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/collection/user-product-collection/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        System.out.println("返回的json=" + result);
//        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
//        assert (jsonObject.getInteger("code").equals(200));
//        JSONObject data = jsonObject.getJSONObject("data");
//        // 数据部分
//        assert data.getString("name").equals("NikeAirForce1");
//        System.out.println();
//        System.out.println("##################################################");
//        System.out.println();
//        List<PinUserProductCollection> list = pinUserProductCollectionMapper.getUserProductCollection(1);
//        for(PinUserProductCollection p : list) {
//            System.out.println("id: " + p.getId());
//            System.out.println("userId: " + p.getUserId());
//            System.out.println("productId: " + p.getProductId());
//            System.out.println("product: ");
//            System.out.println("--prouctName: " + p.getProduct().getName());
//            System.out.println("--imageUrls: " + p.getProduct().getImageUrls());
//            System.out.println("--price: " + p.getProduct().getPrice());
//            System.out.println("storeId: " + p.getProduct().getStore().getId());
//            System.out.println("store: ");
//            System.out.println("--storeName: " + p.getProduct().getStore().getName());
//            System.out.println("--logoUrl: " + p.getProduct().getStore().getLogoUrl());
//            System.out.println("createTime: " + p.getCreateTime());
//        }
//        System.out.println();
//        System.out.println("##################################################");
//        System.out.println();
    }
}