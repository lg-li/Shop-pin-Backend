package cn.edu.neu.shop.pin.consumer.factory;

import cn.edu.neu.shop.pin.consumer.config.HeaderInjector;
import cn.edu.neu.shop.pin.consumer.service.admin.AdminCommentControllerService;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

public class FeignClientFactory {

    public static <T> T getFeignClient(Decoder decoder, Encoder encoder, Client client, Class<T> targetClass) {
        return Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new HeaderInjector())
                .target(targetClass, "http://Pin-Provider");
    }
}
