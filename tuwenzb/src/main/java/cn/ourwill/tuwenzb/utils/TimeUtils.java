package cn.ourwill.tuwenzb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：
 * 时间帮助类
 *
 * @author liupenghao
 * @create 2018-06-14 16:58
 **/
public class TimeUtils {
    /**
     * 返回个时间名的文件名称
     *
     * @return
     */
    public static String getFileNameByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(new Date());
    }
}
