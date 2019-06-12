package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinStoreMapper;
import cn.edu.neu.shop.pin.model.PinStore;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import cn.edu.neu.shop.pin.util.img.ImgUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.List;

@Service
public class StoreService extends AbstractService<PinStore> {

    @Autowired
    private PinStoreMapper pinStoreMapper;

    /**
     * 根据店铺ID 获取该店铺的详细信息
     *
     * @param storeId
     * @return PinStore类
     */
    public PinStore getStoreById(Integer storeId) {
        PinStore pinStore = new PinStore();
        pinStore.setId(storeId);
        return pinStoreMapper.selectOne(pinStore);
    }

    /**
     * 根据店铺所有者的id返回店铺的list
     *
     * @param id 店铺所有者的id
     * @return store list
     */
    public List<PinStore> getStoreListByOwnerId(Integer id) {
        return pinStoreMapper.selectByOwnerId(id);
    }

    /**
     * 管理端用户店主新增店铺
     *
     * @param name
     * @param description
     * @param phone
     * @param email
     * @param logoUrl
     * @param userId
     * @return
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
     *
     * @param pinStore
     * @return
     */
    @Transactional
    public PinStore updateStoreInfo(PinStore pinStore) {
        update(pinStore);
        return pinStore;
    }
}
