package cn.ourwill.tuwenzb.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-29 14:32
 **/
@Component
public class FaceDomain {
    private static String faceDomain;
    private static String  facePlusDomain;


    @Value("${face.domain}")
    public void setFaceDomain(String faceDomain){
        this.faceDomain = faceDomain;
    }
    public String getDetect(){
        return this.faceDomain + "/api/v1/detect";
    }
    public String getCreate(){
        return this.faceDomain + "/api/v1/faceset/create";
    }
    public String geAddface(){
        return this.faceDomain + "/api/v1/faceset/addface";
    }
    public String getSearch(){
        return this.faceDomain + "/api/v1/search";
    }

    @Value("${face.plus.domain}")
    public void setFacePlusDomain(String facePlusDomain){
        this.facePlusDomain = facePlusDomain;
    }
    public String getFacePlusDetect(){
        return this.facePlusDomain +"/detect";
    }
    public String getFacePlusCreate(){
        return this.facePlusDomain + "/faceset/create";
    }
    public String getFacePlusAddFace(){
        return this.facePlusDomain + "/faceset/addface";
    }
    public String getFacePlusSearch(){
        return this.facePlusDomain + "/search";
    }
    public String getFacePlusRemove(){
        return this.facePlusDomain+"/faceset/removeface";
    }
    public String getSetUserId(){return this.facePlusDomain + "/face/setuserid";}

}
