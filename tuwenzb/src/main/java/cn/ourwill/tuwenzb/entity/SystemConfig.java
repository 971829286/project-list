package cn.ourwill.tuwenzb.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/10 0010 17:28
 * @Version1.0
 */
@Data
public class SystemConfig {
    private Integer id;
    private Integer discountSwitch;
    private Date discountStart;
    private Date discountEnd;
}
