package com.souche.niu.manager.workTable.impl;

import com.souche.niu.dao.workTable.WorkTableDao;
import com.souche.niu.manager.workTable.WorkTableManager;
import com.souche.niu.model.workTable.*;
import com.souche.niu.spi.WorkTableSPI;
import com.souche.optimus.cache.CacheService;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.shop.model.Shop;
import com.souche.shop.model.ShopAudit;
import com.souche.sso.client2.AuthNHolder;
import com.souche.user.hessian.model.UserObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("workTableManager")
public class WorkTableManagerImpl implements WorkTableManager {

    private static final Logger logger = LoggerFactory.getLogger(WorkTableManagerImpl.class);

    @Autowired
    private WorkTableSPI workTableSPI;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private WorkTableDao workTableDao;

    /**
     * 获取用户信息
     * @param userPhone 用户手机
     * @param appName  cheniu
     * @return UserDo
     */
    public UserDo getUser(String userPhone, String appName,String shopCode) {

        try{

            logger.info("获取用户数据userPhone={}, appName={},shopCode={}",userPhone,appName,shopCode);

            UserDo userDo = new UserDo();
            UserObject user = workTableSPI.getUser(userPhone, appName);
            userDo.setShopCode(shopCode);
            userDo.setAuthenticate((String) user.getContexts().get("authenticate"));
            userDo.setName((String) user.getContexts().get("name"));
            userDo.setUserAccount((String) user.getContexts().get("account"));
            userDo.setAvatar((String) user.getContexts().get("head_url"));
            userDo.setUserId(user.getId());

            logger.info("获取用户返回数据为userDo={}",userDo);

            return userDo;
        }catch (Exception e){
            logger.error("获取用户信息异常 e=",e);
            return null;
        }
    }

    /**
     * 获取商店信息
     * @param token  用户token
     * @param userAccount 账户
     * @return shopMember
     */
    public ShopMemberDo getShop(String token,String userAccount) {

        try{

            logger.info("店铺查询参数 token={},userAccount={}",token,userAccount);

            ShopMemberDo shopMemberDo = workTableSPI.getShopMember(userAccount);
            Shop shopInfo = getShopInfo(userAccount);
            if (shopInfo!=null){

                shopMemberDo.setName(shopInfo.getName());
            }

            logger.info("店铺查询返回数据 shopMemberDo={}",shopMemberDo);

            return shopMemberDo;
        }catch (Exception e){
            logger.error("获取商店信息异常e=",e);
            return null;
        }

    }

    /**
     * 获取group
     * @param token  用户token
     * @param shopCode  店铺code
     * @return list
     */
    public List<GroupDo> getGroupList(String token,String shopCode) {

        try{
            logger.info("获取车行资料，我的收藏等group信息，参数token={},shopCode={}",token,shopCode);
            List<GroupDo> groups = new ArrayList<GroupDo>();

            GroupDo groupDo1 = new GroupDo();
            groupDo1.setGroup_name("");
            groupDo1.setGroup_eventKey("");
            groupDo1.setElements(getElements1(shopCode));
            groups.add(groupDo1);

            GroupDo groupDo2 = new GroupDo();
            groupDo2.setGroup_name("");
            groupDo2.setGroup_eventKey("");
            groupDo2.setElements(getElements2());
            groups.add(groupDo2);

            GroupDo groupDo3 = new GroupDo();
            groupDo3.setGroup_name("");
            groupDo3.setGroup_eventKey("");
            groupDo3.setElements(getElements3());
            groups.add(groupDo3);

            return groups;
        }catch (Exception e){
            logger.error("获取车行资料，我的收藏等group信息异常 e=",e);
            return null;
        }
    }

    /**
     * 获取information小组
     * @param token  用户token
     * @param userPhone  用户手机
     * @param shopCode  店铺code
     * @return  Information
     */
    public InformationDo getInformation(String token,String userPhone,String shopCode) {

        try {
            logger.info("获取会员账单，我的金币，优惠券，参数token={},userPhone={},shopCode={}",token,userPhone,shopCode);
            InformationDo informationDo = new InformationDo();
            List<ElementsDo> elements = getElements(token,userPhone);
            informationDo.setElements(elements);

            return informationDo;
        }catch (Exception e){
            logger.error("获取会员账单，我的金币，优惠券数据异常e=",e);
            return null;
        }
    }

    /**
     * 会员账单，我的金币，优惠券
     * @param token   用户token
     * @param userPhone  用户手机
     * @return lsit
     */
    private List<ElementsDo> getElements(String token,String userPhone){

        logger.info("会员账单，我的金币，优惠券请求参数为token={},userPhone={}",token,userPhone);
        List<ElementsDo> elementsDoList = new ArrayList<ElementsDo>();

        String userId = AuthNHolder.userId();


        //会员账单
        Double walletBalance = workTableSPI.getWalletBalance();

        ElementsDo elementsDo1 = new ElementsDo();
        elementsDo1.setTitle("会员账单");
        elementsDo1.setDisplay_num(walletBalance);
        elementsDo1.setIosProtocol("cheniu://open.present/myWallet");
        elementsDo1.setAndroidProtocol("cheniu://open/my_wallet");
        elementsDo1.setEventKey("CHENIU_MY_QIANBAO");
        elementsDo1.setProtocol("");

        //我的金币
        String reqUrl = OptimusConfig.getValue("main_server");
        UserCoinDo userCoinDo = workTableDao.getCoin(userPhone);
        int coinNum=0;
        if (userCoinDo!=null){
            coinNum = userCoinDo.getCoinCount();
        }
        ElementsDo elementsDo2 = new ElementsDo();
        elementsDo2.setTitle("我的金币");
        elementsDo2.setDisplay_num(coinNum);
        elementsDo2.setIosProtocol("cheniu://open/webv?url=" + reqUrl +"/coin/get_login_url?token=" + token);
        elementsDo2.setAndroidProtocol("cheniu://open/webv?url=" + reqUrl +"/coin/get_login_url?token=" + token);
        elementsDo2.setEventKey("CHENIU_MY_JINBI");
        elementsDo2.setProtocol("");

        //优惠券
        Double ticketsNumber = workTableSPI.getTicketsNumber(userId);
        ElementsDo elementsDo3 = new ElementsDo();
        elementsDo3.setTitle("优惠券");
        elementsDo3.setDisplay_num(ticketsNumber);
        elementsDo3.setIosProtocol("cheniu://open/couponList");
        elementsDo3.setAndroidProtocol("cheniu://open/couponList");
        elementsDo3.setEventKey("CHENIU_MY_JINBI");
        elementsDo3.setProtocol("CHENIU_MY_YOUHUIQUAN");

        elementsDoList.add(elementsDo1);
        elementsDoList.add(elementsDo2);
        elementsDoList.add(elementsDo3);

        return elementsDoList;
    }

    /**
     * 车行资料，交易管理，员工管理
     * @param shopCode 店铺code
     * @return list
     */
    private List<ElementsDo2> getElements1(String shopCode){

        logger.info("车行资料，交易管理，员工管理的请求参数为shopCode={}",shopCode);
        List<ElementsDo2> elementsList = new ArrayList<ElementsDo2>();

        //车行资料
        ElementsDo2 element1 = new ElementsDo2("车行资料","","http://img.souche.com/20170214/png/439fb53c3b383871cdab3c4c619c2c62.png",
                "CHENIU_MY_STORE","cheniu://open/dealershipInfo","cheniu://open/dealershipInfo",
                0,0,"","");
        Shop shopInfo = getShopInfo(shopCode);
        ShopAudit shopAudit = getAuditStatus(shopCode);
        if (shopInfo.getAuthStatus() != 1){ //未通过车行认证
            int reviewStatus = -1;
            if (shopAudit != null){
                reviewStatus = shopAudit.getReviewStatus();
            }
            if (reviewStatus ==0){
                element1.setSubtitle("审核中...");
            }else if (reviewStatus ==2){
                element1.setSubtitle("审核不通过");
            }else {
                element1.setSubtitle("认证车行，完善信息");
            }
        }else {
            element1.setSubtitle(shopInfo.getName());
        }

        //交易管理
        ElementsDo2 element2 = new ElementsDo2("交易管理","","http://img.souche.com/20170112/png/e8a02e47f15682e1dfad73fae8196b04.png",
                "CHENIU_MY_TRADEMANAGE","cheniu://open/transferManagement","cheniu://open/my/tansferManagement",
                0,0,"","");
        //员工管理
        ElementsDo2 element3 = new ElementsDo2("员工管理","","http://img.souche.com/20170112/png/de7aab8f7944c6c8a101ef67f074cc9c.png",
                "CHENIU_MY_PARENT_CHILD_ACCOUNT","cheniu://open/staffManage","cheniu://open/staffManage",
                0,0,"","");

        elementsList.add(element1);
        elementsList.add(element2);
        elementsList.add(element3);

        return elementsList;

    }

    /**
     * 我的收藏，我的订阅，我的足迹，服务工具，开通业务
     * @return list
     */
    private List<ElementsDo2> getElements2(){

        logger.info("我的收藏，我的订阅，我的足迹，服务工具，开通业务查询开始");
        List<ElementsDo2> elementsList = new ArrayList<ElementsDo2>();

        ElementsDo2 element1 = new ElementsDo2("我的收藏","","http://img.souche.com/20170112/png/8a7206d04beb2320b8b681f1914502d3.png",
                "CHENIU_MY_MYCOLLECT","cheniu://open/collectionList","cheniu://open/car/collected",
                0,0,"","");

        String userId = AuthNHolder.userId();
        String cache = cacheService.get("unread_collection_price_reduction_" + userId);
        if (StringUtils.isNotEmpty(cache)){
            int carCollection = cache.split(",").length;
            element1.setNoticeMsgNotice(1);
        }
        ElementsDo2 element2 = new ElementsDo2("我的订阅","","http://img.souche.com/37501adffbd35cfc5a41895212fedde0.png",
                "CHENIU_WD_WDDY","cheniu://open/subscribeCarList?type=0","cheniu://open/subscribeCarList?type=0",
                0,0,"","");

        ElementsDo2 element3 = new ElementsDo2("我的足迹","","http://img.souche.com/a63c833e3068ade7690057f3333c8929.png",
                "CHENIU_WD_WDZJ","cheniu://open/footMark","cheniu://open/footMark",
                0,0,"","");

        String requrl = OptimusConfig.getValue("f2eSite");
        ElementsDo2 element4 = new ElementsDo2("服务工具","","http://img.souche.com/20170112/png/e51c7cc2848821d465e6f87c2292c3f5.png",
                "CHENIU_MY_TOOLS","cheniu://open/webv?url=" + requrl + "/cheniu/tools/index",
                "cheniu://open/webv?url=" + requrl + "/cheniu/tools/index",
                0,0,"","");

        ElementsDo2 element5 = new ElementsDo2("开通业务","","http://img.souche.com/20170112/png/e28bb95ac2108353096edee76297ff26.png",
                "CHENIU_MY_KAITONGYEWU","cheniu://open/webv?url=" + requrl + "/cheniu/open-business/index",
                "cheniu://open/webv?url=" + requrl + "/cheniu/open-business/index",
                0,0,"","");

        elementsList.add(element1);
        elementsList.add(element2);
        elementsList.add(element3);
        elementsList.add(element4);
        elementsList.add(element5);

        return elementsList;
    }

    /**
     * 设置
     * @return list
     */
    private List<ElementsDo2> getElements3(){

        logger.info("设置信息查询。。。");
        ArrayList<ElementsDo2> elementsList = new ArrayList<>();

        ElementsDo2 element = new ElementsDo2("设置","","http://img.souche.com/20170112/png/1afeca22d32a7180e1e335ff95ab9fe8.png",
                "CHENIU_MY_SET","cheniu://open/setting","cheniu://open/setting",
                0,0,"","");

        elementsList.add(element);

        return elementsList;
    }

    /**
     * 获取shop信息
     * @param shopCode 店铺code
     * @return shop
     */
    private Shop getShopInfo(String shopCode){

        //查询缓存
        Shop shopInfo =  cacheService.getObject("user_shop_detail_"+shopCode, Shop.class);
        if (shopInfo != null){
            return shopInfo;
        }

        //缓存不存在，调服务取回数据放在缓存中
        shopInfo = workTableSPI.getShopInfo(shopCode);
        cacheService.setObject("user_shop_detail_"+shopCode, shopInfo,600);

        return shopInfo;
    }

    /**
     * 获取ShopAudit信息
     * @param shopCode 店铺code
     * @return shopAudit
     */
    private ShopAudit getAuditStatus(String shopCode){

        //查询缓存
        ShopAudit shopAudit = cacheService.getObject("user_shop_certif_" + shopCode, ShopAudit.class);
        if (shopAudit != null){
            return shopAudit;
        }

        //缓存不存在，调服务取回数据放在缓存中
        shopAudit = workTableSPI.getShopAuditByCode(shopCode);
        cacheService.setObject("user_shop_certif_" + shopCode, shopAudit,600);

        return shopAudit;

    }

}
