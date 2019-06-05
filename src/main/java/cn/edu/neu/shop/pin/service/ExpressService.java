package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinSettingsExpressMapper;
import cn.edu.neu.shop.pin.model.PinSettingsExpress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpressService {

    @Autowired
    private PinSettingsExpressMapper pinSettingsExpressMapper;

    public List<PinSettingsExpress> getExpressInfo() {
        return pinSettingsExpressMapper.selectAll();
    }
}
