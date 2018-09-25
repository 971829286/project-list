package cn.ourwill.willcenter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.junit.Test;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/10/18 0018 14:10
 * @Version1.0
 */
@Component
public class UnitTest {

    @Test
    public void test1(){
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }
    public static void main(String[] args){
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        String phone = "18023456789";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if(isMatch){
            System.out.println("您的手机号" + phone + "是正确格式@——@");
        } else {
            System.out.println("您的手机号" + phone + "是错误格式！！！");
        }
    }
}
