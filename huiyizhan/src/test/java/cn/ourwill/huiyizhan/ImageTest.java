package cn.ourwill.huiyizhan;

import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-14 15:12
 **/
public class ImageTest {
    public static void main(String[] args) throws  Exception{
        BufferedImage bufferedImage = ImageIO.read(new File("H:\\email\\1.png"));
        Font font = getDefaultFont();
        Graphics2D g = null;
        int fontsize = 0;
        int x = 0;
        int y = 0;

        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        g = bufferedImage.createGraphics();

        g.setFont(font);
        g.drawString("超级无敌牛逼票",0,0);


        g.dispose();

    }
    public static Font getDefaultFont() throws  Exception{
        String fontUrl = ResourceUtils.getURL("classpath:").getPath()+"/emailAndPdf/font/msyh.ttc";
        System.out.println(fontUrl);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(fontUrl)));
        return Font.createFont(Font.TRUETYPE_FONT,bis);
    }
}
