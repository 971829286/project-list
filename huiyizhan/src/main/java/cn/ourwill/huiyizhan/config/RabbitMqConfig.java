package cn.ourwill.huiyizhan.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-21 09:55
 **/
//TODO 配置类 启用MQ的时候开启
@Configuration
public class RabbitMqConfig {
    public static final String ROUTE_KEY = "email.routeKey";
    public static final String QUEUE_NAME = "email";
    public static final String TOPIC_EXCHANGE = "email.topic";

    //队列
    @Bean
    public Queue queue(){
        //是否持久化
        boolean durable = false;
        //仅创建者可以使用的私有队列,断开后自动删除
        boolean exclusive = false;
        //当所有消费者客户端连接断开后,是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_NAME,durable,exclusive,autoDelete);
    }
    //交换器
    @Bean
    public TopicExchange exchange(){
        //是否持久化
        boolean durable = false;
        //当所有消费者客户端连接断开,是否自动删除
        boolean autoDelete = false;
        return new TopicExchange(TOPIC_EXCHANGE,durable,autoDelete);
    }

    //将交换器和队列进行绑定
    @Bean
    public Binding binding(){

        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTE_KEY);
    }
}
