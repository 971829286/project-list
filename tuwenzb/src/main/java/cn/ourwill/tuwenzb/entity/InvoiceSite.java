package cn.ourwill.tuwenzb.entity;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 16:00
 * 发票寄送地址
 * @Version1.0
 */
@Data
public class InvoiceSite {
    private Integer id; //
    private Integer userId; //'用户id',
    private String receiver; //'收件人',
    private String phone; //'手机号',
    private String address; //'地址',
    private Integer isDefault; //'是否默认 0 不默认  1 默认',
    private Date cTime; //'创建时间',
    private Date uTime; //'更新时间',
    private String addressDisplay;

    public String getAddressDisplay() {
        if(StringUtils.isNotEmpty(address)) {
            addressDisplay = GlobalUtils.getAddressStr(address);
        }
        return addressDisplay;
    }
}
