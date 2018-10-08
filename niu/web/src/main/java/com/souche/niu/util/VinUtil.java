package com.souche.niu.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * VinUtil
 *
 * @author ZhangHui
 * @since 2018-09-10
 */
public final class VinUtil {

    public static  void  main(String[] args) {

        System.out.println(containVin("UU6JA69691D713820"));
    }

    /**
     * 验证vin码只能包含英文字母与阿拉伯数字
     *
     * @author ZhangHui
     * @since 2018-09-10
     */
    public static boolean containVin(String vin) {
        Pattern pattern = Pattern.compile("[0-9A-Z]]");
        Matcher matcher = pattern.matcher(vin);
        return matcher.matches();
    }

    /**
     * 车辆识别码VIN校验位计算方法
     *
     * @author ZhangHui
     * @since 2018-09-10
     */
    public static boolean checkVIN(String vin) {
        Map<Integer, Integer> vinMapWeighting = null;
        Map<Character, Integer> vinMapValue = null;
        vinMapWeighting = new HashMap<Integer, Integer>();
        vinMapValue = new HashMap<Character, Integer>();
        vinMapWeighting.put(1, 8);
        vinMapWeighting.put(2, 7);
        vinMapWeighting.put(3, 6);
        vinMapWeighting.put(4, 5);
        vinMapWeighting.put(5, 4);
        vinMapWeighting.put(6, 3);
        vinMapWeighting.put(7, 2);
        vinMapWeighting.put(8, 10);
        vinMapWeighting.put(9, 0);
        vinMapWeighting.put(10, 9);
        vinMapWeighting.put(11, 8);
        vinMapWeighting.put(12, 7);
        vinMapWeighting.put(13, 6);
        vinMapWeighting.put(14, 5);
        vinMapWeighting.put(15, 4);
        vinMapWeighting.put(16, 3);
        vinMapWeighting.put(17, 2);

        vinMapValue.put('0', 0);
        vinMapValue.put('1', 1);
        vinMapValue.put('2', 2);
        vinMapValue.put('3', 3);
        vinMapValue.put('4', 4);
        vinMapValue.put('5', 5);
        vinMapValue.put('6', 6);
        vinMapValue.put('7', 7);
        vinMapValue.put('8', 8);
        vinMapValue.put('9', 9);
        vinMapValue.put('A', 1);
        vinMapValue.put('B', 2);
        vinMapValue.put('C', 3);
        vinMapValue.put('D', 4);
        vinMapValue.put('E', 5);
        vinMapValue.put('F', 6);
        vinMapValue.put('G', 7);
        vinMapValue.put('H', 8);
        vinMapValue.put('J', 1);
        vinMapValue.put('K', 2);
        vinMapValue.put('M', 4);
        vinMapValue.put('L', 3);
        vinMapValue.put('N', 5);
        vinMapValue.put('P', 7);
        vinMapValue.put('R', 9);
        vinMapValue.put('S', 2);
        vinMapValue.put('T', 3);
        vinMapValue.put('U', 4);
        vinMapValue.put('V', 5);
        vinMapValue.put('W', 6);
        vinMapValue.put('X', 7);
        vinMapValue.put('Y', 8);
        vinMapValue.put('Z', 9);
        boolean reultFlag = false;
        String uppervin = vin.toUpperCase();
        //排除字母O、I
        if (vin == null || uppervin.indexOf("O") >= 0|| uppervin.indexOf("I") >= 0) {
            reultFlag = false;
        } else {
            //1:长度为17
            if (vin.length() == 17) {
                char[] vinArr = uppervin.toCharArray();
                int amount = 0;
                for (int i = 0; i < vinArr.length; i++) {
                    //VIN码从从第一位开始，码数字的对应值×该位的加权值，计算全部17位的乘积值相加
                    amount += vinMapValue.get(vinArr[i])*vinMapWeighting.get(i + 1);
                }
                //乘积值相加除以11、若余数为10，即为字母Ｘ
                if (amount % 11 == 10) {
                    if (vinArr[8] == 'X') {
                        reultFlag = true;
                    } else {
                        reultFlag = false;
                    }

                } else {
                    //VIN码从从第一位开始，码数字的对应值×该位的加权值，
                    //计算全部17位的乘积值相加除以11，所得的余数，即为第九位校验值
                    if (amount % 11 != vinMapValue.get(vinArr[8])) {
                        reultFlag = false;
                    } else {
                        reultFlag = true;
                    }
                }
            }
            //1:长度不为17
            if (!vin.equals("") && vin.length() != 17) {
                reultFlag = false;
            }
        }
        return reultFlag;
    }
}
