package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.SurveyQuestion;

import java.util.List;

public interface ISurveyQuestionService extends IBaseService<SurveyQuestion> {
    Integer updateDeleteStatus(Integer id);
    boolean selectByTitleAndType(String title ,Integer type,Integer survey_id,String option_content);
    List<SurveyQuestion> getBySource(Integer surveyId ,String source);
}
