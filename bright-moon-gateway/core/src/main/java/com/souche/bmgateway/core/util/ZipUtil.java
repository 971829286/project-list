package com.souche.bmgateway.core.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.*;
import java.util.List;

/**
 * [压缩/解压缩]文件和文件夹
 *
 * @author zhaojian
 */
@Slf4j
public class ZipUtil {

    public static final int BUFFER_SIZE = 1024;

    private final static String ENCODING = "GBK";

    /**
     * 压缩文件和文件夹
     *
     * @param srcPathname 需要被压缩的文件或文件夹路径
     * @param zipFilepath 将要生成的zip文件路径
     */
    public static void zip(String srcPathname, String zipFilepath) {
        File file = new File(srcPathname);
        if (!file.exists()) {
            throw new RuntimeException("source file or directory " + srcPathname + " does not exist.");
        }

        Project project = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setProject(project);
        // 判断是目录还是文件
        if (file.isDirectory()) {
            fileSet.setDir(file);
            // ant中include/exclude规则在此都可以使用
            // 比如:
            // fileSet.setExcludes("**/*.txt");
            // fileSet.setIncludes("**/*.xls");
        } else {
            fileSet.setFile(file);
        }

        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(new File(zipFilepath));
        zip.addFileset(fileSet);
        zip.setEncoding(ENCODING);
        zip.execute();
        log.info("compress successed.");
    }

    /**
     * 解压 zip 文件
     *
     * @param zipFile zip 压缩文件
     * @param destDir zip 压缩文件解压后保存的目录
     * @return 返回 zip 压缩文件里的文件名的 list
     */
    public static List<String> unZip(File zipFile, String destDir) {
        // 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
        if (StringUtils.isBlank(destDir)) {
            destDir = zipFile.getParent();
        }

        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        ZipArchiveInputStream is = null;
        List<String> fileNames = Lists.newArrayList();

        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
            ZipArchiveEntry entry;

            while ((entry = is.getNextZipEntry()) != null) {
                fileNames.add(entry.getName());
                File directory = new File(destDir, entry.getName());

                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(directory);
                } else {
                    FileUtils.forceMkdir(directory.getParentFile());
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(directory), BUFFER_SIZE);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch (Exception e) {
            log.error("un zip error,e:{}", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return fileNames;
    }
}