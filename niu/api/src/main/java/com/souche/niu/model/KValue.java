package com.souche.niu.model;

import com.souche.optimus.dao.annotation.SqlField;
import com.souche.optimus.dao.annotation.SqlPrimaryKey;
import com.souche.optimus.dao.annotation.SqlTable;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author XuJinNiu
 * @since 2018-09-11
 */

@Data
@SqlTable("kv_value")
public class KValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @SqlPrimaryKey
    private Integer id;

    @SqlField(value = "group_id")
    private Integer groupId;

    @SqlField(value = "value")
    private String  banner;

    @SqlField(value = "updatedAt")
    private Date dateUpdate;

    @SqlField(value = "createdAt")
    private Date dateCreate;

    @SqlField(value = "sort")
    private Integer sort;

}
