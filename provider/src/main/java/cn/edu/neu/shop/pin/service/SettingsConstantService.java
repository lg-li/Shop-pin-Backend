package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.model.PinSettingsConstant;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class SettingsConstantService extends AbstractService<PinSettingsConstant> {

    public String findByKey(String key) {
        PinSettingsConstant constant = new PinSettingsConstant();
        constant.setConstantKey(key);
        return mapper.selectOne(constant).getConstantValue();
    }
}
