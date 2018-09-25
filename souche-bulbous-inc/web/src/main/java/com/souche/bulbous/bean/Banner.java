package com.souche.bulbous.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xujinniu@souche.com
 * @Description:Banner测试类
 * @date 2018/9/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner {
    private Integer id;
    private String  title;
    private String  images;
    private Date    createDate;
    private Integer status;
    private Integer orderNum;
    private String  operating;
    private String  key;
}
