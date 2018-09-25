package cn.ourwill.huiyizhan.entity.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/27 0027 16:21
 * @Version1.0
 */
@Data
@ApiModel(value="ReturnMap", description = "通用返回")
public class Return<T> {
    @ApiModelProperty("code")
    private Integer code;
    @ApiModelProperty("json数据")
    private T data;
    @ApiModelProperty("返回信息")
    private String msg;

}
