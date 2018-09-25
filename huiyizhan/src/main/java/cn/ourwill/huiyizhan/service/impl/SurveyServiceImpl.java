package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.Survey;
import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import cn.ourwill.huiyizhan.mapper.SurveyMapper;
import cn.ourwill.huiyizhan.mapper.SurveyQuestionMapper;
import cn.ourwill.huiyizhan.service.ISurveyService;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-16 11:46
 **/
@Service
public class SurveyServiceImpl extends BaseServiceImpl<Survey> implements ISurveyService {

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;

    @Value("${system.domain}")
    private String systemDomain;

    @Override
    public Integer add(Survey survey) throws QiniuException, ParseException {
        Date date = new Date();
        survey.setCreateDate(date);
        if(survey.getVisitType()==null){
            survey.setVisitType(0);
        }
        if (survey.getVisitType()==1&&survey.getActivityId()==null){
            return 0;
        }
        this.save(survey);

        // 去除毫秒，避免查不出来
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        Integer surveyId = surveyMapper.getIdByCreateDateAndUid(sdf.parse(dateStr), survey.getUid());
        //surveyMapper.getIdByCreateDateAndUid(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-06-04 17:09:11"),1)
        List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
        if (surveyQuestions != null && surveyQuestions.size() > 0) {
            surveyQuestions.stream().forEach(entity -> {
                if(entity.getRequired()==null){
                    entity.setRequired(0);
                }
                entity.setSurveyId(surveyId);
            });
            surveyQuestionMapper.batchSave(surveyQuestions);
        }
        return surveyId;
    }

    @Override
    public Survey getSurveyWithQuestionById(Integer id) {
        return surveyMapper.getSurveyWithQuestionById(id);
    }

    @Override
    public Integer update(Survey survey) throws QiniuException {
        super.update(survey);

        List<Integer> ids = surveyQuestionMapper.getIdBySurveyId(survey.getId());
        List<Integer> pageDataList = new ArrayList<>();
        ArrayList<Integer> deleteIds = new ArrayList<>();

        List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
        if(surveyQuestions.size()>0){
            for (int i = 0; i < surveyQuestions.size(); i++) {
                SurveyQuestion entity = surveyQuestions.get(i);
                entity.setSurveyId(survey.getId());
                if (entity.getId() != null) {
                    pageDataList.add(entity.getId());
                }else{
                    SurveyQuestion surveyQuestion =  surveyQuestionMapper.selectByTitleAndType(entity.getTitle(),entity.getType(),entity.getSurveyId(),entity.getOptionContent());
                    if (surveyQuestion != null){
                        surveyQuestionMapper.updateAddStatus(surveyQuestion.getId());
                        pageDataList.add(entity.getId());
                    }
                }
                entity.setOrderd(i + 1);
            }

            for (Integer id : ids) {
                // 当前id 所对应的数据已被删除
                if (!pageDataList.contains(id)) {
                    deleteIds.add(id);
                }
            }
            if (deleteIds.size() > 0){
                surveyQuestionMapper.batchUpdateDeleteStatus(deleteIds);
            }
            return surveyQuestionMapper.batchUpdate(surveyQuestions);
        }
        return 0;
    }

    @Override
    public Integer updateDeleteStatus(Integer id) {
        return surveyMapper.updateDeleteStatus(id);
    }

    @Override
    public List<Survey> getUID(Integer uId, int type,String searchText) {
        List<Survey> list =  surveyMapper.getByUid(uId, type,searchText);
        list.stream().forEach(entity->{
            entity.setVoteAddress(systemDomain+"manage#/questionnaireVote/"+entity.getId()+"/wirte");
        });
        return list;
    }

    @Override
    public Integer updateStatus(Integer id) {
        Integer status = surveyMapper.getStatus(id);
        return surveyMapper.updateStatus(id, status == 0 ? 1 : 0);
    }

    @Override
    public void copy(Integer surveyId) throws QiniuException, ParseException {
        Survey survey = surveyMapper.getSurveyWithQuestionById(surveyId);
        Date date = new Date();
        survey.setCreateDate(date);
        survey.setId(null);
        survey.setStatus(0);
        survey.setTitle(survey.getTitle()+"-copy");
        this.save(survey);
        // 去除毫秒，避免查询不出来
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        Integer saveSurveyId = surveyMapper.getIdByCreateDateAndUid(sdf.parse(dateStr), survey.getUid());
        List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
        if (surveyQuestions != null && surveyQuestions.size() > 0) {
            surveyQuestions.stream().forEach(entity -> {
                entity.setId(null);
                entity.setSurveyId(saveSurveyId);
            });
            surveyQuestionMapper.batchSave(surveyQuestions);
        }
    }

    @Override
    public Integer checkPermission(Integer userId, Integer surveyId) {
        Integer resultCode = surveyMapper.checkPermission(userId, surveyId);
        if (resultCode <= 0){
            return -1;
        }
        else{
            return resultCode;
        }
    }
}
