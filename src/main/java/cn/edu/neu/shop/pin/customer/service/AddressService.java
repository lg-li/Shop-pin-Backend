package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserAddressMapper;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public PinUserAddress createAddressByUserId(Integer userId, String realName, String phone, String province, String city, String district, String detail, Integer postCode){
        PinUserAddress pinUserAddress = new PinUserAddress();
        pinUserAddress.setUserId(userId);
        pinUserAddress.setRealName(realName);
        pinUserAddress.setPhone(phone);
        pinUserAddress.setProvince(province);
        pinUserAddress.setCity(city);
        pinUserAddress.setDistrict(district);
        pinUserAddress.setDetail(detail);
        pinUserAddress.setPostCode(postCode);
        save(pinUserAddress);
        return pinUserAddress;
    }
}
