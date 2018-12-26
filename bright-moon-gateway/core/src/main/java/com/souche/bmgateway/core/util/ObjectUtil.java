package com.souche.bmgateway.core.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.TreeMap;

/**
 * 类转换工具
 * Created by xumingming on 15/4/29.
 */
public class ObjectUtil {


    //obj所有变量值转换为string
    public static TreeMap<String,String> convertObjToMap(Object obj){

        TreeMap<String,String> map = new TreeMap<String, String>();

        if(obj==null){
            return map;
        }

        Field[] fields = obj.getClass().getDeclaredFields();

        Field[] parentFields = obj.getClass().getSuperclass().getDeclaredFields();
        if(parentFields!=null){
            fields = ArrayUtils.addAll(fields,parentFields);
        }
        for(int i = 0 , len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(obj);
                if(o==null){
                    continue;
                    //map.put(varName, null);
                }else{
                    map.put(varName, String.valueOf(o));
                }

                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return map;

    }


}
