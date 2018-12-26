package com.souche.bmgateway.core.util;

import com.netfinworks.common.lang.StringUtil;
import com.netfinworks.ues.UesClient;
import com.netfinworks.ues.crypto.model.EncryptType;
import com.netfinworks.ues.ctx.EncryptContext;
import com.netfinworks.ues.ctx.params.EchoSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * UES工具类
 *
 * @since 2018/07/12
 */
public class UesService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UesClient uesClient;

    public String encryptData(String src)  {

        if (StringUtil.isEmpty(src)) {
            return src;
        }

        EncryptData srcData = new EncryptData();
        srcData.setPlaintext(src);

        return encryptData(srcData);
    }

    public String encryptData(EncryptData srcData)  {
        if (srcData == null || StringUtil.isEmpty(srcData.getPlaintext())) {
            return null;
        }

        try {
            //(1)构建原文
            EncryptContext ctx = new EncryptContext(srcData.getPlaintext());
            ctx.asEncryptType(EncryptType.RSA);
            ctx.withDigest();
            if (StringUtil.isNotBlank(srcData.getSummary())) {
                ctx.withSummariable(new EchoSummary(StringUtil.trim(srcData.getSummary())));
            }
            //远程调用计时开始
            long beginTime = System.currentTimeMillis();

            //(2)提交数据
            boolean rest = uesClient.saveData(ctx);
            if (!rest) {
                logger.error("ues invoke error code : {} , message : {}",
                        new Object[] { ctx.getResultCode(), ctx.getResultMessage() });
                throw new RuntimeException("ues 加密失败");
            }

            //远程调用计时结束
            if (logger.isInfoEnabled()) {
                long consumeTime = System.currentTimeMillis() - beginTime;
                //log远程调用耗时和响应
                logger.info("远程调用：{} 耗时: {} (ms)", new Object[] { "uesServiceClient#saveData",
                        consumeTime });
            }
            return ctx.getTicket();
        } catch (Exception e) {
            logger.error("调用ues 加密出错", e);
            throw new RuntimeException(e);
        }
    }


    public List<EncryptContext> encryptDatas(List<String> datas)  {
        if (datas == null || datas.size()==0) {
            return null;
        }

        try {
            //(1)构建原文
            List<EncryptContext> ctxs = new ArrayList<EncryptContext>();
            for(String data : datas){
                EncryptContext ctx = new EncryptContext(data);
                ctx.asEncryptType(EncryptType.RSA);
                ctx.withDigest();
                ctxs.add(ctx);
            }

            //远程调用计时开始
            long beginTime = System.currentTimeMillis();

            //(2)提交数据
            boolean rest = uesClient.saveDatas(ctxs);
            if (!rest) {
                throw new RuntimeException("ues 加密失败");
            }

            //远程调用计时结束
            if (logger.isInfoEnabled()) {
                long consumeTime = System.currentTimeMillis() - beginTime;
                //log远程调用耗时和响应
                logger.info("远程调用：{} 耗时: {} (ms)", new Object[] { "uesServiceClient#saveData",
                        consumeTime });
            }
            return ctxs;
        } catch (Exception e) {
            logger.error("调用ues 加密出错", e);
            throw new RuntimeException(e);
        }
    }

    public String getDataByTicket(String ticket)  {
        if (StringUtil.isEmpty(ticket)) {
            return null;
        }
        EncryptContext context = new EncryptContext().useTicket(ticket.trim());
        //远程调用计时开始
        long beginTime = System.currentTimeMillis();
        try {
            boolean rest = uesClient.getDataByTicket(context);
            if (!rest) {
                logger.error("ues invoke error code : {} , message : {}",
                        new Object[] { context.getResultCode(), context.getResultMessage() });
            }

            if (logger.isInfoEnabled()) {
                //远程调用计时结束
                long consumeTime = System.currentTimeMillis() - beginTime;
                //log远程调用耗时和响应
                logger.info("远程调用：{} 耗时: {} (ms)", new Object[] {
                        "uesServiceClient#getDataByTicket", consumeTime });
            }
            return context.getPlainData();
        } catch (Exception e) {
            logger.error("调用ues 解密出错", e);
            throw new RuntimeException(e);
        }
    }

    public void setUesClient(UesClient uesClient) {
        this.uesClient = uesClient;
    }


    public String decryptData(String src)  {
        if (StringUtil.isEmpty(src)) {
            return src;
        }

        EncryptContext ctx = new EncryptContext().useTicket(src.trim());
        uesClient.getDataByTicket(ctx);

        return ctx.getPlainData();
    }

    public static void main(String[] args) {
        UesService uesUtil = new UesService();
        String dataByTicket = uesUtil.getDataByTicket("P81303-1008");
        System.out.println("===="+dataByTicket);
    }

}
