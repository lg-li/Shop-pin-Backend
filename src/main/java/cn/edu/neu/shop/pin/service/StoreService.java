package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinStoreMapper;
import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreService extends AbstractService<PinStore> {

    private final PinStoreMapper pinStoreMapper;

    public StoreService(PinStoreMapper pinStoreMapper) {
        this.pinStoreMapper = pinStoreMapper;
    }

    /**
     * 根据店铺ID 获取该店铺的详细信息
     * @param storeId 店铺ID
     * @return PinStore类
     */
    public PinStore getStoreById(Integer storeId) {
        return pinStoreMapper.selectByPrimaryKey(storeId);
    }

    /**
     * 根据店铺所有者的id返回店铺的list
     * @param id 店铺所有者的id
     * @return store list
     */
    public List<PinStore> getStoreListByOwnerId(Integer id) {
        return pinStoreMapper.selectByOwnerId(id);
    }

    /**
     * 管理端用户店主新增店铺
     * @param name 姓名
     * @param description 秒数
     * @param phone 手机号
     * @param email 邮箱
     * @param logoUrl 图标地址
     * @param userId 用户ID
     * @return 返回店铺信息
     */
    @Transactional
    public PinStore addStoreInfo(String name, String description, String phone, String email, String logoUrl, Integer userId) {
        PinStore pinStore = new PinStore();
        pinStore.setName(name);
        pinStore.setDescription(description);
        pinStore.setPhone(phone);
        pinStore.setEmail(email);
        pinStore.setLogoUrl(logoUrl);
        pinStore.setOwnerUserId(userId);
        save(pinStore);
        return pinStore;
    }

    /**
     * 管理端 修改店铺信息
     * @param pinStore 店铺
     * @return 返回店铺信息
     */
    @Transactional
    public PinStore updateStoreInfo(PinStore pinStore) {
        update(pinStore);
        return pinStore;
    }
}
