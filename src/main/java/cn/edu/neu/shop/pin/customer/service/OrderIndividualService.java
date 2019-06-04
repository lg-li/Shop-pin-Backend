package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import cn.edu.neu.shop.pin.model.PinOrderIndividual;
import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderIndividualService extends AbstractService<PinOrderIndividual> {
    @Autowired
    UserRoleListTransferService userRoleListTransferService;

    /** 传入一串PinOrderIndividual，返回它们对应的用户list
     * @param list 一串PinOrderIndividual
     * @return 返回它们对应的用户list
     */
    public List<PinUser> getUsers(List<PinOrderIndividual> list){
        ArrayList<PinUser> users = new ArrayList<>();
        for (PinOrderIndividual item:list){
            users.add(userRoleListTransferService.findById(item.getUserId()));
        }
        return users;
    }

}
