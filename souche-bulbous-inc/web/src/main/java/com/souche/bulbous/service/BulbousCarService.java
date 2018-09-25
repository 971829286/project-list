package com.souche.bulbous.service;

import com.google.common.collect.Lists;
import com.souche.bulbous.api.CarService;
import com.souche.bulbous.bean.Job;
import com.souche.bulbous.dto.ProgressDto;
import com.souche.bulbous.excel.ExcelConstant;
import com.souche.bulbous.excel.ExcelGenerate;
import com.souche.bulbous.exception.CustomException;
import com.souche.bulbous.vo.CarStatisticBean;
import com.souche.optimus.cache.Memcached;
import com.souche.optimus.common.upload.FileStorageRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 车辆服务
 *
 * @author XadomGreen
 * @since 2018-08-20
 */

@Service
public class BulbousCarService {

    private Logger logger = LoggerFactory.getLogger(BulbousCarService.class);

    @Resource
    private Memcached cacheService;
    @Resource
    private FileStorageRepository fileStorageRepository;
    @Resource
    private CarService carServciceImpl;

    public ProgressDto exportExcel(String userPhone, String nickName, String modeName,
                                   String beginDate, String endDate, Integer page, Integer pageSize) {

        // 查询总页数 TODO
        int totalCount = carServciceImpl.getCarListCount(userPhone, nickName, modeName, beginDate, endDate);
        /*if (totalCount == 0) {
            throw new CustomException("没有数据可以导出");
        }*/

        final Job job = new Job();
        job.setTotalCount(totalCount);
        cacheService.setObject(ExcelConstant.EXPROT_JOB_ + job.getId(), job, ExcelConstant.EXPRIE_SECOND);
        ExcelConstant.EXECUTOR_POOL.execute(new Runnable() {
            @Override
            public void run() {
                // 计算总页数
                int totalPage = totalCount / ExcelConstant.PAGE_SIZE;
                if (totalCount % ExcelConstant.PAGE_SIZE != 0) {
                    totalPage++;
                }

                // 获取标题列表
                List<String> titleList = getTitle(CarStatisticBean.class);
                XSSFWorkbook xssfWorkbook = ExcelGenerate.generate(null, titleList);

                // 按照每50页一个SHEET生成Excel文档
                for (int i = 0; i < totalPage; i++) {
                    List<CarStatisticBean> list = carServciceImpl.getCatList(userPhone, nickName, modeName, beginDate, endDate, i + 1, ExcelConstant.PAGE_SIZE);
                    List<List<Object>> content = getContent(list);
                    ExcelGenerate.append(xssfWorkbook, content);

                    // 保存导出进度到缓存中
                    job.addNowCount(list.size());
                    cacheService.setObject(ExcelConstant.EXPROT_JOB_ + job.getId(), job, ExcelConstant.EXPRIE_SECOND);
                }
                ExcelGenerate.beautify(xssfWorkbook);

                try

                {
                    // 导出文件到文件流
                    String fileName = getFileName("车牛小程序-车辆列表_", job.getId());
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    xssfWorkbook.write(os);
                    os.close();
                    xssfWorkbook.close();
                    fileStorageRepository.put(fileName, os.toByteArray(), null);
                    job.setStatus(Job.StatusEnum.over.name());
                    job.setUrl(ExcelConstant.RESOURCE_HEAD + fileName);
                    cacheService.setObject(ExcelConstant.EXPROT_JOB_ + job.getId(), job, ExcelConstant.EXPRIE_SECOND);
                } catch (Exception e) {
                    logger.error("导出表格异常:{}", e.getMessage(), e);
                }
            }
        });
        return new ProgressDto(job);
    }

    /**
     * 获取文件名
     *
     * @param prefix
     * @param id
     * @return
     */
    private String getFileName(String prefix, String id) {
        StringBuffer fileName = new StringBuffer();
        fileName.append(prefix).append(id).append(".xlsx");
        return fileName.toString();
    }

    /**
     * 获取任务进度
     *
     * @param jobId
     * @return
     */
    public ProgressDto getProgressByJobId(String jobId) {
        return ExcelGenerate.getProgressByJobId(jobId, this.cacheService);
    }

    /**
     * 文档内容处理
     *
     * @param queryCarList
     * @return
     */
    protected List<List<Object>> getContent(List<CarStatisticBean> queryCarList) {
        List<List<Object>> result = Lists.newArrayList();
        for (CarStatisticBean CarStatisticBean : queryCarList) {

            List<Object> objects = Lists.newArrayList();

            if (StringUtils.isNotBlank(CarStatisticBean.getCarId())) {
                objects.add(CarStatisticBean.getCarId());
            }

            objects.add(CarStatisticBean.getNickName());

            objects.add(CarStatisticBean.getUserPhone());
            objects.add(CarStatisticBean.getModeName());
            objects.add(CarStatisticBean.getCarPrice() + "万元");
            objects.add(CarStatisticBean.getDateSell());
            objects.add(CarStatisticBean.getStatus());
            objects.add(CarStatisticBean.getQuoteCount());

            result.add(objects);
        }
        return result;
    }

    /**
     * 获取文档标题，扫描ApiModelProperty注解
     *
     * @param cla
     * @return
     */
    protected List<String> getTitle(Class cla) {
        return ExcelGenerate.getTitle(cla);
    }
}

