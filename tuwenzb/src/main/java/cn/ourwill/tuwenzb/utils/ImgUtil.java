package cn.ourwill.tuwenzb.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.docx4j.wml.U;
import org.im4java.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/7 0007 15:32
 * @Version1.0
 */
@Component
public class ImgUtil {
    private static String graphicsMagickPath;

//    @Value("${graphicsMagick.path}")
//    public void setGraphicsMagickPath(String graphicsMagickPath) {
//        this.graphicsMagickPath = graphicsMagickPath;
//    }

    //    public static String imageMagickPath = null;
//    static{ /**获取ImageMagick的路径 */
//        Properties prop = new PropertiesFile().getPropertiesFile();
//        imageMagickPath = "C:\\Program Files\\GraphicsMagick-1.3.26-Q8";
//          ProcessStarter.setGlobalSearchPath("/usr/local/bin/gm");
//    }
//    ProcessStarter.setGlobalSearchPath

    private static final Logger log = LogManager.getLogger(ImgUtil.class);

    //获取水印接口url
    public static String getWaterMark(String markImgUrl) {
        String waterMarkUrl = "watermark/1/image/";
        String encode = getBase64(markImgUrl);
        waterMarkUrl = waterMarkUrl + encode + "/ws/0.3/imageslim";
        return waterMarkUrl;
    }

    public static String getBase64(String str) {
        String restr = Base64.encodeBase64String(str.getBytes());
        return restr;
    }

    public static InputStream[] getImageByUrl(List<String> urls) {
        InputStream[] inputStreams = new InputStream[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
//            FileOutputStream outputStream = null;
            try {
//                String urlStr = urls.get(i).substring(0, urls.get(i).lastIndexOf('/') + 1)
//                        + URLEncoder.encode(
//                        urls.get(i).substring(urls.get(i).lastIndexOf('/') + 1), "UTF-8");
                URL url = new URL(urls.get(i).replace("+", "%20"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10 * 1000);
                InputStream inputStream = conn.getInputStream();
                inputStreams[i] = inputStream;
//                byte[] data = readInputStream(inputStream);
//                File imageFile = new File("D://test" + System.currentTimeMillis() + ".jpg");
//                outputStream = new FileOutputStream(imageFile);
//                outputStream.write(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inputStreams;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //把outStream里的数据写入内存
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭输入流
            inStream.close();
        }
        return new byte[0];
    }

    /*
     * 图片缩放按宽度缩放
     * src为源文件目录，dest为缩放后保存目录
     */
    public static BufferedImage zoomImage(BufferedImage bufImg, int w) throws Exception {
//         Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板
        int h = (int) (w * ((bufImg.getHeight() * 1.0) / (bufImg.getWidth() * 1.0)));
        double wr = 0, hr = 0;
        wr = w * 1.0 / bufImg.getWidth();     //获取缩放比例
        hr = h * 1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        return ato.filter(bufImg, null);
    }

    /**
     * @param files 要拼接的文件列表
     * @param type  1 横向拼接， 2 纵向拼接
     * @Description:图片拼接 （注意：必须两张图片长宽一致哦）
     */
    public static void mergeImage(InputStream[] files, int type, OutputStream os) throws Exception {
        int len = files.length;
        if (len < 1) {
            throw new RuntimeException("图片数量小于1");
        }
        //获取缩放比例
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        int MaxWidth = 0;
        //获取最大宽度
        for (int i = 0; i < len; i++) {
            try {
//                src[i] = files[i];
                images[i] = ImageIO.read(files[i]);
                if (images[i].getWidth() > MaxWidth)
                    MaxWidth = images[i].getWidth();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (MaxWidth > 1920) MaxWidth = 1920;
        for (int i = 0; i < len; i++) {
            images[i] = zoomImage(images[i], MaxWidth);
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) {// 纵向
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }
        if (type == 1 && newWidth < 1) {
            return;
        }
        if (type == 2 && newHeight < 1) {
            return;
        }
        System.out.println("生成新图片:" + new Date());
        // 生成新图片
        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }
            //输出想要的图片
            ImageIO.write(ImageNew, "JPG", os);
            System.out.println("生成完毕:" + new Date());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void cropImageCenter(String srcPath, String desPath, int rectw, int recth) throws IOException, InterruptedException, IM4JavaException
//    {
//        IMOperation op = new IMOperation();
//
//        op.addImage();
////        op.resize(rectw, recth, '^').gravity("center").extent(rectw, recth);
//
//        op.addImage();
//
//        ConvertCmd convert = new ConvertCmd(true);
//        convert.setSearchPath(imageMagickPath);
//        //convert.createScript("e:\\test\\myscript.sh",op);
//        convert.run(op, srcPath, desPath);
//
//    }

    public static void addImgWatermark(InputStream[] inputStreams, OutputStream outputStream) throws Exception {
        log.info("+++++++++++拼图++++++++++");
        int len = inputStreams.length;
        BufferedImage[] images = new BufferedImage[len];
        int MaxWidth = 0;
        //获取最大宽度
        for (int i = 0; i < len; i++) {
            try {
//                src[i] = files[i];
                images[i] = ImageIO.read(inputStreams[i]);
//                JPEGImageDecoder decoderFile = JPEGCodec.createJPEGDecoder(inputStreams[i]);
//                images[i] = decoderFile.decodeAsBufferedImage();
                if (images[i].getWidth() > MaxWidth)
                    MaxWidth = images[i].getWidth();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (MaxWidth > 1920) MaxWidth = 1920;
        IMOperation op = new IMOperation();
        ConvertCmd convert = new ConvertCmd(true);
        //linux下 注销
//        convert.setSearchPath(graphicsMagickPath);
        op.appendVertically();
        for (int i = 0; i < inputStreams.length; i++) {
            images[i] = zoomImage(images[i], MaxWidth);
            op.addImage();
        }
        op.addImage("jpeg:-");
        Stream2BufferedImage s2b = new Stream2BufferedImage();
        convert.setOutputConsumer(s2b);
        convert.run(op, (Object[]) images);
        BufferedImage img = s2b.getImage();
        ImageIO.write(img, "JPG", outputStream);
    }


    //获取文件的base64
    public static String getImageBase64(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            return null;
        }
        String imageType = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
        if ("jpg".equalsIgnoreCase(imageType) || "jpeg".equalsIgnoreCase(imageType) || "png".equalsIgnoreCase(imageType)) {
            String tempTarget = UUID.randomUUID().toString();
            try {
                URL url = new URL(imageUrl);
                BufferedImage image = ImageIO.read(url);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(10 * 1000);
                //图片尺寸不符合要求
                if (image.getWidth() > 4096 || image.getHeight() > 4096) {
                    compress(graphicsMagickPath, imageUrl, tempTarget, 50.0, getZoom(image));
                    byte[] bytes = Files.readAllBytes(Paths.get(tempTarget));
                    return Base64.encodeBase64String(bytes);
                } else if (urlConnection.getContentLengthLong() > 2097152) {
                    compress(graphicsMagickPath, imageUrl, tempTarget, 50.0, null);
                    byte[] bytes = Files.readAllBytes(Paths.get(tempTarget));
                    return Base64.encodeBase64String(bytes);
                } else {//图片符合要求
                    byte[] bytes = IOUtils.toByteArray(urlConnection.getInputStream());
                    return Base64.encodeBase64String(bytes);
                }
            } catch (Exception e) {
                log.info("ImgUtil.getImageBase64", e);
            } finally {
                if (Files.exists(Paths.get(tempTarget))) {
                    try {
                        Files.delete(Paths.get(tempTarget));
                    } catch (IOException e) {
                        log.info("ImgUtil.getImageBase64", e);
                    }
                }
            }
        }
        return null;
    }

    public static void compress(String graphicsMagickHome, String sourcePath, String targetPath,
                                double quality, String zoom) throws Exception {
        GMOperation op = new GMOperation();
        //待处理图片的绝对路径
        op.addImage(sourcePath);
        //图片压缩比，有效值范围是0.0-100.0，数值越大，缩略图越清晰  s
        op.quality(quality);
        //width 和height可以是原图的尺寸，也可以是按比例处理后的尺寸
//        op.addRawArgs("-resize", "100");
        //宽高都为100
        if (zoom != null) {
            op.addRawArgs("-resize", zoom);
        }
        op.addRawArgs("-gravity", "center");
        //op.resize(100, null);
        //处理后图片的绝对路径
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

    public static String getZoom(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        int max = Math.max(height, width);
        if (max > 4096) {
            double fix = 4096.0 / max;
            int wr = (int) (width * fix);
            int hr = (int) (height * fix);
            return wr + "x" + hr;
        }
        return null;
    }

    //获取文件的base64
    public static InputStream getCompressInputStream(InputStream inputStream, String fileType) {
        String tempTarget = UUID.randomUUID().toString();
        String tempSource = UUID.randomUUID().toString();
        try {
            BufferedImage image = ImageIO.read(inputStream);
            //图片尺寸不符合要求
            if (image.getWidth() > 4096 || image.getHeight() > 4096) {
                OutputStream outputStream = Files.newOutputStream(Paths.get(tempSource));
                ImageIO.write(image, fileType, outputStream);
                compress(graphicsMagickPath, tempSource, tempTarget, 50.0, getZoom(image));
                return Files.newInputStream(Paths.get(tempTarget));
            } else {
                compress(graphicsMagickPath, tempSource, tempTarget, 50.0, null);
                return Files.newInputStream(Paths.get(tempTarget));
            }
        } catch (Exception e) {
            log.info("ImgUtil.getImageBase64", e);
        } finally {
            if (Files.exists(Paths.get(tempTarget))) {
                try {
                    Files.delete(Paths.get(tempTarget));
                } catch (IOException e) {
                    log.info("ImgUtil.getImageBase64", e);
                }
            }
            if (Files.exists(Paths.get(tempSource))) {
                try {
                    Files.delete(Paths.get(tempSource));
                } catch (IOException e) {
                    log.info("ImgUtil.getImageBase64", e);
                }
            }
        }
        return null;
    }

    public static File webpToPng(File file) {
        String dirStr = "tmpFile";
        String dest = dirStr +"/" + UUID.randomUUID().toString() + ".jpg";
        File fileDir = new File(dirStr);
        if(!fileDir.exists()) {
            fileDir.setReadable(true);
            fileDir.setWritable(true);
            fileDir.setExecutable(true);
            fileDir.mkdir();
        }
        File destFile = new File(dest);
        WebpIO.toNormalImage(file, destFile);
        return destFile;
    }

    public static InputStream resetDirectory(InputStream inputStream, String type) {
        String dirStr = "tmpFile";
        String tempFile = UUID.randomUUID().toString() + "." + type;
        String tempResFile = "tmpFile" + "/" + UUID.randomUUID().toString() + "hello." + type;
        File dirFile = new File(dirStr);
        if (!dirFile.exists()) {
            dirFile.setWritable(true);
            dirFile.setReadable(true);
            dirFile.setExecutable(true);
            dirFile.mkdir();
        }
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            Files.write(Paths.get(tempFile), bytes);
            File file = new File(tempFile);

            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
            int orientation = 0;
            int angle = 0;
            if (directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) { // Exif信息中有保存方向,把信息复制到缩略图
                orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION); // 原图片的方向信息
                // 原图片的方向信息
                if (6 == orientation) {
                    //6旋转90
                    angle = 90;
                } else if (3 == orientation) {
                    //3旋转180
                    angle = 180;
                } else if (8 == orientation) {
                    //8旋转90
                    angle = 270;
                }
                BufferedImage rotate = RotateImage.Rotate(ImageIO.read(file), angle);
                ImageIO.write(rotate, type, new File(tempResFile));
                FileInputStream in = new FileInputStream(new File(tempResFile));
                return in;
            }
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            log.info("ImgUtil.getDirectory", e);
        } finally {
            if (Files.exists(Paths.get(tempFile))) {
                try {
                    Files.delete(Paths.get(tempFile));
                } catch (IOException e) {
                    log.info("ImgUtil.getDirectory", e);
                }
            }
        }
        return null;
    }


}
