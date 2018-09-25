package cn.ourwill.tuwenzb.weixin.Utils;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.*;

import java.io.*;
import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/8/28 0028 17:18
 * @Version1.0
 */
public class Docx4jUtil {
    /**
     *  像往常一样, 我们创建了一个包(package)来容纳文档.
     *  然后我们创建了一个指向将要添加到文档的图片的文件对象.为了能够对图片做一些操作, 我们将它转换
     *  为字节数组. 最后我们将图片添加到包中并保存这个包(package).
     */
    public static void main (String[] args) throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

        File file = new File("src/main/resources/iProfsLogo.png");
        byte[] bytes = convertImageToByteArray(file);
        addImageToPackage(wordMLPackage, bytes);

        wordMLPackage.save(new java.io.File("src/main/files/HelloWord7.docx"));
    }

    /**
     *  Docx4j拥有一个由字节数组创建图片部件的工具方法, 随后将其添加到给定的包中. 为了能将图片添加
     *  到一个段落中, 我们需要将图片转换成内联对象. 这也有一个方法, 方法需要文件名提示, 替换文本,
     *  两个id标识符和一个是嵌入还是链接到的指示作为参数.
     *  一个id用于文档中绘图对象不可见的属性, 另一个id用于图片本身不可见的绘制属性. 最后我们将内联
     *  对象添加到段落中并将段落添加到包的主文档部件.
     *
     *  @param wordMLPackage 要添加图片的包
     *  @param bytes         图片对应的字节数组
     *  @throws Exception    不幸的createImageInline方法抛出一个异常(没有更多具体的异常类型)
     */
    public static void addImageToPackage(WordprocessingMLPackage wordMLPackage,
                                          byte[] bytes) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        int docPrId = 1;
        int cNvPrId = 2;
        Inline inline = imagePart.createImageInline("Filename hint","Alternative text", docPrId, cNvPrId, false);

        P paragraph = addInlineImageToParagraph(inline);

        wordMLPackage.getMainDocumentPart().addObject(paragraph);
    }

    /**
     *  创建一个对象工厂并用它创建一个段落和一个可运行块R.
     *  然后将可运行块添加到段落中. 接下来创建一个图画并将其添加到可运行块R中. 最后我们将内联
     *  对象添加到图画中并返回段落对象.
     *
     * @param   inline 包含图片的内联对象.
     * @return  包含图片的段落
     */
    public static P addInlineImageToParagraph(Inline inline) {
        // 添加内联对象到一个段落中
        ObjectFactory factory = new ObjectFactory();
        P paragraph = factory.createP();
        R run = factory.createR();
        paragraph.getContent().add(run);
        Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return paragraph;
    }

    /**
     * 将图片从文件对象转换成字节数组.
     *
     * @param file  将要转换的文件
     * @return      包含图片字节数据的字节数组
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] convertImageToByteArray(File file)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file );
        long length = file.length();
        // 不能使用long类型创建数组, 需要用int类型.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // 确认所有的字节都没读取
        if (offset < bytes.length) {
            System.out.println("Could not completely read file " +file.getName());
        }
        is.close();
        return bytes;
    }

    private static final ObjectFactory factory = Context.getWmlObjectFactory();

    /**
     * 创建一段文本
     *
     * @param text
     * @return
     */
    public static P newText(String text){
        P p = factory.createP();
        R r = factory.createR();
        Text t = new Text();
        t.setValue(text);
        r.getContent().add(t);
        p.getContent().add(r);
        return p;
    }

    /**
     * 创建包含图片的内容
     *
     * @param word
     * @param sourcePart
     * @param imageFilePath
     * @return
     * @throws Exception
     */
    public static P newImage(WordprocessingMLPackage word,
                             Part sourcePart,
                             String imageFilePath) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
                .createImagePart(word, sourcePart, new File(imageFilePath));
        //随机数ID
        int id = (int) (Math.random() * 10000);
        //这里的id不重复即可
        Inline inline = imagePart.createImageInline("image", "image", id, id * 2, false);

        Drawing drawing = factory.createDrawing();
        drawing.getAnchorOrInline().add(inline);

        R r = factory.createR();
        r.getContent().add(drawing);

        P p = factory.createP();
        p.getContent().add(r);

        return p;
    }

    /**
     * 创建页眉
     *
     * @param word
     * @return
     * @throws Exception
     */
    public static HeaderPart createHeader(
            WordprocessingMLPackage word) throws Exception {
        HeaderPart headerPart = new HeaderPart();
        Relationship rel = word.getMainDocumentPart().addTargetPart(headerPart);
        createHeaderReference(word, rel);
        return headerPart;
    }

    /**
     * 创建页眉引用关系
     *
     * @param word
     * @param relationship
     * @throws InvalidFormatException
     */
    public static void createHeaderReference(
            WordprocessingMLPackage word,
            Relationship relationship )
            throws InvalidFormatException {
        List<SectionWrapper> sections = word.getDocumentModel().getSections();

        SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
        // There is always a section wrapper, but it might not contain a sectPr
        if (sectPr==null ) {
            sectPr = factory.createSectPr();
            word.getMainDocumentPart().addObject(sectPr);
            sections.get(sections.size() - 1).setSectPr(sectPr);
        }
        HeaderReference headerReference = factory.createHeaderReference();
        headerReference.setId(relationship.getId());
        headerReference.setType(HdrFtrRef.DEFAULT);
        sectPr.getEGHdrFtrReferences().add(headerReference);
    }

    /**
     * 创建页脚
     *
     * @param word
     * @return
     * @throws Exception
     */
    public static FooterPart createFooter(WordprocessingMLPackage word) throws Exception {
        FooterPart footerPart = new FooterPart();
        Relationship rel = word.getMainDocumentPart().addTargetPart(footerPart);
        createFooterReference(word, rel);
        return footerPart;
    }

    /**
     * 创建页脚引用关系
     *
     * @param word
     * @param relationship
     * @throws InvalidFormatException
     */
    public static void createFooterReference(
            WordprocessingMLPackage word,
            Relationship relationship )
            throws InvalidFormatException {
        List<SectionWrapper> sections = word.getDocumentModel().getSections();

        SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
        // There is always a section wrapper, but it might not contain a sectPr
        if (sectPr==null ) {
            sectPr = factory.createSectPr();
            word.getMainDocumentPart().addObject(sectPr);
            sections.get(sections.size() - 1).setSectPr(sectPr);
        }
        FooterReference footerReference = factory.createFooterReference();
        footerReference.setId(relationship.getId());
        footerReference.setType(HdrFtrRef.DEFAULT);
        sectPr.getEGHdrFtrReferences().add(footerReference);
    }
}
