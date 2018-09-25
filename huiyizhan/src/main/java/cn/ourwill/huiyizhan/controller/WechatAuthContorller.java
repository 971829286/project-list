package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.service.CoreService;
import cn.ourwill.huiyizhan.utils.AesException;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import cn.ourwill.huiyizhan.weChat.Utils.PageAuthorizeUtils;
import cn.ourwill.huiyizhan.weChat.Utils.PastUtil;
import cn.ourwill.huiyizhan.weChat.pojo.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/4/11 19:45
 * @Description
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class WechatAuthContorller {
    //微信开发者ID
    @Value("${weixin.appid}")
    private String appid;

    //开发者密码
    @Value("${weixin.appsecret}")
    private String secret;

    //获取access_token填写client_credential
    @Value("${weixin.grant_type}")
    private String grant_type;

    //授权后重定向的回调链接地址
    @Value("${weixin.redirect_uri}")
    private String redirect_uri;

    //静默授权后重定向的回调链接地址
    @Value("${weixin.redirect_uri_OPENID}")
    private String redirect_uri_OPENID;

    //应用授权作用域
    @Value("${weixin.scope}")
    private String scope;

    //网站应用ID
    @Value("${weixin.webappid}")
    private String webappid;

    //网站应用开发密码
    @Value("${weixin.webappsecret}")
    private String webappsecret;

    @Value("${system.domain}")
    private String domian;

    //ticket获取方式
    @Value("${weixin.accesstoken.mode}")
    private String mode;

    @Value("${weixin.accesstoken.opening}")
    private boolean opening;

    @Value("${weixin.serviceToken}")
    private String serviceToken;

    @Value("${weixin.encodingAESKey}")
    private String encodingAESKey;

    @Autowired
    private CoreService coreService;

    @GetMapping("/wxConfig")
    @ResponseBody
    public Map getWxConfig(@RequestParam("url") String url){
        try {
            String ticket = PageAuthorizeUtils.getJsApiTicket();
            if(ticket==null){
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
            Map remap = PastUtil.sign(ticket, URLDecoder.decode(url, "utf-8"));
            remap.put("appId",appid);
            return ReturnResult.successResult("data",remap, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("IndexController.getWxConfig",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("/getTicket")
    @ResponseBody
    public Map getTicket(){
        try {
            if(!opening){
                return ReturnResult.errorResult("本服务不对外提供此接口！");
            }
            String ticket = PageAuthorizeUtils.getJsApiTicket();
            if(ticket!=null) {
                return ReturnResult.successResult("ticket", ticket, ReturnType.GET_SUCCESS);
            }else{
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
        } catch (Exception e) {
            log.error("IndexController.getTicket",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("/getAccessToken")
    @ResponseBody
    public Map getAccessToken(){
        try {
            if(!opening){
                return ReturnResult.errorResult("本服务不对外提供此接口！");
            }
            String accessToken = PageAuthorizeUtils.getAccess_token(false);
            if(accessToken!=null) {
                return ReturnResult.successResult("accessToken", accessToken, ReturnType.GET_SUCCESS);
            }else{
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
        } catch (Exception e) {
            log.error("IndexController.getAccessToken",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("/weChatService")
    @ResponseBody
    public String weChatService(HttpServletRequest request){
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (PastUtil.checkSignature(serviceToken, signature, timestamp, nonce)) {
            log.info("echostr=" + echostr);
            return echostr;
        }
        return null;
    }

    @PostMapping("/weChatService")
    public void postSignature(HttpServletRequest request,HttpServletResponse response) throws AesException, IOException{
        // 微信加密签名
        String msg_signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");

        //从请求中读取整个post数据
        InputStream inputStream = request.getInputStream();
        String postData = IOUtils.toString(inputStream, "UTF-8");

        String msg = "";
        WXBizMsgCrypt wxcpt = null;

        try {
            //wxcpt = new WXBizMsgCrypt(serviceToken, encodingAESKey, appid);
            //解密消息,明文不需要解密
            //msg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("requestMsg=" + msg);

        // 调用核心业务类接收消息、处理消息
        String respMessage = coreService.processRequest(postData);
        log.info("responseMsg=" + respMessage);
        /*String encryptMsg = "";

        try {
            //加密回复消息，明文传输，不需要加密
            encryptMsg = wxcpt.EncryptMsg(respMessage, timestamp, nonce);
        } catch (AesException e) {
            e.printStackTrace();
        }*/
        response.getWriter().print(respMessage);
        response.getWriter().close();


    }





}
