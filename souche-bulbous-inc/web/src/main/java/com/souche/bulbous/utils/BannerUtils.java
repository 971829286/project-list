package com.souche.bulbous.utils;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author XuJinNiu
 * @since 2018-10-03
 */
public class BannerUtils {
    public static List<String> targetCityToDb(JSONArray targetCity){
        if(targetCity != null){
            List<String> resList = new ArrayList<>();
            for(int i = 0; i < targetCity.size(); i ++){
                String item = targetCity.getString(i);
                Pattern pattern = Pattern.compile("[^0-9]");
                Matcher matcher = pattern.matcher(item);
                String cityCode = matcher.replaceAll("");
                resList.add(cityCode.trim());
            }
            return resList;
        }
        return null;
    }
}
