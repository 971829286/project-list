package cn.ourwill.tuwenzb.weixin.Utils;

import cn.ourwill.tuwenzb.weixin.pojo.Message;
import cn.ourwill.tuwenzb.weixin.pojo.MessageData;
import cn.ourwill.tuwenzb.weixin.pojo.MessageDataList;
import com.google.gson.Gson;
import com.qiniu.util.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 * 微信推送信息
 *
 * @param
 * @author My
 * @CreateDate 2016-1-19
 */
public class WeixinPushMassage {

    private static final Logger log = LogManager.getLogger(WeixinPushMassage.class);

    //微信开发者ID
    @Value("${weixin.appid}")
    private static String appid;

    //开发者密码
    @Value("${weixin.appsecret}")
    private  static String secret;

    //购买成功通知
    private static final String template_id_successful_purchase = "SIxgJB7eBgWJOzBqc8eurh8JeDGxWgUn19XM9vP-ys0";

    //创建活动成功通知
    private static final String template_id_successful_create = "GN7CkI6NP5-1jFkmSHY9a-TT1dE7HCuHqIIdK9lqRNw";

    //关注活动成功通知
    private static final String template_id_successful_follow = "pSoyzdT0I4cfWWtV4lr4VxxzYMFWmS98ovyyOvjrxbA";

    //提现到帐通知
    private static final String template_id_successful_transfers = "3O0vGXnf_PebH7N4S8YVZyi-5dqDYhwwRNuCwn2f6Lc";

    //提现失败提醒
    private static final String template_id_failing_transfers = "RxR8z_ht-WptdhptQkdsCrBLknFty4stBYfRAI__S5U";

    //2018-06-25 世界杯活动增加
    //审核通过
    private static final String template_id_successful_check_pass = "BygRU5qtPC9GT65f8ZS9oa01-SFUpFYZMbiR-v6DHPM";

    //审核未通过
    private static final String template_id_successful_check_fail = "qj8jo8EzgQivaoInNeRwsgv3AAbuhJU9Yf-7R4NpRUM";

    //
    private static final String template_id_successful_remark = "nI9g8aB1j4v72HCU2GDh6ldn_AXsvupNYFDRKBZujrQ";


    //模板消息请求的URL
    private static final String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";


    public static String wxRemarkSuccess(String name,String money,String buyTime,String remark,String openid,String redirectUrl)throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_remark);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("打赏成功！"));
            messageData.setKeyword1(new MessageDataList(name));
            messageData.setKeyword2(new MessageDataList(money));
            messageData.setKeyword3(new MessageDataList(buyTime));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxRemarkSuccess:" + js);
            return js;
        }
        return null;
    }


    //购买成功通知
    public static String wxPurchaseSuccess(String name,String money,String buyTime,String remark,String openid,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_purchase);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("购买成功！"));
            messageData.setKeyword1(new MessageDataList(name));
            messageData.setKeyword2(new MessageDataList(money));
            messageData.setKeyword3(new MessageDataList(buyTime));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxPurchaseSuccess:" + js);
            return js;
        }
        return null;
    }

    //活动创建成功通知
    public static String wxCreateSuccess(String name,String place,String time,String remark,String openid,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_create);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("活动创建成功！"));
            messageData.setKeyword1(new MessageDataList(name));
            messageData.setKeyword2(new MessageDataList(time));
            messageData.setKeyword3(new MessageDataList(place));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxCreateSuccess:" + js);
            return js;
        }
        return null;
    }

    //活动关注成功
    public static String wxFollowSuccess(String nickName,String time,String remark,String openid,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_follow);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("关注活动成功！"));
            messageData.setKeyword1(new MessageDataList(nickName));
            messageData.setKeyword2(new MessageDataList(time));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxFollowSuccess:" + js);
            return js;
        }
        return null;
    }

    /**
     * @param time 申请时间
     * @param transfersType 提现方式
     * @param requestAmount 提现金额
     * @param procedureAmount 手续费用
     * @param practicalAmount 到账金额
     * @param remark 订单号
     * @param openid
     * @param redirectUrl
     * @return
     * @throws Exception
     */
    //提现到帐通知
    public static String wxTransfersSuccess(String time,String transfersType,String requestAmount, String procedureAmount ,String practicalAmount,String remark,String openid,String redirectUrl)throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_transfers);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList("提现—成功"));
            messageData.setKeyword1(new MessageDataList(time));
            messageData.setKeyword2(new MessageDataList(transfersType));
            messageData.setKeyword3(new MessageDataList(requestAmount));
            messageData.setKeyword4(new MessageDataList(procedureAmount));
            messageData.setKeyword5(new MessageDataList(practicalAmount));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxFollowSuccess:" + js);
            return js;

        }
        return null;
    }

    /**
     *
     * @param requestAmount 提现金额
     * @param time 提现时间
     * @param reason 理由
     * @param remark 订单号
     * @param openid
     * @param redirectUrl
     * @return
     * @throws Exception
     */
    //提现失败提醒
    public static String wxTransfersFailing(String requestAmount,String time,String reason,String remark,String openid,String redirectUrl)throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            if(!StringUtils.isNullOrEmpty(openid)) {
                Gson gson = new Gson();
                String access_token = PageAuthorizeUtils.getAccess_token(false);
                Message message = new Message();
                message.setUrl(redirectUrl);
                message.setTemplate_id(template_id_failing_transfers);
                message.setTouser(openid);
                MessageData messageData = new MessageData();
                messageData.setFirst(new MessageDataList("提现—失败"));
                messageData.setKeyword1(new MessageDataList(requestAmount));
                messageData.setKeyword2(new MessageDataList(time));
                messageData.setKeyword3(new MessageDataList(reason+"  请联系客服人员。联系电话：010-60609868"));
                messageData.setRemark(new MessageDataList(remark));
                message.setData(messageData);
                String str = gson.toJson(message);
                String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
                log.info("WeixinPushMassage.wxFollowSuccess:" + js);
                return js;
            }
        }
        return null;
    }

    //审核通过
    public static String wxCheckPass(String openid,String firstData,String time,String remark,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_check_pass);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList(firstData));
            messageData.setKeyword1(new MessageDataList("审核通过"));
            messageData.setKeyword2(new MessageDataList(time));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxCheckPass:" + js);
            return js;
        }
        return null;
    }

    //审核失败
    public static String wxCheckFail(String openid,String firstData,String time,String remark,String redirectUrl) throws Exception{
        if(!StringUtils.isNullOrEmpty(openid)) {
            Gson gson = new Gson();
            String access_token = PageAuthorizeUtils.getAccess_token(false);
            Message message = new Message();
            message.setUrl(redirectUrl);
            message.setTemplate_id(template_id_successful_check_fail);
            message.setTouser(openid);
            MessageData messageData = new MessageData();
            messageData.setFirst(new MessageDataList(firstData));
            messageData.setKeyword1(new MessageDataList("审核未通过"));
            messageData.setKeyword2(new MessageDataList(time));
            messageData.setRemark(new MessageDataList(remark));
            message.setData(messageData);
            String str = gson.toJson(message);
            String js = HttpURlUtils.httpsRequest(url + access_token, "POST", str);
            log.info("WeixinPushMassage.wxCheckFail:" + js);
            return js;
        }
        return null;
    }
}
