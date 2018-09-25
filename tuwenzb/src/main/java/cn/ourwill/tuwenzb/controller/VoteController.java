package cn.ourwill.tuwenzb.controller;

import cn.ourwill.tuwenzb.entity.*;
import cn.ourwill.tuwenzb.interceptor.Access;
import cn.ourwill.tuwenzb.service.IVoteService;
import cn.ourwill.tuwenzb.utils.*;
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
import java.util.*;

/**
 * 　ClassName:VoteController
 * Description：投票管理控制类
 * User:liufeng
 * CreatedDate:20178/2/27 10:10
 */
@Component
@Path("/vote")
public class VoteController {


    @Autowired
    private IVoteService voteService;

    private static final Logger log = LogManager.getLogger(VoteController.class);

    /**
     * 添加投票
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map castVote(@Context HttpServletRequest request,Vote vote){
        try {
            //获取当前用户id
            Integer userId = GlobalUtils.getUserId(request);
            if (userId != null) {
                //发起投票人
                vote.setUserId(userId);
                vote.setStartTime(new Date());
                vote.setStatus(1);
                voteService.createVote(vote);
                return ReturnResult.successResult("创建投票成功！");
            }else{
                return ReturnResult.errorResult("用户未登录！");
            }
        }catch(Exception e){
            log.error("VoteController.add",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取投票列表
     */
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map listVote(@Context HttpServletRequest request,@QueryParam("activityId") Integer activityId,
                        @DefaultValue("1")@QueryParam("pageNum") Integer pageNum,
                        @DefaultValue("10")@QueryParam("pageSize") Integer pageSize){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            //开始分页,pageNum:页码   pageSize:每页显示数量
            PageHelper.startPage(pageNum, pageSize);
            List<Vote> list = voteService.getVoteList(userId,activityId);
            PageInfo<Vote> pages = new PageInfo<>(list);
            return ReturnResult.successResult("data",pages, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("VoteController.listVote",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 删除投票
     */
    @Path("/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map deleteVoteList(@Context HttpServletRequest request,List<Integer> voteIds){
        try {
            if (voteIds != null && voteIds.size() > 0) {
                voteService.deleteVotes(voteIds);
                return ReturnResult.successResult("删除成功！");

            }else{
                return ReturnResult.errorResult("请选择记录后，再进行删除！");
            }
        }catch (Exception e){
            log.error("VoteController.deleteVoteList",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 删除投票
     */
    @Path("/deleteOption/{optionId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map deleteOption(@Context HttpServletRequest request,@PathParam("optionId") Integer optionId){
        try {
            if (optionId != null) {
                voteService.deleteOptionById(optionId);
                return ReturnResult.successResult("删除成功！");

            }else{
                return ReturnResult.errorResult("请选择记录后，再进行删除！");
            }
        }catch (Exception e){
            log.error("VoteController.deleteVoteList",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取投票列表
     */
    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map updateVote(@Context HttpServletRequest request,Vote vote){
        try {
            voteService.updateVote(vote);
            return ReturnResult.successResult("修改成功！");
        }catch (Exception e){
            log.error("VoteController.deleteVoteList",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取投票信息
     */
    @Path("/detail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getVoteDetail(@Context HttpServletRequest request,@QueryParam("voteId") Integer voteId
                        ){
        try {
            Vote vote = voteService.getVoteById(voteId);
            return ReturnResult.successResult("data",vote, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("VoteController.listVote",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 微信投票
     *
     */
    @POST
    @Path("/addVote/{voteId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map addVote(@Context HttpServletRequest request,@PathParam("voteId") Integer voteId, List<Integer> voteOptionIds) {
        try{
            Integer userId = GlobalUtils.getUserId(request);
            Vote vote = voteService.getVoteById(voteId);
            if(vote==null)
                return ReturnResult.errorResult("投票不存在！");
            if(vote.getEndTime().before(new Date())&&vote.getStatus().equals(-1)){
                return ReturnResult.errorResult("投票已结束！");
            }
            if(voteService.addVoted(voteOptionIds,userId)) {
                return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
            }
            return ReturnResult.errorResult("投票失败,请检查是否重复投票！");
        }catch (Exception e){
            log.error("VoteController.addVote",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 获取投票列表
     */
    @Path("/getVoteByActivityId/{activityId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map getVoteByActivityId(@Context HttpServletRequest request,@PathParam("activityId") Integer activityId){
        try {
            Integer userId = GlobalUtils.getUserId(request);
            List<Vote> list = voteService.getVoteList(userId,activityId);
            return ReturnResult.successResult("data",list, ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("VoteController.listVote",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

    /**
     * 关闭投票
     */
    @Path("/closed/{voteId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Access
    public Map closedVoteById(@Context HttpServletRequest request,@PathParam("voteId") Integer voteId ){
        try {
            voteService.closedById(voteId);
            return ReturnResult.successResult(ReturnType.GET_SUCCESS);
        }catch (Exception e){
            log.error("VoteController.listVote",e);
            return ReturnResult.errorResult(ReturnType.SERVER_ERROR);
        }
    }

}
