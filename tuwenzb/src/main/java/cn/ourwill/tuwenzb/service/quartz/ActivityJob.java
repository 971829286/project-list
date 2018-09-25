package cn.ourwill.tuwenzb.service.quartz;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.service.impl.ActivityStatisticsServiceImpl;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:具体job类
 * @author: XuJinNiu
 * @create: 2018-07-02 16:56
 **/
//@Component
public class ActivityJob implements Job {

//    @Autowired
    IActivityStatisticsService activityStatisticsService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //取出需要增加的点赞数和参与数
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Long likeNum = (Long)jobDataMap.get("likeNum");
        Long partNum = (Long)jobDataMap.get("partNum");
        Integer activityId = (Integer) jobDataMap.get("activityId");

        System.out.println(likeNum);
        System.out.println(partNum);
        System.out.println(activityId);
        //执行
        activityStatisticsService.addLikeNumberRedis(activityId,19);
        activityStatisticsService.addParticipantsNumberRedis(activityId,10);
    }
}
