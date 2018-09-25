package cn.ourwill.willcenter.controller;


import cn.ourwill.willcenter.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Jinniu.xu @ourwill.com.cn
 * @Time 2018年4月8日14:46:04
 * @Version1.0
 */
@Controller
public class VerifyController {
    /**
     *  生成验证码及图片
     * @param request
     * @param response
     */
    @RequestMapping("/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response){
        String heigh = request.getParameter("heigh");
        String width = request.getParameter("width");
        if(StringUtils.isEmpty(heigh)){
            heigh = "50";
        }
        if(StringUtils.isEmpty(width)){
            width = "100";
        }
        response.setContentType("image/jpg");//设置相应内容,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置相应头,告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expire",0);//过期时间
//        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        VerifyCodeUtils verifyCodeUtils = new VerifyCodeUtils(180,80,4,40);
//        log.info("verifyCode:"+verifyCodeUtils.getCodeText().toUpperCase());
        String verifyCode = verifyCodeUtils.getCodeText().toUpperCase();
        HttpSession session = request.getSession();
        session.setAttribute("verifyCode",verifyCode);
        try {
            VerifyCodeUtils.outputImage(Integer.parseInt(width),Integer.parseInt(heigh),response.getOutputStream(),verifyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 为手机发送验证码
     * @param request
     * @return
     */
    @RequestMapping("/sendMessage")
    @ResponseBody
    public Map sendMessage(HttpServletRequest request){
        try{
            String mobPhone = request.getParameter("mobPhone");
            int mobile_code = (int)((Math.random() * 9 + 1) * 100000);
            String content = "验证码为："+mobile_code+"，请在10分钟内输入。为了保护您的账号安全，请勿把验证码提供给别人。";
            if(StringUtils.isEmpty(mobPhone)){
                return ReturnResult.errorResult("手机号为空");
            }else{
                String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|16[6]|(17[0135678])|(18[0,0-9]|19[89]))\\d{8}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(mobPhone);
                boolean isMatch = m.matches();
                if (!isMatch){
                    return ReturnResult.errorResult("请输入正确的手机号");
                }
            }
            boolean isSend = SmsUtil.send(mobPhone,content);
            if(!isSend){
                return ReturnResult.errorResult("发送失败");
            }
            RedisUtils.set("smsCode:"+mobPhone,String.valueOf(mobile_code),10, TimeUnit.MINUTES);
            return  ReturnResult.successResult("发送成功!");
        }catch (Exception e){
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
