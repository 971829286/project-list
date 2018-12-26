package com.souche.bmgateway.core.util;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <p></p>
 *
 * @author ningzong.zeng
 */
@Slf4j
public class CsvUtil {

    /**
     * 读取csv文件
     *
     * @param filePath
     * @param skipHeader 是否跳过表头
     * @return
     * @throws Exception
     */
    public static List<String[]> readCsv(String filePath, boolean skipHeader) throws Exception {
        List<String[]> csvList = Lists.newArrayList();
        if (isCsv(filePath)) {
            CsvReader reader = new CsvReader(filePath, ',', Charset.forName("GBK"));
            // 跳过表头   如果需要表头的话，不要写这句。
            if (skipHeader) {
                reader.readHeaders();
            }
            //逐行读入除表头的数据
            while (reader.readRecord()) {
                csvList.add(reader.getValues());
            }
            reader.close();
        } else {
            log.info("此文件不是CSV文件!");
        }
        return csvList;
    }

    /**
     * 判断是否是csv文件
     *
     * @param fileName
     * @return
     */
    private static boolean isCsv(String fileName) {
        return fileName.matches("^.+\\.(?i)(csv)$");
    }


    /**
     * 写入CSV文件
     *
     * @param path
     * @param fileName
     * @param data     第一行是标题
     */
    public static void writeCsv(String path, String fileName, List<String[]> data) {
        try {
            createDir(path);
            CsvWriter wr = new CsvWriter(path + "/" + fileName, ',', Charset.forName("utf-8"));
            int length = data.size();
            for (int i = 0; i < length; i++) {
                wr.writeRecord(data.get(i));
            }
            wr.close();
        } catch (IOException e) {
            log.error("写入csv文件失败,e:{}", e);
            e.printStackTrace();
        }
    }

    private static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        // 创建目录
        return dir.mkdirs();
    }
}
