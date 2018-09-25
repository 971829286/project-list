package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.ActivityTickets;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.service.IActivityTicketsService;
import cn.ourwill.huiyizhan.service.ITicketsRecordService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/23 18:40
 * @Description
 */
@RestController
@RequestMapping("/api/activityTickets")
@Slf4j
public class ActivityTicketsController {

    @Autowired
    private IActivityTicketsService activityTicketsService;
    @Autowired
    private ITicketsRecordService ticketsRecordService;
    @Autowired
    private IActivityService activityService;


    /**
     * 批量添加活动门票
     * @param request
     * @param activityId
     * @param tickets
     * @return
     */
    @PostMapping("/batchSave/{activityId}")
    @Access
    @ResponseBody
    public Map batchSaveTickets(HttpServletRequest request,@PathVariable("activityId")Integer activityId, @RequestBody List<ActivityTickets> tickets){
        try {
            User user = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, user)) {
                throw new UnPermissionException();
            }
            int count = activityTicketsService.batchSave(user.getId(),activityId,tickets);
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
     * 更新会议门票信息
     * @param request
     * @param id
     * @param ticket
     * @return
     */
    @PutMapping("/{id}")
    @Access
    @ResponseBody
    public Map updateTicket(HttpServletRequest request, @PathVariable("id")Integer id, @RequestBody ActivityTickets ticket){
        try {
            User user = GlobalUtils.getLoginUser(request);
            ActivityTickets oldTicket = activityTicketsService.getById(id);
            if(oldTicket==null) return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            if (!activityService.checkOwnerOrAdmin(oldTicket.getActivityId(), user)) {
                throw new UnPermissionException();
            }
            ticket.setId(id);
            int count = activityTicketsService.update(ticket);
            if(count>0){
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e){
            log.info("ActivityTicketsController.updateTicket", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 删除会议门票信息
     * @param request
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Access
    @ResponseBody
    public Map deleteTicket(HttpServletRequest request, @PathVariable("id")Integer id){
        try {
            User user = GlobalUtils.getLoginUser(request);
            ActivityTickets oldTicket = activityTicketsService.getById(id);
            if(oldTicket==null) return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            if (!activityService.checkOwnerOrAdmin(oldTicket.getActivityId(), user)) {
                throw new UnPermissionException();
            }
            //查询是否有购票记录
            if(ticketsRecordService.checkIsSell(id)){
                return ReturnResult.errorResult("已有购票记录，无法删除！");
            }
            int count = activityTicketsService.delete(id);
            if(count>0){
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e){
            log.info("ActivityTicketsController.deleteTicket", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取单个会议门票信息
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    public Map getTicket(HttpServletRequest request, @PathVariable("id")Integer id){
        try {
            ActivityTickets ticket = activityTicketsService.getById(id);
            return ReturnResult.successResult("data",ticket,ReturnType.GET_SUCCESS);
        } catch (Exception e){
            log.info("ActivityTicketsController.getTicket", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取会议所有门票信息
     * @param request
     * @param activityId
     * @return
     */
    @GetMapping("/activity/{activityId}")
    @ResponseBody
    public Map getByActivity(HttpServletRequest request,@PathVariable("activityId") Integer activityId){
        try{
            List<ActivityTickets> reList = activityTicketsService.getByActivityId(activityId);
            return ReturnResult.successResult("data", reList, ReturnType.GET_SUCCESS);
        } catch (Exception e){
            log.info("ActivityTicketsController.getByActivity", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 前台获取可以购买的门票模版
     * @param request
     * @param activityId
     * @return
     */
    @GetMapping("/valid/activity/{activityId}")
    @ResponseBody
    public Map getValidByActivity(HttpServletRequest request,@PathVariable("activityId") Integer activityId){
        try{
            List<ActivityTickets> reList = activityTicketsService.getValidByActivityId(activityId);
            return ReturnResult.successResult("data", reList, ReturnType.GET_SUCCESS);
        } catch (Exception e){
            log.info("ActivityTicketsController.getByActivity", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PutMapping("/batchUpdate/{activityId}")
    @ResponseBody
//    @Access
    public Map batchUpdate(HttpServletRequest request,@PathVariable("activityId") Integer activityId,@RequestBody List<ActivityTickets> activityTickets){
        try {
            if (activityTickets.size() == 0){
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            User user = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, user)) {
                throw new UnPermissionException();
            }
            if(activityTicketsService.batchUpdate(activityId,activityTickets,user)>0){
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (UnPermissionException e){
            throw e;
        }catch (Exception e){
            log.info("ActivityTicketsController.batchUpdate", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
