package cn.ourwill.huiyizhan.controller;


import cn.ourwill.huiyizhan.entity.BannerHome;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.service.IBannerHomeService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/6 0006 13:56
 * 首页banner图
 * @Version1.0
 */

@Slf4j
@RestController
@RequestMapping("/api/banner")
public class BannerController {
    @Autowired
    private IBannerHomeService bannerHomeService;

    @Autowired
    private IActivityService activityService;

    @Access(level = 1)
    @PostMapping
    @ResponseBody
    public Map create(HttpServletRequest request, @RequestBody  BannerHome bannerHome) {
        try {

            Integer userId = GlobalUtils.getLoginUser(request).getId();
            if (userId != null) {
                bannerHome.setCUser(userId);
                bannerHome.setCTime(new Date());
                bannerHomeService.save(bannerHome);
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        } catch (Exception e) {
            log.info("BannerController.create", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @Access(level = 1)
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public Map delete(@PathVariable("id") Integer id) {
        try {
            BannerHome bannerHome = bannerHomeService.selectById(id);
            if(bannerHome == null){
                return ReturnResult.errorResult("该条记录不存在");
            }
            Integer activityId = bannerHome.getActivityId();
            if (bannerHomeService.delete(id) > 0) {
                if(activityId != null){
                    activityService.updateBannerId(activityId,0);
                }
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        } catch (Exception e) {
            log.info("BannerController.delete", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 轮播图更新
     *
     * @param request
     * @param bannerHome
     * @return
     */
    @Access(level = 1)
    @PutMapping(value = "/{id}")
    @ResponseBody
    public Map update(HttpServletRequest request,@PathVariable("id") Integer id,
                      @RequestBody  BannerHome bannerHome) {
        try {
            User user = GlobalUtils.getLoginUser(request);
            bannerHome.setUUser(user.getId());
            bannerHome.setUTime(new Date());
            if (bannerHomeService.update(bannerHome) > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            } else {
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
        } catch (Exception e) {
            log.info("BannerController.update", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Map getById(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            BannerHome bannerHome = bannerHomeService.getById(id);
            if (bannerHome != null)
                return ReturnResult.successResult("data", bannerHome, ReturnType.GET_SUCCESS);
            else
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
        } catch (Exception e) {
            log.info("BannerController.getById", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping
    @ResponseBody
    public Map selectAll() {
        try {
            List<BannerHome> list = bannerHomeService.findAll();
            PageInfo<BannerHome> pages = new PageInfo<>(list);
            if(pages==null || pages.getSize()<=0){
                return ReturnResult.errorResult( ReturnType.GET_ERROR);
            }
            return ReturnResult.successResult("data", list, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.info("BannerController.selectAll", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    @Access(level = 1)
    @PutMapping("/updatePriority")
    @ResponseBody
    public Map updatePriority(HttpServletRequest request, @RequestBody List<Integer> ids) {
        try {
            if (ids == null && ids.size() < 1) {
                return ReturnResult.errorResult("参数为空！");
            }
            for (int i = 0; i <= ids.size() - 1; i++) {
                bannerHomeService.updatePriorityById(ids.get(i), i);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.info("BannerController.selectAll", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
