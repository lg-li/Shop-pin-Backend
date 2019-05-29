package cn.edu.neu.shop.pin.customer.controller;

import cn.edu.neu.shop.pin.customer.service.security.UserService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class UserCredentialNeededTest {

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected UserService userService;

    protected String token;

    protected MockMvc mvc;

    @Before
    public void setUp() throws Exception{
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        token = userService.signIn(1, "liyifei99");
    }

}
