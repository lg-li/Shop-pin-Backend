//package cn.edu.neu.shop.pin.controller;
//
//import cn.edu.neu.shop.pin.PinApplication;
//import cn.edu.neu.shop.pin.customer.controller.UserCredentialNeededTest;
//import cn.edu.neu.shop.pin.service.security.UserService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = PinApplication.class)
//@WebAppConfiguration
//@EnableAutoConfiguration
//public class SignUpControllerTest {
//    @Autowired
//    public WebApplicationContext context;
//
//    @Autowired
//    UserService userService;
//    private MockMvc mvc;
//
//    @Before
//    public void setUp() throws Exception {
//        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
//    }
//    @Test
//    public void signUpDefault() throws Exception {
//        String result = mvc.perform(MockMvcRequestBuilders.post("/sign-up/default")
//                .param("phone", "13312425345")
//                .param("email", "978345907@qq.mail")
//                .param("password", "woxihuanhaohao")
//                .param("avatarUrl", "https://LeeGen.com/img/1.jpg")
//                .param("nickname", "haohao")
//                .param("currentIp", "179.23.35.0")
//                .param("gender", "1")
//                .param("roles", "[0,1]")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//        System.out.println("返回的json=" + result);
//    }
//}