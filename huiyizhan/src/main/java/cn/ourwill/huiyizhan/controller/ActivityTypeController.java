package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.ActivityType;
import cn.ourwill.huiyizhan.service.IActivityTypeService;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/activityType")
public class ActivityTypeController {
    @Autowired
    IActivityTypeService activityTypeService;


    /**
     * 获取所有会议类型
     */
    @RequestMapping("/list")
    public Map list(){
        List<ActivityType> list = activityTypeService.findAll();
        if(list == null){
            return ReturnResult.errorResult("查询结果不存在");
        }else{
            return ReturnResult.successResult("data",list, ReturnType.GET_SUCCESS);
        }
    }
}
