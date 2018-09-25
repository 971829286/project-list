package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.ActivitySchedule;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityScheduleService;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/25 0025 15:49
 * @Version1.0
 */
@RestController
@RequestMapping("/api/activitySchedule")
@Slf4j
public class ActivityScheduleController {

    @Autowired
    private IActivityScheduleService activityScheduleService;
    @Autowired
    private IActivityService activityService;

    /**
     * 创建会议日程---- 新增
     * @param activitySchedule
     * @return
     */
    @PostMapping
    @ResponseBody
    @Access
    public Map addActivitySchedule(HttpServletRequest request,@RequestBody ActivitySchedule activitySchedule){
        try{
            if (activitySchedule.getActivityId() == null) return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activitySchedule.getActivityId(), loginUser)) {
                throw new UnPermissionException();
            }
            activitySchedule.setCTime(new Date());
            Integer resultCode = activityScheduleService.save(activitySchedule);
            if(resultCode > 0){
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }else{
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
        } catch (UnPermissionException e){
            throw e;
        }catch (Exception e){
            log.info("ActivityScheduleController.add",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量添加   日程
     * @param request
     * @param activityId
     * @param activitySchedules
     * @return
     */
    @PostMapping("/batchSave/{activityId}")
    @Access
    @ResponseBody
    public Map batchSave(HttpServletRequest request,@PathVariable("activityId")Integer activityId, @RequestBody List<ActivitySchedule> activitySchedules){
        try {
            User user = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, user)) {
                throw new UnPermissionException();
            }
            int count = activityScheduleService.batchSave(user.getId(),activityId,activitySchedules);
            if(count>0){
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e){
            log.info("ActivityTicketsController.batchSaveTickets", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 获取会议日程 ---- 获取
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    public Map getActivitySchedule( @PathVariable("id") Integer id){
        try{
            ActivitySchedule activitySchedule = activityScheduleService.getById(id);
            return ReturnResult.successResult("data",activitySchedule,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("ActivityScheduleController.get ",e);
            return ReturnResult.successResult(ReturnType.SERVER_ERROR);
        }

    }

    /**
     * 删除会议日程 ----删除
     * @param id
     * @return
     */
    @DeleteMapping( "/{id}")
    @ResponseBody
    @Access
    public Map deleteActivitySchedule(HttpServletRequest request,@PathVariable("id") Integer id){
        try{
            User user = GlobalUtils.getLoginUser(request);
            ActivitySchedule schedule = activityScheduleService.getById(id);
            if (schedule == null){
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
            if(!activityService.checkOwnerOrAdmin(schedule.getActivityId(),user)){
                throw new UnPermissionException();
            }
            Integer resultCode = activityScheduleService.delete(id);
            if(resultCode >0){
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }else{
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e){
            log.info("ActivityScheduleController.delete",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新会议日程 ----更新
     * @param activitySchedule
     * @return
     */
    @PutMapping("/{id}")
    @ResponseBody
    @Access
    public Map updateActivitySchedule(HttpServletRequest request, @PathVariable("id") Integer id,
                                      @RequestBody ActivitySchedule activitySchedule){
        try{
            User user = GlobalUtils.getLoginUser(request);
            ActivitySchedule schedule = activityScheduleService.getById(id);
            if (schedule == null)
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            if(!activityService.checkOwnerOrAdmin(schedule.getActivityId(),user)){
                throw new UnPermissionException();
            }
            activitySchedule.setId(id);
            Integer resultCode = activityScheduleService.update(activitySchedule);
            if(resultCode >0){
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }else{
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e){
            log.info("ActivityScheduleController.updateActivitySchedule",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新会议日程 ----批量更新
     *
     * @param request
     * @param activitySchedules
     * @return
     */
    @PutMapping(value = "/batchUpdate/{activityId}")
    @ResponseBody
    @Access
    public Map batchUpdate(HttpServletRequest request,@PathVariable("activityId") Integer activityId,
                           @RequestBody List<ActivitySchedule> activitySchedules) {
        try {
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            activitySchedules.stream().forEach(entity->entity.setActivityId(activityId));
            Integer resultCode = activityScheduleService.batchUpdate(activitySchedules,activityId);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e) {
            log.info("ActivityScheduleController.batchUpdate", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
