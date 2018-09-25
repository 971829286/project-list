package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.utils.ImgUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


public class pic {
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
            String resource = ResourceUtils.getURL("classpath:").getPath() + "img/ticket_bg.png";
            //getDefaultFont();
            this.YaHei = ResourceUtils.getURL("classpath:").getPath() + "emailAndPdf/font/msyh.ttc";
            this.YaHeiBold = ResourceUtils.getURL("classpath:").getPath()+"emailAndPdf/font/msyhbd.ttc";
            return ImageIO.read(new File(resource));
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
     */
    public BufferedImage addTitle(BufferedImage img, String title) {
        try {
            g = img.createGraphics();
            g.setColor(Color.BLACK);
            //Font titleFont = new Font("微软雅黑",Font.PLAIN,38);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(YaHeiBold)).deriveFont(38f);
//            font = font.deriveFont(Font.BOLD).deriveFont(38);
            g.setFont(customFont);
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
            System.out.println(e.getMessage());
        }
        return img;
    }

    public BufferedImage addTime(BufferedImage image, String content) {
        try {
            g = image.createGraphics();
            g.setColor(Color.GRAY);
//            Font font = new Font("微软雅黑", Font.PLAIN, 30);
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            g.drawString("开始时间", x, y + 200);
            g.setColor(Color.BLACK);
            g.drawString(content, x, y + 250);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return image;

    }

    public BufferedImage addAddress(BufferedImage image, String content) {
        try {
            g = image.createGraphics();
            g.setColor(Color.GRAY);
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(YaHei)).deriveFont(30f);
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
            String[] lines = builder.toString().split("\n");
            for (int i = 0; i < lines.length; i++) {
                g.drawString(lines[i], x + 500, y + 250 + i * 40);
            }
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return image;
    }


    public BufferedImage addPerson(BufferedImage image, String content) {
        try {
            g = image.createGraphics();
            g.setColor(Color.GRAY);
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            String line1 = "参会人";
            g.drawString(line1, x, y + 450);
            g.setColor(Color.BLACK);

            g.drawString(content, x, y + 500);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return image;
    }

    public BufferedImage addTicketName(BufferedImage image, String content) {
        try {
            g = image.createGraphics();
            g.setColor(Color.GRAY);
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            // 验证输出位置的纵坐标和横坐标
            String line1 = "票名";
            g.drawString(line1, x + 500, y + 450);
            g.setColor(Color.BLACK);

            g.drawString(content, x + 500, y + 500);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return image;
    }

    public BufferedImage addQRCode(BufferedImage image, String QRCode, String code) {
        try {
            g = image.createGraphics();
            g.setColor(Color.BLACK);
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(YaHei)).deriveFont(30f);
            g.setFont(font);
            BufferedImage hello = ImageIO.read(new ByteArrayInputStream(ImgUtil.createQRCode(QRCode, 400, 400)));
//            g.drawImage();
            g.drawImage(hello, null, x + 1100, y + 100);
            // 验证输出位置的纵坐标和横坐标
            g.drawString(code, x + 1250, y + 500);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return image;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("H:\\email\\2.png");
        if (file.exists()) {
            file.delete();
        }
        pic tt = new pic();

        String content = "由唐嫣罗晋主演的电视剧归去来于5月14日首播当天下午唐嫣好友孙坚在微博晒出与唐嫣的聊天记录为自己在朋友圈宣传归去来找主演唐嫣拿广告费" +
                "并称自己朋友圈硬广很贵反被唐嫣呛声回复孙坚随后在微博发文吐槽好友唐嫣让他变" +
                "成了像来碰瓷的他还在评论中留言道别我把逼急了以后每天每集打广告让你月结贵死你";
        if (content.length() > 75) {
            content = content.substring(0, 75);
        }
        BufferedImage image = tt.loadImageLocal();
        tt.addPerson(image, "张三李四王五赵柳");
        tt.addTime(image, "2018-05-11 15:30:11");
//        tt.addTitle(image,"一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十");
        tt.addTitle(image, "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十");
        tt.addTicketName(image, "超级无敌牛逼票");
        tt.addAddress(image, content);
        tt.addQRCode(image, "12345", "665737");
        tt.writeImageLocal("H:\\email\\2.png", image);
    }

}