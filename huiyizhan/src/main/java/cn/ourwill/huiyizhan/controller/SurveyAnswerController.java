package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.Survey;
import cn.ourwill.huiyizhan.entity.SurveyAnswer;
import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.ISurveyAnswerService;
import cn.ourwill.huiyizhan.service.ISurveyQuestionService;
import cn.ourwill.huiyizhan.service.ISurveyService;
import cn.ourwill.huiyizhan.utils.ExportExcel;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-17 19:10
 **/
@RestController
@RequestMapping("/api/surveyAnswer")
@Slf4j
public class SurveyAnswerController {

    @Autowired
    private ISurveyAnswerService surveyAnswerService;

    @Autowired
    private ISurveyQuestionService surveyQuestionService;

    @Autowired
    private ISurveyService surveyService;

    /**
     * 填写问卷，添加答案
     * @param request
     * @param surveyAnswers
     * @return
     */
    @PostMapping
    @Access
    public Map addAnswer(HttpServletRequest request, @RequestBody List<SurveyAnswer> surveyAnswers) {
        try {
            surveyAnswerService.batchSave(surveyAnswers);
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.addAnswer", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 修改答案？？？
     * @param request
     * @param surveyAnswers
     * @return
     */
    @PutMapping
    @Access
    public Map updateAnswer(HttpServletRequest request, @RequestBody List<SurveyAnswer> surveyAnswers) {
        try {
            surveyAnswerService.batchUpdate(surveyAnswers);
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.updateAnswer", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 批量删除答案，根据id
     * @param request
     * @param ids
     * @return
     */
    @DeleteMapping
    @Access
    public Map deleteAnswer(HttpServletRequest request, @RequestBody List<Integer> ids) {
        try {
            surveyAnswerService.updateDeleteStatus(ids);
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.deleteAnswer", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 数据统计-列表
     * @param request
     * @param surveyId
     * @return
     */
    @GetMapping("/{surveyId}")
    @Access
    public Map getAnswer(HttpServletRequest request, @PathVariable("surveyId") Integer surveyId,@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize) {
        try {
            List<Map> answerResultList =  surveyAnswerService.getAnswersBySurveyId(surveyId);
            List<Map> newList=null;
            Integer total=answerResultList.size();
            newList=answerResultList.subList(pageSize*(pageNum-1), ((pageSize*pageNum)>total?total:(pageSize*pageNum)));
            Map data = new HashMap(4);
            data.put("pageNum",pageNum);
            data.put("pageSize",pageSize);
            data.put("total",total);
            data.put("data",newList);
            return ReturnResult.successResult("data", data, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.getAnswer", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GetMapping("getTitle/{surveyId}")
    @Access
    public Map getTitle( @PathVariable("surveyId") Integer surveyId){
        try {
            List<SurveyQuestion> titleResultList = surveyAnswerService.getSurveyQuestionList(surveyId);
            return ReturnResult.successResult("data", titleResultList, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.getAnswer", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 项目统计
     *
     *
     * @param surveyId
     * @return
     */
    @GetMapping("/statistics/{surveyId}")
    public Map getAnswerStatistics(@PathVariable("surveyId") Integer surveyId) {
        try {
            Map data = surveyAnswerService.getAnswerStatisticsBySurveyId(surveyId);
            return ReturnResult.successResult("data", data, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.getAnswerStatistics", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 数据统计-批量删除
     * @param source
     * @return
     */
    @DeleteMapping("/batchDelete")
    @Access
    public Map deleteSurveyAnswer(@RequestBody List<String> source){
        try {
            surveyAnswerService.updateDeleteStatusBySource(source);
            return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.batchDelete", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 数据统计-单条数据-查看详情or编辑
     * @param source
     * @return
     */
    @GetMapping("/getSurveyAnswer")
    @Access
    public Map getSurveyAnswer(@RequestParam("surveyId") Integer surveyId,@RequestParam("source") String source){
        try {
            List<SurveyQuestion> SurveyQuestions= surveyQuestionService.getBySource(surveyId,source);
            return ReturnResult.successResult("data",SurveyQuestions,ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.getSurveyAnswer", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 数据统计-单条数据-编辑保存
     *
     * @param request
     * @param source
     * @param surveyAnswers
     * @return
     */
    @PostMapping("/editSurveyAnswer")
    @Access
    public Map editSurveyAnswer(HttpServletRequest request, @RequestParam("source") String source, @RequestBody List<SurveyAnswer> surveyAnswers) {
        try {
            surveyAnswerService.editSurveyAnswer(source,surveyAnswers);
            return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.addAnswer", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 文本搜索框，现在没用
     * @param surveyId
     * @param searchText
     * @return
     */
    @GetMapping("/search")
    @Access
    public  Map search( @RequestParam("surveyId")Integer surveyId, @RequestParam("searchText") String searchText){
        try {
            Map data = surveyAnswerService.getAnswerBySearchText(surveyId,searchText);
            return ReturnResult.successResult("data", data, ReturnType.GET_SUCCESS);
        } catch (Exception e) {
            log.error("SurveyAnswerController.getAnswerStatistics", e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


//    private static String EXPORT_XLSX_FILE_SUFFIX = ".xlsx";

    @GetMapping("exportExcel")
    public void export(@RequestParam Integer surveyId,HttpServletResponse response) {
        List<SurveyQuestion> titleResultList = surveyAnswerService.getExcelTitle(surveyId);
        //列表头
        String[] headers = new String[titleResultList.size()+1];
        //数据与列对应
        String[] columns = new String[titleResultList.size()+1];

        for(int i=0;i<titleResultList.size()+1;i++){
            if(i==0){
                headers[0]="提交时间";
                columns[0]="createDate";
            }else {
                headers[i]=titleResultList.get(i-1).getTitle();
                columns[i]=titleResultList.get(i-1).getId().toString();
            }
        }

        ExportExcel<List<Map<String, Object>>> exportExcel = new ExportExcel<>();
        //获取数据
        List<Map<String, Object>> answerResultList =  surveyAnswerService.getExcelContent(surveyId);
        Survey survey = surveyService.getById(surveyId);
        StringBuffer filename = new StringBuffer();
        filename.append(survey.getTitle());
        filename.append(System.currentTimeMillis());
        filename.append(".xlsx");
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(filename.toString().getBytes("UTF-8"), "ISO8859-1"));
            out = response.getOutputStream();
            exportExcel.exportXSExcelByColumn("sheet1", headers, columns, answerResultList, out ,null);
        } catch (IOException e) {

        }
    }


}
