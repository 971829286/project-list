package com.souche.bulbous.utils;

import com.souche.optimus.common.util.StringUtil;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
public class URLUtils {

    /**
     * 根据路径获取/最后的部分
     * @param url
     * @return
     */
    public static String getNameByUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return "";
        }
        if(!url.contains("/")){
            return "";
        }
        int index = url.lastIndexOf('/');
        return url.substring(index+1, url.length());
    }
}
