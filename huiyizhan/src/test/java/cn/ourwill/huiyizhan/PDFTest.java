package cn.ourwill.huiyizhan;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.junit.experimental.categories.Category;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-15 14:03
 **/
public class PDFTest {

    public static final String DEST = "H:\\email\\2.pdf";
    public static final String IMAGE = "H:\\email\\2.png";


    public static final String MERGE
            = "H:\\email\\merge.pdf";
    public static final String SOURCE
            = "H:\\email\\2.pdf";



    public static void main(String[] args) throws Exception {
        //mergePdf(DEST);
    }


    public static void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PageSize pageSize = new PageSize(400,175);
        Document doc = new Document(pdfDoc, pageSize);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.addImage(ImageDataFactory.create(IMAGE), pageSize, false);
        doc.close();
    }

}
