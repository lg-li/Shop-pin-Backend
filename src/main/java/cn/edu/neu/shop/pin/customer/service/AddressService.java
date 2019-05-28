package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserAddressMapper;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AddressService extends AbstractService<PinUserAddress> {
    @Autowired
    PinUserAddressMapper pinUserAddressMapper;

    /**
     * 根据用户ID 查询该用户的收获地址
     * @param userId
     * @return
     */
    public List<PinUserAddress> getAllAddressesByUserId(Integer userId) {
        PinUserAddress pinUserAddress = new PinUserAddress();
        pinUserAddress.setUserId(userId);
        return pinUserAddressMapper.select(pinUserAddress);
    }

    public PinUserAddress createAddressByUserId(){

    }
}
