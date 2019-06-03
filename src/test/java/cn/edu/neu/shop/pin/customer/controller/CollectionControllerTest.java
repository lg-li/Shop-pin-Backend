package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.PinApplication;
import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.mapper.PinUserProductCollectionMapper;
import cn.edu.neu.shop.pin.mapper.PinUserStoreCollectionMapper;
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
public class CollectionControllerTest {

    @Autowired
    PinUserProductCollectionMapper pinUserProductCollectionMapper;

    @Autowired
    PinUserStoreCollectionMapper pinUserStoreCollectionMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testGetUserProductCollection() throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/user/user-product-collection/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        System.out.println("返回的json=" + result);
    }

    @Test
    public void testGetUserStoreCollection() throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/user/user-store-collection/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        System.out.println("返回的json=" + result);
    }
}