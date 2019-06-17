package cn.edu.neu.shop.pin.service;

import cn.edu.neu.shop.pin.mapper.PinSettingsExpressMapper;
import cn.edu.neu.shop.pin.model.PinSettingsExpress;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpressService {

    private final PinSettingsExpressMapper pinSettingsExpressMapper;

    public ExpressService(PinSettingsExpressMapper pinSettingsExpressMapper) {
        this.pinSettingsExpressMapper = pinSettingsExpressMapper;
    }

    public List<PinSettingsExpress> getExpressInfo() {
        return pinSettingsExpressMapper.selectAll();
    }
}
