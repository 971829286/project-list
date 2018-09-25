package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.ActivityDynamic;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityStatisticsService;
import cn.ourwill.huiyizhan.service.IWatchListService;
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
 * 会议收藏
 *
 * @author uusao
 */
@RestController
@RequestMapping("/api/watch")
public class WatchListContrller {

    @Autowired
    private IWatchListService watchListService;

    @Autowired
    private IActivityStatisticsService activityStatisticsService;

    /**
     * 获取当前登录人收藏（会议）信息
     * 0 ---  未发布
     * 1--- 已发布
     * 2--- 结束
     *
     */
    @GetMapping("/{status}")
    //@Access
    @ResponseBody
    public Map getWatchList(@PathVariable("status") Integer status,Integer userId,
            HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") Integer page,
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
                PageInfo<Activity> pages = new PageInfo<>(watchListService.getWatchList(userId,status));
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<Activity> watchLists = watchListService.getWatchList(userId,status);
            return ReturnResult.successResult("data", watchLists, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 添加 收藏
     *
     * @param request
     * @param id
     * @return
     */
    @PostMapping(value = "/{id}")
    @Access
    @ResponseBody
    public Map addWatch(HttpServletRequest request, @PathVariable("id") Integer id) {

        try {
            Integer loginUserId = GlobalUtils.getLoginUser(request).getId();
            Boolean isWatch = watchListService.checkWatchStatus(id, loginUserId);
            if (isWatch) {  // 已收藏
                return ReturnResult.errorResult("已收藏！");
            } else {
                watchListService.addWatch(id, loginUserId);
                //发送redis 消息 收藏 +1 ,浏览量+1       同步收藏数量
                activityStatisticsService.addWatch(id);
                activityStatisticsService.syncCollectCount(id);
                return ReturnResult.successResult("收藏成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

    /**
     * 取消 收藏 （对会议的收藏）
     *
     * @param request
     * @param id      要取消关注的会议id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @Access
    @ResponseBody
    public Map cancelWatch(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            Integer loginUserId = GlobalUtils.getLoginUser(request).getId();

            Boolean isWatch = watchListService.checkWatchStatus(id, loginUserId);
            if (!isWatch) {  // 未收藏
                return ReturnResult.errorResult("未收藏！");
            } else {
                watchListService.cancelWatchById(id, loginUserId);
                // 同步收藏数目
                activityStatisticsService.syncCollectCount(id);
                return ReturnResult.successResult("取消收藏成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     *
     * 根据当前登录用户 ,
     * ---------获取会议收藏动态
     */
    @GetMapping("/dynamic")
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
                PageInfo<ActivityDynamic> pages = new PageInfo<ActivityDynamic>(watchListService.getActivityDynamic(userId));
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<ActivityDynamic> peopleDynamics = watchListService.getActivityDynamic(userId);
            return ReturnResult.successResult("data", peopleDynamics, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
