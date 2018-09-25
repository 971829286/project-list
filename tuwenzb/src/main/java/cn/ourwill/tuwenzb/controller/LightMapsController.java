package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.service.ILightMapService;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/19 16:57
 * @Description
 */
@Component
@Slf4j
@Path("/lightMaps")
public class LightMapsController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ILightMapService lightMapService;

    @POST
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map coordinatesSave(@Context HttpServletRequest request,@PathParam("roomId") Integer roomId, List<List<Double>> coordinates){
        for(List<Double> list : coordinates) {
            Long count = redisTemplate.opsForSet().add("lightMaps:" + roomId, list);
        }
//        log.info("插入坐标："+count);
        return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
    }

    @GET
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map getCoordinates(@Context HttpServletRequest request,@PathParam("roomId") Integer roomId){
        Set reList = redisTemplate.opsForSet().members("lightMaps:"+roomId);
//        log.info("插入坐标："+count);
        return ReturnResult.successResult("data",reList,ReturnType.ADD_SUCCESS);
    }

    @GET
    @Path("/{roomId}/treeJson")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getTreeJson(@Context HttpServletRequest request,@PathParam("roomId") Integer roomId){
        try {
            long count = redisTemplate.opsForSet().size("lightMaps:" + roomId);
//        log.info("插入坐标："+count);
            JSONObject reJson = new JSONObject();
            JSONArray nodesArray = new JSONArray();
            JSONArray linksArray = new JSONArray();
            for (int i = 0; i < count; i++) {
                JSONObject node = new JSONObject();
                JSONObject link = new JSONObject();
                node.put("id", String.valueOf(i));
                node.put("group", i);
                nodesArray.put(node);
                if(i>0) {
                    if(i<11){
                        link.put("source", i-1);
                        link.put("target", i-1);
                        link.put("value", 2);
                        linksArray.put(link);
                        JSONObject linkMain = new JSONObject();
                        linkMain.put("source", i);
                        linkMain.put("target", 0);
                        linkMain.put("value", 2);
                        linksArray.put(linkMain);
                    }else{
                        link.put("source", i-1);
                        link.put("target", (i-1) % 10);
                        link.put("value", 2);
                        linksArray.put(link);
                    }
                    if(i==count-1){
                        JSONObject linkEnd = new JSONObject();
                        linkEnd.put("source", i);
                        linkEnd.put("target", (i) % 10);
                        linkEnd.put("value", 2);
                        linksArray.put(linkEnd);
                    }
                }
//                if(i==count-1){
//                    JSONObject linkEnd = new JSONObject();
//                    linkEnd.put("source", i);
//                    linkEnd.put("target", 0);
//                    linkEnd.put("value", 2);
//                    linksArray.put(linkEnd);
//                }
            }
            reJson.put("nodes",nodesArray);
            reJson.put("links", linksArray);
            return reJson.toString();
        } catch (Exception e){
            log.error("getTreeJson",e);
            return null;
//            return ReturnResult.successResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/getConfig/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map getConfig(@Context HttpServletRequest request,@PathParam("id") Integer id){
        return ReturnResult.successResult("data",lightMapService.getById(id),ReturnType.GET_SUCCESS);
    }

}
