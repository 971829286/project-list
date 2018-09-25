package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.Activity;
import cn.ourwill.huiyizhan.entity.Address;
import cn.ourwill.huiyizhan.entity.TicketsRecord;
import cn.ourwill.huiyizhan.entity.TrxOrder;
import cn.ourwill.huiyizhan.service.IActivityService;
import cn.ourwill.huiyizhan.service.ITicketsRecordService;
import cn.ourwill.huiyizhan.service.ITrxOrderService;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.utils.ImgUtil;
import cn.ourwill.huiyizhan.utils.JsonUtil;
import cn.ourwill.huiyizhan.utils.TimeUtils;
import cn.ourwill.huiyizhan.utils.pdf.FreeMarkToHtml;
import cn.ourwill.huiyizhan.utils.pdf.XHtml2Pdf;
import cn.ourwill.huiyizhan.utils.ticket.TicketImage;
import cn.ourwill.huiyizhan.utils.ticket.TicketPDF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.ourwill.huiyizhan.entity.Config.systemDomain;

/**
 * 生成PDF票
 */
@Service
@Slf4j
public class GenerateTicketService {
    @Autowired
    private ITicketsRecordService ticketsRecordService;
    @Autowired
    private IActivityService      activityService;

    @Value("${EmailAndPdfPath}")
    private String PATH;

    /**
     * 静态资源目录
     */
    private static String resourcePath;

    /**
     * PDF模板路径
     */
    private static String inputPDFName;

    static {

        inputPDFName = "PDFTemplate.ftl";
    }

    private IUserService userService;

    /**
     * 生成一张PDF票,并存储到数据库中,返回路径
     *
     * @param ticketsRecords
     * @return
     */
    public String getTicketPDF(List<TicketsRecord> ticketsRecords, Boolean isSaveToDB) {
        try {
            if (ticketsRecords == null || ticketsRecords.size() == 0) {
                log.info("GenerateTicketService.getTicketPDF:传入记录为空,无法生成票据");
                return null;
            }
            String ticketImage;
            List<String> fileNames;
            //用于拼接文件名
            String today = TimeUtils.calDate(0) + "/";

            //拼接子文件夹路径
            String dir = PATH + "pdf/" + today;
            File file = new File(dir);
            if (!file.exists()) {
                file.setWritable(true);
                file.setReadable(true);
                file.mkdirs();
            }
            //拼接文件名 目录+签到码+随机数
            //如果是单张票 不拼接
            if (ticketsRecords.size() == 1) {
                String rand = Math.abs(new Random().nextInt()) + "_signal";
                String ticketPdfName = dir + rand + ".pdf";
                ticketImage = generateTicketImg(ticketsRecords.get(0));
                if(ticketImage == null){
                    log.info("GenerateTicketService.getTicketPDF:该记录的png图片生成失败,无法生成pdf票据");
                    return null;
                }
                boolean flag = TicketPDF.getSinglePDF(ticketImage, ticketPdfName);
                if (flag) {
                    if (isSaveToDB) {
                        ticketsRecordService.updateTicketLink(ticketPdfName, ticketsRecords.get(0).getId());
                    }
                    return ticketPdfName;
                } else {
                    log.info("GenerateTicketService.getTicketPDF:生成PDF票据失败");
                    return null;
                }
            } else {//多张票
                fileNames = new ArrayList<>();
                for (int i = 0; i < ticketsRecords.size(); i++) {
                    String rand = Math.abs(new Random().nextInt()) + "";
                    String ticketPdfName = dir + rand + ".pdf";

                    String fileImageName = generateTicketImg(ticketsRecords.get(i));
                    if (fileImageName == null) {
                        continue;
                    } else {
                        TicketPDF.getSinglePDF(fileImageName, ticketPdfName);
                        fileNames.add(ticketPdfName);
                    }
                }
                String rand = Math.abs(new Random().nextInt()) + "_merge";
                String ticketMergePdf = dir + rand + ".pdf";
                boolean flag = TicketPDF.getLongPdf(fileNames, ticketMergePdf);
                if (flag) {
                    return ticketMergePdf;
                } else {
                    log.info("GenerateTicketService.getTicketPDF:拼接pdf票据失败");
                    return null;
                }
            }
        } catch (Exception e) {
            log.info("generateTicketPDF", e);
            return null;
        }
    }

    public String generateEmailContent(HashMap map, String fileName, String type) {
        try {
            resourcePath = ResourceUtils.getURL("classpath:").getPath() + "emailAndPdf/";
            String today = TimeUtils.calDate(0) + "/";
            //拼接子文件夹路径
            String dir = PATH + "html/" + today;
            //拼接文件名规则
            String rand = Math.abs(new Random().nextInt()) + "_rand";
            String tempId = type == null ? rand : type + rand;

            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                fileDir.setReadable(true);
                fileDir.setWritable(true);
                fileDir.mkdirs();
            }
            //拼接文件名 目录+id+时间+随机数+后缀
            String fileEmailName = dir + tempId + ".html";
            //开始转换Email模板->HTML
            FreeMarkToHtml.freeMarkToHtml(resourcePath, fileName, map, fileEmailName);
            return fileEmailName;
        } catch (Exception e) {
            log.info("GenerateTicketService.generateEmailContent", e);
            return null;
        }
    }

//    //生成一张长票
//    public String getLongTicketPDF(List<TicketsRecord> ticketsRecords, HashMap map, TrxOrder trxOrder) {
//        if (ticketsRecords == null || ticketsRecords.size() == 0) {
//            return null;
//        }
//        try {
//            List<String> fileNames = new ArrayList<>();
//            for (int i = 0; i < ticketsRecords.size(); i++) {
//                String fileName = generateTicketImg(ticketsRecords.get(i));
//                if (fileName == null) {
//                    continue;
//                } else {
//                    fileNames.add(fileName);
//                }
//            }
//            resourcePath = ResourceUtils.getURL("classpath:").getPath() + "emailAndPdf/";
//            String today = TimeUtils.calDate(0) + "/";
//
//            //拼接子文件夹路径
//            String dir = PATH + "pdf/" + today;
//            //拼接文件名规则
//            String rand = Math.abs(new Random().nextInt()) + "_rand";
//            String tempId = trxOrder.getId() == null ? rand : trxOrder.getId() + "";
//
//            String filePDFName = dir + tempId;
//
//            TicketPDF.getLongPdf(fileNames, filePDFName + ".pdf");
//            return filePDFName + ".pdf";
//        } catch (Exception e) {
//            log.info("GenerateTicketService.generateLongTicketPDF", e);
//            return null;
//        }
//    }

    /**
     * 生成一张[图片]票
     *
     * @param ticketsRecord
     * @return
     */
    public String generateTicketImg(TicketsRecord ticketsRecord) {
        if (ticketsRecord == null) {
            log.info("GenerateTicketService.generateTicketImg:传入记录为空,无法生成票据");
            return null;
        }
        Activity activity = ticketsRecord.getActivity();
        if (activity == null) {
            activity = activityService.getById(ticketsRecord.getActivityId());
            if (activity == null) {
                log.info("GenerateTicketService.generateTicketImg:该会议不存在,无法生成票据的Png图片");
                return null;
            }
        }
        //初始化字段
        TicketImage imageTicket = new TicketImage();
        BufferedImage imageBuffer = imageTicket.loadImageLocal();
        String address;
        if(1 == activity.getIsOnline()){
//            address =  systemDomain+"web/activity/"+activity.getId();
            address = "线上活动";
        }else{
            address = JsonUtil.fromJson(activity.getActivityAddress(), Address.class).toString();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(activity.getStartTime());
        //传入参数
        imageTicket.addPerson(imageBuffer, ticketsRecord.getConfereeName());
        imageTicket.addTime(imageBuffer, date);
        imageTicket.addTitle(imageBuffer, activity.getActivityTitle());
        imageTicket.addTicketName(imageBuffer, ticketsRecord.getTicketsName());
        imageTicket.addAddress(imageBuffer, address);
        imageTicket.addQRCode(imageBuffer, ticketsRecord.getAuthCode(), ticketsRecord.getSignCode() + "");

        //用于拼接文件名

        String today = TimeUtils.calDate(0) + "/";
        String rand = Math.abs(new Random().nextInt()) + "_signal";
        String signCode = ticketsRecord.getSignCode() + "_";


        //拼接子文件夹路径
        String dir = PATH + "img/" + today;
        File file = new File(dir);
        if (!file.exists()) {
            file.setWritable(true);
            file.setReadable(true);
            file.mkdirs();
        }
        //拼接文件名 目录+签到码+随机数+
        String ticketImgName = dir + signCode + rand;
        imageTicket.writeImageLocal(ticketImgName + ".png", imageBuffer);
        return ticketImgName + ".png";
    }
}
