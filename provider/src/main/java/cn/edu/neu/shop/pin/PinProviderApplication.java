package cn.edu.neu.shop.pin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

//@EnableEurekaClient
@SpringBootApplication
@MapperScan("cn.edu.neu.shop.pin.mapper")
@EnableScheduling
public class PinProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinProviderApplication.class, args);
    }

}
