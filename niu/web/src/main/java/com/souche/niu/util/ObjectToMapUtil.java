package com.souche.niu.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sid on 2017/11/23.
 */
public class ObjectToMapUtil {

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new ConcurrentHashMap<>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    public static Map<String, String> objectToMapString(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (null==field.get(obj)){
                continue;
            }
            map.put(field.getName(),String.valueOf(field.get(obj)));
        }
        return map;
    }
}
