package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityPartner;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityPartnerService;
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
@RequestMapping("/api/activityPartner")
@Slf4j
public class ActivityPartnerController {


    @Autowired
    private IActivityPartnerService activityPartnerService;

    @Autowired
    private IActivityService activityService;

    /**
     * 创建会议联系人 ---- 新增
     *
     * @param activityPartner
     * @return
     */
    @PostMapping
    @ResponseBody
    @Access
    public Map addActivityPartner(HttpServletRequest request, @RequestBody ActivityPartner activityPartner) {
        try {
            if (activityPartner.getActivityId() == null)
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            Activity activity = activityService.getById(activityPartner.getActivityId());
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId())) {
                throw new UnPermissionException();
            }
            activityPartner.setCTime(new Date());

            Integer resultCode = activityPartnerService.save(activityPartner);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e) {
            log.info("ActivityPartnerController.add", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 批量添加活动门票
     * @param request
     * @param activityId
     * @param activityPartners
     * @return
     */
    @PostMapping("/batchSave/{activityId}")
    @Access
    @ResponseBody
    public Map batchSaveTickets(HttpServletRequest request,@PathVariable("activityId")Integer activityId, @RequestBody List<ActivityPartner> activityPartners){
        try {
            User user = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, user)) {
                throw new UnPermissionException();
            }
            int count = activityPartnerService.batchSave(user.getId(),activityId,activityPartners);
            if(count>0){
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e){
            log.info("ActivityPartnerController.batchSaveTickets", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 获取会议联系人 ---- 获取
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Map getActivityPartner(@PathVariable("id") Integer id) {

        try {
            ActivityPartner activityPartner = activityPartnerService.getById(id);
            return ReturnResult.successResult("data", activityPartner, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityPartnerController.get ", e);
            return ReturnResult.successResult(ReturnType.SERVER_ERROR);
        }

    }

    /**
     * 删除会议联系人 ----删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    @Access
    public Map deleteActivityPartner(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            ActivityPartner activitySchedule = activityPartnerService.getById(id);
            if (activitySchedule.getActivityId() == null) return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            Activity activity = activityService.getById(activitySchedule.getActivityId());
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId())) {
                throw new UnPermissionException();
            }

            Integer resultCode = activityPartnerService.delete(id);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e) {
            log.info("ActivityPartnerController.delete", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新会议联系人 ----更新
     *
     * @param activityPartner
     * @return
     */
    @PutMapping(value = "/{id}")
    @ResponseBody
    @Access
    public Map uploadActivityPartner(HttpServletRequest request, @PathVariable("id") Integer id,
                                     @RequestBody ActivityPartner activityPartner) {
        try {
            Activity activity = activityService.getById(activityPartner.getActivityId());
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId())) {
                throw new UnPermissionException();
            }
            Integer resultCode = activityPartnerService.update(activityPartner);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e){
            throw e;
        } catch (Exception e) {
            log.info("ActivityPartnerController.upload", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新会议合作伙伴 ----批量更新
     *
     * @param request
     * @param activityPartners
     * @return
     */
    @PutMapping(value = "/batchUpdate/{activityId}")
    @ResponseBody
    @Access
    public Map batchUpdate(HttpServletRequest request,@PathVariable("activityId") Integer activityId,
                           @RequestBody List<ActivityPartner> activityPartners) {
        try {
            //验权

            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            activityPartners.stream().forEach(entity->entity.setActivityId(activityId));
            Integer resultCode = activityPartnerService.batchUpdate(activityPartners,activityId);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityPartnerController.batchUpload", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
