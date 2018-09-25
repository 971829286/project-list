package cn.ourwill.huiyizhan.service.search;

import cn.ourwill.huiyizhan.entity.SearchBean;
import cn.ourwill.huiyizhan.entity.SearchIndex;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: XuJinNiu
 * @create: 2018-05-07 11:11
 **/
@Service
@Slf4j
public class ElasticSearchService implements IElasticSearchService {

    @Autowired
    ObjectMapper    objectMapper;
    @Autowired
    TransportClient transportClient;
    @Autowired
    ISearchService  searchService;

    @Override
    public boolean importAll() {
        //deleteAll
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(this.transportClient)
                .filter(QueryBuilders.matchAllQuery())
                .source(SearchIndex.INDEX)
                .get();
        //importAll
        List<SearchBean> allSearchBean = this.searchService.getAllSearchBean();
        BulkRequestBuilder bulkRequestBuilder = this.transportClient.prepareBulk();
        try {
            for (SearchBean searchBean : allSearchBean) {
                bulkRequestBuilder.add(this.transportClient.prepareIndex(SearchIndex.INDEX, SearchIndex.TYPE)
                        .setId(searchBean.getId().toString())
                        .setSource(this.objectMapper.writeValueAsBytes(searchBean), XContentType.JSON));
            }
        } catch (JsonProcessingException e) {
            log.info("ElasticSearchService.importAll", e);
            return false;
        }
        BulkResponse res = bulkRequestBuilder.get();
        return res.status() == RestStatus.OK;
    }

    @Override
    public boolean insert(SearchBean searchBean) {
        if (searchBean == null) {
            log.info("ElasticSearchService.insert: searchBean is null");
            return false;
        }
        IndexResponse response = null;
        try {
            response = this.transportClient.prepareIndex(SearchIndex.INDEX, SearchIndex.TYPE)
                    .setId(searchBean.getId().toString())
                    .setSource(this.objectMapper.writeValueAsBytes(searchBean), XContentType.JSON).get();
        } catch (Exception e) {
            log.info("ElasticSearchService.insert", e);
            return false;
        }
        return response.status() == RestStatus.CREATED;
    }

    @Override
    public boolean inserts(List<SearchBean> searchBeans) {
        if(searchBeans == null || searchBeans.size() == 0){
            log.info("ElasticSearchService.inserts:searchBeans is null or size = 0");
            return false;
        }
        for (SearchBean searchBean : searchBeans){
            insert(searchBean);
        }
        return true;
    }

    /**
     * 通过activityId删除
     *
     * @param activityId
     * @return
     */
    @Override
    public boolean deleteByActivityId(Integer activityId) {
        if (activityId == null || activityId < 0) {
            log.info("ElasticSearchService.deleteByActivityId:activityId is null or < 0");
            return false;
        }
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(this.transportClient)
                .filter(QueryBuilders.termQuery(SearchIndex.ID, activityId))
                .source(SearchIndex.INDEX).get();
        return response.getStatus().getDeleted() > 0;

    }

    /**
     * 通过userId删除
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteByUserId(Integer userId) {
        if (userId == null || userId < 0) {
            log.info("ElasticSearchService.deleteByUserId:userId is null or < 0");
            return false;
        }
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(this.transportClient)
                .filter(QueryBuilders.termQuery(SearchIndex.USER_ID, userId))
                .source(SearchIndex.INDEX)
                .get();
        return response.getStatus().getDeleted() > 0;
    }

//    @Override
//    public boolean isExist(Integer activityId) {
//        if(activityId == null || activityId < 0){
//            return false;
//        }
//        GetResponse response = this.transportClient
//                .prepareGet(SearchIndex.INDEX, SearchIndex.TYPE, activityId.toString())
//                .get();
//        return response.isExists();
//
//    }

//    @Override
//    @Deprecated
//    public boolean updateByActivityId(Integer activityId) {
//        UpdateByQueryAction.INSTANCE
//                .newRequestBuilder(this.transportClient)
//                .filter(QueryBuilders.termQuery(SearchIndex.ID, activityId))
//                .refresh(true);
//        return true;
//    }
//
//    @Override
//    @Deprecated
//    public boolean updateByUserId(Integer userId) {
//        return false;
//    }

    @Override
    @Deprecated
    public boolean deleteAll() {
                DeleteByQueryRequestBuilder source = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(this.transportClient)
                .filter(QueryBuilders.matchAllQuery())
                .source(SearchIndex.INDEX);
        BulkByScrollResponse response= source.get();
        return  response.getStatus().getDeleted()>0;
    }

}
