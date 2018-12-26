package com.souche.bmgateway.core.service.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.souche.bmgateway.core.domain.BillFlow;
import com.souche.bmgateway.core.domain.ShopInfo;
import com.souche.bmgateway.core.dto.mq.BillChargeInfo;
import com.souche.bmgateway.core.dto.sftp.AllinPayBillSftpConfig;
import com.souche.bmgateway.core.enums.BillTypeEnums;
import com.souche.bmgateway.core.enums.FilePathEnums;
import com.souche.bmgateway.core.exception.DaoException;
import com.souche.bmgateway.core.repo.BillFlowRepository;
import com.souche.bmgateway.core.repo.BillSummaryRepository;
import com.souche.bmgateway.core.repo.ShopInfoRepository;
import com.souche.bmgateway.core.service.bill.builder.BillFlowBuilder;
import com.souche.bmgateway.core.service.bill.builder.ShopInfoBuilder;
import com.souche.bmgateway.core.service.bill.service.AllinPayBillHandleService;
import com.souche.bmgateway.core.util.*;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.upload.FileStorageRepository;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.mq.aliyunons.ONSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaojian
 * @date 18/7/13
 */

@Slf4j(topic = "service")
@Service
public class AllinPayBillHandleServiceImpl implements AllinPayBillHandleService, InitializingBean {

    private final static String DETAIL_MARK = "DZD";

    @Autowired
    private BillFlowRepository billFlowRepository;

    @Autowired
    private AllinPayBillSftpConfig allinPayBillSftpConfig;

    @Autowired
    private FilePathUtil filePathUtil;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Resource(name = "billChargeProducer")
    private ONSProducer ssoProducer;

    @Autowired
    private BillSummaryRepository billSummaryRepository;

    private final Integer keepAliveTime = OptimusConfig.getValue("keepAliveTime", Integer.class, 60);

    private final Integer corePoolSize = OptimusConfig.getValue("corePoolSize", Integer.class, 5);

    private Integer maximumPoolSize = OptimusConfig.getValue("maximumPoolSize", Integer.class, 10);

    private String fileNameRegex = "wsyhghqcdate\\d{2}\\.zip$";

    private final String notifyTag = "charge-notify";

    public final String DEFAULT_BUCKET_NAME = OptimusConfig.getValue("aliyun.oss.defaultBucketName", "souche-devqa");

    private ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Resource(name = "fileStorageRepository")
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private ShopInfoRepository shopInfoRepository;

    @Override
    public void handle(String bankDate) {
        log.info("开始处理通联代发对账单文件,bankDate:{}", bankDate);

        List<Object> cleanList = Lists.newArrayList();

        //自动拼接昨天的日期
        if (StringUtils.isBlank(bankDate)) {
            Date yesterday = DateUtils.addDays(new Date(), -1);
            bankDate = DateFormatUtils.format(yesterday, "yyyyMMdd");
        }
        SftpUtil sftpUtil = new SftpUtil(allinPayBillSftpConfig.getUsername(), allinPayBillSftpConfig.getPassword(), allinPayBillSftpConfig.getIp(), allinPayBillSftpConfig.getPort());
        sftpUtil.login();
        try {

            List<SftpUtil.DownLoadFileInfo> downLoadFileInfoList = sftpUtil.downloadByFileNameRegex(allinPayBillSftpConfig.getDirectory(),
                    fileNameRegex.replace("date", bankDate));

            if (CollectionUtils.isNotEmpty(downLoadFileInfoList)) {

                //1. 从sftp服务器下载文件
                File file = new File(filePathUtil.getRelativeFilePath(FilePathEnums.TEMP, downLoadFileInfoList.get(0).getLsEntry().getFilename()));
                OutputStream outputStream = new FileOutputStream(file);
                IOUtils.write(downLoadFileInfoList.get(0).getFileData()
                        , outputStream);


//                file = new File("/Users/dasouche/Documents/download/wsyhdsctm2018062001.zip");

                fileStorageRepository.put(allinPayBillSftpConfig.getSavePath() + downLoadFileInfoList.get(0).getLsEntry().getFilename(),
                        downLoadFileInfoList.get(0).getFileData(), DEFAULT_BUCKET_NAME);

                //2.解压文件
                List<String> saveFilePathList = unZip(file.getAbsolutePath(), "");
                if (CollectionUtils.isNotEmpty(saveFilePathList)) {
                    for (String saveFilePath : saveFilePathList) {
                        if (StringUtils.isNotBlank(saveFilePath) && StringUtils.contains(StringUtils.stripEnd(saveFilePath, File.separator), DETAIL_MARK)) {
                            List<String[]> dataArray = CsvUtil.readCsv(saveFilePath, true);
                            saveAllinPayDetail(dataArray, bankDate);
                            billSummaryRepository.queryAndSaveBillSummary(bankDate, BillTypeEnums.Grand.getCode());
                        } else if ((StringUtils.isNotBlank(saveFilePath) && StringUtils.contains(StringUtils.stripEnd(saveFilePath, File.separator), "JSD"))) {
                            log.info("开始解析通联代发汇总数据,saveFilePath:{}", saveFilePath);
                            List<String[]> dadaArray = CsvUtil.readCsv(saveFilePath, true);
                            saveAllinPayShopInfo(dadaArray, bankDate);
                        }
                    }
                }


                addCleanList(cleanList, sftpUtil, file, outputStream);
            }else {
                log.error("未解析到对账单,请联系通联上传,businessDate : {}", bankDate);
                DingTalkClient.sendMsg("未解析到对账单,请联系通联上传" );
            }

        } catch (Exception e) {
            log.error("从网商下载通联代发对账单失败,e:{}", e);
            DingTalkClient.sendMsg("从网商下载通联代发对账单失败,,bankDate:" + bankDate);
        } finally {
            clean(cleanList);
        }

    }

    private void saveAllinPayShopInfo(List<String[]> dadaArray, String bankDate) {
        List<ShopInfo> shopInfoList = ShopInfoBuilder.buildAllinPayShopInfo(dadaArray);
        for (ShopInfo shopInfo : shopInfoList) {
            shopInfo.setBusinessDate(bankDate);
            shopInfo.setBankCode("323331000001");
            shopInfo.setBankName("浙江网商银行股份有限公司");
            shopInfoRepository.save(shopInfo);
        }
    }

    private void addCleanList(List<Object> cleanList, SftpUtil sftpUtil, File file, OutputStream outputStream) {
        cleanList.add(file);
        cleanList.add(new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("."))));
        cleanList.add(sftpUtil);
        cleanList.add(outputStream);
    }


    private void clean(List<Object> cleanList) {
        if (CollectionUtils.isNotEmpty(cleanList)) {
            for (Object object : cleanList) {
                if (object instanceof SftpUtil) {
                    ((SftpUtil) object).logout();
                }
                if (object instanceof File) {
                    FileUtils.deleteQuietly((File) object);
                }
                if (object instanceof OutputStream) {
                    IOUtils.closeQuietly((OutputStream) object);
                }
            }
        }
    }

    /**
     * 保存明细
     *
     * @param dataArray
     */
    private void saveAllinPayDetail(List<String[]> dataArray, String bankDate) throws DaoException {
        log.info("保存通联代发对账单数据明细,数量:{}", dataArray.size());
        if (CollectionUtils.isNotEmpty(dataArray)) {
            List<BillFlow> billFlowList = BillFlowBuilder.builderAllinPayBillFlowList(dataArray);
            for (BillFlow billFlow : billFlowList) {
                if (!StringUtils.equals(billFlow.getBusinessDate(), bankDate)) {
                    DingTalkClient.sendMsg("通联代发对账单文件名称日期与文件内容日期不一致,bankDate:" + bankDate + ",billFlow:" + ToStringBuilder.reflectionToString(billFlow));
                }
                BillFlow query = new BillFlow();
                query.setBusinessDate(billFlow.getBusinessDate());
                query.setSerialNo(billFlow.getSerialNo());
                query.setBizType(billFlow.getBizType());
                BillFlow result = billFlowRepository.selectSelective(query);
                if (Objects.isNull(result)) {
                    billFlowRepository.insertSelective(billFlow);
                }

            }
            notifyCharge(billFlowList);
        }
        log.info("保存通联代发对账单数据明细完成,数量:{}", dataArray.size());
    }

    private void notifyCharge(List<BillFlow> billFlowList) {
        for (BillFlow billFlow : billFlowList) {
            executorService.execute(
                    () -> send(new BillChargeInfo(billFlow.getOrderCode(),
                            billFlow.getFee().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString()))
            );
        }
    }

    private void send(BillChargeInfo info) {
        Map<String, Object> map = Maps.newHashMap();
        String msgKey = UUIDUtil.getID();
        map.put("data", JSONObject.toJSONString(info));
        log.info("send charge notify key={}, data={}", msgKey, map);
        ssoProducer.send(map, msgKey, notifyTag);
    }


    /**
     * 解压文件并返回需要合并的文件
     *
     * @param zipFilepath
     * @param destDir
     * @return
     */
    private List<String> unZip(String zipFilepath, String destDir) throws Exception {
        log.info("开始解压文件,zipFilepath:{},destDir:{}", zipFilepath, destDir);
        List<String> result = Lists.newArrayListWithCapacity(2);

        ZipUtil.unZip(new File(zipFilepath), "");

        File file = new File(StringUtils.substring(zipFilepath, 0, zipFilepath.length() - 4));

        if (file.exists() && file.isDirectory()) {
            String[] fileNameList = file.list();
            if (CollectionUtils.isNotEmpty(Collections.singleton(fileNameList))) {
                for (String fileName : Objects.requireNonNull(fileNameList)) {
                    result.add(file.getAbsolutePath() + File.separator + fileName);
                }
            }
        }

        result.sort(Comparator.reverseOrder());

        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                    new ThreadPoolExecutor.AbortPolicy());
        }
    }
}
