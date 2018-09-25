package com.souche.niu.util;

import com.souche.niu.model.Banner;
import com.souche.niu.model.banner.BannerDTO;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XuJinNiu
 * @since 2018-09-11
 */
public class BannerUtil {
    public static String convertBannerToValue(Banner banner){
//        String res = JsonUtils.toJson(banner);
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("[").append(res).append("]");
//        return stringBuilder.toString();
//        StringBuilder sb = new StringBuilder();
        //城市列表先隐藏
//        String targetCity = JsonUtils.toJson(banner.getTargetCity());
        List<BannerDTO> list = new ArrayList<>();
        list.add(new BannerDTO("title",banner.getTitle(),"标题","0"));
        list.add(new BannerDTO("image",banner.getImage(),"图片","0"));
        list.add(new BannerDTO("status",banner.getStatus()+"","状态","0"));
        list.add(new BannerDTO("address",banner.getAddress(),"跳转协议地址","0"));
        list.add(new BannerDTO("orderNum",banner.getOrderNum()+"","排序值","0"));
//        list.add(new BannerDTO("targetCity",targetCity,"目标城市列表","0"));
        String resJson = JsonUtils.toJson(list);
        return resJson;

    }

    public static Banner convertValueToBanner(String value) {

        if (StringUtil.isNotEmpty(value)) {
            Banner banner = new Banner();
            int flag = 0;
            List list = JsonUtils.fromJson(value, List.class);
            for(int i = 0; i < list.size(); i ++){
                BannerDTO bannerDTO = JsonUtils.fromJson(list.get(i).toString(),BannerDTO.class);
                if (bannerDTO.getKey().equals("title")) {
                    banner.setTitle(bannerDTO.getValue());
                    flag ++;
                }else if (bannerDTO.getKey().equals("image")) {
                    banner.setImage(bannerDTO.getValue());
                    flag ++;
                }else if (bannerDTO.getKey().equals("address")) {
                    banner.setAddress(bannerDTO.getValue());
                    flag ++;
                }else if (bannerDTO.getKey().equals("orderNum")) {
                    banner.setOrderNum(new Integer(bannerDTO.getValue()));
                    flag ++;
//                    System.out.println("orderNum:"+bannerDTO.getValue());
                }else if (bannerDTO.getKey().equals("status")) {
                    banner.setStatus(new Integer(bannerDTO.getValue()));
                    flag ++;
//                    System.out.println("status:"+bannerDTO.getValue());
                }
                //暂时隐藏
//                if (bannerDTO.getKey().equals("targetCity")) {
//                    List<String> resList = JsonUtils.fromJson(bannerDTO.getValue(), List.class);
//                    banner.setTargetCity(resList);
//                }
            }
            if(flag > 0){
                return banner;
            }
        }
        return null;
    }

}
