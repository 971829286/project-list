package com.souche.bulbous.vo;

import com.souche.bulbous.utils.URLUtils;
import com.souche.niu.model.BannerCfgDto;
import com.souche.optimus.common.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/

public class BannerCfgVo extends BannerCfgDto {

    List<Map<String,Object>> photo;

    public BannerCfgVo() {
        super();
    }

    public BannerCfgVo(BannerCfgDto bannerCfgDto) {
        super();
        this.setId(bannerCfgDto.getId());
        this.setBannerTitle(bannerCfgDto.getBannerTitle());
        this.setUrl(bannerCfgDto.getUrl());
        this.setProtocol(bannerCfgDto.getProtocol());
        photo = new ArrayList<>();
        if (StringUtil.isNotEmpty(bannerCfgDto.getUrl())) {
            Map<String,Object> photos = new HashMap<>();
            photos.put("uid", "-1");
            photos.put("name", URLUtils.getNameByUrl(bannerCfgDto.getUrl()));
            photos.put("status", "done");
            photos.put("url", bannerCfgDto.getUrl());
            photo.add(photos);
        }
    }

    public List<Map<String, Object>> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Map<String, Object>> photo) {
        this.photo = photo;
    }
}
