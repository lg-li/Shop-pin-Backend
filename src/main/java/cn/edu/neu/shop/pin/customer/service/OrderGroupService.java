package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinOrderIndividualMapper;
import cn.edu.neu.shop.pin.model.PinOrderGroup;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author flyhero
 * @Description 获取OrderGroup（当前团单信息）表中的内容
 */
@Service
public class OrderGroupService {
    @Autowired
    PinOrderIndividualMapper individualMapper;
    @Autowired
    OrderIndividualService orderIndividualService;

    /**
     * 传入orderGroup 返回拼单的人
     * @param orderGroup orderGroup
     * @return 拼单的人的list
     */
    public List<PinUser> getUsersByOrderGroup(PinOrderGroup orderGroup) {
        List<PinOrderIndividual> individuals = individualMapper.selectByOrderGroupId(orderGroup.getId());
        return orderIndividualService.getUsers(individuals);
    }

}
