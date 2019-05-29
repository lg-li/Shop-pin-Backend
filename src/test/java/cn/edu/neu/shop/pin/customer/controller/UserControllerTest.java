package cn.edu.neu.shop.pin.customer.controller;


import cn.edu.neu.shop.pin.PinApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PinApplication.class)
@WebAppConfiguration
public class UserControllerTest extends UserCredentialNeededTest {

    @Test
    public void testGetAddressByUserId() throws Exception {
        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/user/address")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        System.out.println("返回的json=" + result);
    }
}
