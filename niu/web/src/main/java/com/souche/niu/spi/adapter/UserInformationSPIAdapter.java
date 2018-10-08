package com.souche.niu.spi.adapter;

import com.alibaba.fastjson.JSONObject;
import com.souche.adapter.search.api.SearchService;
import com.souche.adapter.search.common.SearchQuery;
import com.souche.adapter.search.common.SearchResponse;
import com.souche.niu.spi.UserInformationSPI;
import com.souche.optimus.common.config.OptimusConfig;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.common.util.http.HttpClientGetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInformationSPIAdapter implements UserInformationSPI {

    private static final Logger logger = LoggerFactory.getLogger(UserInformationSPIAdapter.class);

    @Autowired
    private SearchService searchService;

    /**
     * 车辆搜索服务
     * @param query
     * @param indexName
     * @return
     */
    public SearchResponse search(SearchQuery query, String indexName) {
        try {
            logger.info("车辆搜索服务，搜索条件为query={},index={}",JsonUtils.toJson(query),indexName);
            SearchResponse response = searchService.query(query, indexName);
            logger.info("车辆搜索服务，搜索结果为response={}", JsonUtils.toJson(response));
            return response;
        }catch (Exception e){
            logger.error("车辆搜索服务异常e=",e);
            return null;
        }
    }

    /**
     * 获取车店新访客
     * @param userId
     * @param shopCode
     * @return
     */
    public int getNewVistor(String userId, String shopCode) {
        try{
            logger.info("获取车店新访客的查询参数为userId={},shopCode=",userId,shopCode);
            int newVistor = 0;
            String azerothUrl = OptimusConfig.getValue("azerothUrl");
            String uri = azerothUrl + "/app/visitorcontroller/getUnread.json?" + "userId=" + userId + "&shopCode=" + shopCode;
            String response = HttpClientGetUtil.getUrl(uri);
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONObject data = (JSONObject) jsonObject.get("data");
            if (data!=null){
                newVistor = data.getInteger("result");
            }
            logger.info("车店新访客数量为newVistor={}",newVistor);
            return newVistor;
        }catch (Exception e){
            logger.error("查询车店新访客异常e=",e);
            return 0;
        }
    }
}
