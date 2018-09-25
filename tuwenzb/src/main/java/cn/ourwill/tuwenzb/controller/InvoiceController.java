package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.*;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.*;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/9/19 0019 18:22
 * @Version1.0
 */
@Component
@Path("/invoice")
public class InvoiceController {
    private static final Logger log = LogManager.getLogger(TrxOrderController.class);
    @Autowired
    private IInvoiceContentService invoiceContentService;
    @Autowired
    private IInvoiceSiteService invoiceSiteService;
    @Autowired
    private IInvoiceRecordsService invoiceRecordsService;
    @Autowired
    private ILicenseRecordService licenseRecordService;
    /**
     * 发票信息增加
     * @param request
     * @param invoiceContent
     * @return
     */
    @POST
    @Path("/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addInvoiceContent(@Context HttpServletRequest request, InvoiceContent invoiceContent){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            invoiceContent.setUserId(userId);
            Integer count = invoiceContentService.getCountByUserId(invoiceContent.getUserId());
            if(count>=3){
                return ReturnResult.errorResult("发票信息最多保存三条！");
            }
            if(invoiceContentService.save(invoiceContent)>0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.addInvoiceContent",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 发票信息修改
     * @param request
     * @param invoiceContent
     * @param id
     * @return
     */
    @PUT
    @Path("/content/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map updateInvoiceContent(@Context HttpServletRequest request, InvoiceContent invoiceContent, @PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceContent oldInvoiceContent = invoiceContentService.getById(id);
            if(oldInvoiceContent==null) return ReturnResult.errorResult("数据无效！");
            if(!userId.equals(oldInvoiceContent.getUserId())){
                return ReturnResult.errorResult("无权限操作！");
            }
            invoiceContent.setId(id);
            invoiceContent.setUserId(userId);
            if(invoiceContentService.update(invoiceContent)>0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.updateInvoiceContent",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 发票信息获取
     * @param request
     * @param id
     * @return
     */
    @GET
    @Path("/content/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getInvoiceContent(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceContent invoiceContent = invoiceContentService.getById(id);
            return ReturnResult.successResult("data",invoiceContent,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("InvoiceController.getInvoiceContent",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 发票信息删除
     * @param request
     * @param id
     * @return
     */
    @DELETE
    @Path("/content/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map deleteInvoiceContent(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceContent oldInvoiceContent = invoiceContentService.getById(id);
            if(oldInvoiceContent==null) return ReturnResult.errorResult("数据无效！");
            if(!userId.equals(oldInvoiceContent.getUserId())){
                return ReturnResult.errorResult("无权限操作！");
            }
            if(invoiceContentService.delete(id)>0) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.deleteInvoiceContent",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按用户id获取发票信息列表
     * @param request
     * @return
     */
    @GET
    @Path("/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getContentByUserId(@Context HttpServletRequest request){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            return ReturnResult.successResult("data",invoiceContentService.getByUserId(userId),ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("InvoiceController.getContentByUserId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 设置默认发票信息
     * @param request
     * @param id
     * @return
     */
    @POST
    @Path("/content/setDefault/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map setContentDefault(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceContent oldInvoiceContent = invoiceContentService.getById(id);
            if(oldInvoiceContent==null) return ReturnResult.errorResult("数据无效！");
            if(!userId.equals(oldInvoiceContent.getUserId())){
                return ReturnResult.errorResult("无权限操作！");
            }
            if(invoiceContentService.setDefault(id,userId)>0){
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.setContentDefault",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 寄送地址增加
     * @param request
     * @param invoiceSite
     * @return
     */
    @POST
    @Path("/site")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addInvoiceSite(@Context HttpServletRequest request, InvoiceSite invoiceSite){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            invoiceSite.setUserId(userId);
            Integer count = invoiceSiteService.getCountByUserId(invoiceSite.getUserId());
            if(count>=3){
                return ReturnResult.errorResult("寄送地址最多保存三条！");
            }
            if(invoiceSiteService.save(invoiceSite)>0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.addInvoiceSite",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 寄送地址修改
     * @param request
     * @param invoiceSite
     * @param id
     * @return
     */
    @PUT
    @Path("/site/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map updateInvoiceSite(@Context HttpServletRequest request, InvoiceSite invoiceSite, @PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceSite oldInvoiceSite = invoiceSiteService.getById(id);
            if(oldInvoiceSite==null) return ReturnResult.errorResult("数据无效！");
            if(!userId.equals(oldInvoiceSite.getUserId())){
                return ReturnResult.errorResult("无权限操作！");
            }
            invoiceSite.setId(id);
            invoiceSite.setUserId(userId);
            if(invoiceSiteService.update(invoiceSite)>0) {
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.updateInvoiceSite",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 寄送地址获取
     * @param request
     * @param id
     * @return
     */
    @GET
    @Path("/site/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getInvoiceSite(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
//            Integer userId = GlobalUtils.getUserId(request);
            InvoiceSite invoiceSite = invoiceSiteService.getById(id);
            return ReturnResult.successResult("data",invoiceSite,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("InvoiceController.getInvoiceSite",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 寄送地址删除
     * @param request
     * @param id
     * @return
     */
    @DELETE
    @Path("/site/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map deleteInvoiceSite(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceSite oldInvoiceSite = invoiceSiteService.getById(id);
            if(oldInvoiceSite==null) return ReturnResult.errorResult("数据无效！");
            if(!userId.equals(oldInvoiceSite.getUserId())){
                return ReturnResult.errorResult("无权限操作！");
            }
            if(invoiceSiteService.delete(id)>0) {
                return ReturnResult.successResult(ReturnType.DELETE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.DELETE_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.deleteInvoiceSite",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 按用户id获取寄送地址列表
     * @param request
     * @return
     */
    @GET
    @Path("/site")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getSiteByUserId(@Context HttpServletRequest request){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            return ReturnResult.successResult("data", invoiceSiteService.getByUserId(userId),ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("InvoiceController.getSiteByUserId",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 设置默认寄送地址
     * @param request
     * @param id
     * @return
     */
    @POST
    @Path("/site/setDefault/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map setSiteDefault(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceSite oldInvoiceSite = invoiceSiteService.getById(id);
            if(oldInvoiceSite==null) return ReturnResult.errorResult("数据无效！");
            if(!userId.equals(oldInvoiceSite.getUserId())){
                return ReturnResult.errorResult("无权限操作！");
            }
            if(invoiceSiteService.setDefault(id,userId)>0){
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.setSiteDefault",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 申请发票
     * @param request
     * @param invoiceRecords
     * @return
     */
    @POST
    @Path("/record")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addInvoiceRecords(@Context HttpServletRequest request, InvoiceRecords invoiceRecords){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            if(invoiceRecords.getAuthorizationIds()==null||invoiceRecords.getInvoiceSiteId()==null||invoiceRecords.getInvoiceContentId()==null){
                return ReturnResult.errorResult("参数不完整");
            }
            String[] ids = invoiceRecords.getAuthorizationIds().split(",");
            LicenseRecord licenseRecord = null;
            String orderNos = "";
            for (String id: ids) {
                licenseRecord = licenseRecordService.selectById(Integer.valueOf(id));
                if(licenseRecord==null){
                    return ReturnResult.errorResult("所选记录无效！");
                }else if(!licenseRecord.getUserId().equals(userId)){
                    return ReturnResult.errorResult("无权限操作！");
                }else if(!licenseRecord.getInvoiceStatus().equals(0)){
                    return ReturnResult.errorResult("所选记录已申请,不能重复申请！");
                }
                if(licenseRecord.getOrderNo()!=null){
                    if(orderNos.equals("")) {
                        orderNos = licenseRecord.getOrderNo();
                    }else{
                        orderNos = orderNos + "," + licenseRecord.getOrderNo();
                    }
                }
            }
//            orderNos = orderNos.substring(0,orderNos.length());
            invoiceRecords.setOrderNos(orderNos);
            invoiceRecords.setUserId(userId);
            if(invoiceRecordsService.save(invoiceRecords)>0) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult(ReturnType.ADD_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.addInvoiceSite",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 发票记录条件查询
     * @param request
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    @POST
    @Path("/record/selectByParam")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map selectRecordsByParam(@Context HttpServletRequest request, Map params,@QueryParam("pageNum") @DefaultValue("1") Integer pageNum, @QueryParam("pageSize")@DefaultValue("10") Integer pageSize){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            PageHelper.startPage(pageNum, pageSize);
            PageHelper.orderBy(" apply_time desc");
            PageInfo<InvoiceRecords> pages = new PageInfo<>(invoiceRecordsService.selectByParams(params));
            return ReturnResult.successResult("data",pages,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("InvoiceController.selectRecordsByParam",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/record/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map selectByIdWithInfo(@Context HttpServletRequest request,@PathParam("id") Integer id){
        try {
            InvoiceRecords invoiceRecords = invoiceRecordsService.selectById(id);
            if(invoiceRecords != null && invoiceRecords.getAuthorizationIds()!=null){
                String[] ids = invoiceRecords.getAuthorizationIds().split(",");
                List<LicenseRecord> licenseRecords = new ArrayList();
                for (String i : ids){
                    licenseRecords.add(licenseRecordService.selectById(Integer.parseInt(i)));
                }
                invoiceRecords.setLicenseRecords(licenseRecords);
            }
            return ReturnResult.successResult("data",invoiceRecords,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("InvoiceController.selectRecordsByParam",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    @GET
    @Path("/record/auth/{authorizationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map selectByAuthId(@Context HttpServletRequest request,@PathParam("authorizationId") Integer authorizationId){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            InvoiceRecords invoiceRecords = invoiceRecordsService.selectByAuthorizationId(authorizationId,userId);
            if(invoiceRecords != null && invoiceRecords.getAuthorizationIds()!=null){
                String[] ids = invoiceRecords.getAuthorizationIds().split(",");
                List<LicenseRecord> licenseRecords = new ArrayList();
                for (String i : ids){
                    licenseRecords.add(licenseRecordService.selectById(Integer.parseInt(i)));
                }
                invoiceRecords.setLicenseRecords(licenseRecords);
            }
            return ReturnResult.successResult("data",invoiceRecords,ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("InvoiceController.selectRecordsByParam",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }


    /**
     * 完成开票
     * @param request
     * @param params
     * @return
     */
    @POST
    @Path("/record/generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map updateRecordStatus(@Context HttpServletRequest request, Map params){
        try {
            return invoiceRecordsService.updateStatus((String) params.get("ids"),(String) params.get("expressCompany"),(String) params.get("expressNo"));
        }catch (Exception e){
            log.error("InvoiceController.updateRecordStatus",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 快递信息修改
     * @param request
     * @param id
     * @param invoiceRecords
     * @return
     */
    @POST
    @Path("/record/updateExpress/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access(level = 1)
    public Map updateRecordExpress(@Context HttpServletRequest request,@PathParam("id") Integer id, InvoiceRecords invoiceRecords){
        try {
            if(StringUtils.isEmpty(invoiceRecords.getExpressCompany())&&StringUtils.isEmpty(invoiceRecords.getExpressNo()))
                return ReturnResult.errorResult("参数为空！");
            InvoiceRecords param = new InvoiceRecords();
            if(StringUtils.isNotEmpty(invoiceRecords.getExpressCompany()))
                param.setExpressCompany(invoiceRecords.getExpressCompany());
            if(StringUtils.isNotEmpty(invoiceRecords.getExpressNo()))
                param.setExpressNo(invoiceRecords.getExpressNo());
            param.setId(id);
            int count = invoiceRecordsService.update(param);
            if(count>0)
                return ReturnResult.successResult(ReturnType.UPDATE_SUCCESS);
            return ReturnResult.errorResult(ReturnType.UPDATE_ERROR);
        }catch (Exception e){
            log.error("InvoiceController.updateRecordExpress",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }
}
