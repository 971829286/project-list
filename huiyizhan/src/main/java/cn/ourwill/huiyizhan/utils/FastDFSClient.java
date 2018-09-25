package cn.ourwill.huiyizhan.utils;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

/**
 * 描述：
 *
 * @author zhaoqing
 * @create 2018-05-31 19:31
 **/
public class FastDFSClient {

    private static TrackerClient trackerClient ;
    private static TrackerServer trackerServer ;
    private static StorageServer storageServer ;
    private static StorageClient1 storageClient ;

    //private static StorageClient storageClientTEST ;

    public FastDFSClient() throws Exception {

//        if (conf.contains("classpath:")) {
//            String url = this.getClass().getResource("/").getPath();
//            url = url.substring(1);
//            conf = conf.replace("classpath:", url);
//        }
        String conf = "fastDFS_config.properties";
        ClientGlobal.initByProperties(conf);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageServer = null;
    }

    /**
     *
     * @param fileName 文件全路径
     * @param extName 文件扩展名，不包含（.）
     * @param metas 文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.upload_file1(fileName, extName, metas);
    }
    public String uploadFile(String fileName, String extName) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.upload_file1(fileName, extName, null);
    }

    public String uploadFile(String fileName) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.upload_file1(fileName, null, null);
    }

    /**
     *
     * @param fileContent 文件的内容，字节数组
     * @param extName 文件扩展名
     * @param metas 文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.upload_file1(fileContent, extName, metas);
    }

    /**
     *
     * @param group_name
     * @param fileContent
     * @param extName
     * @return
     * @throws Exception
     */
    public String uploadFile(String group_name, byte[] fileContent, String extName) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.upload_file1(group_name,fileContent, extName, null);
    }

    /**
     *
     * @param fileContent 文件的内容，字节数组
     * @param extName 文件扩展名
     * @return
     * @throws Exception
     */
    public String uploadFile(byte[] fileContent, String extName) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.upload_file1(fileContent, extName, null);
    }

    public String uploadFile(byte[] fileContent) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.upload_file1(fileContent, null, null);
    }

    /**
     * 获取文件信息
     * @param file_id
     * @return
     * @throws Exception
     */
    public static FileInfo getFile(String file_id ) throws Exception {
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.get_file_info1(file_id);
    }

    public Integer deleteFile(String file_id)throws Exception{
        storageClient = new StorageClient1(trackerServer, storageServer);
        return storageClient.delete_file1(file_id);
    }

}


