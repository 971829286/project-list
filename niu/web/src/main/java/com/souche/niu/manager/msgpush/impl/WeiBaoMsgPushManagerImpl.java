package com.souche.niu.manager.msgpush.impl;

import com.google.common.collect.Maps;
import com.souche.niu.constant.Constants;
import com.souche.niu.manager.carmaintenance.MaintenanceRecordManager;
import com.souche.niu.manager.msgpush.WeiBaoMsgPushManager;
import com.souche.niu.model.maintenance.MaintenanceRecordDO;
import com.souche.niu.spi.MsgPushSPI;
import com.souche.niu.vo.MsgPushVO;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.Base64Util;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.user.hessian.api.UserAPI;
import com.souche.user.hessian.model.UserObject;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: niu
 * @ClassName: WeiBaoMsgPushManagerImpl
 * @description: 对维保返回结果进行消息推送
 * @author: malin
 * @create: 2018-09-26 11:20
 **/

@Service
public class WeiBaoMsgPushManagerImpl implements WeiBaoMsgPushManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiBaoMsgPushManagerImpl.class);

    @Autowired
    private MaintenanceRecordManager maintenanceRecordManager;

    @Autowired
    private MsgPushSPI msgPushSPI;

    /**
     * @Description:
     * @Param1: msgPushContent
     * @Param2: orderId
     * @return: void
     * @Author: malin
     * @Date: 2018/9/26
     */
    @Override
    public void MsgPush(String msgPushContent, String orderId) {
        LOGGER.info("维保结果开始发起消息推送 发送参数 msgPushContent={},orderId={}", msgPushContent, orderId);
        /*请求头封装对象*/
        List<Header> headers = new ArrayList<>();
        String title;
        String bodyText;
        String content;
        /*初始化消息推送字段值对象*/
        MsgPushVO msgPushVO = init();

        try {
            if ("success".equals(msgPushContent.toLowerCase())) {
                title = orderId + "订单查询完成";
                bodyText = "您的保养记录查询已完成，点击进入APP中查看";
                content = orderId + "订单保养记录查询已完成，请查看";
            } else {
                title = orderId + "订单查询失败";
                bodyText = "您的保养记录查询失败，查询费用退换至支付用户，具体失败原因点击进入APP查看,5分钟后可再次查询";
                content = orderId + "订单保养记录查询失败，请查看";
            }

            Map<String, String> map = getMsgPushCardDef(title, bodyText);

            String msgPushCardDef = JsonUtils.toJson(map);
            LOGGER.info("维保消息推送内容 msgPushCardDef={}", msgPushContent);

            /*
             * 请求头的封装
             * */
            headers.add(new BasicHeader("Authorization", "Basic " +
                    Base64Util.encrypt(msgPushVO.getMsgPushUser() + ":" + msgPushVO.getMsgPushPass())));

            String userId = getUserId(orderId);
            if (StringUtil.isEmpty(userId)) {
                throw new RuntimeException("维保用户不存在");
            }

            Map<String, String> params = getRequestParams(userId, content, msgPushCardDef, msgPushVO);

            LOGGER.info("维保消息推送内容 params={}", JsonUtils.toJson(params));

            String result = msgPushSPI.msgPushFunction(msgPushVO.getMsgPushUrl(), params, headers);
            if (StringUtil.isEmpty(result)) {
                throw new RuntimeException("消息推送接口调用异常");
            }
            Map resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (!("SUCCESS".equals(resultMap.get("msg")))) {
                throw new RuntimeException("维保消息推送失败");
            }
            LOGGER.info("维保消息推送成功 返回结果 result={}", result);
        } catch (Exception e) {
            LOGGER.error("维保推送消息异常", e);
        }
    }

    /**
     * @Description:
     * @Param1:
     * @return: com.souche.niu.vo.MsgPushVO
     * @Author: malin
     * @Date: 2018/9/26
     */
    public MsgPushVO init() {
        MsgPushVO vo = new MsgPushVO(OptimusConfig.getValue("wb.msg.push.user"),
                OptimusConfig.getValue("wb.msg.push.pass"),
                OptimusConfig.getValue("wb.msg.push.url"),
                OptimusConfig.getValue("wb.msg.push.sound"),
                OptimusConfig.getValue("wb.msg.push.channel"),
                OptimusConfig.getValue("wb.msg.push.type"),
                OptimusConfig.getValue("wb.msg.push.mode"),
                OptimusConfig.getValue("wb.msg.push.cardType"),
                OptimusConfig.getValue("wb.msg.push.protocol"));

        LOGGER.info("消息获取properties配置字段信息 msgPushVO={}", JsonUtils.toJson(vo));

        return vo;
    }

    /**
     * @Description:
     * @Param1: userId
     * @Param2: msgPushContent
     * @Param3: msgPushCardDef
     * @Param4: vo
     * @Author: malin
     * @Date: 2018/9/26
     */
    public Map<String, String> getRequestParams(String userId, String content, String msgPushCardDef, MsgPushVO vo) {
        /*请求参数封装对象*/
        Map<String, String> params = Maps.newHashMap();

        /*
         * 请求实体的封装
         * */
        params.put("content", content);
        params.put("sound", vo.getMsgPushSound());
        params.put("userId", userId);
        params.put("channel", vo.getMsgPushChannel());
        params.put("type", vo.getMsgPushType());
        params.put("mode", vo.getMsgPushMode());
        params.put("cardType", vo.getMsgPushCardType());
        params.put("cardDef", msgPushCardDef);
        params.put("protocol", vo.getMsgPushProtocol());
        return params;
    }

    /**
     * @Description:
     * @Param1: title
     * @Param2: bodyText
     * @Author: malin
     * @Date: 2018/9/26
     */
    public Map<String, String> getMsgPushCardDef(String title, String bodyText) {

        Map<String, String> map = Maps.newHashMap();
        map.put("title", title);
        map.put("titleViceRight", "");
        map.put("bodyText", bodyText);
        map.put("bodyLink", "");
        map.put("isShowFooter", "false");
        return map;
    }

    /**
     * @Description:
     * @Param1: orderId
     * @return: java.lang.String
     * @Author: malin
     * @Date: 2018/9/26
     */
    @Override
    public String getUserId(String orderId) {
        LOGGER.info("根据orderId参数查询用户ID 传入参数orderId={}", orderId);
        try {
            MaintenanceRecordDO carMaintenance = maintenanceRecordManager.getCarMaintenance(orderId);
            String phone = carMaintenance.getPhone();
            Map<String, String> mapParam = new HashMap<>();
            mapParam.put("phone", phone);
            String params = JsonUtils.toJson(mapParam);
            UserObject user = msgPushSPI.findByQuery(params, Constants.USERAPI_FINDONE_APPNAME);
            if (user == null) {
                throw new RuntimeException("查询user结果异常为空");
            }
            LOGGER.info("查询userId的结果 userId={}", user.getId());
            return user.getId();
        } catch (Exception e) {
            LOGGER.error("查询用户Id异常", e);
            return null;
        }
    }

}
