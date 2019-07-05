//package cn.edu.neu.shop.pin.customer.controller;
//
//import cn.edu.neu.shop.pin.PinApplication;
//import org.junit.TestController;
//import org.junit.runner.RunWith;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = PinApplication.class)
//@WebAppConfiguration
//@EnableAutoConfiguration
//public class OrderControllerTest extends UserCredentialNeededTest {
//
//    @TestController
//    public void testGetTopTenOrderGroupsByStoreId() throws Exception {
//        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/order-group/by-store-id/3")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//
//        System.out.println("返回的json=" + result);
//    }
//
//    @TestController
//    public void testGetAllOrderItems() throws Exception {
//        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/order/get-order-items/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//
//        System.out.println("返回的json=" + result);
//    }
//
//    @TestController
//    public void testAddOrderItem() throws Exception {
//        String result = mvc.perform(MockMvcRequestBuilders.get("/commons/order-item/add")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//
//        System.out.println("返回的json=" + result);
//    }
//
//    @TestController
//    public void getGroupOrderInfo() throws Exception {
//        String result;
//        result = mvc.perform(MockMvcRequestBuilders.get("/commons/order/beg-group-order").param("orderGroupId", "1")
//                .header("Authorization", "Bearer " + token)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//
//        System.out.println("返回的json=" + result);
//    }
//}