package cn.ourwill.huiyizhan.entity;

import cn.ourwill.huiyizhan.baseEnum.QuType;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;

@Data
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 问卷ID
     */
    private Integer id;
    /**
     * 问题标题
     */
    private String title;
    /**
     * 是否必填(0:必答1:非必答)
     */
    private Integer required;
    /**
     * 所属问卷
     */
    private Integer surveyId;
    /**
     * 题目类型
     */
    private QuType quType;
    /**
     * 排序ID（题号）
     */
    private Integer orderById;

    /**
     * 附加属性，不作映射
     */
    //问题选项
    @Transient
    private List<QuRadio> quRadios;
    @Transient
    private List<QuCheckbox> quCheckboxs;
//    private List<Question> questions=new ArrayList<Question>();
//    private List<QuMultiText> quMultiText=new ArrayList<QuMultiText>();

    //题答卷
    /**
     * 文本题答案
     */
    @Transient
    private AnswerText anAnswer;
    /**
     * 多选题答案
     */
    @Transient
    private List<AnswerCheckbox> answerCheckboxs;
    /**
     * 单选题答案
     */
    @Transient
    private AnswerRadio answerRadio;

//    private List<AnDFillblank> anDFillblanks=new ArrayList<AnDFillblank>();
//    private AnFillblank anFillblank=new AnFillblank();
//    private AnYesno anYesno=new AnYesno();
//
//    private List<AnScore> anScores=new ArrayList<AnScore>();
//    private List<AnChenRadio> anChenRadios=new ArrayList<AnChenRadio>();
//    private List<AnChenCheckbox> anChenCheckboxs=new ArrayList<AnChenCheckbox>();
//    private List<AnChenFbk> anChenFbks=new ArrayList<AnChenFbk>();
//    private List<AnCompChenRadio> anCompChenRadios=new ArrayList<AnCompChenRadio>();
//    private List<AnChenScore> anChenScores=new ArrayList<AnChenScore>();



}
