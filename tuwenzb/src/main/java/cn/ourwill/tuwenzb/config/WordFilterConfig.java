package cn.ourwill.tuwenzb.config;

import cn.ourwill.tuwenzb.utils.WordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/1 0001 16:19
 * @Version1.0
 */
@Configuration
public class WordFilterConfig {
    @Autowired
    private Environment env;

    @Bean
    public int readConf() {
        WordFilter.setWordPath(env.getProperty("wordfilter.url"));
        return 1;
    }
}
