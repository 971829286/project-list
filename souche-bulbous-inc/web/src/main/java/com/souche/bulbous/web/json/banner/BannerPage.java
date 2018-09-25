package com.souche.bulbous.web.json.banner;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.souche.bulbous.excel.ExcelConstant;
import com.souche.bulbous.vo.CarStatisticBean;
import com.souche.bulbous.web.json.CarPage;
import com.souche.bulbous.web.json.UploadAPI;
import com.souche.niu.api.CmsService;
import com.souche.niu.model.Banner;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.page.Page;
import com.souche.optimus.common.upload.FileInfo;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.core.annotation.Param;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.controller.Context;
import com.souche.optimus.core.sad.AbstractReactAction;
import com.souche.optimus.core.sad.Props;
import com.souche.optimus.core.sad.ReactAction;
import com.souche.optimus.core.sad.State;
import com.souche.optimus.core.web.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author xujinniu@souche.com
 * @Description:Banner数据交互类
 * @date 2018/9/3
 */
@View("BannerPage")
public class BannerPage extends AbstractReactAction {

    private Logger logger = LoggerFactory.getLogger(CarPage.class);

    @Resource
    private CmsService cmsService;

    @Resource
    private UploadAPI uploadAPI;

    @Override
    @ReactAction(desc = "init")
    public Map<String, Object> init(Props props) {
        logger.info("--> 初始化banner数据...");
        Map<String, Object> result = Maps.newHashMap();
        if(cmsService == null){
            logger.error("banner服务不可用[can not found Banner Service provided!]...");
            result.put("result", new Page<CarStatisticBean>(0, 1, ExcelConstant.PAGE_SIZE, null));
            return result;
        }
        //分页不考虑
        List<Banner> list = cmsService.getBannerListByStatus(3, null, null);
        if(list != null && list.size() > 0){
            result.put("result",new Page<Banner>(list.size(),1,list.size(),list));
        }else{
//            Banner banner = new Banner(-1,"暂无数据","暂无数据",-1,-1,"暂无数据",null);
//            list.add(banner);
            result.put("result",new Page<Banner>(1,1,1,null));
        }
        return result;
    }

    /**
     * 获取列表
     */
    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取列表")
    public void getList(){
        State state = super.getState();
        logger.info("--> 获取列表...");
        Map<String, Object> result = Maps.newHashMap();
        if(cmsService == null){
            logger.error("banner服务不可用[can not found Banner Service provided!]...");
            result.put("result", new Page<CarStatisticBean>(0, 1, ExcelConstant.PAGE_SIZE, null));
        } else{
            List<Banner> list = cmsService.getBannerListByStatus(3, null, null);
        }
    }


    @ReactAction(desc = "按照id删除")
    public void deleteById(@Param(value = "state.bannerId",required = true,errMsg = "id不能为空") Integer bannerId){
        Boolean flag = cmsService.deleteById(bannerId);
        State state = super.getState();
        if(flag){
            state.set("isDelete",true);
        }else{
            state.set("isDelete",false);
        }
    }

    @ReactAction(desc = "按照id获取banner")
    public void updateBanner(@Param(value = "state.bannerId",required = true,errMsg = "请输入bannerId")Integer bannerId){
        System.out.println(bannerId);
        State state = super.getState();
        Map<String, String> itemData = (Map<String, String>)state.get("itemData");
        String title = itemData.get("title")+"";
        String images = itemData.get("image")+"";
        String address = itemData.get("address")+"";
        Banner banner = new Banner();
        banner.setTitle(title);
        banner.setImage(images);
        banner.setAddress(address);
        banner.setStatus(0);
        banner.setOrderNum(1);
        Boolean flag = cmsService.updateBanner(bannerId, banner);
        if(flag){
            state.set("isUpdate",true);
        }else{
            state.set("isUpdate",false);
        }
    }

    @ReactAction(desc = "按照状态获取banner列表")
    public void getBannerByStatus(@Param(value = "state.status",required = true,errMsg = "请输入status")Integer status){
        String statusStr = "";
        if(status == 0){

            statusStr = "未上架";

        }else if(status == 1){

            statusStr = "已上架";

        }else if(status == 2){

            statusStr = "已下架";

        }else if (status == 3){

            statusStr = "全部";
        }
        System.out.println("获取"+statusStr+"的banner列表");
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "保存")
    public void saveInfo(){
        State state = super.getState();
        Map<String, String> itemData = (Map<String, String>)state.get("itemData");
        String title = itemData.get("title")+"";
        String images = itemData.get("image")+"";
        String address = itemData.get("address")+"";
        //TODO image优化
        if(StringUtil.isNotEmpty(title) && StringUtil.isNotEmpty(address)) {
            Banner banner = new Banner();
            banner.setTitle(title);
            banner.setAddress(address);
            banner.setImage(images);
            banner.setStatus(0);
            banner.setOrderNum(1);
            Integer flag = cmsService.saveBanner(banner);
            if(flag > 1){
                state.set("isSave",true);
            }else{
                state.set("isSave",false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "按照id获取banner")
    public void getBannerById(@Param(value = "state.bannerId",required = true,errMsg = "请输入bannerId")Integer bannerId){
        Banner banner = cmsService.getBannerById(bannerId);
        State state = super.getState();
        if(banner == null){
            state.set("itemData",null);
        }
        state.set("itemData", banner);
    }



}
