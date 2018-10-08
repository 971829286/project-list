package com.souche.niu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author XuJinNiu
 * @since 2018-09-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City implements Serializable {
    private static final long serialVersionUID = 1L;
    //城市编码
    private String label;
    //城市的值
    private String value;
}
