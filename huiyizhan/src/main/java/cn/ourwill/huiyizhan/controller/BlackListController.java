package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.BlackList;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IBlackListService;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 16:48
 * @Version1.0
 */

@RestController
@RequestMapping("/api/blackList")
@Slf4j
public class BlackListController {
    @Autowired
    private IBlackListService blackListService;
    @Autowired
    private IUserService userService;

    @PostMapping
    @ResponseBody
    @Access(level = 1)
    public Map create(HttpServletRequest request, @RequestBody BlackList blackList){
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            User user = userService.selectUserByUsername(blackList.getUsername());
            if(user==null)
                return ReturnResult.errorResult("用户不存在！");
            if(blackListService.isInBlackList(user.getId())){
                return ReturnResult.errorResult("该用户已存在生效的黑名单！");
            }
            blackList.setUserId(user.getId());
            blackList.setCId(loginUser.getId());
            if(blackListService.save(blackList)>0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.info("BlackListController.create",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

//    @DELETE
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Access(level = 1)
//    public Map delete(@Context HttpServletRequest request,@PathParam("id") Integer id){
//        try {
//            if (blackListService.delete(id) > 0) {
//                return ReturnResult.successResult( ReturnType.DELETE_SUCCESS);
//            }
//            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ReturnResult.errorResult("server error");
//        }
//    }

    @PostMapping("/unlock")
    @ResponseBody
    public Map unlock(HttpServletRequest request,@RequestBody List<Integer> ids){
        try {
            if(ids==null||ids.size()<1){
                return ReturnResult.errorResult("参数为空！");
            }
            if (blackListService.unlock(ids) > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.info("BlackListController.unlock",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Access(level = 1)
    public Map update(HttpServletRequest request,@PathVariable("id") Integer id,@RequestBody BlackList blackList){
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            BlackList oldObj = blackListService.getById(id);
            if(oldObj==null) return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            //判断是否可以修改
            Date now = new Date();
            if(oldObj.getStatus().equals(0)||now.after(oldObj.getEndDate())) {
                return ReturnResult.errorResult("本记录已失效，请重新添加！");
            }
            BlackList newObj = new BlackList();
            newObj.setId(id);
            newObj.setStartDate(oldObj.getStartDate());
            newObj.setType(blackList.getType());
            newObj.setUId(loginUser.getId());
            newObj.setReason(blackList.getReason());
            if (blackListService.update(newObj) > 0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.info("BlackListController.update",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PostMapping("/selectByParams")
    @ResponseBody
    @Access(level = 1)
    public Map selectByParams( @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                         @RequestBody(required = false) Map param){
        try {
            PageHelper.startPage(pageNum,pageSize);
            if(param==null) param = new HashMap();
            List<BlackList> list = blackListService.selectByParams(param);
            PageInfo<BlackList> pages = new PageInfo<>(list);
            return ReturnResult.successResult("data", pages, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.info("BlackListController.selectByParams",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
