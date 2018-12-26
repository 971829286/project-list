package com.souche.bmgateway.core.manager.shop;

import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.enums.ShopInfoEnums;
import com.souche.bmgateway.core.exception.ManagerException;
import com.souche.bmgateway.core.util.HttpClientUtil;
import com.souche.optimus.common.config.OptimusConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 店铺服务实现类
 *
 * @author chenwj
 * @since 2018/7/19
 */
@Service("shopManager")
@Slf4j(topic = "manager")
public class ShopManagerImpl implements ShopManager {

    /**
     * 店铺服务HTTP接口请求地址
     */
    private static final String SHOP_URL = OptimusConfig.getValue("shop.http.url");

    /**
     * 查询企业信息
     *
     * @param shopCode 内部商户号
     * @return HttpBaseResponse
     * @throws ManagerException 自定义Manager异常
     */
    @Override
    public HttpBaseResponse queryShopInfo(String shopCode) throws ManagerException {
        HttpBaseResponse response = new HttpBaseResponse();
        try {
            String shopAuth = HttpClientUtil.sendPost(SHOP_URL, "shopCode=" + shopCode);
            if (StringUtils.isBlank(shopAuth)) {
                log.info("<查询商户信息>请求参数->{}，返回为空", shopCode);
                return response;
            }
            Map<String, Object> map = new HashMap<>(16);
            map.put(ShopInfoEnums.SHOP_AUTH.getValue(), shopAuth);
            response.setData(map);
            response.setSuccess(true);
            log.info("<查询商户信息>HTTP响应->{}", response.toString());
            return response;

        } catch (Exception e) {
            throw new ManagerException(ErrorCodeEnums.SYSTEM_ERROR);
        }
    }

}
