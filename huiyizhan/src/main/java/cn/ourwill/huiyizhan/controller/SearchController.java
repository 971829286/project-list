package cn.ourwill.huiyizhan.controller;

import cn.ourwill.huiyizhan.entity.ActivityStatistics;
import cn.ourwill.huiyizhan.entity.SearchBean;
import cn.ourwill.huiyizhan.entity.SearchIndex;
import cn.ourwill.huiyizhan.interceptor.Access;
import cn.ourwill.huiyizhan.service.impl.ActivityStatisticsServiceImpl;
import cn.ourwill.huiyizhan.service.search.IElasticSearchService;
import cn.ourwill.huiyizhan.service.search.ISearchService;
import cn.ourwill.huiyizhan.utils.CamelCaseUtil;
import cn.ourwill.huiyizhan.utils.ReturnResult;
import cn.ourwill.huiyizhan.utils.ReturnType;
import cn.ourwill.huiyizhan.utils.TimeUtils;
import cn.ourwill.huiyizhan.utils.page.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.ourwill.huiyizhan.entity.Config.bucketDomain;
import static cn.ourwill.huiyizhan.entity.Config.willcenterBucketDomain;

@RestController
@RequestMapping("/api/search")
@Slf4j
public class SearchController {
    private String[] column = {"activity_title", "activity_address", "activity_description"
            , "nick_name"};
    @Autowired
    TransportClient client;

    @Autowired
    ActivityStatisticsServiceImpl activityStatisticsService;

    @Autowired
    IElasticSearchService elasticSearchService;
    @Autowired
    ISearchService searchService;
    /**
     * 条件查询
     *
     * @param field
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param range
     * @param preTags
     * @param postTags
     * @return
     */
    @RequestMapping("/activity/conditionalQuery")
    public Map searchByKeyWord(@RequestParam(value = "field",defaultValue = "activityTitle") String field,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               @RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "range", defaultValue = "null") String range,
                               @RequestParam(value = "preTags",required = false) String preTags,
                               @RequestParam(value = "postTags",required = false) String postTags,
                               @RequestParam(value = "isSort", defaultValue = "true") boolean isSort,
                               @RequestParam(value = "isMobile", defaultValue = "false") boolean isMobile,
                               @RequestParam(value = "typeName",required = false) String typeName
    ) {
        Integer from = pageSize * (pageNum - 1);//当前页的起始偏移
        HighlightBuilder highlightBuilder = null;
        //初始化前端传来的高亮标签,如果为空,则不进行高亮
        if (StringUtils.isNotEmpty(preTags) && StringUtils.isNotEmpty(postTags)) {
            highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
            highlightBuilder.preTags(preTags);
            highlightBuilder.postTags(postTags);
        }
        //设置排序
        SortBuilder sortBuilder = SortBuilders.fieldSort(SearchIndex.START_TIME).order(SortOrder.DESC);
        //初始化搜索范围
        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch(SearchIndex.INDEX);
        searchRequestBuilder.setTypes(SearchIndex.TYPE);

        if (highlightBuilder != null) {
            searchRequestBuilder.highlighter(highlightBuilder);
        }
        String[] needHighLightFields; //高亮字段
        //手机端
        if (isMobile) {
            //手机端匹配所有字段
            MultiMatchQueryBuilder queryMobile = QueryBuilders.multiMatchQuery(keyword,column);
            needHighLightFields = this.column;
            searchRequestBuilder.setQuery(queryMobile);
            searchRequestBuilder.addSort(sortBuilder);
        } //手机端if 结束
        else {
//            String fieldLine = CamelCaseUtil.toLine(field);
            String fieldLine = field;
            //PC端
            needHighLightFields = new String[]{fieldLine};
            //设置查询-PC端
//            BoolQueryBuilder shouldPC = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(fieldLine, keyword));
            BoolQueryBuilder queryPC = QueryBuilders.boolQuery();
            MatchQueryBuilder keywordQuery = null;
//            MatchPhraseQueryBuilder keywordQuery = null;
//            MatchPhrasePrefixQueryBuilder keywordQuery = null;
//            TermQueryBuilder keywordQuery = null;
            if(StringUtils.isNotEmpty(keyword)){
                keywordQuery = QueryBuilders.matchQuery(fieldLine, keyword);
            }
            MatchQueryBuilder typeNameQuery = null;
            if(StringUtils.isNotEmpty(typeName) && !"所有活动".equals(typeName)){
                typeNameQuery  = QueryBuilders.matchQuery(SearchIndex.TYPE_NAME, typeName);
            }
            //组装查询开始
            if(keywordQuery != null){
                queryPC.must(keywordQuery);
            }
            //时间条件过滤

//            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("startTime").gte(TimeUtils.calDate(0)).lte("now"+range);
            //如果range字段为空,则不进行时间过滤
            RangeQueryBuilder rangeQuery = null;
            if (range.indexOf("null") == -1) {
                rangeQuery = QueryBuilders.rangeQuery(SearchIndex.START_TIME).format("yyyy-MM-dd")
                        .gte(TimeUtils.calDate(0)).
                                lte(TimeUtils.calDate(Integer.parseInt(range)));
                queryPC.must(rangeQuery);
//                shouldPC.must(rangeQuery);
            }
            if(typeNameQuery != null){
                queryPC.must(typeNameQuery);
//                shouldPC.must(typeNameQuery);
            }
            if (isSort) {
                searchRequestBuilder.addSort(sortBuilder);
            }//组装查询结束
            searchRequestBuilder.setFrom(from);
            searchRequestBuilder.setSize(pageSize);
            searchRequestBuilder.setQuery(queryPC);
        }//pc端else结束

        //执行查询动作
        SearchResponse response = searchRequestBuilder.get();
        Long totalHits = response.getHits().totalHits;
        if (totalHits == 0 || response == null) {
            return ReturnResult.errorResult("查询结果不存在");
        }
        List<Map<String, Object>> res = new ArrayList<>();
        //处理数据开始for
        for (SearchHit hit : response.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> source = hit.getSource();
            //处理高亮 获取高亮字符串
            if (highlightFields != null && highlightFields.size() != 0) {
                //String[] needHighLightFields = needHighLightFields;
                for (String needHighLightField : needHighLightFields) {
                    HighlightField titleField = highlightFields.get(needHighLightField);
                    if (titleField != null) {
                        Text[] fragments = titleField.fragments();
                        if (fragments != null && fragments.length != 0) {
                            StringBuilder name = new StringBuilder();
                            for (Text text : fragments) {
                                name.append(text);
                            }
                            source.put(needHighLightField, name.toString());
                        }
                    }
                }
            }
            //处理拼接url
            String activityBannerTemp = (String) hit.getSource().get("activityBanner");
            if (StringUtils.isNotEmpty(activityBannerTemp) && activityBannerTemp.indexOf("http") < 0) {
                String activityBanner = bucketDomain + activityBannerTemp;
                hit.getSource().replace("activityBanner", activityBanner);
            }
            String activityBannerMobileTemp = (String) hit.getSource().get("activityBannerMobile");
            if (StringUtils.isNotEmpty(activityBannerMobileTemp) && activityBannerMobileTemp.indexOf("http") < 0) {
                String activityBannerMobile = bucketDomain + activityBannerMobileTemp;
                hit.getSource().replace("activityBannerMobile", activityBannerMobile);
            }
            String avatarTemp = (String) hit.getSource().get("avatar");

            if ( avatarTemp != null && avatarTemp.indexOf("http") < 0) {
                String avatar = willcenterBucketDomain + avatarTemp;
                hit.getSource().replace("avatar", avatar);

            }else if(avatarTemp == null){
                String avatar = willcenterBucketDomain + "group1/M00/00/07/rBAAlFs0TvSAf3e8AAAB-a-PFXI164.png";
                hit.getSource().replace("avatar", avatar);
            }
            String usernameTemp = (String) hit.getSource().get("userName");
            if (StringUtils.isNotEmpty(usernameTemp) && usernameTemp.length()>3){
                hit.getSource().replace("userName", usernameTemp.replace(usernameTemp.substring(1, usernameTemp.length()-1), "***"));
            }
            //获取会议ID 从redis取出观看人数,收藏
            Integer activityId = (Integer) hit.getSource().get("id");
            ActivityStatistics activityStatistics =
                    activityStatisticsService.getActivityStatisticsFromRedis(activityId);
            hit.getSource().put("statistics",activityStatistics);

            res.add(hit.getSource());
        }//处理数据结束for
        //分页
        PageInfo pageInfo = new PageInfo(res, pageNum, pageSize, totalHits);
        return ReturnResult.successResult("data", pageInfo, ReturnType.GET_SUCCESS);
    }


    @PostMapping("/activity/importAll")
    @Access(level = 1)
    public Map importAll(){
        this.elasticSearchService.importAll();
        return ReturnResult.successResult("OK");
    }

    @DeleteMapping("/activity/deleteAll")
    @Access(level = 1)
    public Map deleteAll(){
        this.elasticSearchService.deleteAll();
        return ReturnResult.successResult("OK");
    }
}
