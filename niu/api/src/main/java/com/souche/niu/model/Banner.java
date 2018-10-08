package com.souche.niu.model;

import com.souche.optimus.dao.annotation.SqlTable;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Banner 实体类
 * @author XuJinNiu
 * @since 2018-09-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("bannerId")
    private Integer bannerId;

    @ApiModelProperty("banner标题")
    private String title;

    @ApiModelProperty("banner图的url")
    private String image;

    @ApiModelProperty("banner的状态")
    private Integer status;

//    @ApiModelProperty("生效时间")
//    private Date dateEffect;

    @ApiModelProperty("banner的排序值,1-1000,数值越大,排序越靠前")
    private Integer orderNum;

    @ApiModelProperty("banner图的跳转协议地址,http&本地协议")
    private String address;

    @ApiModelProperty("创建时间")
    private Date dateCreate;

    @ApiModelProperty("目标城市")
    private List<String> targetCity;

}
