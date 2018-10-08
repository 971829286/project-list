package com.souche.bulbous.excel;

import com.google.common.collect.Lists;
import com.souche.bulbous.bean.Job;
import com.souche.bulbous.dto.ProgressDto;
import com.souche.bulbous.exception.CustomException;
import com.souche.optimus.cache.CacheService;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Excel工具类
 */
public class ExcelGenerate {

    /**
     * 获取任务进度
     *
     * @param jobId
     * @return
     */
    public static ProgressDto getProgressByJobId(String jobId, CacheService cacheService) {
        Job job = cacheService.getObject(ExcelConstant.EXPROT_JOB_ + jobId, Job.class);
        if (null == job) {
            throw new CustomException("任务已过期");
        }
        ProgressDto dto = new ProgressDto();
        dto.setJobId(jobId);
        if (Job.StatusEnum.exporting.name().equals(job.getStatus())) {
            dto.setProgress(job.getProgress());
        } else if (Job.StatusEnum.over.name().equals(job.getStatus())) {
            dto.setProgress(100);
            dto.setUrl(job.getUrl());
        } else if (Job.StatusEnum.noDate.name().equals(job.getStatus())) {
            throw new CustomException("无数据可导出");
        } else {
            throw new CustomException("数据导出出错");
        }
        return dto;
    }

    /**
     * 获取文档标题，扫描ApiModelProperty注解
     *
     * @param cla
     * @return
     */
    public static List<String> getTitle(Class cla) {
        List<String> result = Lists.newArrayList();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (null != annotation) {
                result.add(annotation.value());
            }
        }
        return result;
    }

    public static XSSFWorkbook generate(List<List<Object>> content, List<String> titleList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        if (CollectionUtils.isNotEmpty(titleList)) {
            int rowNum = 0;
            XSSFRow titleRow = sheet.createRow(rowNum++);
            // title
            XSSFCellStyle titleStyle = getTitleStyle(workbook);
            for (int i = 0; i < titleList.size(); i++) {
                XSSFCell createCell = titleRow.createCell(i);
                createCell.setCellValue(titleList.get(i));
                createCell.setCellStyle(titleStyle);
            }
        }
        // content
        if (CollectionUtils.isNotEmpty(content)) {
            for (List<Object> objectList : content) {
                setData(workbook, objectList);
            }
        }

        return workbook;
    }

    public static void append(XSSFWorkbook workbook, List<List<Object>> rowObject) {
        for (List<Object> objectList : rowObject) {
            setData(workbook, objectList);
        }
    }

    public static void beautify(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);
        for (Iterator<Cell> it = row.cellIterator(); it.hasNext(); ) {
            Cell cell = it.next();
            sheet.autoSizeColumn(cell.getColumnIndex(), true);
        }
    }

    private static void setData(XSSFWorkbook workbook, List<Object> rowObject) {
        XSSFCellStyle dateStyle = getDateStyle(workbook);

        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getPhysicalNumberOfRows();
        XSSFRow row = sheet.createRow(rowNum);
        int cellNum = 0;// 列计数器
        if (CollectionUtils.isNotEmpty(rowObject)) {
            for (Object object : rowObject) {
                XSSFCell createCell = row.createCell(cellNum++);
                if (null == object) {
                    createCell.setCellValue("");
                } else {
                    if (object instanceof Date) {
                        createCell.setCellValue((Date) object);
                        createCell.setCellStyle(dateStyle);
                    } else if (object instanceof Number) {
                        createCell.setCellValue(((Number) object).doubleValue());
                    } else if (object instanceof String) {
                        createCell.setCellValue((String) object);
                    } else if (object instanceof Boolean) {
                        createCell.setCellValue((Boolean) object);
                    } else {
                        createCell.setCellValue(object.toString());
                    }
                }
            }
        }
    }

    private static XSSFCellStyle getTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle titleStyle = workbook.createCellStyle();// title的样式
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 背景颜色
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);// 好像是填充方式
        titleStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
        titleStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
        titleStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
        titleStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 居中
        return titleStyle;
    }

    private static XSSFCellStyle getDateStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();// title的样式
        CreationHelper creationHelper = workbook.getCreationHelper();
        style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy/MM/dd  hh:mm:ss"));
        return style;
    }

}
