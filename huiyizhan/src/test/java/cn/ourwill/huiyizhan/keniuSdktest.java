package cn.ourwill.huiyizhan;

import cn.ourwill.huiyizhan.weChat.Utils.PageAuthorizeUtils;
import cn.ourwill.huiyizhan.weChat.pojo.QRCodeParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/6/29 0029 18:55
 * @Version1.0
 */
public class keniuSdktest {
    @Test
    public void getToken(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone1());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "oExs5QcN9corLuXDINZCO1avOt8M0_6x0Actmu7s";
        String secretKey = "H1o6qK6QhRttbSz4u4lfP8AQ7tCpJFusgsQPSoGD";
        String bucket = "tuwenzhibo";
//        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "C:\\Users\\1\\Pictures\\code.jpg";
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = "hahaha";
        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);


            Response response = uploadManager.put(byteInputStream, "123", upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testGson(){
        String img = "[['123'],['234']]";
        List<String> strList = new ArrayList<>();
        strList.add("123");
        strList.add("234");
        Gson gson = new Gson();
        String toJson = gson.toJson(strList);

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> imgUrls= gson.fromJson(toJson,type);
        List<String> reList = imgUrls.stream().map(imgu -> imgu="123-"+imgu).collect(Collectors.toList());
        System.out.printf(toJson);
        System.out.printf(reList.toString());
    }
    @Test
    public void getAddressByIP() {
        try {
            String strIP = "120.27.234.152";
            URL url = new URL( "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + strIP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while((line = reader.readLine()) != null)
            {
                result.append(line);
            }
            reader.close();
            Object obj= JSONValue.parse(result.toString());
            JSONObject json =(JSONObject)obj;
            String country = (String) json.get("country");
            String province = (String) json.get("province");
            String city = (String) json.get("city");
            if(!country.equals("中国")){
                System.out.println("来自"+country+"的网友");
            }else if(!city.equals(province)){
                System.out.println("来自"+province+"省"+city+"市的网友");
            }else{
                System.out.println("来自"+city+"市的网友");
            }
        }
        catch( IOException e) {
        }
    }
    @Test
    public void codec(){
        String b = "http://orvehsaif.bkt.clouddn.com/image/jpeg/20170707/123123/123123/123123/adfasdf/adsfasdf/asdfasdf/adfasdfa/adsfasdf/asdfasdfasdf/1500692203342.JPG";
//        System.out.println(b);
//        byte[] bytes = b.getBytes();
//        String c = new String(bytes);
//        System.out.println(c);

        String bb = Base64.encodeBase64String(b.getBytes());
//        String bbb = new String(bb);
        System.out.println(bb);
//        System.out.println(bbb);
//        String url = ImgUtil.getWaterMark(bbb);
//        System.out.println(url);
        byte[] c = Base64.encodeBase64("alkdfaj;lkdfja;lskdfja;lksdfjlsakdfjalskdfja;lkdsfqwerqwerqwerqwerqwerqwerqwer".getBytes(),true);
        System.out.println(new String(c));
    }

    @Test
    public void testQRCode(){
        QRCodeParam qrCodeParam = new QRCodeParam();
        qrCodeParam.setAccess_token("9_Nl-iHDz-7Ql_KU_Lp3vVCC1gSmAUkSalCPd2fjE66tVRgbx0CbIB83eM0ZdEjrfId-DMuNdck5qCOSLyHZWc8w6eHJfWDUIvPwZHD-x-oVEqz9soXOuqvsfJ94IyvjrVLPAt7GZDNXj-syZcFQDcAHARCP");
        qrCodeParam.setAction_name("test");
        qrCodeParam.setExpire_seconds(7200);
        qrCodeParam.setScene_id(123);
        try {
            String url = PageAuthorizeUtils.getQRCodeTicket(qrCodeParam);
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
