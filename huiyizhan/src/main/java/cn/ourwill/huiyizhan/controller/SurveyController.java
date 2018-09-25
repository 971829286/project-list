package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.Survey;
import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.service.ISurveyQuestionService;
import cn.ourwill.huiyizhan.service.ISurveyService;
import cn.ourwill.huiyizhan.utils.GlobalUtils;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-16 11:37
 **/
@RestController
@RequestMapping("/api/survey")
@Slf4j
public class SurveyController {

    @Autowired
    private ISurveyService surveyService;

    @Autowired
    private ISurveyQuestionService surveyQuestionService;

    @Autowired
    private IActivityService activityService;

    /**
     * <pre>
     *     初始保存采用此方法
     *      添加 调查问卷 ，
     *      包括 问卷问题
     * </pre>
     */
    @PostMapping
    @Access
    public Map addSurvey(HttpServletRequest request, @RequestBody Survey survey) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            Integer uId = loginUser.getId();
            survey.setUid(uId);
            Integer surveyId = surveyService.add(survey);
            Survey reSurvey = null;
            if(surveyId!=null&&surveyId!=0){
                reSurvey = surveyService.getSurveyWithQuestionById(surveyId);
                return ReturnResult.successResult("data",reSurvey,ReturnType.ADD_SUCCESS);
            }else {
                return ReturnResult.successResult(ReturnType.ADD_ERROR);
            }
        } catch (Exception e) {
            log.error("SurveyController.addSurvey", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     *     初始保存采用此方法
     *      添加 调查问卷 ，
     *      包括 问卷问题
     * </pre>
     */
    @PostMapping("/copy/{surveyId}")
    @Access
    public Map copySurvey(HttpServletRequest request, @PathVariable("surveyId") Integer surveyId) {
        try {
            surveyService.copy(surveyId);
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyController.copySurvey", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *     更具调查id
     *           获取 调查 信息，(包括 问卷问题)
     * </pre>
     */
    @GetMapping("/{id}")
    @Access
    public Map getSurvey(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            Survey survey = surveyService.getSurveyWithQuestionById(id);
            return ReturnResult.successResult("data", survey, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyController.getSurvey", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * <pre>
     *  通过
     *      用户id(当前登录用户id) 和 调查类型获取 调查信息（0-问卷，1-投票）
     *  获取 问卷 信息，
     * </pre>
     */
    @GetMapping("/base/{type}")
    @Access
    public Map getSurveyByUid(HttpServletRequest request, @PathVariable("type") Integer type,
                              @RequestParam(value = "searchText", defaultValue = "") String searchText,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "9") Integer pageSize) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            Integer uId = loginUser.getId();
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<Survey> surveys = new PageInfo<>(surveyService.getUID(uId, type,searchText));
            return ReturnResult.successResult("data", surveys, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyController.getSurveyByUid", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *  删除问卷：
     *      问卷问题 不删除
     *
     * </pre>
     */
    @DeleteMapping("/{surveyId}")
    @Access
    public Map deleteSurvey(HttpServletRequest request, @PathVariable("surveyId") Integer surveyId) {
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            Integer containPermission = surveyService.checkPermission(loginUser.getId(), surveyId);
            if (containPermission + 1 == 0) {
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }

            Integer resultCode = surveyService.updateDeleteStatus(surveyId);
            if (resultCode < 0){
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);

        } catch (Exception e) {
            log.error("SurveyController.deleteSurvey", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 更新
     */
    @PutMapping("/{surveyId}")
    @Access
    public Map updateSurvey(HttpServletRequest request, @RequestBody Survey survey, @PathVariable("surveyId") Integer surveyId) {
        try {
            Integer resultCode = surveyService.update(survey);
            if (resultCode < 0){
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);

        } catch (Exception e) {
            log.error("SurveyController.updateSurvey", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @PutMapping("/status/{status}")
    @Access
    public Map updateSurvey(HttpServletRequest request, @PathVariable("status") Integer id) {
        try {
            Integer resultCode = surveyService.updateStatus(id);
            if (resultCode < 0){
                return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
            }
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);

        } catch (Exception e) {
            log.error("SurveyController.updateSurvey", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /******************************************问题的增删改查:start***********************************/
    /**
     * 添加 问题 ，
     */
    @PostMapping("/question")
    @Access
    public Map addSurveyQuestion(HttpServletRequest request, @RequestBody SurveyQuestion surveyQuestion) {
        try {
            surveyQuestionService.save(surveyQuestion);
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyController.addSurveyQuestion", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * <pre>
     *     删除问题
     * </pre>
     */
    @DeleteMapping("question/{id}")
    @Access
    public Map deleteSurveyQuestion(HttpServletRequest request, @PathVariable("id") Integer id) {
        try {
            Integer resultCode = surveyQuestionService.updateDeleteStatus(id);
            if (resultCode < 0){
                return ReturnResult.successResult(ReturnType.DELETE_ERROR);
            }
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);

        } catch (Exception e) {
            log.error("SurveyController.deleteSurveyQuestion", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

/******************************************问题的增删改查:end***********************************/

    /**
     * 获取当前用户的活动列表
     * @param request
     * @param visitType
     * @return
     */
    @GetMapping("getActivity/{visitType}")
    @Access
    public Map getActivityList(HttpServletRequest request,@PathVariable("visitType") Integer visitType){
        List<Activity> list= null;
        try {
            User loginUser = GlobalUtils.getLoginUser(request);
            if(visitType==1){
                list = activityService.getAllActivityNotOver(loginUser.getId());
            }else if (visitType==2){
                list = activityService.getActivityOver(loginUser.getId());
            }else {
                return ReturnResult.errorResult("请选择活动状态！");
            }
            return ReturnResult.successResult("data",list,ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }

    }
}
