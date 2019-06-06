package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinUserAddressMapper;
import cn.edu.neu.shop.pin.model.PinUserAddress;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService extends AbstractService<PinUserAddress> {

    public static final int STATUS_DELETE_ADDRESS_SUCCESS = 0;
    public static final int STATUS_DELETE_ADDRESS_INVALID_ID = -1;
    public static final int STATUS_DELETE_ADDRESS_PERMISSION_DENIED = -2;

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

    /**
     * 根据用户Id创建地址
     * @param userId
     * @param realName
     * @param phone
     * @param province
     * @param city
     * @param district
     * @param detail
     * @param postCode
     * @return
     */
    @Transactional
    public PinUserAddress createAddressByUserId(Integer userId, String realName, String phone, String province, String city, String district, String detail, Integer postCode) {
        PinUserAddress pinUserAddress = assignAddress(userId, realName, phone, province, city, district, detail, postCode);
        save(pinUserAddress);
        return pinUserAddress;
    }

    /**
     * 根据用户Id删除地址
     * @param addressId
     * @param currentUserId
     * @return
     */
    @Transactional
    public int deleteAddressByUserId(Integer addressId, Integer currentUserId) {
        PinUserAddress pinUserAddress = findById(addressId);
        if (pinUserAddress == null) {
            return STATUS_DELETE_ADDRESS_INVALID_ID;
        }
        if (pinUserAddress.getUserId().equals(currentUserId)) {
            // 有权限
            deleteById(addressId);
            return STATUS_DELETE_ADDRESS_SUCCESS;
        } else {
            return STATUS_DELETE_ADDRESS_PERMISSION_DENIED;
        }
    }

    /**
     * 根据用户Id更新地址
     * @param currentUserId
     * @param pinUserAddress
     * @return
     */
    @Transactional
    public PinUserAddress updateAddressByUserId(Integer currentUserId, PinUserAddress pinUserAddress) {
        if (pinUserAddress.getUserId().equals(currentUserId)) {
            // 有权限
            update(pinUserAddress);
        } else {
            return null;
        }
        return pinUserAddress;
    }

    /**
     * 修改地址
     * @param userId
     * @param realName
     * @param phone
     * @param province
     * @param city
     * @param district
     * @param detail
     * @param postCode
     * @return
     */
    private PinUserAddress assignAddress(Integer userId, String realName, String phone, String province, String city, String district, String detail, Integer postCode) {
        PinUserAddress pinUserAddress = new PinUserAddress();
        pinUserAddress.setUserId(userId);
        pinUserAddress.setRealName(realName);
        pinUserAddress.setPhone(phone);
        pinUserAddress.setProvince(province);
        pinUserAddress.setCity(city);
        pinUserAddress.setDistrict(district);
        pinUserAddress.setDetail(detail);
        pinUserAddress.setPostCode(postCode);
        return pinUserAddress;
    }
}