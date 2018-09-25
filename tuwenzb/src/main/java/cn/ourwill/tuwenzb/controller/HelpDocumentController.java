package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.HelpDocument;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IHelpDocumentService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Path("/helpdocument")
public class HelpDocumentController {

    @Autowired
    private IHelpDocumentService helpDocumentService;

    private static final Logger log = LogManager.getLogger(HelpDocumentController.class);

    /**
     *  GET（SELECT）：从服务器取出资源（一项或多项）
     *  POST（CREATE）：在服务器新建一个资源。
     *  PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
     *  PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
     *  DELETE（DELETE）：从服务器删除资源。
     *
     */

    //帮助文档上传
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level=1)
    public Map<String, Object> save(@Context HttpServletRequest request, HelpDocument helpDocument){
        if(helpDocument == null)
            return ReturnResult.errorResult("输入数据不存在");
        try {
            Integer manager_id = GlobalUtils.getUserId(request);
            if(manager_id==null){
                return ReturnResult.errorResult(ReturnType.ADD_ERROR);
            }
            //设置管理员ID
            helpDocument.setManagerId(manager_id);
            //设置上传时间
            helpDocument.setUploadTime(new Date());
            //设置更新时间
            helpDocument.setUpdateTime(new Date());
            if(helpDocumentService.save(helpDocument)>0)
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.error("HelpDocumentController.saveHelpDocument",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //获取分页帮助文档
    @GET
    @Path("/getPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level=1)
    public Map getPageHelpDocument(@DefaultValue("1")@QueryParam("pageNum")final Integer pageNum, @DefaultValue("10")@QueryParam("pageSize")final Integer pageSize){
        if(pageNum == null || pageSize ==null)
            return ReturnResult.errorResult("输入数据不存在");
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<HelpDocument> list = helpDocumentService.findAll();
            PageInfo<HelpDocument> pages = new PageInfo<>(list);
            return ReturnResult.successResult("data",pages, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("HelpDocumentController.getPageHelpDocument",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //删除多条信息
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/delete")
    @Access(level=1)
    public Map deleteBatch(@Context HttpServletRequest request, List<Integer> ids){
        if(ids == null)
            return ReturnResult.errorResult("输入数据不存在");
        try{
            if(null == ids ||ids.size()<=0){
                return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
            }
            if(helpDocumentService.deleteBatch(ids)>0)
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch (Exception e){
            log.error("HelpDocumentController.deleteBatch",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //修改帮助文档
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level=1)
    public Map update(@Context HttpServletRequest request,HelpDocument helpDocument){
        if(helpDocument == null)
            return ReturnResult.errorResult("输入数据不存在");
        try {
            Integer manager_id = GlobalUtils.getUserId(request);
            if(manager_id!=null)
                helpDocument.setManagerId(manager_id);
            //查看文档id是否存在
            if(helpDocument.getId() == null){
                return ReturnResult.errorResult("文档不");
            }
            //设置修改时间
            helpDocument.setUpdateTime(new Date());
            if(helpDocumentService.update(helpDocument)>0)
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("HelpDocumentController.update",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    //根据ID获取帮助文档
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level=1)
    public Map getHelpDocumentById(@PathParam("id")Integer id,@Context HttpServletRequest request){
        if(id == null)
            return ReturnResult.errorResult("输入数据不存在");
        try {
            HelpDocument helpDocument = helpDocumentService.getById(id);
            if (helpDocument == null) {
                return ReturnResult.errorResult(ReturnType.GET_ERROR);
            }
            return ReturnResult.successResult("data", helpDocument, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("HelpDocumentController.getHelpDocumentById",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
