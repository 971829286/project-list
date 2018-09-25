package cn.ourwill.huiyizhan.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * <p><b>VerifyCodeUtils Description:</b> (验证码生成)</p>
 * <b>DATE:</b> 2016年6月2日 下午3:53:34
 */
public class VerifyCodeUtils {
	
	//使用到Algerian字体，系统里没有的话需要安装字体，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
	public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	private static Random random = new Random();


	/**
	 * 使用系统默认字符源生成验证码
	 * @param verifySize	验证码长度
	 * @return
	 */
	public static String generateVerifyCode(int verifySize){
		return generateVerifyCode(verifySize, VERIFY_CODES);
	}
	/**
	 * 使用指定源生成验证码
	 * @param verifySize	验证码长度
	 * @param sources	验证码字符源
	 * @return
	 */
	public static String generateVerifyCode(int verifySize, String sources){
		if(sources == null || sources.length() == 0){
			sources = VERIFY_CODES;
		}
		int codesLen = sources.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(verifySize);
		for(int i = 0; i < verifySize; i++){
			verifyCode.append(sources.charAt(rand.nextInt(codesLen-1)));
		}
		return verifyCode.toString();
	}
	
	/**
	 * 生成随机验证码文件,并返回验证码值
	 * @param w
	 * @param h
	 * @param outputFile
	 * @param verifySize
	 * @return
	 * @throws IOException
	 */
	public static String outputVerifyImage(int w, int h, File outputFile, int verifySize) throws IOException{
		String verifyCode = generateVerifyCode(verifySize);
		outputImage(w, h, outputFile, verifyCode);
		return verifyCode;
	}
	
	/**
	 * 输出随机验证码图片流,并返回验证码值
	 * @param w
	 * @param h
	 * @param os
	 * @param verifySize
	 * @return
	 * @throws IOException
	 */
	public static String outputVerifyImage(int w, int h, OutputStream os, int verifySize) throws IOException{
		String verifyCode = generateVerifyCode(verifySize);
		outputImage(w, h, os, verifyCode);
		return verifyCode;
	}
	
	/**
	 * 生成指定验证码图像文件
	 * @param w
	 * @param h
	 * @param outputFile
	 * @param code
	 * @throws IOException
	 */
	public static void outputImage(int w, int h, File outputFile, String code) throws IOException{
		if(outputFile == null){
			return;
		}
		File dir = outputFile.getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		try{
			outputFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(outputFile);
			outputImage(w, h, fos, code);
			fos.close();
		} catch(IOException e){
			throw e;
		}
	}
	
	/**
	 * 输出指定验证码图片流
	 * @param w
	 * @param h
	 * @param os
	 * @param code
	 * @throws IOException
	 */
	public static void outputImage(int w, int h, OutputStream os, String code) throws IOException{
		int verifySize = code.length();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Color[] colors = new Color[5];
		Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN,
				Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
				Color.PINK, Color.YELLOW };
		float[] fractions = new float[colors.length];
		for(int i = 0; i < colors.length; i++){
			colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
			fractions[i] = rand.nextFloat();
		}
		Arrays.sort(fractions);
		
		g2.setColor(Color.GRAY);// 设置边框色
		g2.fillRect(0, 0, w, h);
		
		Color c = getRandColor(200, 250);
		g2.setColor(c);// 设置背景色
		g2.fillRect(0, 2, w, h-4);
		
		//绘制干扰线
		Random random = new Random();
		g2.setColor(getRandColor(160, 200));// 设置线条的颜色
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(w - 1);
			int y = random.nextInt(h - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			g2.drawLine(x, y, x + xl + 40, y + yl + 20);
		}
		
		// 添加噪点
		float yawpRate = 0.05f;// 噪声率
		int area = (int) (yawpRate * w * h);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int rgb = getRandomIntColor();
			image.setRGB(x, y, rgb);
		}
		
		shear(g2, w, h, c);// 使图片扭曲

		g2.setColor(getRandColor(100, 160));
		int fontSize = h-4;
		Font font = new Font("Algerian", Font.ITALIC, fontSize);
		g2.setFont(font);
		char[] chars = code.toCharArray();
		for(int i = 0; i < verifySize; i++){
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verifySize) * i + fontSize/2, h/2);
			g2.setTransform(affine);
			g2.drawChars(chars, i, 1, ((w-10) / verifySize) * i + 5, h/2 + fontSize/2 - 10);
		}
		
		g2.dispose();
		ImageIO.write(image, "jpg", os);
	}
	
	private static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	private static int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}
	
	private static int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	private static void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}
	
	private static void shearX(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	private static void shearY(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(40) + 10; // 50;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}
	public static void main(String[] args) throws IOException{
		File dir = new File("F:/verifies");
		int w = 200, h = 80;
		for(int i = 0; i < 50; i++){
			String verifyCode = generateVerifyCode(4);
			File file = new File(dir, verifyCode + ".jpg");
			outputImage(w, h, file, verifyCode);
		}
	}

	//验证码
	private String codeText;
	//验证码的宽（默认110）
	private Integer codeImgWidth;
	//验证码图片的高（默认25）
	private Integer codeImgHeight;
	//验证码的个数（默认4）
	private Integer codeSize;
	//验证码的字体大小
	private Integer fontSize;
	//干扰线的条数(默认为5)
	private Integer lineSize;
	//干扰线的透明度(100)alpha 值为 255 则意味着颜色完全是不透明的，alpha 值为 0意味着颜色是完全透明的
	private Integer lineAlpha;
	//噪点(干扰点)的个数
	private Integer randomPointSize;
	//背景色
	private Color backgroundColor;
	//纯数字验证码
	private final static Integer MODE_FULL_NUMBER=1;
	//纯字母验证码
	private final static Integer MODE_FULL_CHAR=2;
	//数字和字母组合验证码
	private final  static Integer MODE_NUMBER_AND_CHAE=3;
	//验证码的类型
	private  Integer modeType;
	//验证码图片
	private  BufferedImage codeImage;

	//构造方法
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight, Integer codeSize, Integer fontSize, Integer lineSize, Integer lineAlpha, Integer randomPointSize, Color backgroundColor, Integer modeType){
		this.codeImgWidth=codeImgWidth;
		this.codeImgHeight=codeImgHeight;
		this.codeSize=codeSize;
		this.fontSize=fontSize;
		this.lineSize=lineSize;
		this.lineAlpha=lineAlpha;
		this.randomPointSize=randomPointSize;
		this.backgroundColor=backgroundColor;
		this.modeType=modeType;
		this.codeImage=createCodeImage();
	}
	//构造方法
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight, Integer codeSize, Integer fontSize, Integer lineSize, Integer lineAlpha, Integer randomPointSize, Color backgroundColor){
		this(codeImgWidth,codeImgHeight,codeSize,fontSize,lineSize,lineAlpha,randomPointSize,backgroundColor,null);
	}
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight, Integer codeSize, Integer fontSize, Integer lineSize, Integer lineAlpha, Integer randomPointSize){
		this(codeImgWidth,codeImgHeight,codeSize,fontSize,lineSize,lineAlpha,randomPointSize,null);
	}
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight, Integer codeSize, Integer fontSize, Integer lineSize, Integer lineAlpha){
		this(codeImgWidth,codeImgHeight,codeSize,fontSize,lineSize,lineAlpha,null);
	}
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight, Integer codeSize, Integer fontSize, Integer lineSize){
		this(codeImgWidth,codeImgHeight,codeSize,fontSize,lineSize,null);
	}
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight, Integer codeSize, Integer fontSize){
		this(codeImgWidth,codeImgHeight,codeSize,fontSize,null);
	}
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight, Integer codeSize){
		this(codeImgWidth,codeImgHeight,codeSize,null);
	}
	public VerifyCodeUtils(Integer codeImgWidth, Integer codeImgHeight){
		this(codeImgWidth,codeImgHeight,null);
	}
	public VerifyCodeUtils(){
		this(null,null);
	}

	//生成0~9随机数
	private Integer getRandomNumber(){
		Random random=new Random();
		return random.nextInt(10);
	}
	//生成a-z和A-Z随机字符
	private char getRandomChar() {
		Random random=new Random();
		int randomNum=random.nextInt(26);
		int Lower_Or_UpperCase=random.nextInt(2);
		if(Lower_Or_UpperCase==0){
			return (char)(randomNum+65);
		}
		return (char)(randomNum+97);
	}
	//生成随机颜色 alpha 值为 255 则意味着颜色完全是不透明的，alpha 值为 0意味着颜色是完全透明的
	private Color getRandomColor(Integer alpha){
		Random random=new Random();
		int red=random.nextInt(150);
		int green=random.nextInt(150);
		int blue=random.nextInt(150);
		if(alpha==null){
			alpha=255;
		}
		return new Color(red,green,blue,alpha);
	}
	//创建BufferImage
	private BufferedImage createBufferedImage(){
		if(this.codeImgWidth==null){
			this.codeImgWidth=110;
		}
		if(this.codeImgHeight==null){
			this.codeImgHeight=25;
		}
		BufferedImage bufferedImage=new BufferedImage(this.codeImgWidth,this.codeImgHeight,BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D=(Graphics2D)bufferedImage.getGraphics();
		if(this.backgroundColor==null){
			backgroundColor=new Color(255,255,255);
		}
		graphics2D.setBackground(this.backgroundColor);
		graphics2D.fillRect(0,0,codeImgWidth,codeImgHeight);
		return bufferedImage;
	}
	//画干扰线
	private BufferedImage drawLine(BufferedImage bufferedImage){
		Random random=new Random();
		if(this.lineSize==null){
			lineSize=5;
		}
		Graphics2D graphics2D=(Graphics2D)bufferedImage.getGraphics();
		for(int i=0;i<lineSize;i++){
			//直线起点的x坐标
			int startX=random.nextInt(bufferedImage.getWidth()-1);
			//直线起点的y坐标
			int startY=random.nextInt(bufferedImage.getHeight()-1);
			//直线结束点的x坐标
			int endX=random.nextInt(bufferedImage.getWidth()-1);
			//直线结束点的y坐标
			int endY=random.nextInt(bufferedImage.getHeight()-1);
			if(this.lineAlpha==null){
				this.lineAlpha=100;
			}
			//直线的颜色
			graphics2D.setColor(getRandomColor(lineAlpha));
			// 处理干扰线与边框的交界（1f为线条的宽度）
			graphics2D.setStroke(new BasicStroke(1f));
			graphics2D.drawLine(startX,startY,endX,endY);
		}
		return bufferedImage;
	}
	//添加噪声点
	//画干扰线
	private BufferedImage drawRandomPoint(BufferedImage bufferedImage){
		Random random=new Random();
		if(this.randomPointSize==null){
			randomPointSize=20;
		}
		for(int i=0;i<randomPointSize;i++){
			int x=random.nextInt(bufferedImage.getWidth()-1);
			int y=random.nextInt(bufferedImage.getHeight()-1);
			int rgb=getRandomColor(100).getRGB();
			bufferedImage.setRGB(x, y, rgb);
		}
		return bufferedImage;
	}
	//根据模式生成验证码字符
	private String careateCode(){
		String result="";
		if(this.modeType==null){
			this.modeType=MODE_FULL_NUMBER;
		}
		if(modeType==MODE_FULL_NUMBER){
			result=getRandomNumber()+"";
		}
		if(modeType==MODE_FULL_CHAR){
			result=getRandomChar()+"";
		}
		if(modeType==MODE_NUMBER_AND_CHAE){
			Random random=new Random();
			int randomChoseNum=random.nextInt(2);
			if(randomChoseNum==0){
				result=getRandomNumber()+"";
			}else{
				result=getRandomChar()+"";
			}
		}
		return result;
	}
	//得到验证码
	public String getCodeText(){
		return this.codeText;
	}

	//输出验证码到HttpResponse
	public void writeOut(OutputStream os){
		try{
			ImageIO.write(this.codeImage,"PNG",os);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//输出验证码图片BufferedImage
	public BufferedImage getBufferedImage(){
		return this.codeImage;
	}
	//设置字体
	private Font getFont() {
		if(this.fontSize==null){
			fontSize=19;
		}
		return new Font("Fixedsys", Font.CENTER_BASELINE, fontSize);
	}
	//生成验证码图片
	private BufferedImage createCodeImage(){
		BufferedImage image = createBufferedImage();//创建图片缓冲区
		image=drawLine(image);
		image=drawRandomPoint(image);
		Graphics2D g2 = (Graphics2D)image.getGraphics();//得到绘制环境
		g2.setStroke(new BasicStroke(1.5f));
		g2.setFont(getFont());
		if(this.codeSize==null){
			this.codeSize=4;
		}
		//用来装验证码
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<codeSize;i++) {
			g2.setColor(getRandomColor(null));
			String codeChar=careateCode();
			sb.append(codeChar);
			//平移或旋转变换
			Random random=new Random(System.nanoTime());
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4 * random.nextDouble() * (random.nextBoolean() ? 1 : -1), (codeImgWidth/ codeSize) * i + 18/2, codeImgHeight/2);
			g2.setTransform(affine);
			// 首字符的基线位于用户空间中的 (x,y) 位置处
			g2.drawString(codeChar, 6 + i * (codeImgWidth/codeSize), Math.round(codeImgHeight*0.8)-10);
		}
		this.codeText=sb.toString();//验证码文本
		return image;
	}

}
