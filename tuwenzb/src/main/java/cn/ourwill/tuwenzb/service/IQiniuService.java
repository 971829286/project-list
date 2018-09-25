package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityPhoto;
import com.qiniu.util.StringMap;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    String dataPersistence(Integer activityId, Integer albumId, String path);

    List<String> dataPersistence(Integer activityId, Integer albumId, List<ActivityPhoto> photos);

    List<String> dataPersistenceApp(Integer activityId, Integer albumId, List<ActivityPhoto> photos);

    Boolean delete(String path);

    List<Boolean> delete(List<String> paths);

    void redExif(ActivityPhoto photo);

    void redExif(List<ActivityPhoto> photos);

    List<Integer> movePath(Integer activityId, Integer fAlbumId, Integer tAlbumId, List<ActivityPhoto> photos);

    String dataPersistenceForReplace(Integer activityId, Integer albumId, String photoPath);

    Boolean reName(String oldPath, String oldPathCopy);


    /**
     * <pre>
     *      在七牛上---- 将所要打包下载的图片进行 打包
     * </pre>
     *
     * @param pics             所要打包的图片url,注意url 为全路径
     * @param downLoadFileName 打包的包名（不包括后缀）
     * @return 打包整合信息（是否成功，下载地址）
     */
    String packagePics(List<String> pics, String downLoadFileName);

    /**
     * <pre>
     *      在七牛上---- 将所要打包下载的图片进行 打包
     * </pre>
     *
     * @param pics             所要打包的图片url,注意url 为全路径
     * @param prefix           打包的索引文件 和 打包后的生成文件  路径
     * @param downLoadFileName 打包的包名（不包括后缀）
     * @return 打包整合信息（是否成功，下载地址）
     */
    String packagePics(List<String> pics, String prefix, String downLoadFileName);

    /**
     * <pre>
     *     获取打包状态
     * </pre>
     *
     * @param packageId
     * @return
     */
    Map getPackageStatus(String packageId) throws Exception;

}
