package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.service.IAppVersionService;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Component
@Path("/appVersion")
public class AppversionController {

    @Autowired
    private IAppVersionService appVersionService;
    private static final Logger log = LogManager.getLogger(AppversionController.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map findTheLast(@Context HttpServletRequest request){
        try{
            return ReturnResult.successResult("data",appVersionService.findTheLast(), ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("ActivityAlbumController.selectPhotoByAlbum",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
