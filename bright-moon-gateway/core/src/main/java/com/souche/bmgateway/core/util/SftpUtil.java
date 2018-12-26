package com.souche.bmgateway.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jcraft.jsch.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 类说明 sftp工具类
 *
 * @author zhaojian
 */
@Slf4j
@NoArgsConstructor
public class SftpUtil {

    private ChannelSftp sftp;

    /**
     * 会话
     */
    private Session session;

    /**
     * SFTP 登录用户名
     */
    private String username;

    /**
     * SFTP 登录密码
     */
    private String password;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * SFTP 服务器地址IP地址
     */
    private String host;

    /**
     * SFTP 端口
     */
    private int port;


    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public class DownLoadFileInfo {
        private byte[] fileData;
        private ChannelSftp.LsEntry lsEntry;
    }

    /**
     * 构造基于密码认证的sftp对象
     */
    public SftpUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     */
    public SftpUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    /**
     * 构建sftp对象
     */
    public SftpUtil(String username, String password, String host, int port, String privateKey) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    /**
     * 连接sftp服务器
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            if (StringUtils.hasText(privateKey)) {
                // 设置私钥
                jsch.addIdentity(privateKey);
            }

            session = jsch.getSession(username, host, port);

            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error("连接SFTP服务器失败,配置信息:{},异常:{}", this, e);
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     *
     * @param basePath     服务器的基础路径
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     */
    public void upload(String basePath, String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(basePath);
            sftp.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String[] dirs = directory.split(File.separator);
            String tempPath = basePath;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) {
                    continue;
                }
                tempPath += File.separator + dir;
                try {
                    sftp.cd(tempPath);
                } catch (SftpException ex) {
                    sftp.mkdir(tempPath);
                    sftp.cd(tempPath);
                }
            }
        }
        //上传文件
        sftp.put(input, sftpFileName);
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (StringUtils.hasText(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        log.info("开始从SFTP下载文件,目录:{},待下载的文件名称:{}", directory, downloadFile);
        if (StringUtils.hasText(directory)) {
            sftp.cd(directory);
        }
        InputStream inputStream = sftp.get(downloadFile);
        return IOUtils.toByteArray(inputStream);
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        log.info("开始从SFTP删除文件,目录:{},待删除的文件名称:{}", directory, deleteFile);
        sftp.cd(directory);
        sftp.rm(deleteFile);
        log.info("从SFTP删除文件完成,目录:{},待删除的文件名称:{}", directory, deleteFile);
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    public Vector<ChannelSftp.LsEntry> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }


    /**
     * 根据文件名称正则表达式下载目录下的文件
     *
     * @param directory
     * @param fileNameRegex
     * @return
     * @throws SftpException
     * @throws IOException
     */
    public List<DownLoadFileInfo> downloadByFileNameRegex(String directory, String fileNameRegex) throws SftpException, IOException {
        Map<ChannelSftp.LsEntry, String> lsEntryStringHashMap = Maps.newHashMap();
        List<DownLoadFileInfo> downLoadFileInfoList = Lists.newArrayList();
        listFiles(directory, lsEntryStringHashMap);
        if (!CollectionUtils.isEmpty(lsEntryStringHashMap)) {
            Set<ChannelSftp.LsEntry> lsEntrySet = lsEntryStringHashMap.keySet();
            Iterator<ChannelSftp.LsEntry> lsEntryIterator = lsEntrySet.iterator();
            while (lsEntryIterator.hasNext()) {
                ChannelSftp.LsEntry lsEntry = lsEntryIterator.next();
                if (Pattern.matches(fileNameRegex, lsEntry.getFilename())) {
                    downLoadFileInfoList.add(new DownLoadFileInfo(download(lsEntryStringHashMap.get(lsEntry), lsEntry.getFilename()), lsEntry));
                }
            }

        }
        return CollectionUtils.isEmpty(downLoadFileInfoList) ? null : downLoadFileInfoList;
    }

    /**
     * 递归遍历出目录下面所有文件
     *
     * @param directory 需要遍历的目录，必须以"/"开始和结束
     * @throws SftpException
     */
    private void listFiles(String directory, Map<ChannelSftp.LsEntry, String> allFiles) throws SftpException {
        if (directory.startsWith(File.separator) && directory.endsWith(File.separator)) {
            //更换目录到当前目录
            sftp.cd(directory);
            Vector<ChannelSftp.LsEntry> lsEntryVector = sftp.ls(directory);
            for (ChannelSftp.LsEntry lsEntry : lsEntryVector) {
                if (lsEntry.getAttrs().isDir()) {
                    if (!".".equals(lsEntry.getFilename()) && !"..".equals(lsEntry.getFilename())) {
                        listFiles(directory + lsEntry.getFilename() + File.separator, allFiles);
                    }
                } else {
                    allFiles.put(lsEntry, directory);
                }
            }
        }
    }

    /**
     * 上传文件测试
     *
     * @param args
     * @throws SftpException
     * @throws IOException
     */
    public static void main(String[] args) throws SftpException, IOException {

        SftpUtil sftp = new SftpUtil("fivmhzulwn", "zfbMrCuD^!hT", "139.224.233.66", 2121);

        try {
            sftp.login();
            List<DownLoadFileInfo> downLoadFileInfoList = sftp.downloadByFileNameRegex("/216610000005847511045/", "trade_\\d+_\\d+\\.csv");
            for (DownLoadFileInfo downLoadFileInfo : downLoadFileInfoList) {
                IOUtils.write(downLoadFileInfo.getFileData(), new FileOutputStream("/Users/dasouche/Documents/download/" + downLoadFileInfo.getLsEntry().getFilename()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftp.logout();
        }
    }
}