package com.souche.niu.model.banner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XuJinNiu
 * @since 2018-09-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {
    private String key;
    private String value;
    private String desc;
    private String type;
}
