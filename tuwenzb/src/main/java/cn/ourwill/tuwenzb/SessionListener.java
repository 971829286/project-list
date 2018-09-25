package cn.ourwill.tuwenzb;

import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/25 0025 16:56
 * @Version1.0
 */
@WebListener
public class SessionListener implements HttpSessionListener {
//    @Autowired
//    private IActivityStatisticsService activityStatisticsService;
    private static final Logger log = LogManager.getLogger(SessionListener.class);
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("session create");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        Integer activityId = (Integer) session.getAttribute("lastActivityId");
        if(activityId!=null) {
            RedisUtils.minusOnlineNumberRedis(activityId);
            //tomcat中此service失效
//            activityStatisticsService.minusOnlineNumberRedis(activityId);
        }
        log.info("session delete");
    }
}
