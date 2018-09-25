package cn.ourwill.huiyizhan.service;


import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.TrxOrder;
import cn.ourwill.huiyizhan.mapper.ActivityMapper;
import cn.ourwill.huiyizhan.utils.WillCenterConstants;
import cn.ourwill.huiyizhan.weChat.Utils.MessageUtil;
import cn.ourwill.huiyizhan.weChat.Utils.PageAuthorizeUtils;
import cn.ourwill.huiyizhan.weChat.pojo.Article;
import cn.ourwill.huiyizhan.weChat.pojo.NewsMessage;
import cn.ourwill.huiyizhan.weChat.pojo.TextMessage;
import cn.ourwill.huiyizhan.weChat.pojo.UserInfoReturn;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * @Package: cn.ourwill.huiyizhan.weChat.pojo
 * @Author: LiuFeng
 * @Description:
 * @Date: Created in 2018/4/26
 */
@Service
public class CoreService {

    @Autowired
    private ITrxOrderService orderService;
    @Autowired
    private ActivityMapper activityMapper;

    @Value("${weixin.my.ticket}")
    private String myTicket;

    @Value("${weixin.img.ticket.big}")
    private String imgTicketBig;

    @Value("${weixin.img.ticket.small}")
    private String imgTicketSmall;

    @Value("${weixin.ticket.detail.url}")
    private String ticketDetailUrl;

    @Autowired
    private WillCenterConstants willCenter;
    @Autowired
    private IUserService userService;

    public  String processRequest(String msg) {
        String respMessage = "success";
        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";

            // xml请求解析
            Map requestMap = MessageUtil.parseXml(msg);

            System.out.println("Event=="+requestMap.get("Event"));

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName").toString();
            // 公众帐号
            String toUserName = requestMap.get("ToUserName").toString();
            // 消息类型
            String msgType = requestMap.get("MsgType").toString();



            //-----------------------------------------------------------
            // 回复文本消息 //暂时默认回复这个
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            //-----------------------------------------------------------

            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                String content = requestMap.get("Content").toString();
                respContent = "映目活动提示：您发送的是文本消息！内容是："+content;
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
            // 图片消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "映目活动提示：您发送的是图片消息！";
            }
            // 地理位置消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "映目活动提示：您发送的是地理位置消息！";
            }
            // 链接消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "映目活动提示：您发送的是链接消息！";
            }
            // 音频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "映目活动提示：您发送的是音频消息！";
            }
            // 视频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VODEO)) {
                respContent = "映目活动提示：您发送的是视频消息！";
            }
            // 小音频消息
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVODEO)) {
                respContent = "映目活动提示：您发送的是小视频消息！";
            }// 事件推送
            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event").toString();
                NewsMessage newsMessage = new NewsMessage();
                // 自定义菜单点击事件
                if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_CLICK)) {
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = requestMap.get("EventKey").toString();
                    if(myTicket.equals(eventKey)){
                        List<TrxOrder> trxOrders=orderService.selectByOpenId(fromUserName);
                        if(trxOrders!=null && trxOrders.size()>0){
                            List<Article> articleList = new ArrayList<>();
                            List<TrxOrder> onlytrxOrders=new ArrayList<TrxOrder>();
                            HashMap<Integer,TrxOrder> hashMap=new HashMap<Integer, TrxOrder>();
                            for (TrxOrder trxOrder:trxOrders){
                                int key=trxOrder.getActivityId();
                                if (!hashMap.containsKey(key)){
                                    hashMap.put(key,trxOrder);
                                }
                            }
                            for (int key:hashMap.keySet()){
                                onlytrxOrders.add(hashMap.get(key));
                            }
                            if (onlytrxOrders.size()>8){
                                onlytrxOrders=onlytrxOrders.subList(0,8);
                            }
                            for (int i = 0 ;i< onlytrxOrders.size();i++){
                                Activity activity=activityMapper.selectByPrimaryKey(onlytrxOrders.get(i).getActivityId());
                                if(activity==null) continue;
                                Article article = new Article();
                                if(i == 0){
                                    article.setPicUrl(imgTicketBig);
                                }else{
                                    article.setPicUrl(imgTicketSmall);
                                }

                                article.setUrl(ticketDetailUrl+onlytrxOrders.get(i).getActivityId()+"?openId="+fromUserName);
                                article.setDescription("");
                                article.setTitle(activity.getActivityTitle());
                                articleList.add(article);
                            }
                            if(articleList.size()<1){
                                respContent = "映目活动提示：您还未关注或者报名该活动！请您参加活动后，再来领取电子票!";
                                respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                            }else {
                                newsMessage.setFuncFlag(0);
                                newsMessage.setArticleCount(articleList.size());
                                newsMessage.setArticles(articleList);
                                newsMessage.setCreateTime(new Date().getTime());
                                newsMessage.setFromUserName(toUserName);
                                newsMessage.setToUserName(fromUserName);
                                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                                respMessage = MessageUtil.newsMessageToXml(newsMessage);
                            }
                        }else{
                            respContent = "映目活动提示：您还未关注或者报名该活动！请您参加活动后，再来领取电子票!";
                            respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                        }
                    }

                    // 扫一扫事件
                }else if(eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_SUBSCRIBE) ||
                        eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_SCAN)){


                    String eventKey = requestMap.get("EventKey").toString();
                    if(eventKey.contains("qrscene_")){
                        eventKey = eventKey.replace("qrscene_","");
                    }

                    String orderNo = "";
                    //如果包含TR_则是获取电子票操作
                    if(eventKey.contains("TR_")){
                        orderNo = eventKey.replace("TR_","");
                        if (orderNo==null || "".equals(orderNo)){
                            respContent = "映目活动提示：欢迎关注映目活动!";
                            respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                            return respMessage;
                        }

                        TrxOrder order = orderService.getByNo(orderNo);
                        if(order != null){
                            String openId = order.getOpenId();
                            if(StringUtils.isEmpty(openId)||openId.equalsIgnoreCase(fromUserName)){
                                if(StringUtils.isEmpty(openId)){
                                    order.setOpenId(fromUserName);
                                    orderService.updateOrderOpenIdByNO(order);
                                }
                                Activity activity=activityMapper.selectByPrimaryKey(order.getActivityId());
                                List<Article> articleList = new ArrayList<>();
                                Article article = new Article();
                                article.setPicUrl(imgTicketBig);
                                article.setUrl(ticketDetailUrl+order.getActivityId()+"?openId="+fromUserName);
                                article.setDescription("");
                                article.setTitle(activity.getActivityTitle());
                                articleList.add(article);

                                newsMessage.setFuncFlag(0);
                                newsMessage.setArticleCount(articleList.size());
                                newsMessage.setArticles(articleList);
                                newsMessage.setCreateTime(new Date().getTime());
                                newsMessage.setFromUserName(toUserName);
                                newsMessage.setToUserName(fromUserName);
                                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                                respMessage = MessageUtil.newsMessageToXml(newsMessage);
                            }else{
                                respContent = "映目活动提示：您还未关注或者报名该活动！请您参加活动后，再来领取电子票!";
                                respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                            }
                        }else{

                            respContent = "映目活动提示：您还未关注或者报名该活动！请您参加活动后，再来领取电子票!";
                            respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                        }
                    }else if (eventKey.contains("BIND_")){//包含BIND_为微信绑定操作
                        String access_token = PageAuthorizeUtils.getAccess_token(false);
                        String uuid = eventKey.replace("BIND_","");;
                        if (uuid==null || "".equals(uuid)){
                            respContent = "获取不到用户信息，绑定失败";
                            respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                            return respMessage;
                        }
                        //通过openid获取unionid
                        UserInfoReturn userInfoReturn = PageAuthorizeUtils.getUserInfo(access_token,fromUserName);
                        String unionId = userInfoReturn.getUnionid();
                        if(unionId==null || "".equals(unionId)){
                            respContent = "获取不到微信用户信息，绑定失败";
                            respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                            return respMessage;
                        }
                        //更新huiyizhan的user的unionId
                        Integer count = userService.changeUnionIdByUuid(unionId,uuid);
                        if (count > 0){
                            //更新willchenter 的 user的unionId
                            JSONObject jspnParam = new JSONObject();
                            jspnParam.put("uuid", uuid);
                            jspnParam.put("unionid", unionId);
                            StringEntity s = new StringEntity(jspnParam.toString(),"UTF-8");
                            s.setContentType("application/json");
                            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                            HttpPost post = new HttpPost(willCenter.addUnionIdById());
                            ObjectMapper mapper = new ObjectMapper();
                            post.setEntity(s);
                            CloseableHttpResponse reponse = httpClient.execute(post);
                            if (reponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                HttpEntity entity = reponse.getEntity();
                                InputStream inputStream = entity.getContent();
                                String res = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                                Map resMap = mapper.readValue(res, Map.class);
                                String code = resMap.get("code").toString();
                                if ("0".equals(code)) {
                                    respContent = "绑定成功！！！";
                                } else {
                                    respContent = resMap.get("msg").toString();
                                }
                                respMessage = sendErrorTextMessage(fromUserName,toUserName,respContent);
                            }
                        }
                    }else if(eventKey.contains("自定义参数类型")){ //如果包含 自定义参数类型 则是对应类型操作

                    }
                }else if(eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_VIEW)){
                    respMessage = "视图跳转";
                }else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    respMessage = "取消关注";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            respMessage="有异常了。。。";
        }
        return respMessage;
    }

    private String sendErrorTextMessage(String toUserName,String fromUserName,String respContent){
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(toUserName);
        textMessage.setFromUserName(fromUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);
        textMessage.setContent(respContent);
        return MessageUtil.textMessageToXml(textMessage);
    }

}
