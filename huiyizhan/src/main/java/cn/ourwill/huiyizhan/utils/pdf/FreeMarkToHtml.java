package cn.ourwill.huiyizhan.utils.pdf;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Map;

@Slf4j
public class FreeMarkToHtml {
    private static String resourcePath;

    static {
        try{
            resourcePath = ResourceUtils.getURL("classpath:").getPath() + "emailAndPdf/";
        }catch (Exception e){
            resourcePath = null;
            log.info("FreeMarkToHtml.static{}",e);
        }
    }

    /**
     * 通过路径 和文件名称 获取魔板
     *
     * @param templateFilePath
     * @param templateFileName
     * @return
     */
    public static Template getTemplate(String templateFilePath, String templateFileName) {
        try {
            Configuration cfg = new Configuration();
            // 防止 ${value}  value 没有值对应 ,抛出异常.
            cfg.setClassicCompatible(true);
            TemplateLoader templateLoader = new FileTemplateLoader(new File(
                    templateFilePath));
            cfg.setTemplateLoader(templateLoader);
            return cfg.getTemplate(templateFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将freemark 魔板 转换成 html 文件
     *
     * @param templateFilePath 加载的模板路径
     * @param templateFileName 加载的模板文件名
     * @param replaceData      需要替换的数据
     * @param outFile          生成指定文件 (包含路径)
     * @return 成功，返回文件名；失败，返回null。
     */
    public static String freeMarkToHtml(String templateFilePath, String templateFileName, Map<String, Object> replaceData,
                                        String outFile) {

        String path = null;
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFile), "UTF-8"));
            Template temp = getTemplate(templateFilePath, templateFileName);
            temp.setEncoding("UTF-8");
            temp.process(replaceData, out);
            path = outFile;

        } catch (IOException e) {
            e.printStackTrace();
            path = null;
        } catch (TemplateException e) {
            e.printStackTrace();
            path = null;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static String freeMarkToHtml(String inutFileName, Map<String, Object> replaceData, String outFile) {
        return freeMarkToHtml(resourcePath,inutFileName,replaceData,outFile );
    }
}
