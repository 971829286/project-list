package com.souche.bulbous.service;

import com.google.common.collect.Lists;
import com.souche.bulbous.api.UserService;
import com.souche.bulbous.bean.Job;
import com.souche.bulbous.dto.ProgressDto;
import com.souche.bulbous.excel.ExcelConstant;
import com.souche.bulbous.excel.ExcelGenerate;
import com.souche.bulbous.vo.UserManagerBean;
import com.souche.optimus.cache.Memcached;
import com.souche.optimus.common.upload.FileStorageRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 用户服务
 *
 * @author XadomGreen
 * @since 2018-08-21
 */
@Service
public class BulbousUserService {

    @Resource
    private Memcached cacheService;
    @Resource
    private FileStorageRepository fileStorageRepository;
    @Resource
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(BulbousQuoteService.class);

    public ProgressDto exportExcel(String userPhone, String nickName, String beginDate, String endDate) {

        int totalCount = userService.getUserManagerListCount(userPhone, nickName, beginDate, endDate);
        final Job job = new Job();
        job.setTotalCount(totalCount);
        cacheService.setObject(ExcelConstant.EXPROT_JOB_ + job.getId(), job, ExcelConstant.EXPRIE_SECOND);
        ExcelConstant.EXECUTOR_POOL.execute(new Runnable() {
            @Override
            public void run() {
                int totalPage = totalCount / ExcelConstant.PAGE_SIZE;
                if (totalCount % ExcelConstant.PAGE_SIZE != 0) {
                    totalPage++;
                }

                // TODO
                List<String> titleList = getTitle(UserManagerBean.class);
                XSSFWorkbook xssfWorkbook = ExcelGenerate.generate(null, titleList);

                // TODO

                for (int i = 0; i < totalPage; i++) {
                    List<UserManagerBean> queryCarList =
                            userService.getUserManagerList(userPhone, nickName, beginDate, endDate, i + 1, ExcelConstant.PAGE_SIZE);
                    List<List<Object>> content = getContent(queryCarList);
                    ExcelGenerate.append(xssfWorkbook, content);

                    job.addNowCount(queryCarList.size());
                    cacheService.setObject(ExcelConstant.EXPROT_JOB_ + job.getId(), job, ExcelConstant.EXPRIE_SECOND);
                }
                ExcelGenerate.beautify(xssfWorkbook);

                try

                {
                    String fileName = getFileName("车牛小程序-用户列表_", job.getId());
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

    private String getFileName(String prefix, String id) {
        StringBuffer fileName = new StringBuffer();
        fileName.append(prefix).append(id).append(".xlsx");
        return fileName.toString();
    }

    public ProgressDto getProgressByJobId(String jobId) {
        return ExcelGenerate.getProgressByJobId(jobId, this.cacheService);
    }

    protected List<List<Object>> getContent(List<UserManagerBean> queryCarList) {
        List<List<Object>> result = Lists.newArrayList();
        for (UserManagerBean userManagerBean : queryCarList) {

            List<Object> objects = Lists.newArrayList();
            objects.add(userManagerBean.getNickName());
            objects.add(userManagerBean.getPhone());
            objects.add(userManagerBean.getDateCreate());
            objects.add(userManagerBean.getSellNum() + "次");
            objects.add(userManagerBean.getQuoteCount() + "次");

            result.add(objects);
        }
        return result;
    }

    protected List<String> getTitle(Class cla) {
        return ExcelGenerate.getTitle(cla);
    }
}
