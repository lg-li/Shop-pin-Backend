package cn.edu.neu.shop.pin.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "Pin-Provider")
public class ProviderService {

}
