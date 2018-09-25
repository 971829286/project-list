package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.Survey;
import com.qiniu.common.QiniuException;

import java.text.ParseException;
import java.util.List;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-16 11:38
 **/
public interface ISurveyService extends IBaseService<Survey> {

    Integer add(Survey survey) throws QiniuException, ParseException;


    Survey getSurveyWithQuestionById(Integer id);

    @Override
    Integer update(Survey entity) throws QiniuException;

    Integer updateDeleteStatus(Integer id);

    List<Survey> getUID(Integer uId, int type,String searchText);

    Integer updateStatus(Integer id);

    void copy(Integer surveyId) throws QiniuException, ParseException;

    Integer checkPermission(Integer userId, Integer surveyId);
}
