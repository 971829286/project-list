package cn.ourwill.huiyizhan.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    /**
     * 计算时间,返回字符串=当前时间+amount
     * 支持传入负数
     * 例如:当前2017-4-1 传入1后 得到2017-4-2
     *
     * @param amount
     * @return
     */
    public static String calDate(int amount) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * <pre>
     *     格式化时间
     * </pre>
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
