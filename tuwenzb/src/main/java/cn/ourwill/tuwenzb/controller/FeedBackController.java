package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.Feedback;
import cn.ourwill.tuwenzb.service.IFeedBackService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-05 15:35
 **/
@Component
@Path("/feedback")
public class FeedBackController {

    @Autowired
    private IFeedBackService feedBackService;

    private static final Logger log = LogManager.getLogger(FeedBackController.class);

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map savaFeedBack(@Context HttpServletRequest request,Feedback feedback){

        try {

            if(feedback.getName()==null || "".equals(feedback.getName()==null)){
                return ReturnResult.errorResult("请填写姓名");
            }
            if(feedback.getPhone()==null || "".equals(feedback.getPhone()==null)){
                return ReturnResult.errorResult("请填写手机号");
            }else{
                String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|16[6]|(17[0135678])|(18[0,0-9]|19[89]))\\d{8}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(feedback.getPhone());
                boolean isMatch = m.matches();
                if (!isMatch){
                    return ReturnResult.errorResult("请输入正确的手机号");
                }
            }
            if(feedback.getRemark()==null || "".equals(feedback.getRemark()==null)){
                return ReturnResult.errorResult("请填写备注");
            }
            Integer userId = GlobalUtils.getUserId(request);
            feedback.setUserId(userId);
            if (feedBackService.save(feedback)>0){
                return ReturnResult.successResult( ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }catch (Exception e){
            log.error("FeedBackController.save",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
