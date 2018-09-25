package cn.ourwill.tuwenzb.utils;

import cn.ourwill.tuwenzb.entity.Comment;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * wanghao
 * excel操作类
 */
public class ExcelUtil {

    //2007+版office使用
    public static List<Comment> newExcel(String filePath,Integer activityId) {
        List<Comment> comments = new ArrayList<Comment>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
            XSSFSheet sheet = workbook.getSheetAt(0);
            Comment comment = new Comment();
            comment.setActivityId(activityId);
            comment.setAvatar("app/static/69user.png");
            comment.setCheck(2);
            for (int k = 1; k <= sheet.getLastRowNum()-1; k++) {

                XSSFRow row = sheet.getRow(k);
                XSSFCell cDate = row.getCell(0);
                if(cDate!=null){
                    cDate.setCellType(Cell.CELL_TYPE_NUMERIC);
                    comment.setCTime(cDate.getDateCellValue());
                }
                XSSFCell nickName = row.getCell(1);
                if(nickName!=null){
                    nickName.setCellType(Cell.CELL_TYPE_STRING);
                    comment.setNickname(nickName.getStringCellValue());
                }
                XSSFCell content = row.getCell(2);
                if(content!=null){
                    content.setCellType(Cell.CELL_TYPE_STRING);
                    comment.setContent(content.getStringCellValue());
                }
                comments.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    //老版本office
    public static List<Comment> oldExcel(String filePath,Integer activityId){
        List<Comment> comments = new ArrayList<Comment>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filePath));
            HSSFSheet sheet = workbook.getSheetAt(0);
            Comment comment = new Comment();
            comment.setActivityId(activityId);
            comment.setAvatar("app/static/69user.png");
            comment.setCheck(2);
            for (int k = 1; k <= sheet.getLastRowNum()-1; k++) {

                HSSFRow row = sheet.getRow(k);
                HSSFCell cDate = row.getCell(0);
                if(cDate!=null){
                    cDate.setCellType(Cell.CELL_TYPE_NUMERIC);
                    comment.setCTime(cDate.getDateCellValue());
                }
                HSSFCell nickName = row.getCell(1);
                if(nickName!=null){
                    nickName.setCellType(Cell.CELL_TYPE_STRING);
                    comment.setNickname(nickName.getStringCellValue());
                }
                HSSFCell content = row.getCell(2);
                if(content!=null){
                    content.setCellType(Cell.CELL_TYPE_STRING);
                    comment.setContent(content.getStringCellValue());
                }
                comments.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    //读取Excel
    public static List<Comment> readExcel(String filePath, String fileName,Integer activityId){
        String[] str = fileName.split("\\.");
        List<Comment> comments = new ArrayList<Comment>();
        if(str.length>0){
            if("xls".equals(str[str.length-1])){
                //1997-2003 excel
                comments = oldExcel(filePath,activityId);
            }
            if("xlsx".equals(str[str.length-1])){
                //2007+ excel
                comments = newExcel(filePath,activityId);
            }
        }
        return comments;
    }
}
