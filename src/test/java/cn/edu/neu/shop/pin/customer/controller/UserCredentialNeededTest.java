package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.PinApplication;
import cn.edu.neu.shop.pin.customer.service.security.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= PinApplication.class)
@EnableAutoConfiguration
public abstract class UserCredentialNeededTest {

    @Autowired
    public WebApplicationContext context;

    @Autowired
    protected UserService userService;

    protected String token;

    protected MockMvc mvc;

   @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        token = userService.signIn(1, "liyifei99");
    }

}
