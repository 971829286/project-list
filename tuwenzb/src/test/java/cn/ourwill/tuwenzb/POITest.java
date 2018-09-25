package cn.ourwill.tuwenzb;

import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.WebpIO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.im4java.core.ConvertCmd;
import org.im4java.core.GMOperation;
import org.im4java.core.IMOperation;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/24 0024 15:36
 * @Version1.0
 */
@Component
public class POITest {

    @Test
    public void generWordTest() throws IOException {
        //Blank Document
        XWPFDocument document= new XWPFDocument();

        //Write the Document in file system
        String path = "C:\\xuanlinDoc\\twzbDoc\\wordTest";
        if (!new File(path).isDirectory())
        {
            new File(path).mkdirs();
        }
        FileOutputStream out = new FileOutputStream(new File(path+File.separator+"create_table.docx"));

        //添加标题
        XWPFParagraph titleParagraph = document.createParagraph();
        //设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraphRun.setText("“幸福四季汇”之环古城河徒步公益行");
        titleParagraphRun.setColor("000000");
        titleParagraphRun.setFontSize(20);


        //段落
        XWPFParagraph firstParagraph = document.createParagraph();
        XWPFRun run = firstParagraph.createRun();
        run.setText("为进一步帮助园区职工了解姑苏文化与历史，同时给单身职工更多的联谊机会，园区总工会结合“金秋助学”公益活动，将于8月26日举办“幸福四季汇”之环古城河徒步公益行活动。");
//        run.setColor("696969");
        run.setFontSize(16);

        //设置段落背景颜色
        CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
        cTShd.setVal(STShd.CLEAR);
        cTShd.setFill("97FFFF");


        //换行
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun paragraphRun1 = paragraph1.createRun();
        paragraphRun1.setText("\r");


        //基本信息表格
        XWPFTable infoTable = document.createTable();
        //去表格边框
        infoTable.getCTTbl().getTblPr().unsetTblBorders();


        //列宽自动分割
        CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
        infoTableWidth.setType(STTblWidth.DXA);
        infoTableWidth.setW(BigInteger.valueOf(9072));


        //表格第一行
        XWPFTableRow infoTableRowOne = infoTable.getRow(0);
        infoTableRowOne.getCell(0).setText("主办方");
        infoTableRowOne.addNewTableCell().setText(": 苏州工业园区总工会");

        //表格第二行
        XWPFTableRow infoTableRowTwo = infoTable.createRow();
        infoTableRowTwo.getCell(0).setText("时间");
        infoTableRowTwo.getCell(1).setText(": 2017-08-26");

        //表格第三行
        XWPFTableRow infoTableRowThree = infoTable.createRow();
        infoTableRowThree.getCell(0).setText("地点");
        infoTableRowThree.getCell(1).setText(": 相门古城墙");

        //表格第四行
        XWPFTableRow infoTableRowFour = infoTable.createRow();
        infoTableRowFour.getCell(0).setText("发布人");
        infoTableRowFour.getCell(1).setText(": 钱裕超");



        //两个表格之间加个换行
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText("\r");



//        //工作经历表格
//        XWPFTable ComTable = document.createTable();
//
//
//        //列宽自动分割
//        CTTblWidth comTableWidth = ComTable.getCTTbl().addNewTblPr().addNewTblW();
//        comTableWidth.setType(STTblWidth.DXA);
//        comTableWidth.setW(BigInteger.valueOf(9072));
//
//        //表格第一行
//        XWPFTableRow comTableRowOne = ComTable.getRow(0);
//        comTableRowOne.getCell(0).setText("开始时间");
//        comTableRowOne.addNewTableCell().setText("结束时间");
//        comTableRowOne.addNewTableCell().setText("公司名称");
//        comTableRowOne.addNewTableCell().setText("title");
//
//        //表格第二行
//        XWPFTableRow comTableRowTwo = ComTable.createRow();
//        comTableRowTwo.getCell(0).setText("2016-09-06");
//        comTableRowTwo.getCell(1).setText("至今");
//        comTableRowTwo.getCell(2).setText("seawater");
//        comTableRowTwo.getCell(3).setText("Java开发工程师");
//
//        //表格第三行
//        XWPFTableRow comTableRowThree = ComTable.createRow();
//        comTableRowThree.getCell(0).setText("2016-09-06");
//        comTableRowThree.getCell(1).setText("至今");
//        comTableRowThree.getCell(2).setText("seawater");
//        comTableRowThree.getCell(3).setText("Java开发工程师");


        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

        //添加页眉
        CTP ctpHeader = CTP.Factory.newInstance();
        CTR ctrHeader = ctpHeader.addNewR();
        CTText ctHeader = ctrHeader.addNewT();
        String headerText = "图文直播-专属您的图文直播平台";
        ctHeader.setStringValue(headerText);
        XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
        //设置为右对齐
        headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
        parsHeader[0] = headerParagraph;
        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);


        //添加页脚
        CTP ctpFooter = CTP.Factory.newInstance();
        CTR ctrFooter = ctpFooter.addNewR();
        CTText ctFooter = ctrFooter.addNewT();
        String footerText = "http://www.tuwenzhibo.com";
        ctFooter.setStringValue(footerText);
        XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFParagraph[] parsFooter = new XWPFParagraph[1];
        parsFooter[0] = footerParagraph;
        policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);


        document.write(out);
        out.close();
        System.out.println("create_table document written success.");
    }
    public static void main(String[] args) throws Exception{
/*        String graphicsMagickHome = "H:\\xjn\\program\\GraphicsMagick-1.3.29-Q16";
        String targetPath = "H:\\45.jpg";
        String sourcePath = "http://s.tupianzhibo.cn/test/2531/280/20180530/26_1527647821118.jpg";
        BufferedImage read = ImageIO.read(new File("C:\\Users\\wl\\Desktop\\photo\\4096.jpg"));

        Object size = read.getProperty("size");
        System.out.println(size+"");
        */


/*        System.out.println(list.size());
        String str = "";

        System.out.println(hang);
        int temp = 1;
        for(int i =0; i < list.size(); i ++){
            if(i % 5 == 0 && StringUtils.isNotEmpty(str)){
                System.out.println(str.substring(1) + "        temp:"+temp);
                temp ++;
                str = "";
            }
            str += ","+list.get(i);
            if(temp == hang && i == list.size() -1 ){
                System.out.println(str.substring(1) + "        temp:"+temp);
            }
        }*/
/*        int width = read.getWidth();
        int height = read.getHeight();
        int max = Math.max(height, width);
        double fix = 0;
        if(max > 4096){
            fix = 4096.0/max;
        }
        System.out.println(fix);

        double quality = 50.0;
        compress(graphicsMagickHome,sourcePath,targetPath,quality,(int)(width*0.8),(int)(height*0.8));*/
        long start = System.currentTimeMillis();
        String str  = "H:\\3.webp";
        String dest = "H:\\a.png";
/*        File sourceFile = new File("C:\\Users\\wl\\Desktop\\mmexport1528197286860.webp");
        File destFile = new File("H:\\a.png");
        BufferedImage read = ImageIO.read(sourceFile);
        ImageIO.write(read,"png",destFile);*/
        File srcFile = new File(str);
        File destFile = new File(dest);
        WebpIO.toNormalImage(srcFile,destFile);
        WebpIO.close();

        Thread.sleep(100*1000);
    }
    public static void compress(String graphicsMagickHome,String sourcePath, String targetPath,
                                double quality,int width,int height) throws Exception {
        GMOperation op = new GMOperation();
        //待处理图片的绝对路径
        op.addImage(sourcePath);

        //图片压缩比，有效值范围是0.0-100.0，数值越大，缩略图越清晰  s
        op.quality(quality);
        //width 和height可以是原图的尺寸，也可以是按比例处理后的尺寸
//        op.addRawArgs("-resize", "100");
        //宽高都为100
        op.addRawArgs("-resize", width+"x"+height);
        op.addRawArgs("-gravity", "center");
        //op.resize(100, null);
        //处理后图片的绝对路径
        File smallFile = new File(targetPath);
        if (!smallFile.getParentFile().exists()) {
            smallFile.mkdir();
        }
        op.addImage(targetPath);

        // 如果使用ImageMagick，设为false,使用GraphicsMagick，就设为true，默认为false
        ConvertCmd convert = new ConvertCmd(true);
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            //linux下不要设置此值，不然会报错
            convert.setSearchPath(graphicsMagickHome);
        }
        convert.run(op);
    }
    public static BufferedImage zoomImage(BufferedImage bufImg) throws Exception {
        int height = bufImg.getHeight();
        int width = bufImg.getWidth();
        int max = Math.max(height,width);
        if(max > 4096){
            double fix = 4096.0/max;
            double wr  = width * fix;
            double hr = height * fix;
            AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
            return ato.filter(bufImg, null);
        }
        return bufImg;
    }

}
