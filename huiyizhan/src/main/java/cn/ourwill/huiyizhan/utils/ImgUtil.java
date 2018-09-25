package cn.ourwill.huiyizhan.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.StringUtils;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.io.IOUtils;
import org.omg.CORBA.portable.InputStream;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;


/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/7 0007 15:32
 * @Version1.0
 */
public class ImgUtil {
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

    /**
     * @param qrCodeData   将要嵌入的数据
     * @param charset      字符编码
     * @param hintMap
     * @param qrCodeheight 二维码长
     * @param qrCodewidth  二维码高
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] createQRCode(String qrCodeData, String format,
                                      String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {
        ByteArrayOutputStream outputStream = null;
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, format, outputStream);
//            MatrixToImageWriter.writeToPath(matrix, format, Paths.get("H:\\1.jpeg"));
            byte[] bytes = outputStream.toByteArray();
            return bytes;
        } catch (Exception e) {
            // e.printStackTrace();
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * 封装
     * @param QRCodeDate
     * @return
     */
    public static byte[] createQRCode(String QRCodeDate) {
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        return createQRCode(QRCodeDate, "jpeg", "UTF-8", hintMap, 200, 200);
    }

    /**
     * 获取二维码解析成base64的字符串
     * @param QRCodeData
     * @return
     */
    public static String getQRCodeBase64(String QRCodeData) {
        byte[] bytes = createQRCode(QRCodeData);
        if(bytes == null)
            return null;
        else{
            return Base64.encodeBase64String(bytes);
        }
    }
    public static byte[] createQRCode(String QRCodeDate,int height,int width) {
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        return createQRCode(QRCodeDate, "jpeg", "UTF-8", hintMap, height, width);
    }
}
