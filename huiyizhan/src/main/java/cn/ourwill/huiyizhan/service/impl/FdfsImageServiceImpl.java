package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.FdfsImage;
import cn.ourwill.huiyizhan.mapper.FdfsImageMapper;
import cn.ourwill.huiyizhan.service.IFdfsImageService;
import cn.ourwill.huiyizhan.utils.FastDFSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-06-13 15:37
 **/
@Service
@Slf4j
public class FdfsImageServiceImpl implements IFdfsImageService {

    @Autowired
    FdfsImageMapper fdfsImageMapper;

    @Override
    public Integer save(FdfsImage fdfsImage){
        return fdfsImageMapper.save(fdfsImage);
    }

    @Override
    public Integer insert(String url ){
        return fdfsImageMapper.insert(url);
    }

    @Override
    public Integer update(FdfsImage fdfsImage){
        return fdfsImageMapper.update(fdfsImage);
    }

    /**
     * 图片持久化 置1
     * @param path
     * @return
     */
    @Override
    public Integer dataPersistence (String path){
        FdfsImage fdfsImage = fdfsImageMapper.selectByUrl(path);
        if (fdfsImage==null){
            FdfsImage insertFdfsImage = new FdfsImage();
            insertFdfsImage.setFlag(1);
            insertFdfsImage.setUrl(path);
            insertFdfsImage.setUploadTime(new Date());
            return fdfsImageMapper.save(insertFdfsImage);
        }else if (fdfsImage!=null && fdfsImage.getFlag()==0){
            fdfsImage.setFlag(1);
            return fdfsImageMapper.update(fdfsImage);
        }else if (fdfsImage!=null && fdfsImage.getFlag()==1){
            return 1;
        }else {
            return 0;
        }
    }

    public void delete(String path) throws Exception{
        FdfsImage fdfsImage = fdfsImageMapper.selectByUrl(path);
        String url = fdfsImage.getUrl();
        FastDFSClient client = new FastDFSClient();
        Integer num = client.deleteFile(url);
        if (num>0){
            fdfsImageMapper.deleteFdfsImage(url);
        }
    }

    /**
     * 定时删除不在用图片
     * @throws Exception
     */
    @Scheduled(cron = "0 0 1 ? * MON")
    public void deleteFdfsImage() throws Exception{
        List<FdfsImage> lists =  fdfsImageMapper.selectByFlag(0);
        for (int i=0;i<lists.size();i++) {
            FdfsImage fdfsImage = lists.get(i);
            String url = fdfsImage.getUrl();
            FastDFSClient client= new FastDFSClient();
            Integer num = client.deleteFile(url);
            if (num>0){
                fdfsImageMapper.deleteFdfsImage(url);
            }
        }
    }
}
