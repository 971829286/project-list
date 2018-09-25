package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：
 *  投稿活动
 * @author zhaoqing
 * @create 2018-06-20 11:59
 **/
@Data
public class Contribute extends Config{

    //id,user_id,name,sex,mob_phone,email,address,pic_url,check_status
    private Integer id;

    private Integer userId;

    private Integer fUserId;//推广人

    private String name;

    private Integer sex;

    private String mobPhone;

    private String email;

    private String address;

    private String workTitle;

    private String picUrl;

    private Integer checkStatus;

    private String smsCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date subTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date checkTime;

    private String feedback;

    private List<String> urlList;

    private List<String> picList;

//    private String verifyCode;

    public List<String> getUrlList() {
        if(StringUtils.isNotEmpty(picUrl)){
            String[] pics = picUrl.split(";");
            return Arrays.asList(pics).stream().map(pic->{
                if(StringUtils.isNotEmpty(pic)&&pic.indexOf("http")<0)
                    return pic = bucketDomain + pic;
                else
                    return pic;
            }).collect(Collectors.toList());
        }
        return null;
    }
}
