package cn.ourwill.huiyizhan.baseEnum;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-18 09:49
 **/
public enum  EmailType {
    INFORM("通知邮件","informEmail.ftl",1),
    TICKET("收票邮件","email.ftl",2),
    BINDING("绑定账户","binding.ftl",3),
    RESET_PASSWORD("重置密码","resetPassword.ftl",4),
    REGISTER("注册账号","register.ftl",5),
    AD_EMAIL("广告邮件","adEmail.ftl",6);
    private String typeName;
    private String templateName;
    private Integer index;

    EmailType(String typeName,String templateName,Integer index){
        this.templateName = templateName;
        this.typeName = typeName;
        this.index = index;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Integer getIndex() {
        return index;
    }

}
