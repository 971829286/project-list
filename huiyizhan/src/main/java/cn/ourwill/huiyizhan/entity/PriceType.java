package cn.ourwill.huiyizhan.entity;

import lombok.Data;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/22 0022 15:30
 * 价格类型
 * @Version1.0
 */
@Data
public class PriceType {
    private Integer id;
    private Integer type;//价格类型
    private String description;//描述
    private Integer price;//价格
    private Integer number;//包含场次数
    private Integer photoLive;//是否照片直播（0 图文直播  1 照片直播）
}
