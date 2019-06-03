package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.model.PinOrderGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author flyhero
 *
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinOrderGroupMapperTest {

    @Autowired
    private PinOrderGroupMapper pinOrderGroupMapper;

    @Test
    public void testGetTopTenOrderGroups() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
        List<PinOrderGroup> list = pinOrderGroupMapper.getTopTenOrderGroups(3);
        for(PinOrderGroup g : list) {
            System.out.println("order_group_id: " + g.getId());
            System.out.println("owner_user_id: " + g.getOwnerUserId());
            System.out.println("owner_user_avatar_url: " + g.getPinUser().getAvatarUrl());
            System.out.println("owner_user_nickname: " + g.getPinUser().getNickname());
            System.out.println("store_id: " + g.getStoreId());
            System.out.println("status: " + g.getStatus());
            System.out.println("create_time: " + g.getCreateTime());
            System.out.println("close_time: " + g.getCloseTime());
            System.out.println("actual_finish_time: " + g.getId());
            System.out.println("total_amount_of_money_paid: " + g.getTotalAmountOfMoneyPaid());
        }
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
    }
}
