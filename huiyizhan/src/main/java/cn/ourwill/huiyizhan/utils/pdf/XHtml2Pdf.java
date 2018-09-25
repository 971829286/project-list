package cn.ourwill.huiyizhan.utils.pdf;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;

import java.io.*;


/**
 * @Description: xhtml  --> pdf
 * @Author: liupenghao
 * @Date: 2018/4/17 ❤❤ 11:16
 */
public class XHtml2Pdf {

    /**
     * 转化方法
     *
     * @param html html文件输入路径(带文件名称)
     * @param pdf  pdf文件输出路径(带文件名称)
     */
    public static void XHtml2Pdf(String html, String pdf) throws Exception{

        int i = html.lastIndexOf(".html");
        String xhtml = null;
        xhtml = html.substring(0, i) + ".xhtml";
        xhtml = Html2Xhtml.html2Xhtml(html, xhtml);

        if (xhtml != null) {
            ConverterProperties props = new ConverterProperties();
//            defaultFontProvider.addDirectory("src/test/resources/font/noto/");
//            defaultFontProvider.addSystemFonts();
//            defaultFontProvider.addStandardPdfFonts();
//            PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
//            FontProgram fontProgram = FontProgramFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
//            defaultFontProvider.addFont()
//            PdfFont f2 = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H",true);
            FontProvider font = new FontProvider();
            font.addFont("STSongStd-Light", "UniGB-UCS2-H");

//            font.addFont("H:\\fonts\\msyh.ttc",PdfEncodings.PDF_DOC_ENCODING);
//            font.addFont("H:\\fonts\\msyh.ttc",PdfEncodings.IDENTITY_H);
//            font.addFont("H:\\fonts\\msyh.ttc",PdfEncodings.CP1252);
//            font.addFont("H:\\fonts\\msyh.ttc",PdfEncodings.UTF8);

            props.setFontProvider(font);
            HtmlConverter.convertToPdf(new File(html), new File(pdf),props);
//            Document document = new Document();
//            Document document = new Document();
//            PdfWriter writer = PdfWriter.getInstance(document,
//                    new FileOutputStream(pdf));
//            document.open();
//            FontProvider fontProvider = new FontProvider();
//            fontProvider.addFontSubstitute("lowagie", "garamond");
//            fontProvider.setUseUnicode(true);
//            // 使用我们的字体提供器，并将其设置为unicode字体样式
//            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
//            HtmlPipelineContext htmlContext = new HtmlPipelineContext(
//                    cssAppliers);
//            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
//            CSSResolver cssResolver = XMLWorkerHelper.getInstance()
//                    .getDefaultCssResolver(true);
//            Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
//                    new HtmlPipeline(htmlContext, new PdfWriterPipeline(
//                            document, writer)));
//            XMLWorker worker = new XMLWorker(pipeline, true);
//            XMLParser p = new XMLParser(worker);
//            File input = new File(xhtml);
//            p.parse(new InputStreamReader(new FileInputStream(input), "UTF-8"));
//            document.close();
        }

    }


}
