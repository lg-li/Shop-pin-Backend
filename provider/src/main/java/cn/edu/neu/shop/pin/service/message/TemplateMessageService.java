package cn.edu.neu.shop.pin.service.message;

import cn.edu.neu.shop.pin.model.*;
import cn.edu.neu.shop.pin.service.OrderItemService;
import cn.edu.neu.shop.pin.service.StoreService;
import cn.edu.neu.shop.pin.service.WechatUserService;
import cn.edu.neu.shop.pin.util.PinConstants;
import cn.edu.neu.shop.pin.util.WechatConstants;
import cn.edu.neu.shop.pin.util.base.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ccm on 2019/04/10.
 */
@Service
public class TemplateMessageService extends AbstractService<PinMiniProgramFormIdRecord> {

    private static Logger logger = Logger.getLogger(TemplateMessageService.class);

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private WechatUserService wechatUserService;

    @Override
    public Integer save(PinMiniProgramFormIdRecord model) {
        if (model.getFormId().equals("the formId is a mock one"))
            return null;
        return super.save(model);
    }

    public PinMiniProgramFormIdRecord findByFormIdWechatUserId(Integer wechatUserId) {
        PinMiniProgramFormIdRecord formIdRecord = new PinMiniProgramFormIdRecord();
        formIdRecord.setWechatUserId(wechatUserId);
        List<PinMiniProgramFormIdRecord> formIdRecordList = mapper.select(formIdRecord);
        if (formIdRecordList != null && formIdRecordList.size() > 0) {
            formIdRecord = formIdRecordList.get(0);
            mapper.deleteByPrimaryKey(formIdRecord.getId());
            return formIdRecord;
        } else {
            return null;
        }
    }

    /**
     * 商户名
     * {{keyword1.DATA}}
     *
     * 商品名称
     * {{keyword2.DATA}}
     *
     * 订单号码
     * {{keyword3.DATA}}
     *
     * 支付时间
     * {{keyword4.DATA}}
     *
     * 订单金额
     * {{keyword5.DATA}}
     *
     * 支付方式
     * {{keyword6.DATA}}
     *
     * 订单状态
     * {{keyword7.DATA}}
     * @param orderIndividual
     * @return
     */
    public boolean sendPaymentSuccessMessageToIndividualOrderOwner(PinOrderIndividual orderIndividual) {
        PinStore store = storeService.getStoreById(orderIndividual.getStoreId());
        PinWechatUser wechatUser = wechatUserService.findWechatUserByUserId(orderIndividual.getUserId());
        if (wechatUser == null) {
            logger.info("用户不存在微信对应，跳过推送");
            return false;
        }
        JSONObject jsonObject = TemplateMessageFactory.generateOrderPaymentSuccessMessage(
                wechatUser.getOpenId(),
                "pages/splash-screen/splash-screen?action=toOrderIndividualDetail&orderIndividualId=" + orderIndividual.getId(),
                findByFormIdWechatUserId(wechatUser.getId()).getFormId(),
                store.getName(),
                "Pin-" + orderIndividual.getId() + "号个人单",
                orderIndividual.getId().toString(),
                orderIndividual.getPayTime().toString(),
                orderIndividual.getTotalPrice().toString(),
                orderIndividual.getPayType().equals(PinOrderIndividual.PAY_TYPE_BALANCE)?"余额支付":"微信支付",
                orderIndividual.getStatus().equals(PinOrderIndividual.STATUS_DEPENDING_TO_SHIP)?"待发货":"已付款");
        return sendTemplateMessage(wechatUser, jsonObject);
    }


    /**
     * 商家
     * {{keyword1.DATA}}
     *
     * 购买时间
     * {{keyword2.DATA}}
     *
     * 发货时间
     * {{keyword3.DATA}}
     *
     * 快递公司
     * {{keyword4.DATA}}
     *
     * 收货人电话
     * {{keyword5.DATA}}
     *
     * 收货人
     * {{keyword6.DATA}}
     *
     * 收货地址
     * {{keyword7.DATA}}
     * @param orderIndividual
     * @return
     */
    public boolean sendOrderShippedMessageToIndividualOrderOwner(PinOrderIndividual orderIndividual) {
        PinStore store = storeService.getStoreById(orderIndividual.getStoreId());
        PinWechatUser wechatUser = wechatUserService.findWechatUserByUserId(orderIndividual.getUserId());
        if (wechatUser == null) {
            logger.info("用户不存在微信对应，跳过推送");
            return false;
        }
        JSONObject jsonObject = TemplateMessageFactory.generateOrderShippedMessage(
                wechatUser.getOpenId(),
                "pages/splash-screen/splash-screen?action=toOrderIndividualDetail&orderIndividualId=" + orderIndividual.getId(),
                findByFormIdWechatUserId(wechatUser.getId()).getFormId(),
                store.getName(),
                "Pin-" + orderIndividual.getId() + "号个人单",
                orderIndividual.getPayTime().toString(),
                orderIndividual.getDeliverTime().toString(),
                orderIndividual.getDeliveryName() == null ? "未使用快递发货":orderIndividual.getDeliveryName(),
                orderIndividual.getReceiverPhone(),
                orderIndividual.getReceiverName(),
                orderIndividual.getDeliveryAddress());
        return sendTemplateMessage(wechatUser, jsonObject);
    }


    /**
     * 商家名称
     * {{keyword1.DATA}}
     * <p>
     * 成团人数
     * {{keyword2.DATA}}
     * <p>
     * 商品名称
     * {{keyword3.DATA}}
     * <p>
     * 订单金额
     * {{keyword4.DATA}}
     * <p>
     * 收货地址
     * {{keyword5.DATA}}
     * <p>
     * 拼团奖励
     * {{keyword6.DATA}}
     */
    public boolean sendGroupSuccessfullyClosedMessageToIndividualOrderOwner(Integer peopleCountInGroup, PinOrderIndividual orderIndividual, PinOrderGroup orderGroup) {
        PinStore store = storeService.getStoreById(orderGroup.getStoreId());
        PinWechatUser wechatUser = wechatUserService.findWechatUserByUserId(orderIndividual.getUserId());
        if (wechatUser == null) {
            logger.info("用户不存在微信对应，跳过推送");
            return false;
        }
        JSONObject jsonObject = TemplateMessageFactory.generateGroupSuccessfullyClosedMessage(
                wechatUser.getOpenId(),
                "pages/splash-screen/splash-screen?action=toOrderIndividualDetail&orderIndividualId=" + orderIndividual.getId(),
                findByFormIdWechatUserId(wechatUser.getId()).getFormId(),
                store.getName(),
                peopleCountInGroup.toString(),
                "Pin-" + orderIndividual.getId() + "号个人单",
                orderIndividual.getTotalPrice().toString(),
                orderIndividual.getDeliveryAddress(),
                "");
        return sendTemplateMessage(wechatUser, jsonObject);
    }


    /**
     * 订单号
     * {{keyword1.DATA}}
     * <p>
     * 商户名称
     * {{keyword2.DATA}}
     * <p>
     * 确认时间
     * {{keyword3.DATA}}
     * <p>
     * 订单金额
     * {{keyword4.DATA}}
     * <p>
     * 商品名称
     * {{keyword5.DATA}}
     * <p>
     * 订单信息
     * {{keyword6.DATA}}
     *
     * @param orderIndividual
     * @return
     */
    public boolean sendConfirmReceiptMessageToIndividualOrderOwner(PinOrderIndividual orderIndividual) {
        PinStore store = storeService.getStoreById(orderIndividual.getStoreId());
        PinWechatUser wechatUser = wechatUserService.findWechatUserByUserId(orderIndividual.getUserId());
        if (wechatUser == null) {
            logger.info("用户不存在微信对应，跳过推送");
            return false;
        }
        JSONObject jsonObject = TemplateMessageFactory.generateGroupSuccessfullyClosedMessage(
                wechatUser.getOpenId(),
                "pages/splash-screen/splash-screen?action=toOrderIndividualDetail&orderIndividualId=" + orderIndividual.getId(),
                findByFormIdWechatUserId(wechatUser.getId()).getFormId(),
                orderIndividual.getId().toString(),
                store.getName(),
                new Date().toString(),
                orderIndividual.getTotalPrice().toString(),
                "Pin-" + orderIndividual.getId() + "号个人单",
                "拼团返现已经充值到您的账户，您可使用余额继续畅享优惠拼团购。");
        return sendTemplateMessage(wechatUser, jsonObject);
    }

    private synchronized boolean sendTemplateMessage(PinWechatUser wechatUser, JSONObject jsonObject) {
        JSONObject result = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + getAccessToken(), jsonObject, JSONObject.class);
        if (result != null) {
            if (result.getInteger("errcode").equals(0)) {
                logger.info("微信用户" + wechatUser.getOpenId() + " 模板消息发送成功。");
                return true;
            } else {
                logger.error("微信用户" + wechatUser.getOpenId() + " 模板消息发送失败，返回结果: " + result.toString());
                return false;
            }
        } else {
            logger.error("微信用户" + wechatUser.getOpenId() + " 模板消息发送失败，原因: 网络出错返回结果为空");
            return false;
        }
    }

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public String getAccessToken() {
        // 取redis数据
        String key = WechatConstants.WECHAT_ACCESS_TOKEN;
        String accessToken = redisTemplate.boundValueOps(key).get();
        if (accessToken != null) {
            return accessToken;
        }

        // 通过接口取得access_token
        JSONObject jsonObject = restTemplate.getForObject(MessageFormat.format(WechatConstants.BASE_ACCESS_TOKEN, PinConstants.WECHAT_APP_ID, PinConstants.WECHAT_SECRET_KEY), JSONObject.class);
        String newAccessToken = (String) jsonObject.get("access_token");
        if (StringUtils.isNotBlank(newAccessToken)) {
            redisTemplate.boundValueOps(key).setIfAbsent(newAccessToken);
            redisTemplate.boundValueOps(key).expire(3600, TimeUnit.SECONDS);
            return newAccessToken;
        } else {
            logger.error("获取微信accessToken出错，微信返回信息为：" + jsonObject.toString());
        }
        return null;
    }
}
