package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IUserStatisticsService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 描述：用户统计信息接口
 *
 * @author liupenghao
 * @create 2018-04-04 9:58
 **/
@RestController
@RequestMapping("/api/userStatistics")
@Slf4j
public class UserStatisticsController {

    @Autowired
    private IUserStatisticsService userStatisticsService;

    @PostMapping("/addActivityCount")
    public Map addActivityCount(HttpServletRequest request) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            //userStatisticsService.addActivityCount(loginUser.getId());
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("UserStatisticsController.addActivityCount", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @DeleteMapping("/reduceActivityCount")
    public Map reduceActivityCount(HttpServletRequest request) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
           // userStatisticsService.reduceActivityCount(loginUser.getId());
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("UserStatisticsController.reduceActivityCount", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 为 当前浏览用户添加粉丝
     *
     * @param userId 当前浏览的用户id
     */
    @PostMapping("/addFans/{userId}")
    public Map addFansCount(HttpServletRequest request, @PathVariable("userId") Integer userId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            //userStatisticsService.addFansCount(userId);
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("UserStatisticsController.addFansCount", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @DeleteMapping("/reduceFans/{userId}")
    public Map reduceFansCount(HttpServletRequest request, @PathVariable("userId") Integer userId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("UserStatisticsController.reduceFansCount", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @PostMapping("/popularity/{userId}")
    public Map addPopularity(HttpServletRequest request, @PathVariable("userId") Integer userId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            userStatisticsService.addPopularity(userId);
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("UserStatisticsController.addPopularity", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{userId}")
    @Access
    public Map deleteByActivityIdFromRedis(HttpServletRequest request, @PathVariable("userId") Integer userId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null) {
                throw new UnPermissionException();
            }
            userStatisticsService.deleteByUserIdFromRedis(userId);
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);

        }  catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.error("UserStatisticsController.deleteByActivityIdFromRedis", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("/syncRedisDataToMysql")
    public Map syncRedisDataToMysql(HttpServletRequest request) {
        try {
            userStatisticsService.syncRedisToMySQL();
            return ReturnResult.successResult("更新成功！");
        } catch (Exception e) {
            log.error("UserStatisticsController.syncRedisDataToMysql", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
