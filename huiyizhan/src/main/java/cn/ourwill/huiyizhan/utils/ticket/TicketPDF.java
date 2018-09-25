package cn.ourwill.huiyizhan.utils.ticket;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-15 15:23
 **/
@Slf4j
public class TicketPDF {
    /**
     * 根据图片生成单张pdf
     *
     * @param srcImage
     * @param destFileName
     * @return
     */
    public static boolean getSinglePDF(String srcImage, String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            file.delete();
        }
        Document doc = null;
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(destFileName));
            PageSize pageSize = new PageSize(500, 225);
            doc = new Document(pdfDoc, pageSize);
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
            canvas.addImage(ImageDataFactory.create(srcImage), pageSize, false);
            return true;
        } catch (Exception e) {
            log.info("TicketPDF.getSinglePDF", e);
            return false;
        } finally {
            if (doc != null) {
                doc.close();
            }
        }
    }

    /**
     * 将文件夹下多张pdf拼接
     *
     * @param srcFolder
     * @param destFileName
     * @return
     */
    public static boolean getLongPDF(String srcFolder, String destFileName) {
        File src = new File(srcFolder);
        if (!src.exists()) {
            return false;
        }
        String[] fileListName = src.list();
        if (fileListName == null || fileListName.length == 0)
            return false;
        PDFMergerUtility pdfMerge = new PDFMergerUtility();
        pdfMerge.setDestinationFileName(destFileName);
        try {
            for (int i = 0; i < fileListName.length; i++) {
                File pdf = new File(srcFolder + fileListName[i]);
                PDDocument document = PDDocument.load(pdf);
                pdfMerge.addSource(pdf);
                document.close();
            }
            pdfMerge.mergeDocuments(null);
        }catch (Exception e){
            log.info("TicketPDF.getLongPDF",e);
            return false;
        }
        return true;
    }

    public static boolean getLongPdf(List<String> srcFileNames, String destFileName){
        if(srcFileNames == null || srcFileNames.size() == 0){
            return false;
        }
        PDFMergerUtility pdfMerge = new PDFMergerUtility();
        pdfMerge.setDestinationFileName(destFileName);
        try{
            for(int i = 0; i < srcFileNames.size(); i ++){
                File pdf = new File(srcFileNames.get(i));
                PDDocument document = PDDocument.load(pdf);
                pdfMerge.addSource(pdf);
                document.close();
            }
            pdfMerge.mergeDocuments(null);
        }catch (Exception e){
            log.info("TicketPDF.getLongPdf",e);
            return false;
        }
        return true;
    }
}
