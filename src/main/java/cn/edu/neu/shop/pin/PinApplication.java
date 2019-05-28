package cn.edu.neu.shop.pin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.edu.neu.shop.pin.mapper")
public class PinApplication {

    // LLG is very niubi
    public static void main(String[] args) {
        SpringApplication.run(PinApplication.class, args);
    }

}
