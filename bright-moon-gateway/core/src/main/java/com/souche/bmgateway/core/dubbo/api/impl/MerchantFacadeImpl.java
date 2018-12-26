package com.souche.bmgateway.core.dubbo.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.souche.bmgateway.core.domain.MerchantSettleFlow;
import com.souche.bmgateway.core.enums.ErrorCodeEnums;
import com.souche.bmgateway.core.repo.MerchantSettleFlowRepository;
import com.souche.bmgateway.dubbo.api.MerchantFacade;
import com.souche.bmgateway.model.request.MerchantQueryRequest;
import com.souche.bmgateway.model.response.MerchantResultResponse;
import com.souche.optimus.exception.OptimusExceptionBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 商户入驻结果查询
 *
 * @author chenwj
 * @since 2018/7/24
 */
@Service("merchantFacade")
@Slf4j(topic = "dubbo.impl")
public class MerchantFacadeImpl implements MerchantFacade {

    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private MerchantSettleFlowRepository repository;

    /**
     * 商户入驻结果查询
     *
     * @param request 请求参数
     * @return MerchantResultResponse
     */
    @Override
    public MerchantResultResponse queryMerchantResult(MerchantQueryRequest request) {
        log.info("<商户入驻结果查询>开始，请求参数 ->{}", request.toString());
        MerchantSettleFlow result;
        try {
            // 若5分钟内再次请求则从缓存中获取数据
            if (redisTemplate.hasKey(request.getMemberId())) {
                JSONObject json = JSONObject.parseObject(redisTemplate.opsForValue().get(request.getMemberId()));
                log.info("<商户入驻结果查询>从缓存中获取数据：{}", json.toJSONString());
                return MerchantResultResponse.success(json.getString("status"), json.getString("outMerchantId"));
            }
            // 若缓存查不到去数据库查并放到缓存
            MerchantSettleFlow flow = new MerchantSettleFlow();
            flow.setMemberId(request.getMemberId());
            result = repository.selectByMemberIdLimit(flow);
            if (result == null) {
                log.error("<商户入驻结果查询>查询数据库记录为空");
                return MerchantResultResponse.fail(ErrorCodeEnums.RET_OBJECT_IS_NULL.getCode(),
                        "<商户入驻结果查询>查询数据库记录为空");
            }
            log.info("<商户入驻结果查询>从数据库中获取数据：{}", result.toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", result.getStatus());
            if (!StringUtils.isBlank(result.getOutMerchantId())) {
                jsonObject.put("outMerchantId", result.getOutMerchantId());
            } else {
                jsonObject.put("outMerchantId", "");
            }
            log.info("<商户入驻结果查询>设置缓存：{}", jsonObject.toJSONString());
            redisTemplate.opsForValue().set(request.getMemberId(), jsonObject.toJSONString(), 5, TimeUnit.MINUTES);

        } catch (OptimusExceptionBase e) {
            log.error("<商户入驻结果查询>异常", e.getMessage());
            return MerchantResultResponse.fail(ErrorCodeEnums.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        log.info("<商户入驻结果查询>响应：{}，查询结束...");
        return MerchantResultResponse.success(result.getStatus(), result.getOutMerchantId());
    }

}
