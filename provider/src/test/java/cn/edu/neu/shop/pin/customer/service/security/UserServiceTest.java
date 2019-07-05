//package cn.edu.neu.shop.pin.customer.service.security;
//
//import cn.edu.neu.shop.pin.PinApplication;
//import cn.edu.neu.shop.pin.service.security.UserService;
//import org.junit.TestController;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes= PinApplication.class)
//@EnableAutoConfiguration
//public class UserServiceTest {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserService userService;
//
//    @TestController
//    public void signin() {
//        /*System.out.println(passwordEncoder.encode("liyifei99"));*/
//        String token = userService.signIn(1, "liyifei99");
//    }
//
//    @TestController
//    public void signup() {
//    }
//
//    @TestController
//    public void delete() {
//    }
//
//    @TestController
//    public void search() {
//    }
//
//    @TestController
//    public void whoami() {
//    }
//
//    @TestController
//    public void refresh() {
//    }
//}