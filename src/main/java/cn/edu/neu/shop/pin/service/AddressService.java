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
    public static final int STATUS_UPDATE_ADDRESS_SUCCESS = 0;
    public static final int STATUS_UPDATE_ADDRESS_INVALID_ID = -1;
    public static final int STATUS_UPDATE_ADDRESS_PERMISSION_DENIED = -2;

    @Autowired
    PinUserAddressMapper pinUserAddressMapper;

    /**
     * @param userId
     * @return
     * @author flyhero
     * 根据用户ID 返回其默认的地址
     */
    public PinUserAddress getDefaultAddress(Integer userId) {
        PinUserAddress pinUserAddress = new PinUserAddress();
        pinUserAddress.setUserId(userId);
        pinUserAddress.setDefault(true);
        return pinUserAddressMapper.select(pinUserAddress).get(0);
    }

    /**
     * @param userId
     * @return
     * @author flyhero
     * 根据用户ID 查询该用户的收获地址
     */
    public List<PinUserAddress> getAllAddresses(Integer userId) {
        PinUserAddress pinUserAddress = new PinUserAddress();
        pinUserAddress.setUserId(userId);
        return pinUserAddressMapper.select(pinUserAddress);
    }

    /**
     * @param pinUserAddress
     * @return
     * @author flyhero
     * 根据用户Id创建地址
     */
    @Transactional
    public void createAddress(PinUserAddress pinUserAddress) {
        checkAddressIsDefaultAndDoModify(pinUserAddress);
        this.save(pinUserAddress);
    }

    /**
     * @param addressId
     * @param userId
     * @return
     * @author cqf, flyhero
     * 根据用户Id删除地址
     */
    @Transactional
    public int deleteAddress(Integer userId, Integer addressId) {
        PinUserAddress pinUserAddress = findById(addressId);
        if (pinUserAddress == null) { // 没有相应的地址记录，无法删除
            return STATUS_DELETE_ADDRESS_INVALID_ID;
        } else if (!pinUserAddress.getUserId().equals(userId)) { // 用户ID不符，无权限删除
            return STATUS_DELETE_ADDRESS_PERMISSION_DENIED;
        } else { // 正常删除
            deleteById(addressId);
            return STATUS_DELETE_ADDRESS_SUCCESS;
        }
    }

    /**
     * @param currentUserId
     * @param pinUserAddress
     * @return
     * @author flyhero
     * 根据用户Id更新地址
     */
    @Transactional
    public Integer updateAddress(Integer currentUserId, PinUserAddress pinUserAddress) {
        if (findById(pinUserAddress.getId()) == null) { // 没有相应的地址记录，无法修改
            return STATUS_UPDATE_ADDRESS_INVALID_ID;
        } else if (!pinUserAddress.getUserId().equals(currentUserId)) { // 用户ID不符，无权限修改
            return STATUS_UPDATE_ADDRESS_PERMISSION_DENIED;
        } else { // 正常修改
            checkAddressIsDefaultAndDoModify(pinUserAddress);
            update(pinUserAddress);
            return STATUS_UPDATE_ADDRESS_SUCCESS;
        }
    }

    /**
     * 检查传入的地址的isDefault是否为true，若是则将当前用户其他地址全部设成非default
     *
     * @param pinUserAddress
     */
    private void checkAddressIsDefaultAndDoModify(PinUserAddress pinUserAddress) {
        if (pinUserAddress.getDefault()) {
            pinUserAddressMapper.setAllDefaultToZero(pinUserAddress.getUserId());
        }
    }
}