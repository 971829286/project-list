package cn.ourwill.huiyizhan.entity;

import cn.ourwill.huiyizhan.baseEnum.EmailType;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-17 12:00
 **/
@Data
public class EmailBean {
    private static final String              USER_NAME  = "event@ourwill.com.cn";
    private static final String              PASSWORD   = "Will2014";
    private static final String              EMAIL_FROM = "北京韦尔科技";
    private              String              emailSubject;
    private              String              emailTo;
    private              boolean             isAttach;//是否有附件
    private              EmailType           emailType;
    private              HashMap             map;
    private              List<TicketsRecord> ticketsRecords;
    public EmailBean(){
        this.map = new HashMap();
        map.put("systemDomain",Config.systemDomain);//http://sso.inmuu.com/login?service=http://event.inmuu.com/
        map.put("SSODomain",Config.willCenterDomain + "?service="+Config.systemDomain);
    }
    public boolean getIsAttach() {
        return this.isAttach;
    }
}
