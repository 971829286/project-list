package cn.ourwill.huiyizhan.entity;

import lombok.Data;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/10 0010 17:59
 * @Version1.0
 */
@Data
public class Contract {
    private Integer priceType;//价格type
    private Integer activityNum;//直播场次
    private Integer activityDays;//直播时长
    private Integer activityValid;//有效期
    private Integer activityAmount;//实价
    private Integer activityOrignAmount;//原价
}
