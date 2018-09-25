package cn.ourwill.willcenter.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/7 0007 15:32
 * @Version1.0
 */
public class ImgUtil {
    //获取水印接口url
    public static String getWaterMark(String markImgUrl){
        String waterMarkUrl = "watermark/1/image/";
        String encode = getBase64(markImgUrl);
        waterMarkUrl=waterMarkUrl+encode+"/ws/0.3/imageslim";
        return waterMarkUrl;
    }

    public static String getBase64(String str){
       String restr = Base64.encodeBase64String(str.getBytes());
       return restr;
    }
}
