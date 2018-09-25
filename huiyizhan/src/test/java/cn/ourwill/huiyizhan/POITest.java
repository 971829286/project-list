package cn.ourwill.huiyizhan;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

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
}
