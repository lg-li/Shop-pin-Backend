package cn.edu.neu.shop.pin.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class PinCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinCenterApplication.class, args);
    }

}
