package com.souche.bmgateway.core.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件上传工具类
 *
 * @author chenwj
 * @since 2017/07/31
 */
public class UploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * 从url上下载文件
     *
     * @param urlString 下载链接
     * @param filename  文件名
     * @param savePath  保存路径
     */
    public static void download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File saveDir = new File(savePath);
        //如果文件的目录不存在
        if (!saveDir.exists()) {
            boolean mkdirs = saveDir.mkdirs();
            if (!mkdirs) {
                return;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(saveDir.getPath() + "/" + filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (Exception e) {
            logger.error("<download>Exception,原因：{}", e);
        } finally {
            // 完毕，关闭所有链接
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                logger.error("<download>IOException,原因：{}", ex);
            }
        }
    }

    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return base64
     */
    public static String getImgStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            int read = in.read(data);
            in.close();
        } catch (IOException e) {
            logger.error("<getImgStr>IOException,原因：{}", e);
        } finally {
            // 完毕，关闭所有链接
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error("<getImgStr>IOException,原因：{}", ex);
            }
        }
        if (data != null) {
            return new String(Base64.encodeBase64(data));
        }
        return null;
    }

    /**
     * 压缩文件
     *
     * @param filePath 文件路径
     * @param filename 文件名
     */
    public static void compressPicture(String filePath, String filename) {
        String path = filePath + filename;
        // 压缩大小
        try {
//            getFileSize(path);
            Thumbnails.of(path).scale(0.8f).outputQuality(0.5f).toFile(path);
//            getFileSize(path);

        } catch (IOException e) {
            logger.error("<compressPicture>IOException,原因:{}", e);
        }
    }

    /**
     * 输出文件大小
     *
     * @param filePath 文件路径
     */
    private static void getFileSize(String filePath) {
        FileChannel fc = null;
        try {
            File f = new File(filePath);
            if (f.exists() && f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                fc = fis.getChannel();
                logger.info("-----文件大小size:{}mb", String.valueOf((double) fc.size() / (1024 * 1024)));
            } else {
                logger.info("file doesn't exist or is not a file");
            }

        } catch (FileNotFoundException e) {
            logger.error("<getFileSize>FileNotFoundException,原因:{}", e);
        } catch (IOException e) {
            logger.error("<getFileSize>IOException,原因:{}", e);
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    logger.error("<getFileSize>IOException,原因:{}", e);
                }
            }
        }
    }

}
