package cn.ourwill.willcenter.utils;

import cn.ourwill.willcenter.entity.User;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 　ClassName:GlobalUtils
 * Description：
 * User:hasee
 * CreatedDate:2017/7/4 10:15
 */
@Component
public class GlobalUtils {

    /**
     * 保存全局属性值
     */
//    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 保存敏感词汇List
     */
    private static List<String> filterWords = new ArrayList<String>();

    /**
     * 属性文件加载对象
     */
    private static Resource resource = new ClassPathResource("/application.properties");

    //从Session中获取值
    public static   Object getSessionValue(HttpServletRequest request,String keyName){
        //使用redis作为缓存
        //RedisUtils.get(keyName);
        HttpSession session=request.getSession();
       return  session.getAttribute(keyName);
    }
    //向Session中添加值
    public static   void setSessionValue(HttpServletRequest request,String keyName,Object keyValue){
        //使用redis作为缓存
        //RedisUtils.set(keyName,keyvalue);
        HttpSession session=request.getSession();
        session.setAttribute(keyName,keyValue);
    }

    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static String getAddressByIP(String strIP) {
        try {
            URL url = new URL( "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + strIP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while((line = reader.readLine()) != null)
            {
                result.append(line);
            }
            reader.close();
            if(result.indexOf("{")!=-1&&result.indexOf("}")!=-1){

                Object obj= JSONValue.parse(result.toString());
                JSONObject json =(JSONObject)obj;
                String country = (String) json.get("country");
                String province = (String) json.get("province");
                String city = (String) json.get("city");
                if(!country.equals("中国")) {
                    return "来自"+country+"的网友";
                }else if(!city.equals(province)){
                    return "来自"+province+"省"+city+"市的网友";
                }else{
                    return "来自"+city+"市的网友";
                }
            }
            return null;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * 获取配置 -----maven 多环境配置文件弃用
//     */
//    public static String getConfig(String key){
//        try {
//            String value = map.get(key);
//            if (value == null){
//                Properties props = null;
//
//                    props = PropertiesLoaderUtils.loadProperties(resource);
//                value = (String) props.get(key);
//                map.put(key, value != null ? value : StringUtils.EMPTY);
//            }
//            return value;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }

    public static Integer getUserId(HttpServletRequest request){
       return (Integer) getSessionValue(request,"userId");
    }

    public static int daysOfTwo(Date fDate, Date oDate) {
        //适用跨年
        Calendar aCalendar = Calendar.getInstance();
        Calendar bCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        bCalendar.setTime(oDate);
        int days = 0;
        while(aCalendar.before(bCalendar)){
            days++;
            aCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    /**
     * 将密码就行md5加密...
     * 2016-3-11
     * @param s
     * @return
     */
    public final static String getMD5(String s) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
                'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static int getTimestamp(){
        String timestamp = String.valueOf(System.currentTimeMillis()/1000);
        return Integer.valueOf(timestamp);
    }

    /**
     * 生成随机字符串
     * 2016-3-11
     * @param
     * @return
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static boolean checkVerifyCode(HttpServletRequest request,String verifyCode){
        if(verifyCode==null||!verifyCode.trim().toUpperCase().equals(GlobalUtils.getSessionValue(request,"verifyCode"))){
            return false;
        }
        return true;
    }

    public static void setFilterWords(List<String> filterWords) {
        GlobalUtils.filterWords = filterWords;
    }

    public static List<String> getFilterWords() {
        return filterWords;
    }

    //地址解析
    public static String getAddressStr(String jsonStr){
        org.json.JSONObject jsonObject = null;
        try {
            jsonObject = new org.json.JSONObject(jsonStr);
            String province = jsonObject.getString("province");
            String city = jsonObject.getString("city").equals("市辖区")?"":jsonObject.getString("city");
            String district = jsonObject.getString("district");
            String street = jsonObject.getString("street");
            return province+city+district+street;
        } catch (JSONException e) {
            return null;
        }
    }

    public static String generateTicket(User user){
        long times = System.currentTimeMillis();
        String ticket = getMD5(user.getUsername())+times;
        RedisUtils.set("ticket:"+ticket,user,10, TimeUnit.MINUTES);
        return ticket;
    }

    //从session中删除值
    public static void removeSessionValue(HttpServletRequest request,String keyName){
        HttpSession session = request.getSession();
        session.removeAttribute(keyName);
    }
    public static String paramHelperString(String param,String defStr){
        if(StringUtils.isNotEmpty(param))
            return param;
        else
            return defStr;
    }
    public static Integer paramHelperInteger(String param,Integer defInt){
        if(StringUtils.isNotEmpty(param))
            return Integer.parseInt(param);
        else
            return defInt;
    }

}
