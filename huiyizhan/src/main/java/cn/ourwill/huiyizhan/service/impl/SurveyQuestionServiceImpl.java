package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.SurveyAnswer;
import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import cn.ourwill.huiyizhan.mapper.SurveyAnswerMapper;
import cn.ourwill.huiyizhan.mapper.SurveyQuestionMapper;
import cn.ourwill.huiyizhan.service.ISurveyQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-17 16:28
 **/
@Service
public class SurveyQuestionServiceImpl extends BaseServiceImpl<SurveyQuestion> implements ISurveyQuestionService {
    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;

    @Autowired
    private SurveyAnswerMapper surveyAnswerMapper;

    @Override
    public Integer updateDeleteStatus(Integer id) {
        return surveyQuestionMapper.updateDeleteStatus(id);
    }

    @Override
    public boolean selectByTitleAndType(String title ,Integer type,Integer survey_id ,String option_content){
        SurveyQuestion surveyQuestion =  surveyQuestionMapper.selectByTitleAndType(title,type,survey_id,option_content);
        if (surveyQuestion == null){
            return true;
        }else {
            surveyQuestionMapper.updateAddStatus(surveyQuestion.getId());
            return false;
        }
    }

    @Override
    public List<SurveyQuestion> getBySource(Integer surveyId ,String source){
        List<SurveyQuestion> reList = surveyQuestionMapper.getBySurveyId(surveyId);
        reList.stream().forEach(entity -> {
            List<SurveyAnswer> list= surveyAnswerMapper.getAnswerByQuestionSource(entity.getId(),source);
            entity.setAnswer(list);
        });
        return  reList;

    }

}