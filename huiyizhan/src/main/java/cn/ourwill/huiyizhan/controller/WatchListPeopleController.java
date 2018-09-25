package cn.ourwill.huiyizhan.controller;

/**
 * 描述：
 *
 * @author uusao
 * @create 2018-03-27 14:29
 **/

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.entity.PeopleDynamic;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBasicInfo;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IUserStatisticsService;
import cn.ourwill.huiyizhan.service.IWatchListPeopleService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 人收藏
 *
 * @author uusao
 */
@RestController
@RequestMapping("/api/watchPeople")
public class WatchListPeopleController {

    @Autowired
    private IWatchListPeopleService watchListPeopleService;


    @Autowired
    private IUserStatisticsService userStatisticsService;

    /**
     * 添加 关注 （对人的关注）
     *
     * @param request
     * @param id      需要关注人的id
     * @return
     */
    @PostMapping(value = "/{id}")
    @Access
    @ResponseBody
    public Map addWatch(HttpServletRequest request, @PathVariable("id") Integer id) {

        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null || loginUser.getId() == null) {
                throw new UnPermissionException();
            }
            Integer loginUserId = loginUser.getId();
            Boolean isWatch = watchListPeopleService.checkWatchStatus(id, loginUserId);
            if (isWatch) {  // 已关注
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            } else {
                watchListPeopleService.addWatch(id, loginUserId);

                //发送redis消息, 当前浏览用户 粉丝数目 +1,浏览量 +1
                // userStatisticsService.addFansCount(id);
                userStatisticsService.syncFansCount(id);
                userStatisticsService.addPopularity(id);
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

    /**
     * 取消关注 （对人的关注）
     *
     * @param request
     * @param id      要取消关注的人id
     * @return
     */

    @DeleteMapping(value = "/{id}")
    @Access
    @ResponseBody
    public Map cancelWatch(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null || loginUser.getId() == null) {
                throw new UnPermissionException();
            }
            Integer loginUserId = loginUser.getId();
            Boolean isWatch = watchListPeopleService.checkWatchStatus(id, loginUserId);
            if (!isWatch) {  // 已关注
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            } else {
                watchListPeopleService.cancelWatchById(id, loginUserId);
                //发送redis消息, 当前浏览用户 粉丝数目-1
                // userStatisticsService.reduceFansCount(id);
                userStatisticsService.syncFansCount(id);
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 根据当前登录用户 ,
     * 获取其关注人列表
     */
    @GetMapping("/users")
   // @Access
    @ResponseBody
    public Map getBeWatchedUsers(HttpServletRequest request,Integer userId,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize,
                                 @RequestParam(value = "orderBy", defaultValue = "0") Integer orderBy) {
        try {
            if (userId == null) { // 没传id ,说明是后台查询
                User loginUser = GlobalUtils.getLoginUser(request);
                if (loginUser == null) {
                    return ReturnResult.errorResult(ReturnType.GET_ERROR);
                }
                userId = loginUser.getId();
            }

            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                List<UserBasicInfo> data = watchListPeopleService.getWatchPeopleInfo(userId);
                PageInfo<UserBasicInfo> pages = new PageInfo<UserBasicInfo>(data);
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<UserBasicInfo> beWatchedUsers = watchListPeopleService.getWatchPeopleInfo(userId);
            return ReturnResult.successResult("data", beWatchedUsers, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 根据当前登录用户 ,
     * 获取其粉丝列表
     */
    @GetMapping("/fans")
    @Access
    //TODO  登录失败，权限校验。。。
    @ResponseBody
    public Map getFansUsers(HttpServletRequest request,
                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize,
                            @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
        try {

            User loginUser = GlobalUtils.getLoginUser(request);
            if (loginUser == null || loginUser.getId() == null) {
                throw new UnPermissionException();
            }
            Integer userId = loginUser.getId();
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<UserBasicInfo> pages = new PageInfo<UserBasicInfo>(watchListPeopleService.getFansInfo(userId));
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<UserBasicInfo> beWatchedUsers = watchListPeopleService.getFansInfo(userId);
            return ReturnResult.successResult("data", beWatchedUsers, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 根据当前登录用户 ,
     * 获取关注动态
     */
    @GetMapping("/dynamic")
    //TODO  登录失败，权限校验。。。
    @ResponseBody
   // @Access
    public Map getPeopleDynamic(HttpServletRequest request,Integer userId,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize) {
        try {
            if (userId == null) { // 没传id ,说明是后台查询
                User loginUser = GlobalUtils.getLoginUser(request);
                if (loginUser == null) {
                    return ReturnResult.errorResult(ReturnType.GET_ERROR);
                }
                userId = loginUser.getId();
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<PeopleDynamic> pages = new PageInfo<PeopleDynamic>(watchListPeopleService.getPeopleDynamic(userId));
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<PeopleDynamic> peopleDynamics = watchListPeopleService.getPeopleDynamic(userId);
            return ReturnResult.successResult("data", peopleDynamics, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}

