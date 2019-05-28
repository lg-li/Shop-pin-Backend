package cn.edu.neu.shop.pin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan(value = {"cn.edu.neu.shop.pin.mapper"})
public class PinApplication {

    // LLG is very niubi
    public static void main(String[] args) {
        SpringApplication.run(PinApplication.class, args);
    }

}
