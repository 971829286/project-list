package cn.ourwill.tuwenzb;

import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * 描述：接口文档，帮助类
 *
 * @author uusao
 * @create 2018-03-24 10:16
 **/
public class TestOnlyByJunit4 {


    /**
     * 暴力反射，获取类的私有字段
     */
    @Test
    public void getProperties() {
        System.out.println(getValues(ActivityAlbum.class, false));
    }

    /**
     * 多条数据查询  帮助类
     *
     * @param clazz
     * @param containId
     * @return
     */
    public static String getValues(Class clazz, boolean containId) {
        StringBuilder values = new StringBuilder();
        values.append("\"(");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //System.out.println(field.getName());
            String propertyName = field.getName();
            if (propertyName.equals("id")) {
                if (!containId) { //不包含id
                    continue;
                }
            }
            String value = "#{item." + propertyName + "}" + ",";
            if (fields[fields.length - 1].getName().equals(propertyName)) {
                value = "#{item." + propertyName + "}";
            }

            values.append(value);
        }
        values.append(")\"");
        //System.out.println(values.toString());
        return values.toString();
    }


    String columnNoId = "activity_id,user_id,album_name,description,default_flag,c_time";
    String columns = "id,"+columnNoId;
    @Test
    public void getUploadValue() {

        String[] array = columns.split(",");
        StringBuilder resultStr = new StringBuilder();
        resultStr.append("\"");
        for (int i = 0; i < array.length; i++) {

            if (i == array.length - 1) {
                resultStr.append(array[i] + " = VALUES(" + array[i] + ")");
            } else {
                resultStr.append(array[i] + " = VALUES(" + array[i] + "),");
            }
        }
        resultStr.append("\"");
        System.out.println(resultStr.toString());
    }


    /**
     * Integer 默认值  为空
     */
    @Test
    public void test1() {
        // Integer integer = new Integer();

        // System.out.println(integer);
        
        /*String str = "user_id";
        int index = str.indexOf("_");

        String value = (str.charAt(index + 1) + "").toUpperCase();
        System.out.println(str.substring(0,index) + value + str.substring(index + 2,str.length() ));*/


        String[] userInfo = "id,level,nickname,username,avatar,mob_phone,tel_phone,email,qq,company,address,version,info,personalized_signature".split(",");


        /**
         * 获取 需要查询的 用户的列的  case when
         */

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < userInfo.length; i++) {
            str.append("(case \n" +
                    "\t\t\twhen u.id = #{userId} then u2." + userInfo[i] + "\n" +
                    "\t\t\telse u." + userInfo[i] + " end\n" +
                    "\t) as " + userInfo[i] + ","
            );
        }
        System.out.println(str.toString());

    }


    @Test
    public void testBreak() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(j ==2){
                    break;
                }
                 System.out.println(j);
            }
        }
    }
}
