package cn.ourwill.huiyizhan.entity;

import lombok.Data;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-12 10:43
 **/
@Data
public class ImageTmp {
    private Integer id;
    private String tableName;
    private String fieldName;
    private Integer entityId;
    private String urlOld;
    private String urlNew;
    private String flag;
}
