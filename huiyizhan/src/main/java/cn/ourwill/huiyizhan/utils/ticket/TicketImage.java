package cn.ourwill.huiyizhan.utils.ticket;

import cn.ourwill.huiyizhan.utils.ImgUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-15 15:08
 **/
@Slf4j
@Data
public class TicketImage {
    private Graphics2D g = null;
    private int        x = 230;
    private int        y = 130;
    private String YaHei;
    private String YaHeiBold;

    /**
     * 导入本地图片到缓冲区
     */
    public BufferedImage loadImageLocal() {
        try {
            String resource = ResourceUtils.getURL("classpath:").getPath() + "emailAndPdf/img/ticket_bg.png";
            this.YaHei = ResourceUtils.getURL("classpath:").getPath() + "emailAndPdf/font/msyh.ttc";
            this.YaHeiBold = ResourceUtils.getURL("classpath:").getPath() + "emailAndPdf/font/msyhbd.ttc";
            return ImageIO.read(new File(resource));
        } catch (IOException e) {
            log.info("TicketImage.loadImageLocal", e);
        }
        return null;
    }

    /**
     * 生成新图片到本地
     */
    public void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputFile = new File(newImage);
                ImageIO.write(img, "png", outputFile);
            } catch (IOException e) {
                log.info("TicketImage.writeImageLocal", e);
            }
        }
    }

    /**
     * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
     */
    public BufferedImage addTitle(BufferedImage img, String title) {
        try {
            if (StringUtils.isEmpty(title)) {
                title = " ";
            }
            g = img.createGraphics();
            g.setColor(Color.BLACK);
            Font font = Font.createFont(Font.TRUETYPE_FONT,new File(YaHeiBold)).deriveFont(38f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            if (title.length() >= 25) {
                String line1 = title.substring(0, 25);
                String line2 = title.substring(25);
                g.drawString(line1, x, y);
                g.drawString(line2, x, y + 80);
            } else {
                g.drawString(title, x, y);
            }
            g.dispose();
        } catch (Exception e) {
            log.info("TicketImage.addTitle", e);
        }
        return img;
    }

    public BufferedImage addTime(BufferedImage image, String content) {
        try {
            if (StringUtils.isEmpty(content)) {
                content = " ";
            }
            g = image.createGraphics();
            g.setColor(Color.GRAY);
            Font font = Font.createFont(Font.TRUETYPE_FONT,new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            g.drawString("开始时间", x, y + 200);
            g.setColor(Color.BLACK);
            g.drawString(content, x, y + 250);
            g.dispose();
        } catch (Exception e) {
            log.info("TicketImage.addTime", e);
        }
        return image;

    }

    public BufferedImage addAddress(BufferedImage image, String content) {
        try {
            if (StringUtils.isEmpty(content)) {
                content = " ";
            }
            g = image.createGraphics();
            g.setColor(Color.GRAY);
            Font font = Font.createFont(Font.TRUETYPE_FONT,new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            String line1 = "活动地址";
            g.drawString(line1, x + 500, y + 200);
            g.setColor(Color.BLACK);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                if (i % 15 == 0 && i != 0) {
                    builder.append("\n");
                }
                builder.append(content.charAt(i));
            }
            //分行对齐
            String[] lines = builder.toString().split("\n");
            for (int i = 0; i < lines.length; i++) {
                g.drawString(lines[i], x + 500, y + 250 + i * 40);
            }
            g.dispose();
        } catch (Exception e) {
            log.info("TicketImage.addAddress", e);
        }
        return image;
    }


    public BufferedImage addPerson(BufferedImage image, String content) {
        try {
            if (StringUtils.isEmpty(content)) {
                content = " ";
            }
            g = image.createGraphics();
            g.setColor(Color.GRAY);
            Font font = Font.createFont(Font.TRUETYPE_FONT,new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            String line1 = "参会人";
            g.drawString(line1, x, y + 450);
            g.setColor(Color.BLACK);

            g.drawString(content, x, y + 500);
            g.dispose();
        } catch (Exception e) {
            log.info("TicketImage.addPerson", e);
        }
        return image;
    }

    public BufferedImage addTicketName(BufferedImage image, String content) {
        try {
            if (StringUtils.isEmpty(content)) {
                content = " ";
            }
            g = image.createGraphics();
            g.setColor(Color.GRAY);
            Font font = Font.createFont(Font.TRUETYPE_FONT,new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            String line1 = "票名";
            g.drawString(line1, x + 500, y + 450);
            g.setColor(Color.BLACK);

            g.drawString(content, x + 500, y + 500);
            g.dispose();
        } catch (Exception e) {
            log.info("TicketImage.addTicketName", e);
        }
        return image;
    }

    public BufferedImage addQRCode(BufferedImage image, String QRCode, String code) {
        try {
            if (StringUtils.isEmpty(QRCode)) {
                log.info("传入QRcode为空,无法生成二维码");
                return null;
            }
            g = image.createGraphics();
            g.setColor(Color.BLACK);
            Font font = Font.createFont(Font.TRUETYPE_FONT,new File(YaHeiBold)).deriveFont(30f);
            g.setFont(font);
            BufferedImage hello = ImageIO.read(new ByteArrayInputStream(ImgUtil.createQRCode(QRCode, 400, 400)));
//            g.drawImage();
            g.drawImage(hello, null, x + 1100, y + 100);
            // 验证输出位置的纵坐标和横坐标
            g.drawString(code, x + 1250, y + 500);
            g.dispose();
        } catch (Exception e) {
            log.info("TicketImage.addQRCode", e);
        }
        return image;
    }

}
