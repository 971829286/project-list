package cn.ourwill.huiyizhan.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CamelCaseUtil {
    private static   ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
    }
    public static Map toCamlCase(Map map){

//        Iterator keyIterator = map.keySet().iterator();
//        Map resMap = new HashMap();
//        while(keyIterator.hasNext()){
//            String next = keyIterator.next();
//            String value = (String) map.get(next);
//            map.put(toCamlCase(next.toString(),true),value);
//        }
//        return resMap;

        Map resMap = new HashMap();
        for(Object key : map.keySet()){
            resMap.put(toCamlCase(key.toString(),true),map.get(key));
        }
        return resMap;
    }

    /**
     * 下划线转驼峰法
     * @param line
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return
     */
    public static String toCamlCase(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String toLine(String str){
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    public static String[] toLine(String [] strs){
        String[] res = new String[strs.length];
        for(int i = 0; i <strs.length; i ++){
            res[i] = toLine(strs[i]);
        }
        return res;
    }
    /*
    测试
    public static void main(String[] args) {
        String str = "hello_ni_ma_shi_shab";
        System.out.println(toCamlCase(str,true));
        Map map = new HashMap();
        map.put("user_name","xujinniu");
        map.put("nick_name","nickname");
        map.put("id","123");
        Map res = toCamlCase(map);
        Iterator iterator = res.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            String value = (String) map.get(key);
            System.out.println("key:"+key+"  value:"+value);
        }

    }
    */
//    public static void main(String[] args) {
//        String str = "niHao,userName,nickName";
//        String[] strs = {"niHao","userName","nickName"};
//
//        System.out.println(Arrays.toString(toLine(strs)));
//        System.out.println(toLine(str));
//    }

}
