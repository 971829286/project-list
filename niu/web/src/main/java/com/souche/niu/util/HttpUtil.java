package com.souche.niu.util;

import org.apache.commons.io.IOUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by sid on 2018/9/5.
 */
public class HttpUtil {

    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        return IOUtils.toByteArray(request.getInputStream());
    }
}
