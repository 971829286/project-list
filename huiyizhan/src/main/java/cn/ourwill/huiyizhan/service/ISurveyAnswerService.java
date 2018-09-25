package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.SurveyAnswer;
import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import com.qiniu.common.QiniuException;

import java.util.List;
import java.util.Map;

public interface ISurveyAnswerService extends IBaseService<SurveyAnswer> {
    Integer add(SurveyAnswer surveyAnswer) throws QiniuException;

    Integer batchSave(List<SurveyAnswer> surveyAnswers);

    Integer batchUpdate(List<SurveyAnswer> surveyAnswers);

    List<Map>  getAnswersBySurveyId(Integer surveyId);

    Integer updateDeleteStatus(List<Integer> ids);

    Map getAnswerStatisticsBySurveyId(Integer surveyId);

    public Map getAnswerStatisticsBySurveyId2(Integer surveyId);

    Integer updateDeleteStatusBySource(List<String> sourceList);

    void editSurveyAnswer(String source,List<SurveyAnswer> surveyAnswers);

    Map getAnswerBySearchText(Integer surveyId,String searchText);

    List<SurveyQuestion> getSurveyQuestionList(Integer surveyId);

    List<SurveyQuestion> getExcelTitle(Integer surveyId);


    List<Map<String, Object>> getExcelContent(Integer surveyId);
}
