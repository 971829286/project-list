package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityStatisticsService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-04-02 19:12
 **/

@RestController
@RequestMapping("/api/activityStatistics")
@Slf4j
public class ActivityStatisticsController {

    @Autowired
    private IActivityStatisticsService activityStatisticsService;

    @PostMapping("/addCollect/{activityId}")
    public Map addCollectNumber(HttpServletRequest request, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityStatisticsController.addCollectNumber", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @DeleteMapping("/reduceCollect/{activityId}")
    public Map reduceCollectNumber(HttpServletRequest request, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);

        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityStatisticsController.reduceNumber", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @PostMapping("/addWatch/{activityId}")
    public Map addWatchNumber(HttpServletRequest request, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            activityStatisticsService.addWatch(activityId);
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);

        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityStatisticsController.addWatchNumber", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{activityId}")
    @Access
    public Map deleteByActivityIdFromRedis(HttpServletRequest request, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            activityStatisticsService.deleteByActivityIdFromRedis(activityId);
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);

        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("ActivityStatisticsController.deleteByActivityIdFromRedis", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("/syncRedisDataToMysql")
    public Map syncRedisDataToMysql(HttpServletRequest request) {
        try {
            activityStatisticsService.syncRedisToMySQL();
            return ReturnResult.successResult("更新成功！");
        } catch (Exception e) {
            log.error("ActivityStatisticsController.syncRedisDataToMysql", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
