package com.souche.niu.spi.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.souche.coupon.api.CouponApiService;
import com.souche.coupon.constant.UserTypeEnum;
import com.souche.coupon.model.dto.CouponListDTO;
import com.souche.coupon.model.dto.TabCountDTO;
import com.souche.coupon.result.CommonResult;
import com.souche.niu.model.workTable.ShopMemberDo;
import com.souche.niu.spi.WorkTableSPI;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.http.HttpClientGetUtil;
import com.souche.optimus.common.util.http.HttpClientPostUtil;
import com.souche.scashier.sdk.enums.MemberTypeEnums;
import com.souche.scashier.sdk.facade.wallet.WalletMemberFacade;
import com.souche.scashier.sdk.request.wallet.member.QueryBalanceRequest;
import com.souche.scashier.sdk.request.wallet.member.entity.MemberEntity;
import com.souche.scashier.sdk.response.wallet.member.QueryBalanceResponse;
import com.souche.scashier.sdk.response.wallet.member.entity.BalanceAccountEntity;
import com.souche.shop.api.ShopAuditService;
import com.souche.shop.api.ShopMemberService;
import com.souche.shop.api.ShopService;
import com.souche.shop.model.Shop;
import com.souche.shop.model.ShopAudit;
import com.souche.shop.model.ShopMember;
import com.souche.sso.client2.AuthNHolder;
import com.souche.user.hessian.api.UserAPI;
import com.souche.user.hessian.model.Result;
import com.souche.user.hessian.model.UserObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WorkTableSPIAdapter implements WorkTableSPI {


    private static final Logger logger = LoggerFactory.getLogger(WorkTableSPIAdapter.class);

    @Resource
    private ShopMemberService shopMemberService;

    @Resource
    private ShopService shopService;

    @Resource
    private ShopAuditService shopAuditService;

    @Resource
    private UserAPI userAPI;

    @Autowired
    private CouponApiService couponApiService;

    @Autowired
    private WalletMemberFacade walletMemberFacade;


    /**
     * 获取shopmember信息
     * @param userAccount 用户账户
     * @return ShopMemberDo
     */
    public ShopMemberDo getShopMember(String userAccount) {

        ShopMemberDo shopMemberDo = new ShopMemberDo();
        ShopMember shopMember = null;
        try{
            logger.info("获取shopMember查询参数为userAccount={}",userAccount);
            shopMember = shopMemberService.getUserBelongShop(userAccount);
            logger.info("返回的shopMember数据为{}",shopMember);
        }catch (Exception e){
            logger.error("获取shopMember信息异常e=",e);
        }
        if (shopMember == null){
            shopMemberDo.setRemarkName("");
            shopMemberDo.setShopCode(userAccount);
            shopMemberDo.setRole(1);
            shopMemberDo.setRoleName("店长");
            shopMemberDo.setUserAccount(userAccount);
            shopMemberDo.setStatus(404);
            shopMemberDo.setName(null);
        }else {
            shopMemberDo.setRemarkName(shopMember.getRemarkName());
            shopMemberDo.setShopCode(shopMember.getShopCode());
            shopMemberDo.setRole(shopMember.getRole());
            shopMemberDo.setRoleName(shopMember.getRole()==1 ? "店长" : "店员");
            shopMemberDo.setUserAccount(shopMember.getUserAccount());
            shopMemberDo.setStatus(shopMember.getStatus());
        }

        return shopMemberDo;
    }

    /**
     * 根据手机号获取用户信息
     * @param phone 手机
     * @param appName app名称 cheniu/dafengche
     * @return UserObject
     */
    public UserObject getUser(String phone, String appName) {
        try{
            logger.info("查询用户参数phone={},appName={}",phone,appName);
            Map<String,Object> map= Maps.newHashMap();
            map.put("phone",phone);
            UserObject userObject = userAPI.findOne(JsonUtils.toJson(map), appName);
            logger.info("返回的用户查询数据为userObject={}",userObject);
            return userObject;
        }catch (Exception e){
            logger.error("返回用户数据时异常 e=",e);
            return null;
        }
    }

    /**
     * 根据token获取user信息
     * @param token 用户token
     * @return Result
     */
    public Result findUserByToken(String token){

        Result<Map<String, Object>> result = userAPI.findUserInfoByToken(token);
        return result;
    }

    /**
     * 根据shopcode获取shop信息
     * @param shopCode 店铺code
     * @return Shop
     */
    public Shop getShopInfo(String shopCode){
        try {
            Shop shop = shopService.loadShopByCode(shopCode);
            return shop;
        }catch (Exception e){
            logger.error("获取shop信息异常e=",e);
            return null;
        }
    }

     /**
     * 根据shop code加载车行认证信息
     * @param shopCode 店铺code
     * @return ShopAudit
     */
    public ShopAudit getShopAuditByCode(String shopCode) {
        try {
            logger.info("车行认证信息查询参数为shopCode={}",shopCode);
            ShopAudit shopAudit = shopAuditService.loadAuditByShopCode(shopCode);
            logger.info("车行查询信息为shopAudit={}",shopAudit);
            return shopAudit;
        }catch (Exception e){
            logger.error("加载车行认证信息异常e=",e);
            return null;
        }
    }

    /**
     * 用户钱包余额
     * @return
     */
    public Double getWalletBalance() {

        try{
            logger.info("获取账户钱包余额。。。");
            String balance = "0.00";
            String shopCode = AuthNHolder.shopCode();
            if (StringUtils.isEmpty(shopCode)){
                shopCode = AuthNHolder.userPhone();
            }
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setMemberType(MemberTypeEnums.TYPE_SHOP_CODE);
            memberEntity.setMemberValue(shopCode);
            //查询条件
            QueryBalanceRequest queryBalanceRequest = new QueryBalanceRequest();
            queryBalanceRequest.setMemberEntity(memberEntity);
            //调用服务查询余额信息
            QueryBalanceResponse queryBalanceResponse = walletMemberFacade.queryBalance(queryBalanceRequest);
            List<BalanceAccountEntity> balanceAccountEntityList = queryBalanceResponse.getBalanceAccountEntityList();
            if (balanceAccountEntityList ==null || balanceAccountEntityList.size()==0){
                return Double.parseDouble(balance);
            }
            for (BalanceAccountEntity balanceAccountEntity : balanceAccountEntityList) {
                if (balanceAccountEntity.getAccountType().equals("201")){ //201 企业用户基本户
                    if (balanceAccountEntity.getBalance() != null || !balanceAccountEntity.getBalance().isEmpty()){
                        balance = balanceAccountEntity.getBalance();
                    }
                }
            }
            Double welletNum = Double.parseDouble(balance);
            logger.info("账户钱包余额为welletNum={}",welletNum);
            return welletNum;
        }catch (Exception e){
            logger.error("会员账单服务请求异常e=",e);
            return 0.0;
        }
    }

    /**
     * 优惠券数量
     * @param userId
     * @return
     */
    public Double getTicketsNumber(String userId) {

        try{
            logger.info("优惠券查询参数为userId={}",userId);

            int totalNum=0;

            CommonResult<TabCountDTO> result = couponApiService.userCouponsCount(userId, UserTypeEnum.cheniu_user, null);
            int expiredNum= result.getData().getExpiredCount();
            int receivedNum= result.getData().getReceivedCount();
            int usedNum= result.getData().getUsedCount();

            totalNum = receivedNum + expiredNum + usedNum;
            logger.info("优惠券查询结果为totalNum={}",totalNum);
            return Double.valueOf(totalNum);
        }catch (Exception e){
            logger.error("优惠券获取服务异常 e=",e);
            return 0.0;
        }
    }
}
