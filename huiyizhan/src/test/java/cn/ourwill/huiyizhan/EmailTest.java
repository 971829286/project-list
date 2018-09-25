package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.utils.EmailUtil;
import cn.ourwill.huiyizhan.utils.pdf.FreeMarkToHtml;
import cn.ourwill.huiyizhan.utils.pdf.XHtml2Pdf;
//import com.itextpdf.text.DocumentException;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

import static cn.ourwill.huiyizhan.TemplateToPdf.initData;
import static cn.ourwill.huiyizhan.utils.EmailUtil.SMTP;
import static cn.ourwill.huiyizhan.utils.EmailUtil.SMTP_QQ;

/*
<div>
	<p>亲爱的<b>{{ username }}</b>, 欢迎加入JavaChina!</p>
  	<p>当您收到这封信的时候，您已经可以正常登录了。</p>
  	<p>请点击链接登录首页: <a href='http://www.baidu.com'>百度一下,你还是不知道</a></p>
  	<p>如果您的email程序不支持链接点击，请将上面的地址拷贝至您的浏览器(如IE)的地址栏进入。</p>
  	<p>如果您还想申请管理员权限，可以联系管理员 {{ email }}</p>
  	<p>我们对您产生的不便，深表歉意。</p>
  	<p></p>
  	<p>-----------------------</p>
  	<p></p>
  	<p>(这是一封自动产生的email，请勿回复。)</p>
</div>
*/
/**
 * 邮件测试
 */
public class EmailTest {
    @Before
    public void before() throws GeneralSecurityException {
        // 配置，一次即可
        EmailUtil.config(SMTP(false), "event@ourwill.com.cn", "Will2014");
    }

    @Test
    public void testSendText() throws MessagingException {
        EmailUtil.subject("这是一封测试TEXT邮件")
                .from("蜗牛快跑")
                .to("862990693@qq.com")
                .text("信件内容")
                .send();
    }

    @Test
    public void testSendHtml() throws MessagingException {
        EmailUtil.subject("这是一封测试HTML邮件")
                .from("蜗牛快跑")
                .to("862990693@qq.com")
                .html("<h1 font=red>信件内容</h1>")
                .send();
    }

    @Test
    public void testSendAttach() throws MessagingException {
        EmailUtil.subject("这是一封测试附件邮件")
                .from("蜗牛快跑")
                .to("862990693@qq.com")
                .html("<h1 font=red>信件内容</h1>")
                .attach(new File("H:\\测试图片.jpg"), "测试图片.jpg")
                .send();
    }

    @Test
    public void testPebble() throws IOException, PebbleException, MessagingException {
        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("/webapp/templates/index.html");

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("username", "蜗牛快跑");
        context.put("email", "515891584@qq.com");

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);

        String output = writer.toString();
        System.out.println(output);

        EmailUtil.subject("这是一封测试Pebble模板邮件")
                .from("蜗牛快跑")
                .to("862990693@qq.com")
                .html(output)
                .send();
    }

    @Test
    public void testJetx() throws IOException, PebbleException, MessagingException {
        JetEngine engine = JetEngine.create();
        JetTemplate template = engine.getTemplate("H:\\register.jetx");

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("username", "蜗牛快跑");
        context.put("email", "862990693@qq.com");
        context.put("url", "<a href='www.baidu.com'>百度一下，你还不知道</a>");

        StringWriter writer = new StringWriter();
        template.render(context, writer);
        String output = writer.toString();
        System.out.println(output);

        EmailUtil.subject("这是一封测试Jetx模板邮件")
                .from("蜗牛快跑")
                .to("862990693@qq.com")
                .html(output)
                .send();
    }

    //@Before
    public void testBefore(){
        EmailUtil.config(SMTP_QQ(false), "515891584@qq.com", "vnhkyepmsxiubiec");
    }

    @Test
    public void testTemplateToPdf(){
        URL url = Thread.currentThread().getContextClassLoader().getResource("");

        Map replaceData = initData();
        FreeMarkToHtml.freeMarkToHtml(url.getPath(), "template.ftl", replaceData, url.getPath() + "result.html");
        try {
           // XHtml2Pdf.XHtml2Pdf(url.getPath() + "result.html", url.getPath() + "result.pdf");
            BufferedReader reader = new BufferedReader(new FileReader(new File("H:\\result.html")));
            String line;
            String html = "";
            while((line = reader.readLine()) != null){
                html += line;
            }

            EmailUtil.subject("测试HTML")
                    .from("蜗牛快跑")
                    .to("862990693@qq.com")
                    .html(html)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testString() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("H:\\result.html")));
        String html = "";
        while((html = reader.readLine()) != null){
          System.out.println(html);
        }
    }
}
