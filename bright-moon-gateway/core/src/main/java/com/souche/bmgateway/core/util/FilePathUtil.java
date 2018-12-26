package com.souche.bmgateway.core.util;

import com.netfinworks.common.lang.FileUtil;
import com.netfinworks.common.lang.StringUtil;
import com.souche.bmgateway.core.enums.FilePathEnums;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * <p>文件路径适配器</p>
 *
 * @author xuxizun
 */
@Service
public class FilePathUtil implements SystemConstant {
    /**
     * 默认文件根目录，用CLASSPATH的绝对路径作为默认根目录
     */
    private static final String DEFAULT_ROOT_PATH = FilePathUtil.class.getResource("/")
            .getPath();
    /**
     * 名称分隔符
     */
    public static final String NAME_SPLIT = "_";
    /**
     * 文件根目录，由参数配置注入
     */
    private String fileRootPath = DEFAULT_ROOT_PATH;
    /**
     * 配置根目录
     */
    private String configRootPath;

    /**
     * 默认构造
     */
    public FilePathUtil() {
    }

    /**
     * 根据文件根路径构造
     *
     * @param fileRootPath
     */
    public FilePathUtil(String fileRootPath) {
        this.fileRootPath = fileRootPath;
    }

    /**
     * 获取私有目录
     *
     * @param parent
     * @param privatePath
     * @return
     */
    public File getPrivateHome(File parent, String privatePath) {
        File privateHome = new File(parent, privatePath);
        if (privateHome.exists()) {
            return privateHome;
        }
        privateHome.mkdirs();

        return privateHome;
    }

    /**
     * 获取根目录
     *
     * @return
     */
    public File getRootPath() {
        return new File(this.fileRootPath);
    }

    /**
     * 获取模板文件（用于文件生成）
     *
     * @param templateName
     * @return
     */
    public File getTemplateFile(String templateName) {
        File configFile = new File(this.getFileDir(FilePathEnums.TEMPLATE), templateName);
        if (!configFile.exists()) {
            throw new IllegalArgumentException("模板不存在，" + templateName);
        }

        return configFile;
    }

    /**
     * 获取脚本文件（用于文件解析）
     *
     * @param scriptFile
     * @return
     */
    public File getScriptFile(String scriptFileUrl, String scriptFile) {
        return new File(new File(scriptFileUrl), scriptFile);
    }

    /**
     * 获取文件目录
     * 不提供配置文件目录的获取
     *
     * @param dirKey
     * @return
     */
    public File getFileDir(FilePathEnums dirKey) {
        if (dirKey == null) {
            return null;
        }

        String rootPath = dirKey == FilePathEnums.TEMPLATE ? this.configRootPath : this.fileRootPath;
        File dir = new File(rootPath + dirKey.getCode());
        if (dir.exists()) {
            return dir;
        }
        dir.mkdirs();

        return dir;
    }

    /**
     * 获取文件相对路径
     *
     * @param dirKey   文件目录键值
     * @param filePath
     * @return
     */
    public String getRelativeFilePath(FilePathEnums dirKey, String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }

        File fileDir = this.getFileDir(dirKey);
        filePath = FileUtil.normalizePath(filePath);
        String dirPath = FileUtil.normalizePath(fileDir.getAbsolutePath());
        return filePath.replace(dirPath, "");
    }

    /**
     * 设置文件主目录路径
     *
     * @param fileRootPath
     */
    public void setFileRootPath(String fileRootPath) {
        // 只有在不是占位符的情况才设置
        if (!StringUtil.contains(fileRootPath, PLACEHOLDER_PREFIX)) {
            this.fileRootPath = fileRootPath;
        }
    }

    public void setConfigRootPath(String configRootPath) {
        this.configRootPath = configRootPath;
    }
}
