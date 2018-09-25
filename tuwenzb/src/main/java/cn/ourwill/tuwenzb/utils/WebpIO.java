package cn.ourwill.tuwenzb.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Webp converter
 *
 * @author biezhi
 * @date 2017/10/2
 */
@Slf4j
public class WebpIO {

    /**
     * cwebp/dwebp/gif2webp
     * <p>
     * binary command file path
     */
    private static       String CMD_DIR;
    private static       String OS_NAME       = System.getProperty("os.name").toLowerCase();
    private static       String OS_ARCH       = System.getProperty("os.arch").toLowerCase();
    private static final String CWEBP_TMP_DIR = "cwebp_tmp";

    static {
        String osName = getOsName();
        String extension = getExtensionByOs(osName);
        String webpPath = "cwebp/" + osName;
        if (osName.indexOf("windows") == -1) {
            //linux
            CMD_DIR = "/home/twzb/" + "cwebp/linux_x86_64";
        } else {
            CMD_DIR = WebpIO.class.getResource("/").getPath() + webpPath;
        }
    }


    /**
     * Converter webp file to normal image
     *
     * @param src  webp file path
     * @param dest normal image path
     */
    public static void toNormalImage(String src, String dest) {
        toNormalImage(new File(src), new File(dest));
    }

    /**
     * Converter webp file to normal image
     *
     * @param src  webp file path
     * @param dest normal image path
     */
    public static void toNormalImage(File src, File dest) {
        String command = CMD_DIR + (dest.getName().endsWith(".gif") ? "/gif2webp" : "/dwebp ") + src.getPath() + " -o " + dest.getPath();
        System.out.println("Execute: " + command);
        String output = executeCommand(command);
        if (!"".equals(output)) {
            System.out.println("Output: " + output);
        }
    }

    /**
     * Convert normal image to webp file
     *
     * @param src  nomal image path
     * @param dest webp file path
     */
    public static void toWEBP(String src, String dest) {
        toWEBP(new File(src), new File(dest));
    }

    /**
     * Convert normal image to webp file
     *
     * @param src  nomal image path
     * @param dest webp file path
     */
    public static void toWEBP(File src, File dest) {
        try {
            String command = CMD_DIR + (src.getName().endsWith(".gif") ? "/gif2webp " : "/cwebp ") + src.getPath() + " -o " + dest.getPath();
//            System.out.println("Execute: " + command);
            System.out.println(command);
            System.out.println(CMD_DIR);
            String output = executeCommand(command);
            if (!"".equals(output)) {
                System.out.println("Output: " + output);
            }
        } catch (Exception e) {
            log.info("WebpIO.toWEBP", e);
        }
    }

    /**
     * execute command
     *
     * @param command command direct
     * @return
     */
    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            log.info("WebpIO.executeCommand", e);
        }
        return output.toString();
    }

    /**
     * delete temp dir and commands
     */
    public static void close() {
        File tmp = new File(CWEBP_TMP_DIR);
        if (tmp.exists() && tmp.isDirectory()) {
            File[] files = tmp.listFiles();
            for (File file : files) {
                file.delete();
            }
            tmp.delete();
        }
    }

    private static void copy(InputStream in, File dest) throws IOException {
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        //copy the file content in bytes
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        dest.setWritable(true);
        dest.setReadable(true);
        dest.setExecutable(true);
        in.close();
        out.close();
    }

    /**
     * get os name and arch
     *
     * @return
     */
    private static String getOsName() {
        // windows
        if (OS_NAME.indexOf("win") >= 0) {
            boolean is64bit = (System.getenv("ProgramFiles(x86)") != null);
            return "windows_" + (is64bit ? "x86_64" : "x86");
        } else if (OS_NAME.indexOf("mac") >= 0) {
            // mac osx
            return "mac_" + OS_ARCH;
        } else if (OS_NAME.indexOf("nix") >= 0 || OS_NAME.indexOf("nux") >= 0 || OS_NAME.indexOf("aix") > 0) {
            // unix
            return "linux_" + OS_ARCH;
        } else {
            log.info("Hi boy, Your OS is not support!!");
            return null;
        }
    }

    /**
     * Return the Os specific extension
     *
     * @param os: operating system name
     */

    private static String getExtensionByOs(String os) {
        if (os == null || os.isEmpty()) return "";
        else if (os.contains("win")) return ".exe";
        return "";
    }

    public static void main(String[] args) {
        String str = "H:\\3.webp";
        String dest = "H:\\aa.jpg";
/*        File sourceFile = new File("C:\\Users\\wl\\Desktop\\mmexport1528197286860.webp");
        File destFile = new File("H:\\a.png");
        BufferedImage read = ImageIO.read(sourceFile);
        ImageIO.write(read,"png",destFile);*/
        File srcFile = new File(str);
        File destFile = new File(dest);
        WebpIO.toNormalImage(srcFile, destFile);
    }
}