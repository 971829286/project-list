package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.entity.ActivityTickets;
import cn.ourwill.huiyizhan.entity.SurveyAnswer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：接口文档，帮助类
 *
 * @author liupenghao
 * @create 2018-03-24 10:16
 **/
public class TestOnlyByJunit4 {

    /**
     * 生成参数列表，用于提高接口
     * <pre>
     * 说明文档的编写效率：
     *          pp
     * <pre>
     * 思路： 利用反射，映射字段
     */
    // public static void
    @Test
    public void testMessage() {

        List<ActivityTickets> tickets = new ArrayList<>();
        ActivityTickets activityTickets = new ActivityTickets();
        activityTickets.setActivityId(14);
        activityTickets.setTicketName("小米发布会");
        activityTickets.setIsCheck(1);

        ActivityTickets activityTickets1 = new ActivityTickets();
        activityTickets1.setActivityId(15);
        activityTickets1.setTicketName("华为发布会");
        activityTickets1.setIsCheck(1);

        tickets.add(activityTickets);
        tickets.add(activityTickets1);

        StringBuilder sql = new StringBuilder();
        MessageFormat mf = new MessageFormat("(#{list[{0}].activityId},#{list[{0}].ticketName},#{list[{0}].ticketPrice},#{list[{0}].ticketExplain},#{list[{0}].startTime},#{list[{0}].cutTime},#{list[{0}].isPublishSell},#{list[{0}].isCheck},#{list[{0}].sellStatus},#{list[{0}].singleLimits},#{list[{0}].totalNumber},#{list[{0}].userId},#{list[{0}].cTime},#{list[{0}].uId},#{list[{0}].uTime})");

        // MessageFormat mf = new MessageFormat("#{0},#{0}");
        for (int i = 0; i < tickets.size(); i++) {
            sql.append(mf.format(new Object[]{i}));
            if (i < tickets.size() - 1) {
                sql.append(",");
            }
        }
        System.out.println(sql.toString());
    }

    /******************************************  批量插入帮助类***********************************/
    /**
     * 暴力反射，获取类的私有字段
     */
    @Test
    public void getProperties() {
        System.out.println(getValues(SurveyAnswer.class, true, false));
    }

    /**
     * 多条数据查询  帮助类
     *
     * @param clazz
     * @param containId
     * @return
     */
    public static String getValues(Class clazz, boolean containId, boolean containSerialVersionUID) {
        StringBuilder values = new StringBuilder();
        values.append("\"(");
        Field[] fields = clazz.getDeclaredFields();
        boolean isFirst = true;
        for (Field field : fields) {
            //System.out.println(field.getName());
            String propertyName = field.getName();
            if (propertyName.equals("id")) {
                if (!containId) { //不包含id
                    continue;
                }
            }
            if (propertyName.equals("serialVersionUID")) {
                if (!containSerialVersionUID) { //不包含id
                    continue;
                }
            }
            String value = "#{item." + propertyName + "}";
            if (!isFirst) {
                value = "," + value;
            } else {
                isFirst = false;
            }


            values.append(value);
        }
        values.append(")\"");
        //System.out.println(values.toString());
        return values.toString();
    }

    /******************************************批量更新帮助类***********************************/
    String columnNoId = " survey_id, survey_question_id, answer, source, create_date";
    String column = "id," + columnNoId;

    @Test
    public void getUploadValue() {

        String[] array = column.split(",");
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
    public void test(){
        Integer id = null;

        Map map = (Map) null;
    }
}
