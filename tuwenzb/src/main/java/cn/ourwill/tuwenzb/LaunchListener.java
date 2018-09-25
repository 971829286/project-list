package cn.ourwill.tuwenzb;

import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.utils.WordFilter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/26 0026 14:08
 * @Version1.0
 */
@Component
public class LaunchListener implements ApplicationListener<ApplicationReadyEvent>{
    @Value("${system.initializeOnlineNum}")
    private boolean isInitialize;

    @Value("${wordfilter.switch}")
    private boolean wordfilter;

    @Autowired
    private IActivityStatisticsService activityStatisticsService;
    private static final Logger log = LogManager.getLogger(LaunchListener.class);
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if(isInitialize) {
            activityStatisticsService.initializeOnlineNum();
        }
        if(wordfilter){
            WordFilter.init();
        }
    }
}
