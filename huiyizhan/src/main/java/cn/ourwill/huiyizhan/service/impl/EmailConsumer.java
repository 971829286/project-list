package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.baseEnum.EmailType;
import cn.ourwill.huiyizhan.config.RabbitMqConfig;
import cn.ourwill.huiyizhan.entity.EmailBean;
import cn.ourwill.huiyizhan.entity.TicketsRecord;
import cn.ourwill.huiyizhan.utils.EmailUtil;
import cn.ourwill.huiyizhan.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-21 11:35
 **/
@Service
@Slf4j
public class EmailConsumer {
    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    Queue queue;

    @Value("${EmailAccount}")
    private String EmailAccount;

    @Value("${EmailPassword}")
    private String EmailPassword;

    @Value("${EmailFrom}")
    private String EmailFrom;

    @Autowired
    GenerateTicketService generateTicketService;
    //开启多个消费者进行发送->轮询
    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void consumer1(Message message) {
        try{
            parseAndSend(message);
        }catch (Exception e){
            log.info("EmailConsumer.parseAndSend",e);
        }
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void consumer2(Message message) {
        try{
            parseAndSend(message);
        }catch (Exception e){
            log.info("EmailConsumer.parseAndSend",e);
        }
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void consumer3(Message message) {
        try{
            parseAndSend(message);
        }catch (Exception e){
            log.info("EmailConsumer.parseAndSend",e);
        }
    }

/*    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void consumer4(Message message) {
        try{
            parseAndSend(message);
        }catch (Exception e){
            log.info("EmailConsumer.parseAndSend",e);
        }
    }
    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void consumer5(Message message) {
        try{
            parseAndSend(message);
        }catch (Exception e){
            log.info("EmailConsumer.parseAndSend",e);
        }
    }*/
    public void parseAndSend(Message message) throws Exception {
        //解析数据
        String body = new String(message.getBody());
        EmailBean emailBean = JsonUtil.fromJson(body, EmailBean.class);
        HashMap map = emailBean.getMap();
        String emailSubject = emailBean.getEmailSubject();
        String emailTo = emailBean.getEmailTo();
        EmailType emailType = emailBean.getEmailType();
        boolean isAttach = emailBean.getIsAttach();
        EmailUtil.config(EmailUtil.SMTP(false), EmailAccount, EmailPassword);
        String emailContent = "";
        //获取EmailHTML内容
        switch (emailType){
            case TICKET:
                emailContent = generateTicketService.generateEmailContent(map,
                        emailType.getTemplateName(),
                        emailType.getTemplateName().replace(".ftl",""));
                break;
            case INFORM:
                emailContent = generateTicketService.generateEmailContent(map,
                        emailType.getTemplateName(),
                        emailType.getTemplateName().replace(".ftl",""));
                break;
            case BINDING:
                emailContent = generateTicketService.generateEmailContent(map,
                        emailType.getTemplateName(),
                        emailType.getTemplateName().replace(".ftl",""));
                break;
            case AD_EMAIL:
                break;
            case REGISTER:
                break;
            case RESET_PASSWORD:
                break;
        }
        //解析HTML->StringBuffer
        String content = Files.readAllLines(Paths.get(emailContent), StandardCharsets.UTF_8).stream().collect(Collectors.joining("\n"));
        EmailUtil email = EmailUtil.subject(emailSubject)
                .from(EmailFrom)
                .html(content);
        if (isAttach) {
            List<TicketsRecord> ticketsRecords = emailBean.getTicketsRecords();
            //过滤待审核的票
            ticketsRecords.removeIf(e->e.getTicketStatus() == 3);
            String ticketPDF = generateTicketService.getTicketPDF(ticketsRecords, false);
            email.attach(new File(ticketPDF), "ticket.pdf");
        }
        email.to(emailTo);
        email.send();

    }

}
