package com.souche.bulbous.web.json.banner;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.souche.bulbous.excel.ExcelConstant;
import com.souche.bulbous.utils.BannerUtils;
import com.souche.bulbous.vo.CarStatisticBean;
import com.souche.bulbous.web.json.CarPage;
import com.souche.bulbous.web.json.UploadAPI;
import com.souche.niu.api.CmsService;
import com.souche.niu.model.Banner;
import com.souche.optimus.common.page.Page;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.core.annotation.Param;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.sad.AbstractReactAction;
import com.souche.optimus.core.sad.Props;
import com.souche.optimus.core.sad.ReactAction;
import com.souche.optimus.core.sad.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
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
        List<Banner> list = cmsService.getBannerListByStatus(3, 1, 10);
        Integer count = cmsService.getBannerListCountByStatus(3);
        if(list != null && list.size() > 0){
            result.put("result",new Page<Banner>(count,1,10,list));
        }else{
            result.put("result",new Page<Banner>(1,1,10,null));
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
        Map<String, Object> query = (Map<String, Object>)state.get("query");
        String statusStr = query.get("status")+"";
        String pageStr =  query.get("page")+"";
        String pageSizeStr =  query.get("pageSize")+"";

        Integer status = Integer.parseInt(statusStr);
        Integer page = Integer.parseInt(pageStr);
        Integer pageSize = Integer.parseInt(pageSizeStr);


        Integer count = cmsService.getBannerListCountByStatus(status);
        logger.info("--> 获取列表...");
        Map<String, Object> result = Maps.newHashMap();
        if(cmsService == null){
            logger.error("banner服务不可用[can not found Banner Service provided!]...");
            result.put("result", new Page<Banner>(0, 1, pageSize, null));
        } else{
            List<Banner> list = cmsService.getBannerListByStatus(status, page, pageSize);
            state.set("result",new Page<Banner>(count, page, pageSize, list));
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
        Map<String, Object> itemData = (Map<String, Object>)state.get("itemData");
        String title = itemData.get("title")+"";
        String images = itemData.get("image")+"";
        String address = itemData.get("address")+"";
        String status = itemData.get("status") + "";
        String orderNum = itemData.get("orderNum") + "";
        JSONArray targetCity = (JSONArray) itemData.get("targetCity");

        Banner banner = new Banner();
        banner.setTitle(title);
        banner.setImage(images);
        banner.setAddress(address);
        banner.setStatus(Integer.parseInt(status));
        banner.setOrderNum(Integer.parseInt(orderNum));
        banner.setTargetCity(BannerUtils.targetCityToDb(targetCity));

        Boolean flag = cmsService.updateBanner(bannerId, banner);
        if(flag){
            state.set("isUpdate",true);
        }else{
            state.set("isUpdate",false);
        }
    }


    @SuppressWarnings("unchecked")
    @ReactAction(desc = "保存")
    public void saveInfo(){
        State state = super.getState();
        Map<String, Object> itemData = (Map<String, Object>)state.get("itemData");
        String title = itemData.get("title")+"";
        String images = itemData.get("image")+"";
        String address = itemData.get("address")+"";
        String orderNum = itemData.get("orderNum") +"";
        JSONArray targetCity = (JSONArray) itemData.get("targetCity");
        if(StringUtil.isNotEmpty(title) && StringUtil.isNotEmpty(address)) {
            Banner banner = new Banner();
            banner.setTitle(title);
            banner.setAddress(address);
            banner.setImage(images);
            //默认未上架
            banner.setStatus(0);
            banner.setOrderNum(Integer.parseInt(orderNum));
            banner.setTargetCity(BannerUtils.targetCityToDb(targetCity));

            System.out.println(banner.getTargetCity());
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
