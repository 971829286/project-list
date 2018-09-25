package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Contribute;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.mapper.ContributeMapper;
import cn.ourwill.tuwenzb.mapper.UserMapper;
import cn.ourwill.tuwenzb.service.IContributeService;
import cn.ourwill.tuwenzb.weixin.Utils.WeixinPushMassage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：投稿service层
 *
 * @author zhaoqing
 * @create 2018-06-20 14:43
 **/

@Service
@Slf4j
public class ContributeServiceImpl extends BaseServiceImpl<Contribute> implements IContributeService {
    @Value("${system.domain}")
    private String domian;

    @Autowired
    private ContributeMapper contributeMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer save(Contribute contribute){
        return contributeMapper.save(contribute);
    }

    @Override
    public Integer update(Contribute contribute){
        return contributeMapper.update(contribute);
    }

    @Override
    public Contribute selectOneByUserId(Integer userId){

        Contribute contribute = contributeMapper.selectOneByUserId(userId);
//        List<String> urlList = new ArrayList<>();
//        Contribute contribute = contributes.get(0);
//        for(int i=0;i<contributes.size();i++){
//            String url = contributes.get(i).getPicUrl();
//            urlList.add(url);
//        }
//        contribute.setUrlList(urlList);
        return contribute;
    }

    @Override
    public List<Contribute>selectByUserId(Integer userId) {
        return contributeMapper.selectByUserId(userId);
    }

    @Override
    public List<Contribute> getContributeList( Map<String,Object> param){
//        List<Contribute> contributes =  null;
//        if (param.get("checkStatus")!=null){
//            contributes = contributeMapper.getContributeUserList(param);
//            contributes.stream().forEach(entity -> {
//                entity.setPicList(contributeMapper.getUrlByUserIdStatus(entity.getUserId(),Integer.valueOf(param.get("checkStatus").toString())));
//            });
//        }else {
//            contributes = contributeMapper.getContributeUserListAll(param);
//        }
        return contributeMapper.getContributeUserList(param);
    }

    @Override
    public List<Contribute> findAll(){
        return contributeMapper.findAll();
    }

    @Override
    public Integer updateStatus(Integer id,Integer status,String feedback) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = sdf.format(new Date());
        int count = contributeMapper.updateStatus(id,status,feedback,new Date());
        if(count>0){
            //微信推送
            Contribute contribute = contributeMapper.getById(id);
            User user = userMapper.getById(contribute.getUserId());
            String firstData = null;
            String remark = "点击查看详情";
            String redirectUrl = domian+"/photolive/#/ballconfirm";
            if(status.equals(2)){
                firstData = "恭喜您的作品《"+contribute.getWorkTitle()+"》成功通过审核！";
                WeixinPushMassage.wxCheckPass(user.getWechatNum(),firstData,time,remark,redirectUrl);
            } else if(status.equals(3)){
                firstData = "很抱歉您的作品《"+contribute.getWorkTitle()+"》未通过审核！";
                WeixinPushMassage.wxCheckFail(user.getWechatNum(),firstData,time,remark,redirectUrl);
//                WeixinPushMassage.wxFollowSuccess(user.getNickname(),time,remark,user.getWechatNum(),redirectUrl);
            }
        }
        return count;
    }

    @Override
    public Integer batchCheck(Integer checkStatus, List<Integer> list,String feedBack) {
        int count = contributeMapper.batchCheck(checkStatus,list,feedBack,new Date());
        if(count > 0){
                list.parallelStream()
                        .forEach(id->{
                            Contribute contribute = contributeMapper.getById(id);
                            User user = userMapper.getById(contribute.getUserId());
                            String firstData = null;
                            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                            String remark = "点击查看详情";
                            String redirectUrl = domian+"/photolive/#/ballconfirm";
                            try {
                                if (checkStatus.equals(2)) {
                                    firstData = "恭喜您的作品《" + contribute.getWorkTitle() + "》成功通过审核！";
                                    WeixinPushMassage.wxCheckPass(user.getWechatNum(), firstData, time, remark, redirectUrl);
                                } else if (checkStatus.equals(3)) {
                                    firstData = "很抱歉您的作品《"+contribute.getWorkTitle()+"》未通过审核！";
                                    WeixinPushMassage.wxCheckFail(user.getWechatNum(),firstData,time,remark,redirectUrl);
                                }
                            }catch (Exception e){
                                log.info("ContributeServiceImpl.batchCheck",e);
                            }
                        });
        }
        return count;
    }

    @Override
    public Integer getPromotionNum(Integer userId) {
        return contributeMapper.getPromotionNum(userId);
    }
}
