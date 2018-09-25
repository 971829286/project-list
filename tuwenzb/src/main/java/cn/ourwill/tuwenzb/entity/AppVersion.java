package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AppVersion {
    private Integer id;
    private Integer versionCode;
    private String versionName;
    private String introduce;
    private String downloadUrl;
    private Date cTime;
}
