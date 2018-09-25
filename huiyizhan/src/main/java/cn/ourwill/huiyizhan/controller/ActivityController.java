package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.aop.UnPermissionException;
import cn.ourwill.huiyizhan.aop.UnauthenticatedException;
import cn.ourwill.huiyizhan.entity.*;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.*;
import cn.ourwill.huiyizhan.service.search.IElasticSearchService;
import cn.ourwill.huiyizhan.service.search.ISearchService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/25 0025 15:49
 * @Version1.0
 */
@RestController
@RequestMapping("/api/activity")
@Slf4j

/*@Api(value = "活动/会议",description="活动会议相关接口")*/
public class ActivityController {


    @Autowired
    private IActivityService activityService;

    @Autowired
    private ITicketsRecordService ticketsRecordService;

    @Autowired
    private IActivityStatisticsService activityStatisticsService;

    @Autowired
    private IBannerHomeService bannerHomeService;

    @Autowired
    private IElasticSearchService elasticSearchService;

    @Autowired
    private ISearchService searchService;

    @Autowired
    private IImgService imgService;

    @Autowired
    private IActivityTicketsService activityTicketsService;

    @Autowired
    private RedisTemplate redisTemplate;
/*    @ApiOperation(value = "创建活动", notes = "根据activity对象创建活动")
//    @ApiImplicitParams({
//            @ApiImplicitParam(dataType = "activity", name = "activity", value = "活动信息", required = true)
//    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "增加成功", response = Return.class),
            @ApiResponse(code = 500, message = "接口异常")
    })*/

    /**
     * 创建会议  ---- 新增
     *
     * @param activity
     * @return
     */
    @PostMapping
    @ResponseBody
    @Access
    public Map addActivity(HttpServletRequest request, @RequestBody Activity activity) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            activity.setUserId(loginUser.getId());
            Integer resultCode = activityService.save(activity);
            if (resultCode > 0) {
                //向ES中插入数据
                // 公开 && 发布
                if ((activity.getIsOpen() != null && activity.getIsOpen() == 1)
                        && (activity.getIssueStatus() != null && activity.getIssueStatus() == 1)) {
                    SearchBean searchBean = searchService.getSearchBean(activity.getId());
                    elasticSearchService.insert(searchBean);
                }
                //redis同步数据
                userStatisticsService.syncActivityCount(loginUser.getId());
//                userStatisticsService.addTotalActivityCount(loginUser.getId());
//                if(activity.getIssueStatus() == 1){
//                    userStatisticsService.addActivityCount(loginUser.getId());
//                }
                return ReturnResult.successResult("data", activity, ReturnType.ADD_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
        } catch (Exception e) {
            log.info("ActivityController.addActivity", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取会议  ---- 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    public Map getActivity(@PathVariable("id") Integer id) {
        try {
            Activity activity = activityService.getById(id);
            if (activity == null) {
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            return ReturnResult.successResult("data", activity, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getActivity ", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

    /**
     * 删除会议  ----删除
     *
     * @param id
     * @return
     */
//    @DeleteMapping("/{id}")
//    @ResponseBody
//    @Access
//    public Map deleteActivity(HttpServletRequest request, @PathVariable("id") Integer id) {
//        try {
//            User loginUser = GlobalUtils.getLoginUser(request);
//            //验权
//            if (!activityService.checkOwnerOrAdmin(id, loginUser)) {
//                throw new UnPermissionException();
//            }
//            Activity activity = activityService.getById(id);
//            Integer resultCode = activityService.delete(id);
//            if (resultCode > 0) {
//                //在搜索中删除该条记录
//                elasticSearchService.deleteByActivityId(id);
//                //在banner图中删除
//                if(activity.getBannerId() != null && activity.getBannerId() > 0){
//                    bannerHomeService.delete(activity.getBannerId());
//                }
//                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
//            } else {
//                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
//            }
//        } catch (UnPermissionException e) {
//            throw e;
//        } catch (Exception e) {
//            log.info("ActivityController.deleteActivity", e);
//            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
//        }
//    }

    /**
     * 更新会议  ----更新
     *
     * @param activity
     * @return
     */
    @PutMapping("/{id}")
    @ResponseBody
    @Access
    public Map uploadActivity(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody Activity activity) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(id, loginUser)) {
                throw new UnPermissionException();
            }
//            System.out.println(activity);
            activity.setId(id);
            activity.setUId(loginUser.getId());
            activity.setUTime(new Date());
            Integer resultCode = activityService.update(activity);
            if (resultCode > 0) {
//                同步搜索中的数据
                Activity activityFlag = activityService.getById(id);
                Integer isOpen = activityFlag.getIsOpen();
                Integer issueStatus = activityFlag.getIssueStatus();
                //不公开 || 未发布
                if ((isOpen != null && isOpen == 0) || (issueStatus != null && issueStatus == 0)) {
                    elasticSearchService.deleteByActivityId(id);
                }
                elasticSearchService.insert(searchService.getSearchBean(activity.getId()));
                //更新bnaner
//                if(activity.getBannerId() != null && activity.getBannerId() > 0){
//                    BannerHome bannerHome = new BannerHome();
//                    bannerHome.setId(activity.getBannerId());
//                    bannerHome.setMobilePic(activity.getActivityBannerMobile());
//                    bannerHome.setPic(activity.getActivityBanner());
//                    bannerHomeService.update(bannerHome);
//                }
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityController.uploadActivity", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取会议列表
     */
    @GetMapping("/getAll")
    @ResponseBody
    public Map getActivity(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                           @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
        try {
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<Activity> pages = new PageInfo<Activity>(activityService.getActivityList());
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<Activity> activities = activityService.getActivityList();
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }


    /**
     * 获取会议  ---- 包含（联系人、嘉宾、合作伙伴、日程、票）
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Map getActivityDetail(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            Activity activity = activityService.getDetailById(id, loginUser);
            boolean access = false;
            if (activity == null) {
                return ReturnResult.errorResult("活动不存在或已被关闭！");
            }
            if (loginUser != null) {
                access = activityService.checkOwnerOrAdmin(activity.getId(), loginUser);
            }
            if ((!access && activity.getIsOpen() != null && activity.getIsOpen().equals(0)) ||
                    (!access && activity.getIssueStatus() != null && activity.getIssueStatus().equals(0))) {
                //不是管理员或者创建者并且会议不公开
                return ReturnResult.errorResult("该活动已关闭或者尚未公开！");
            }
            activity.setActivityStatistics(activityStatisticsService.getActivityStatisticsFromRedis(id));
            activity.setValidActivityTickets(activityTicketsService.getValidByActivityId(id));
            return ReturnResult.successResult("data", activity, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getActivity ", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }


    /**
     * 获取会议通过userId  ---- 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/userId")
    @ResponseBody
    public Map getActivityByUserId(Integer id, HttpServletRequest request,
                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                   @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy,
                                   @RequestParam(value = "status", required = false) Integer status) {
        try {
            if (id == null) {
                User user = GlobalUtils.getLoginUser(request);
                if (user == null) {
                    throw new UnauthenticatedException();
                }
                id = user.getId();
            }
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy(" a.id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy(" a.id asc");
            }
            HashMap param = new HashMap();
            if (status != null)
                param.put("status", status);
            param.put("userId", id);
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                List<Activity> activityList = activityService.selectByParam(param);
                PageInfo<Activity> pages = new PageInfo<Activity>(activityList);
                activityList.stream().forEach(entity -> {
                    entity.setActivityStatistics(activityStatisticsService.getActivityStatisticsFromRedis(entity.getId()));
                    if ((entity.getBannerId() == null || entity.getBannerId() == 1 || entity.getBannerId() == -1)) {
                        entity.setBannerId(1);
                    } else {
                        entity.setBannerId(0);
                    }
                });
                return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
            }
            List<Activity> activities = activityService.getByUserId(id);
            activities.stream().forEach(entity -> {
                if ((entity.getBannerId() == null || entity.getBannerId() == -1 || entity.getBannerId() == 1)) {
                    entity.setBannerId(1);
                } else {
                    entity.setBannerId(0);
                }
            });
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (UnauthenticatedException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityController.getActivity ", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }

    /**
     * 获取我参与的活动
     *
     * @param request //     * @param pageNum
     *                //     * @param pageSize
     * @return
     */
    @GetMapping("/myJoin")
    @Access
    public Map getActivityJoin(HttpServletRequest request,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               @RequestParam(value = "isValid", defaultValue = "true") Boolean isValid) {
        try {
            User user = GlobalUtils.getLoginUser(request);
            if (user.getId() == null) {
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            List<Activity> reList = new ArrayList<>();
            if (page != null && page == 0) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<Activity> activityList = new PageInfo<>(activityService.getActivityJoin(user, isValid));
                if (activityList.getList() != null && activityList.getList().size() > 0) {
                    reList = activityList.getList().stream().map(entity -> {
                        if (entity.getId() != null) {
                            List<TicketsRecord> ticketsRecords = ticketsRecordService.getByActivityIdUserId(entity.getId(), user.getId(), isValid);
                            entity.setTicketsRecords(ticketsRecords);
                        }
                        return entity;
                    }).collect(Collectors.toList());
//                            .filter(entity -> entity.getTicketsRecords().size() > 0).collect(Collectors.toList());
                }
                activityList.setList(reList);
                return ReturnResult.successResult("data", activityList, ReturnType.GET_SUCCESS);
            } else {
                List<Activity> activityList = activityService.getActivityJoin(user, isValid);
                if (activityList != null && activityList.size() > 0) {
                    reList = activityList.stream().map(entity -> {
                        if (entity.getId() != null) {
                            entity.setTicketsRecords(ticketsRecordService.getByActivityIdUserId(entity.getId(), user.getId(), isValid));
                        }
                        return entity;
                    }).collect(Collectors.toList());
//                            .filter(entity -> entity.getTicketsRecords().size() > 0).collect(Collectors.toList());
                }
                return ReturnResult.successResult("data", reList, ReturnType.GET_SUCCESS);
            }
        } catch (Exception e) {
            log.info("ActivityController.getActivityJoin", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *
     *  设置是否热门(置反操作)
     * </pre>
     */
    @PutMapping("/hot/{id}")
    @Access(level = 1)
    public Map setIsHot(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            Activity activity = activityService.getById(id);
            if (activity == null) {
                return ReturnResult.errorResult("活动不存在！");
            }
            if (activity.getIsOpen().equals(0)) {
                return ReturnResult.errorResult("活动未对外开放，设置失败！");
            }
            activityService.setIsHot(activity.getId(), activity.getIsHot());
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.setIsHot", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     *
     *  设置是否最近(置反操作)
     * </pre>
     */
    @PutMapping("/recent/{id}")
    @Access(level = 1)
    public Map setIsRecent(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            Activity activity = activityService.getById(id);
            if (activity == null) {
                return ReturnResult.errorResult("活动不存在！");
            }
            if (activity.getIsOpen().equals(0)) {
                return ReturnResult.errorResult("活动未对外开放，设置失败！");
            }
            activityService.setIsRecent(activity.getId(), activity.getIsRecent());
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.setIsRecent", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *   获取当前登录人的热门会议列表
     *
     * </pre>
     */
    @GetMapping("/hots")
    @Access
    public Map getHostList(HttpServletRequest request) {
        try {
            List<Activity> activities = activityService.getHotList();
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getHostList", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     *     更新当前登录人的热门会议列表顺序
     *
     * </pre>
     */
    @PutMapping("/hots")
    @Access
    public Map updateHostList(HttpServletRequest request, @RequestBody List<Integer> activityIds) {
        try {
            Integer code = activityService.updateHotSort(activityIds);
            if (code < 0) {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.updateHostList", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *   获取当前登录人的最近会议列表
     *
     * </pre>
     */
    @GetMapping("/recents")
    @Access
    public Map getRecentList(HttpServletRequest request) {
        try {
            List<Activity> activities = activityService.getRecentList();
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getRecentList", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     *     更新当前登录人的热门会议列表顺序
     *
     * </pre>
     */
    @PutMapping("/recents")
    @Access
    public Map updateRecentList(HttpServletRequest request, @RequestBody List<Integer> activityIds) {
        try {
            Integer code = activityService.updateRecentSort(activityIds);
            if (code < 0) {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.updateRecentList", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *
     *    发布---置反
     * </pre>
     *
     * @param id 会议id
     */
    @PutMapping("/issue/{id}")
    @Access
    public Map issue(HttpServletRequest request, @PathVariable("id") int id) {
        try {

            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(id, loginUser)) {
                throw new UnPermissionException();
            }
            Integer code = activityService.updateIssueStatus(id, loginUser.getId());
            if (code == null || code < 0) {
                if (code == -2) return ReturnResult.errorResult("已售出并存在有效门票的活动，不可取消发布！");
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            } else {
                Activity activity = activityService.getById(id);
                //同步redis
                userStatisticsService.syncActivityCount(activity.getUserId());
                Integer issueStatus = activity.getIssueStatus();
                //未发布,同步搜索中的数据
                if (issueStatus != null && issueStatus == 0) {
                    elasticSearchService.deleteByActivityId(id);
                    //设置为未发布后,将banner图取消
                    Integer bannerId = activity.getBannerId();
                    if (bannerId != null && bannerId > 1) {
                        Integer delete = bannerHomeService.delete(bannerId);
                        Integer update = activityService.updateBannerId(id, 0);//0标识非banner
                    }
                } else {
                    SearchBean searchBean = searchService.getSearchBean(id);
                    elasticSearchService.insert(searchBean);
                }
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityController.issue", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @Autowired
    private IUserStatisticsService userStatisticsService;

    /**
     * 数据中心 ----- 获取数据
     *
     * @return
     */
    @GetMapping("/dataCenter")
    @Access
    public Map getDataCenter(HttpServletRequest request,
                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                             @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
//            //验权
//            if (loginUser == null || loginUser.getId() == null || loginUser.getId() != userId) {
//                throw new UnPermissionException();
//            }
            HashMap<String, Object> map = new HashMap<>();
            UserStatistics userStatistics = userStatisticsService.getUserStatisticsFromRedis(loginUser.getId());
            userStatistics.setActivityCount(activityService.getActivityCountWithOutDelete(loginUser.getId()));

            if (page != null && page == 1) {
                if (orderBy == 1) {//1是正序,0是倒序
                    PageHelper.orderBy("start_time asc");
                } else {
                    PageHelper.orderBy("start_time desc");
                }
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<Activity> pageInfo = new PageInfo<>(activityService.getINGActivity(loginUser.getId()));
                map.put("userStatistics", userStatistics);
                map.put("ingActivitys", pageInfo);
                return ReturnResult.successResult("data", map, ReturnType.GET_SUCCESS);
            }

            // 获取统计信息
            // 获取进行中的活动,
            List<Activity> ingActivitys = activityService.getINGActivity(loginUser.getId());
            map.put("userStatistics", userStatistics);
            map.put("ingActivitys", ingActivitys);
            return ReturnResult.successResult("data", map, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityController.getDataCenter", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 会议管理 ----- 获取已经发布/未发布的会议
     * <p>
     * 0: no
     * 1:yes
     *
     * @return
     */
    @GetMapping("/issue/{status}")
    //@Access
    public Map getActivityByStatus(HttpServletRequest request, Integer userId,
                                   @PathVariable("status") int status,
                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                   @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
        try {
            if (userId == null) { // 没传id ,说明是后台查询
                User loginUser = GlobalUtils.getLoginUser(request);
                if (loginUser == null) {
                    return ReturnResult.errorResult(ReturnType.GET_ERROR);
                }
                userId = loginUser.getId();
            }
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy("id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy("id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<Activity> pageInfo = new PageInfo<>(activityService.getActivityByStatus(userId, status));
                return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
            }
            List<Activity> activities = activityService.getActivityByStatus(userId, status);
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getActivityByStatus", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 会议管理 ----- 获取已经结束的会议
     *
     * @return
     */
    @GetMapping("/issueOver")
    @Access
    public Map getActivityOver(HttpServletRequest request, Integer userId,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
        try {
            if (userId == null) { // 没传id ,说明是后台查询
                User loginUser = GlobalUtils.getLoginUser(request);
                if (loginUser == null) {
                    return ReturnResult.errorResult(ReturnType.GET_ERROR);
                }
                userId = loginUser.getId();
            }
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy("id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy("id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<Activity> pageInfo = new PageInfo<>(activityService.getActivityOver(userId));
                return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
            }
            List<Activity> activities = activityService.getActivityOver(userId);
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getActivityOver", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取会议类型
     *
     * @param request
     * @return
     */
    @GetMapping("/getActivityType")
    @ResponseBody
    public Map getActivityType(HttpServletRequest request) {
        try {
            List<ActivityType> list = activityService.getActivityTypeList();
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getActivityType", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("/{activityId}/ticketStatistics")
    @ResponseBody
    @Access
    public Map getTicketStatistics(HttpServletRequest request, @PathVariable("activityId") Integer activityId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            Map reMap = ticketsRecordService.selectStatisticsByActivityId(activityId);

            return ReturnResult.successResult("data", reMap, ReturnType.GET_SUCCESS);
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityController.getTicketStatistics", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 管理员按状态获取全部会议
     *
     * @param request
     * @param status
     * @return
     */
    @GetMapping("/issueByAdmin/{status}")
    @Access(level = 1)
    public Map getActivityByStatusByAdmin(HttpServletRequest request, @PathVariable("status") int status,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                          @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
        try {
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy("id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy("id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<Activity> pageInfo = new PageInfo<>(activityService.getAllActivityByStatus(status));
                return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
            }
            List<Activity> activities = activityService.getAllActivityByStatus(status);
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getActivityByStatus", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 管理员获取全部已经结束的会议
     *
     * @return
     */
    @GetMapping("/issueOverByAdmin")
    @Access(level = 1)
    public Map getActivityOverByAdmin(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "1") Integer orderBy) {
        try {
            if (orderBy != null && orderBy == 0) {
                PageHelper.orderBy("id desc");
            } else if (orderBy != null && orderBy == 1) {
                PageHelper.orderBy("id asc");
            }
            if (page != null && page == 1) {
                PageHelper.startPage(pageNum, pageSize);
                PageInfo<Activity> pageInfo = new PageInfo<>(activityService.getAllActivityOver());
                return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
            }
            List<Activity> activities = activityService.getAllActivityOver();
            return ReturnResult.successResult("data", activities, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("ActivityController.getActivityOver", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 将会议设置为banner图,可以置反
     *
     * @param activityId
     * @param request
     * @return
     */
    @PutMapping("/toggleBanner/{activityId}")
    @Access(level = 1)
    public Map toggleBanner(@PathVariable("activityId") Integer activityId, HttpServletRequest request) {
        User loginUser = GlobalUtils.getLoginUser(request);
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            return ReturnResult.errorResult("该记录不存在");
        }
        if (activity.getActivityBanner() == null) {
            return ReturnResult.errorResult("设置失败:缺少PC端banner图");
        }
        if (activity.getActivityBannerMobile() == null) {
            return ReturnResult.errorResult("设置失败:缺少移动端banner图");
        }
        if (activity.getIsOpen().equals(0)) {
            return ReturnResult.errorResult("活动未对外开放，设置失败");
        }
        if (activity.getIssueStatus().equals(0)) {
            return ReturnResult.errorResult("活动未公开，设置失败");
        }
        Integer bannerId = activity.getBannerId();
        if (bannerId == null || bannerId == 0) {
            //将该会议设置成banner
            BannerHome bannerHome = new BannerHome();
            bannerHome.setMobilePic(activity.getActivityBannerMobile());
            bannerHome.setPic(activity.getActivityBanner());
            bannerHome.setPriority(0);
            bannerHome.setTitle(activity.getActivityTitle());
            bannerHome.setCTime(new Date());
            bannerHome.setCUser(loginUser.getId());
            bannerHome.setUTime(new Date());
            bannerHome.setUUser(loginUser.getId());
            bannerHome.setLink("/web/activity/" + activityId);
            bannerHome.setActivityId(activityId);
            try {
                bannerHomeService.save(bannerHome);
                //更新bannerId
                activityService.updateBannerId(activityId, bannerHome.getId());
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } catch (QiniuException e) {
                log.info("BannerController.create", e);
                return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
            }
        } else {
            //将会议取消设置banner
            Integer delete = bannerHomeService.delete(bannerId);
            Integer update = activityService.updateBannerId(activityId, 0);//0标识非banner
            if (delete < 1 || update < 1) {
                return ReturnResult.errorResult("修改失败");
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        }
    }

    @PostMapping("/insertDefaultImage")
    public Map insertImg(@RequestBody Image image) {
        Integer type = image.getType();
        if (image.getPic() == null) {
            return ReturnResult.errorResult("url为空");
        }
        if (type == null) image.setType(1);
        //默认type=1 标识手机端
        try {
            Integer flag = imgService.save(image);
            if (flag > 0) {
                return ReturnResult.successResult("图片上传成功");
            } else {
                return ReturnResult.errorResult("图片上传失败");
            }
        } catch (Exception e) {
            log.info("ActivityController.insertImg", e);
            return ReturnResult.errorResult("图片上传失败");
        }
    }


    @GetMapping("/getDefaultImages")
    public Map getDefaultImages() {
        List<Image> all = imgService.findAll();
        if (all == null) {
            return ReturnResult.errorResult("获取数据失败");
        }
        return ReturnResult.successResult("data", all, ReturnType.GET_SUCCESS);
    }

    @PutMapping("/activityConfig/{activityId}")
    @Access
    public Map updateConfig(HttpServletRequest request, @PathVariable("activityId") Integer activityId, @RequestBody Activity activity) {
        try {
            if (activity == null || (activity.getTicketConfig() == null && activity.getCustomUrl() == null))
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            User loginUser = GlobalUtils.getLoginUser(request);
            //验权
            if (!activityService.checkOwnerOrAdmin(activityId, loginUser)) {
                throw new UnPermissionException();
            }
            Activity param = new Activity();
            if (activity.getTicketConfig() != null) param.setTicketConfig(activity.getTicketConfig());
            if (activity.getCustomUrl() != null) {
                if (!activityService.checkCustomUrl(activity.getCustomUrl(), activityId)) {
                    return ReturnResult.errorResult("自定义URL已存在，请更换后校验！");
                }
                param.setCustomUrl(activity.getCustomUrl());
            }
            param.setId(activityId);
            param.setUId(loginUser.getId());
            param.setUTime(new Date());
            Activity oldActivity = activityService.getById(activityId);
            Integer resultCode = activityService.update(param);
            if (resultCode > 0) {
                if (StringUtils.isNotEmpty(activity.getCustomUrl())) {
                    if (StringUtils.isNotEmpty(oldActivity.getCustomUrl())) {
                        //删除旧制
                        redisTemplate.delete("customUrl:" + oldActivity.getCustomUrl());
                    }
                    redisTemplate.opsForValue().set("customUrl:" + activity.getCustomUrl(), activityId);
                }
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (UnPermissionException e) {
            throw e;
        } catch (Exception e) {
            log.info("ActivityController.uploadActivity", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("/checkUrl/{activityId}")
    @Access
    public Map validUrl(@RequestParam("customUrl") String customUrl, @PathVariable("activityId") Integer activityId) {
        if (!activityService.checkCustomUrl(customUrl, activityId)) {
            return ReturnResult.errorResult("已存在！");
        }
        return ReturnResult.successResult("可用！");
    }

    @GetMapping("/getIdBySuffix")
    public Map getActivityIdBySuffix(@RequestParam String customUrl) {
        Integer id = (Integer) redisTemplate.opsForValue().get("customUrl:" + customUrl);
        if (id == null) {
            return ReturnResult.errorResult(ReturnType.GET_ERROR);
        }
        return ReturnResult.successResult("id", id, ReturnType.GET_SUCCESS);
    }
}
