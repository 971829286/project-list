package cn.ourwill.tuwenzb.utils;

import com.github.pagehelper.util.StringUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by thomasong on 2/29/16.
 */
public class ReturnResult {

    public static Map<String, Object> result(ResultEnum result, Map<String,Object> map) {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("code", result.getCode());
        if(null!=map&&map.size()>0){
            Iterator<Map.Entry<String,Object>> iter = map.entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry<String,Object> e = iter.next();
                String key = e.getKey();
                Object value = e.getValue();
                _map.put(key,value);
            }

        }
        return _map;
    }

    public static Map<String, Object> custonResult(Integer code, Map<String,Object> map) {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("code", code);
        if(null!=map&&map.size()>0){
            Iterator<Map.Entry<String,Object>> iter = map.entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry<String,Object> e = iter.next();
                String key = e.getKey();
                Object value = e.getValue();
                _map.put(key,value);
            }

        }
        return _map;
    }


    public static Map<String, Object> successResult(String key, Object value,String msg) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("msg",msg);
        if(!StringUtil.isEmpty(msg)){
            map.put("msg",msg);
        }
        if(!StringUtil.isEmpty(key)) {
            map.put(key, value);
        }
        return result(ResultEnum.SUCCESS, map);
    }
    public static Map<String, Object> successResult(String key, Object value,ReturnType retype) {
        return successResult(key,value,retype.getName());
    }

    public static Map<String, Object> successResult(ReturnType retype) {
        return successResult(null,null,retype.getName());
    }

    public static Map<String, Object> successResult(String msg) {
        return successResult(null,null,msg);
    }
     /***返回结果**/


    public  static Map<String,Object> errorResult(String msg){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("msg",msg);
        if(!StringUtil.isEmpty(msg)){
            map.put("msg",msg);
        }
        return result(ResultEnum.ERROR, map);
    }

    public  static Map<String,Object> customResult(Integer code,String msg){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("msg",msg);
        if(!StringUtil.isEmpty(msg)){
            map.put("msg",msg);
        }
        return custonResult(code, map);
    }

    public  static Map<String,Object> errorResult(ReturnType retype){
        return errorResult(retype.getName());
    }

    public  static Map<String,Object> errorResult(ResultEnum resultEnum,String msg){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("msg",msg);
        if(!StringUtil.isEmpty(msg)){
            map.put("msg",msg);
        }
        return result(resultEnum, map);
    }
}
