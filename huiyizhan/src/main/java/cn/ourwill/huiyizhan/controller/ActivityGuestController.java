package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityGuest;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityGuestService;
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
@RequestMapping("/api/activityGuest")
@Slf4j
public class ActivityGuestController {


    @Autowired
    private IActivityGuestService activityGuestService;

    @Autowired
    private IActivityService activityService;

    /**
     * 创建会议联系人 ---- 新增
     *
     * @param activityGuest
     * @return
     */
    @PostMapping
    @ResponseBody
    @Access
    public Map addActivityGuest(HttpServletRequest request, @RequestBody ActivityGuest activityGuest) {

        try {
            if (activityGuest.getActivityId() == null) return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            Activity activity = activityService.getById(activityGuest.getActivityId());
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId())) {
                throw new UnPermissionException();
            }
            activityGuest.setCTime(new Date());

            Integer resultCode = activityGuestService.save(activityGuest);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityGuestController.add", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 批量添加 嘉宾
     *
     * @param request
     * @param activityId
     * @param activityGuests
     * @return
     */
    @PostMapping("/batchSave/{activityId}")
    @Access
    @ResponseBody
    public Map batchSave(HttpServletRequest request, @PathVariable("activityId") Integer activityId, @RequestBody List<ActivityGuest> activityGuests) {
        try {
            User user = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, user)) {
                throw new UnPermissionException();
            }
            int count = activityGuestService.batchSave(user.getId(), activityId, activityGuests);
            if (count > 0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityGuestController.batchSaveTickets", e);
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
    public Map getActivityGuest(@PathVariable("id") Integer id) {
        try {
            ActivityGuest activityGuest = activityGuestService.getById(id);
            return ReturnResult.successResult("data", activityGuest, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityGuestController.get ", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
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
    public Map delete(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            ActivityGuest activityGuest = activityGuestService.getById(id);
            if (activityGuest.getActivityId() == null) return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            Activity activity = activityService.getById(activityGuest.getActivityId());
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId())) {
                throw new UnPermissionException();
            }
            Integer resultCode = activityGuestService.delete(id);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }

        } catch (UnPermissionException e) {
            throw e;
        }  catch (Exception e) {
            log.info("ActivityGuestController.delete", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新会议联系人 ----更新
     *
     * @param activityGuest
     * @return
     */
    @PutMapping(value = "/{id}")
    @ResponseBody
    @Access
    public Map uploadActivityGuest(HttpServletRequest request, @PathVariable("id") Integer id,
                                   @RequestBody ActivityGuest activityGuest) {
        try {
            Activity activity = activityService.getById(activityGuest.getActivityId());
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId())) {
                throw new UnPermissionException();
            }
            Integer resultCode = activityGuestService.update(activityGuest);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }

        } catch (UnPermissionException e) {
            throw e;
        }  catch (Exception e) {
            log.info("ActivityGuestController.upload", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新会议嘉宾 ----批量更新
     *
     * @param request
     * @param activityGuests
     * @return
     */
    @PutMapping(value = "/batchUpdate/{activityId}")
    @ResponseBody
    @Access
    public Map batchUpdate(HttpServletRequest request,@PathVariable("activityId") Integer activityId,
                           @RequestBody List<ActivityGuest> activityGuests) {
        try {
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            activityGuests.stream().forEach(entity->entity.setActivityId(activityId));
            Integer resultCode = activityGuestService.batchUpdate(activityGuests,activityId);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityGuestController.batchUpload", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
