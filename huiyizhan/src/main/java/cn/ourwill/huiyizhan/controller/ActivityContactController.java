package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityContact;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityContactService;
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
@RequestMapping("/api/activityContact")
@Slf4j
public class ActivityContactController {


    @Autowired
    private IActivityContactService activityContactService;
    @Autowired
    private IActivityService activityService;

    /**
     * 创建会议联系人 ---- 新增
     *
     * @param activityContact
     * @return
     */
    @PostMapping
    @ResponseBody
    @Access
    public Map addActivityContact(HttpServletRequest request, @RequestBody ActivityContact activityContact) {
        try {
            if (activityContact.getActivityId() == null)
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            Activity activity = activityService.getById(activityContact.getActivityId());
            if (activity == null)
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId()))
                throw new UnPermissionException();

            activityContact.setCTime(new Date());
            Integer resultCode = activityContactService.save(activityContact);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityContactController.add", e);
            return ReturnResult.successResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *  批量
     *      创建会议联系人 ---- 新增
     * </pre>
     *
     * @param request
     * @param activityContacts
     * @return
     */
    @PostMapping("/batchSave/{activityId}")
    @ResponseBody
    @Access
    public Map addActivityContactBatch(HttpServletRequest request, @PathVariable("activityId") Integer activityId,
                                       @RequestBody List<ActivityContact> activityContacts) {
        try {
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            Integer resultCode = activityContactService.batchSave(loginUser.getId(), activityId, activityContacts);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityContactController.add", e);
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
    public Map getActivityContact(@PathVariable("id") Integer id) {

        try {
            ActivityContact activityContact = activityContactService.getById(id);
            return ReturnResult.successResult("data", activityContact, ReturnType.GET_SUCCESS);

        } catch (Exception e) {
            log.info("ActivityContactController.get ", e);
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
    public Map deleteActivityContact(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            ActivityContact activitySchedule = activityContactService.getById(id);
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

            Integer resultCode = activityContactService.delete(id);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }

        } catch (Exception e) {
            log.info("ActivityContactController.delete", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新会议联系人 ----更新
     *
     * @param activityContact
     * @return
     */
    @PutMapping(value = "/{id}")
    @ResponseBody
    @Access
    public Map uploadActivityContact(HttpServletRequest request, @PathVariable("id") Integer id,
                                     @RequestBody ActivityContact activityContact) {
        try {
            Activity activity = activityService.getById(activityContact.getActivityId());
            if (activity == null)
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activity.getUserId().equals(loginUser.getId())) {
                throw new UnPermissionException();
            }
            Integer resultCode = activityContactService.update(activityContact);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }

        } catch (Exception e) {
            log.info("ActivityContactController.upload", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 更新会议联系人 ----批量更新
     *
     * @param request
     * @param activityContacts
     * @return
     */
    @PutMapping(value = "/batchUpdate/{activityId}")
    @ResponseBody
    @Access
    public Map batchUpdate(HttpServletRequest request, @PathVariable("activityId") Integer activityId,
                           @RequestBody List<ActivityContact> activityContacts) {
        try {
            //验权
            User loginUser = GlobalUtils.getLoginUser(request);
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            activityContacts.stream().forEach(entity -> entity.setActivityId(activityId));
            Integer resultCode = activityContactService.batchUpdate(activityContacts, activityId);
            if (resultCode > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }

        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityContactController.upload", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
