package cn.ourwill.tuwenzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/26 11:34
 * @Description
 */
@Data
public class LightMap extends Config{
        private Integer id;
        private String logoUrl;
        private String shareUrl;
        private String name;
        private String backImgUrl;
        private String textImgUrl;
        private String desc;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
        private Date date;
        private String sponsor;
        private String album;
        private List<String> albumArray;
        private String albumDesc;
        private String qrCodeUrl;
    public List<String> getAlbumArray(){
        try {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            Gson gson = new Gson();
            List<String> imgUrls = gson.fromJson(this.album, type);
            return imgUrls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
