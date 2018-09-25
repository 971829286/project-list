package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import cn.ourwill.huiyizhan.utils.WillCenterConstants;
import cn.ourwill.huiyizhan.weChat.Utils.PageAuthorizeUtils;
import cn.ourwill.huiyizhan.weChat.pojo.QRCodeParam;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-05-14 11:57
 **/

@Controller
@RequestMapping("/api/user")
public class WechatBindController {

    @Value("${weixin.expire.seconds}")
    private Integer expire_seconds;

    @Autowired
    private WillCenterConstants willCenter;
    @Autowired
    private IUserService userService;


    /**
     *
     * @return
     */
    @RequestMapping("/getQRCode")
    @ResponseBody
    @Access
    public String getQRCode(HttpServletRequest request){
        try{
            //获取access token
            String accessToken = PageAuthorizeUtils.getAccess_token(false);
            //封装二维码参数
            String uuid = GlobalUtils.getLoginUser(request).getUUID();
            QRCodeParam qrCodeParam = new QRCodeParam();
            qrCodeParam.setAccess_token(accessToken);
            qrCodeParam.setExpire_seconds(expire_seconds);
            qrCodeParam.setAction_name("QR_SCENE");
            qrCodeParam.setScene_str("BIND_"+uuid);
            String QRCodeTicketUrl = PageAuthorizeUtils.getQRCodeTicket(qrCodeParam);
            return QRCodeTicketUrl;
        }catch (Exception e){
            return e.toString();
        }
    }

    /**
     * 取消绑定
     * @param request
     * @return
     */
    @RequestMapping("/relieveWechatBind")
    @ResponseBody
    @Access
    public Map relieveWechatBind(HttpServletRequest request){

        try{
            String uuid =  GlobalUtils.getLoginUser(request).getUUID();
            //更新huiyizhan的user的unionId
            Integer count = userService.changeUnionIdByUuid(null,uuid);
            if (count>0){
                //更新willchenter的user的unionId
                JSONObject jspnParam = new JSONObject();
                jspnParam.put("uuid", uuid);
                jspnParam.put("unionid", null);
                StringEntity s = new StringEntity(jspnParam.toString(),"UTF-8");
                s.setContentType("application/json");
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(willCenter.deleteUnionIdById());
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
                        return ReturnResult.successResult("解绑成功！");
                    } else {
                        return resMap;
                    }
                }
            }
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }catch(Exception e){
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


}
