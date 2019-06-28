package cn.edu.neu.shop.pin.consumer.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign配置
 * 使用FeignClient进行服务间调用，传递headers信息
 */
@Component
public class HeaderInjector implements RequestInterceptor {

    private final static String KEY_AUTHORIZATION = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        System.out.println("Intercepter" + attributes.toString());
        if(attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            //添加token
            requestTemplate.header(KEY_AUTHORIZATION, request.getHeader(KEY_AUTHORIZATION));
        }
    }
}