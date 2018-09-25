package cn.ourwill.huiyizhan.config;

import io.swagger.models.auth.In;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import static org.reflections.util.ConfigurationBuilder.build;

@Configuration
public class ElasticsearchConfig {
    @Value("${ElasticSearchServerIp}")
    String ElasticSearchServerIp;
    @Bean
    public TransportClient client(){
        TransportClient client = null;
        try {
            InetSocketTransportAddress node = new InetSocketTransportAddress(
                InetAddress.getByName(ElasticSearchServerIp),9300
            );
            Settings settings = Settings.builder()
                    .put("cluster.name","elasticsearch")
                    .build();
            client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(node);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

}
