package cn.edu.neu.shop.pin.customer.controller;


import cn.edu.neu.shop.pin.PinApplication;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PinApplication.class)
public class AdminStoreControllerTest extends UserCredentialNeededTest{

    @Test
    public void testGetProductByStoreId() throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/store/1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        System.out.println("返回的json=" + result);
    }

    @Test
    public void testGetStoreInfoByStoreId() throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/store/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

        System.out.println("返回的json=" + result);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        assert (jsonObject.getInteger("code").equals(200));
        JSONObject data = jsonObject.getJSONObject("data");
        // 数据部分
        assert data.getString("name").equals("NIKE官方旗舰店");
    }

}
