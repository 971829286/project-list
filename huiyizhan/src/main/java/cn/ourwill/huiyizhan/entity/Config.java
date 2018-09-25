package cn.ourwill.huiyizhan.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/1 0001 17:23
 * 配置类
 * @Version1.0
 */
@Component
public class Config {
    //静态配置项
    public static String bucketDomain;//云储存配置项
    public static String systemDomain;//系统域名配置项
    public static String defaultAvator;//默认头像配置
    public static String willcenterBucketDomain;//用户中心云储存配置项
    public static String willCenterDomain;

    //初始化读取配置信息
    @Value("${upload.bucketDomain}")
    public void setBucketDomain(String bucketDomain) {
        Config.bucketDomain = bucketDomain;
    }
    @Value("${system.domain}")
    public void setSystemDomain(String systemDomain) {
        Config.systemDomain = systemDomain;
    }
    @Value("${comment.defaultAvator}")
    public void setDefaultAvator(String defaultAvator){
        Config.defaultAvator = defaultAvator;
    }
    @Value("${willCenter.upload.bucketDomain}")
    public void setWillcenterBucketDomain(String willcenterBucketDomain){
        Config.willcenterBucketDomain = willcenterBucketDomain;
    }
    @Value("${willCenter.domain}")
    public void setWillCenterDomain(String willCenterDomain){
        Config.willCenterDomain = willCenterDomain;
    }
}
