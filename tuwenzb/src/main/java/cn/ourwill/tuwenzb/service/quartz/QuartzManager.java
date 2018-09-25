package cn.ourwill.tuwenzb.service.quartz;

import cn.ourwill.tuwenzb.entity.Activity;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @description:定时任务管理
 * @author: XuJinNiu
 * @create: 2018-07-02 16:57
 **/
@Slf4j
//@Component
public class QuartzManager {

//    @Value("${add.likenum.scheduled}")
    private  String cron;
    private  SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public  void addJob(Class jobClass,Activity activity) {
        try {

            //计算增加点赞方案
            Date startTime = activity.getStartTime();
            Date endTime = activity.getEndTime();
            Integer expectLikeNum = activity.getExpectLikeNum();
            Integer expectParticipateNum = activity.getExpectParticipateNum();
            Long minutes = (endTime.getTime() - startTime.getTime())/60000;

            Long likeNum = expectLikeNum / minutes;
            Long partNum = expectParticipateNum / minutes;

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("likeNum",10L);
            jobDataMap.put("partNum",10L);
            jobDataMap.put("activityId",activity.getId());


            Scheduler sched = schedulerFactory.getScheduler();

            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .setJobData(jobDataMap)
                    .withIdentity("activity"+activity.getId(), "activity").build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.endAt(activity.getEndTime());
            triggerBuilder.startNow();

            // 触发器名,触发器组
            triggerBuilder.withIdentity("activityTrigger:"+activity.getId(), "activityGroupTrigger");

            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);

            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @Description: 修改一个任务的触发时间
     *
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param cron   时间设置，参考quartz说明文档
     */
    public  void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                /** 方式一 ：调用 rescheduleJob 开始 */
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                sched.rescheduleJob(triggerKey, trigger);
                /** 方式一 ：调用 rescheduleJob 结束 */


                /** 方式二：先删除，然后在创建一个新的Job  */
                //JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                //Class<? extends Job> jobClass = jobDetail.getJobClass();
                //removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                //addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
                /** 方式二 ：先删除，然后在创建一个新的Job */
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    public  void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @Description:启动所有定时任务
     */
    public  void startJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     */
    public  void shutdownJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  boolean isExist(Activity activity) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            Scheduler sched = schedulerFactory.getScheduler();
            return sched.checkExists(JobKey.jobKey("activity"+activity.getId()));
        } catch (Exception e) {
            log.info("QuartzManager.isExist",e);
            return true;
        }
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public void getRunningJob() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();

            executingJobs.parallelStream().forEach(e -> {
                System.out.println(e.getJobDetail().getKey().toString());
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
