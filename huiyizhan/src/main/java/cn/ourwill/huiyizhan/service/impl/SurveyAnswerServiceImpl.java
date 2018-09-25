package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.baseEnum.QuType;
import cn.ourwill.huiyizhan.entity.AnswerStatistics;
import cn.ourwill.huiyizhan.entity.SurveyAnswer;
import cn.ourwill.huiyizhan.entity.SurveyQuestion;
import cn.ourwill.huiyizhan.mapper.SurveyAnswerMapper;
import cn.ourwill.huiyizhan.mapper.SurveyQuestionMapper;
import cn.ourwill.huiyizhan.service.ISurveyAnswerService;
import cn.ourwill.huiyizhan.utils.JsonUtil;
import cn.ourwill.huiyizhan.utils.TimeUtils;
import com.qiniu.common.QiniuException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 描述：
 *
 * @author liupenghao
 * @create 2018-05-17 19:04
 **/
@Service
public class SurveyAnswerServiceImpl extends BaseServiceImpl<SurveyAnswer> implements ISurveyAnswerService {

    @Autowired
    private SurveyAnswerMapper surveyAnswerMapper;

    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;

    @Override
    public Integer add(SurveyAnswer surveyAnswer) throws QiniuException {
        surveyAnswer.setCreateDate(new Date());
        return this.save(surveyAnswer);
    }

    @Override
    public Integer batchSave(List<SurveyAnswer> surveyAnswers) {
        //批量保存的答案
        List<SurveyAnswer> saveSurveyAnswers = new ArrayList<SurveyAnswer>();
        //确保问卷答案的唯一性
        String uuid = UUID.randomUUID().toString();
        Date now = new Date();
        surveyAnswers.stream().forEach(entity -> {
            //多选题答案
            if (entity.getSurveyQuestionType()==2){
                List<SurveyAnswer> list = entity.getAnswerList();
                for (int i = 0;i<list.size();i++){
                    SurveyAnswer addAnswer = new SurveyAnswer();
                    addAnswer.setAnswer(list.get(i).getAnswer());
                    addAnswer.setType(list.get(i).getType());
                    addAnswer.setSurveyQuestionId(entity.getSurveyQuestionId());
                    addAnswer.setCreateDate(now);
                    addAnswer.setSource(uuid);
                    saveSurveyAnswers.add(addAnswer);
                }
            }else {
                entity.setCreateDate(now);
                entity.setSource(uuid);
                entity.setAnswer(entity.getAnswerList().get(0).getAnswer());
                entity.setType(entity.getAnswerList().get(0).getType());
                saveSurveyAnswers.add(entity);
            }
        });
        return surveyAnswerMapper.batchSave(saveSurveyAnswers);
    }

    @Override
    public Integer batchUpdate(List<SurveyAnswer> surveyAnswers) {
        return surveyAnswerMapper.batchUpdate(surveyAnswers);
    }


    /**
     *
     */
    @Override
    public List<Map> getAnswersBySurveyId(Integer surveyId) {
        // 从数据库查询的数据
        List<SurveyAnswer> surveyAnswers = surveyAnswerMapper.getAnswerBySurveyId(surveyId);

        // 封装数据
        LinkedHashMap<Object, HashMap> answerResultMap = new LinkedHashMap<>();
        List<Map> answerResultList = new ArrayList<Map>();
        surveyAnswers.stream().forEach(entity -> {
            HashMap<Object, Object> tempMap = null;
            tempMap = answerResultMap.get(entity.getSource());
            if (tempMap == null) {
                tempMap = new HashMap<>();
                answerResultMap.put(entity.getSource(), tempMap);
            }
            tempMap.put("createDate", TimeUtils.formatDate(entity.getCreateDate()));
            tempMap.put("source", entity.getSource());
            //封装答案为
            Map<Integer, String> innerTempMap = null;
            innerTempMap = (Map) tempMap.get("answerDetail");
            if (innerTempMap == null) {
                innerTempMap = new LinkedHashMap<Integer, String>();
                tempMap.put("answerDetail", innerTempMap);
            }
            innerTempMap.put(entity.getSurveyQuestionId(), entity.getAnswer());
        });

        Iterator<Map.Entry<Object, HashMap>> iterator = answerResultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            answerResultList.add(iterator.next().getValue());
        }
        return answerResultList;
    }

    @Override
    public List<Map<String, Object>> getExcelContent(Integer surveyId) {
        // 从数据库查询的数据
        List<SurveyAnswer> surveyAnswers = surveyAnswerMapper.getAnswerBySurveyId(surveyId);
        // 封装数据
        LinkedHashMap<Object, HashMap> answerResultMap = new LinkedHashMap<>();
        List<Map<String, Object>> answerResultList = new ArrayList<Map<String, Object>>();
        surveyAnswers.stream().forEach(entity -> {
            HashMap<String, Object> tempMap = null;
            tempMap = answerResultMap.get(entity.getSource());
            if (tempMap == null) {
                tempMap = new HashMap<>();
                answerResultMap.put(entity.getSource(), tempMap);
            }
            tempMap.put("source", entity.getSource());
            //封装答案为
            Map<String, Object> innerTempMap = null;
            innerTempMap = (Map) tempMap.get("answerDetail");
            if (innerTempMap == null) {
                innerTempMap = new LinkedHashMap<String, Object>();
                tempMap.put("answerDetail", innerTempMap);
            }
            innerTempMap.put("createDate", TimeUtils.formatDate(entity.getCreateDate()));
            innerTempMap.put(entity.getSurveyQuestionId().toString(), entity.getAnswer());
        });
        Iterator<Map.Entry<Object, HashMap>> iterator = answerResultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            answerResultList.add((Map<String, Object>)iterator.next().getValue().get("answerDetail"));
        }
        return answerResultList;
    }

    @Override
    public List<SurveyQuestion> getSurveyQuestionList(Integer surveyId){
        List<SurveyQuestion> titleResultList = surveyQuestionMapper.getBySurveyIdNoOther(surveyId);
        return titleResultList;
    }

    @Override
    public List<SurveyQuestion> getExcelTitle(Integer surveyId){
        List<SurveyQuestion> titleResultList = surveyQuestionMapper.getBySurveyIdNoOther(surveyId);
        return titleResultList;
    }

    @Override
    public Integer updateDeleteStatus(List<Integer> ids) {
        return surveyAnswerMapper.updateDeleteStatus(ids);
    }

    /**
     *项目统计废弃
     * @param surveyId
     * @return
     */
    @Override
    public Map getAnswerStatisticsBySurveyId2(Integer surveyId) {
        List<AnswerStatistics> answerStatistics = surveyAnswerMapper.getAnswerStatisticsBySurveyId(surveyId);

        List<HashMap<String,Integer>> answerTotal = surveyAnswerMapper.getAnswerTotalBySurveyId(surveyId);
        Map<String, Map> data = new LinkedHashMap<String, Map>();

        final String splitStr = "::::::";

        answerStatistics.stream().forEach(entity -> {
            Map<String, Object> tempMap = data.get(entity.getId()+splitStr+entity.getType() + splitStr + entity.getTitle());
            if (tempMap == null) {
                tempMap = new HashMap<String, Object>();
                data.put(entity.getId()+splitStr+entity.getType() + splitStr + entity.getTitle(), tempMap);
            }
            tempMap.put(entity.getAnswer(), entity.getCount());
        });

        //转换数据
        List<Map> resultListData = new ArrayList<>();
        Iterator<Map.Entry<String, Map>> map = data.entrySet().iterator();
        while (map.hasNext()) {
            Map.Entry<String, Map> temp = map.next();
            String key = temp.getKey();
            String[] keyArray = key.split(splitStr);
            Map value = temp.getValue();

            HashMap<String, Object> element = new HashMap<>();

            element.put("id", keyArray[0]);
            element.put("type", QuType.getNameByIndex(Integer.parseInt(keyArray[1])));
            element.put("title", keyArray[2]);
            element.put("data", value);
            resultListData.add(element);
        }

        for (int i =0;i<resultListData.size();i++){
            Integer id = answerTotal.get(i).get("id");
            for (int j=0;j<answerTotal.size();j++){
                if (id.equals(answerTotal.get(j).get("id"))){
                    resultListData.get(i).put("total",answerTotal.get(j).get("count"));
                    break;
                }
            }
        }

        Map resultMap = new HashMap<String, Object>();
        resultMap.put("id", surveyId);
        resultMap.put("answerStatistics", resultListData);
        return resultMap;
    }

    /**
     *项目统计
     * @param surveyId
     * @return
     */
    @Override
    public Map getAnswerStatisticsBySurveyId(Integer surveyId) {
        Map resultMap = new HashMap<String, Object>();
        List<SurveyQuestion> surveyQuestions = surveyQuestionMapper.getBySurveyIdStatistics(surveyId);
        surveyQuestions.stream().forEach(entity->{
            Map data = new HashMap<String, Object>();
            List<HashMap<String,Object>> options = JsonUtil.fromJsonToList(entity.getOptionContent());
            List<HashMap<String,Object>> answers = surveyAnswerMapper.getAnswerNumBySurveyQuestionId(entity.getId());
            for (int i=0;i<options.size();i++){
                if(StringUtils.isNotEmpty(options.get(i).get("type").toString())&&Integer.parseInt(options.get(i).get("type").toString())==1){
                    Integer num = surveyAnswerMapper.selectOtherAnswerNumByQuestionId(entity.getId());
                    data.put("其他",num);
                }else {
                    data.put(options.get(i).get("value"),0);
                }
            }
            for (int j = 0 ;j<answers.size();j++){
                data.put(answers.get(j).get("answer").toString(),answers.get(j).get("count"));
            }
            entity.setTypeText(QuType.getNameByIndex(entity.getType()));
            entity.setData(data);
            entity.setTotal(surveyAnswerMapper.selectAnswerNumByQuestionId(entity.getId()));
        });
        resultMap.put("id", surveyId);
        resultMap.put("answerStatistics", surveyQuestions);
        return resultMap;
    }

    @Override
    public Integer updateDeleteStatusBySource(List<String> sourceList) {
        return surveyAnswerMapper.updateDeleteStatusBySource(sourceList);
    }

    @Override
    public void editSurveyAnswer(String source,List<SurveyAnswer> surveyAnswers){
        surveyAnswers.stream().forEach(entity -> {
            if (entity.getSurveyQuestionType()==2){
                List<SurveyAnswer> list = entity.getAnswerList();
                List<SurveyAnswer> oldList = surveyAnswerMapper.getAnswerByQuestionSourceAll(entity.getSurveyQuestionId(),source);
                for (int i = 0;i<list.size();i++){
                    if (!oldList.contains(list.get(i))){
                        SurveyAnswer addAnswer = new SurveyAnswer();
                        addAnswer.setAnswer(list.get(i).getAnswer());
                        addAnswer.setType(list.get(i).getType());
                        addAnswer.setSurveyQuestionId(entity.getSurveyQuestionId());
                        addAnswer.setSource(source);
                        addAnswer.setCreateDate(new Date());
                        surveyAnswerMapper.insertSelective(addAnswer);
                    }else {
                        surveyAnswerMapper.addAnswerBySource(entity.getSurveyQuestionId(),source,list.get(i).getAnswer());
                    }
                }
                for(int i = 0;i<oldList.size();i++){
                    if (!list.contains(oldList.get(i))){
                        surveyAnswerMapper.deleteAnswerBySource(entity.getSurveyQuestionId(),source,oldList.get(i).getAnswer());
                    }
                }
            }else {
                surveyAnswerMapper.editSurveyAnswer(source,entity.getAnswerList().get(0).getAnswer(),entity.getSurveyQuestionId());
            }
        });
    }

    @Override
    public Map getAnswerBySearchText(Integer surveyId,String searchText) {
        // 从数据库查询的数据
        List<SurveyAnswer> surveyAnswers = surveyAnswerMapper.getAnswerBySearchText(surveyId,searchText);

        // 封装数据
        HashMap<String, Object> resultData = new HashMap<>();
        LinkedHashMap<Object, HashMap> answerResultMap = new LinkedHashMap<>();
        List<SurveyQuestion> titleResultList = surveyQuestionMapper.getBySurveyIdNoOther(surveyId);
        List<Map> answerResultList = new ArrayList<Map>();
        surveyAnswers.stream().forEach(entity -> {
            HashMap<Object, Object> tempMap = null;
            tempMap = answerResultMap.get(entity.getSource());
            if (tempMap == null) {
                tempMap = new HashMap<>();
                answerResultMap.put(entity.getSource(), tempMap);
            }
            tempMap.put("createDate", TimeUtils.formatDate(entity.getCreateDate()));
            tempMap.put("source", entity.getSource());
            //封装答案为
            Map<Integer, String> innerTempMap = null;
            innerTempMap = (Map) tempMap.get("answerDetail");
            if (innerTempMap == null) {
                innerTempMap = new LinkedHashMap<Integer, String>();
                tempMap.put("answerDetail", innerTempMap);
            }
            innerTempMap.put(entity.getOrderd(), entity.getAnswer());
        });

        Iterator<Map.Entry<Object, HashMap>> iterator = answerResultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            answerResultList.add(iterator.next().getValue());
        }
        resultData.put("title", titleResultList);
        resultData.put("answer", answerResultList);
        return resultData;
    }

}
