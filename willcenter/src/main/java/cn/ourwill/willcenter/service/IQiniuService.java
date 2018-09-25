package cn.ourwill.willcenter.service;

import com.qiniu.common.QiniuException;
import com.qiniu.util.StringMap;

import java.io.InputStream;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2017/11/9 0009 9:36
 * @Description
 */
public interface IQiniuService {
    String getUpToken();

    String getUpTokenReplace(String key);

    String upload(InputStream is, String uploadDir, StringMap params, String mime);

    String replaceUpload(InputStream is, String uploadDir, StringMap params, String mime);

    String dataPersistence(Integer activityId, String path) throws QiniuException;

    Boolean delete(String path);

    List<Boolean> delete(List<String> paths);

    String dataPersistenceForReplace(Integer activityId, Integer albumId, String photoPath);

    Boolean reName(String oldPath, String oldPathCopy);
}
