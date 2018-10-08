package com.souche.bulbous.web.json;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.souche.bulbous.api.QuoteService;
import com.souche.bulbous.excel.ExcelConstant;
import com.souche.bulbous.vo.QuoteBean;
import com.souche.bulbous.service.BulbousQuoteService;
import com.souche.bulbous.utils.MockDataUtil;
import com.souche.optimus.common.page.Page;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.sad.AbstractReactAction;
import com.souche.optimus.core.sad.Props;
import com.souche.optimus.core.sad.ReactAction;
import com.souche.optimus.core.sad.State;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 报价数据交互类
 *
 * @author XadomGreen
 * @since 2018-08-17
 */
@View("QuotePage")
public class QuotePage extends AbstractReactAction {

    private Logger logger = LoggerFactory.getLogger(QuotePage.class);

    @Autowired
    private BulbousQuoteService bulbousQuoteService;
    @Resource
    private QuoteService quoteService;

    private String carId = null;

    @Override
    public Map<String, Object> init(Props props) {

        logger.info("初始化报价列表");
        Map<String, Object> result = Maps.newHashMap();
        State state = super.getState();
        if (props.get("carId") != null) {
            carId = props.get("carId").toString();
            state.set("carId", carId);
        }

        logger.info("--> 初始化参数：[{}],[{}]", JsonUtils.toJson(props), carId);
        // 查询总记录数
        int totalNumber = quoteService.getQuoteListCount(carId, null, null, null, null, null);
        // 查询记录列表
        List<QuoteBean> list = null;
        if (quoteService != null) {
            list = quoteService.getQuoteList(carId, null, null, null, null, null, 1, ExcelConstant.PAGE_SIZE);
        }
        result.put("result", new Page<QuoteBean>(totalNumber, 1, ExcelConstant.PAGE_SIZE, list));
        return result;
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取列表")
    public void getList() {
        carId = null;
        logger.info("获取报价数jRrrEJFOuj62FNza据列表List");
        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("query");
        String beginDate = MapUtils.getString(query, "beginDate");
        String endDate = MapUtils.getString(query, "endDate");
        Integer page = MapUtils.getInteger(query, "page");
        Integer pageSize = MapUtils.getInteger(query, "pageSize");
        String userPhone = MapUtils.getString(query, "userPhone");
        String nickName = MapUtils.getString(query, "nickName");
        String modeName = MapUtils.getString(query, "modeName");
        // 查询列表不支持carId
        String carId = null;
        logger.info("--> 查询参数为： [{}]", JsonUtils.toJson(query).toString());

        // 查询总记录数
        int totalNumber = quoteService.getQuoteListCount(carId, userPhone, nickName, modeName, beginDate, endDate);
        // 查询记录列表
        List<QuoteBean> list = null;
        if (quoteService != null) {
            list = quoteService.getQuoteList(carId, userPhone, nickName, modeName, beginDate, endDate, page, pageSize);
        }
        state.set("result", new Page<QuoteBean>(totalNumber, page, ExcelConstant.PAGE_SIZE, list));

        logger.info("--> 查询结果为：共计[{}]条记录 [{}]", totalNumber, JsonUtils.toJson(state.get("result")).toString());
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "导出报价Excel")
    public void exportExcel() {
        logger.info("--> 导出报价Excel");
        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("query");
        String beginDate = MapUtils.getString(query, "beginDate");
        String endDate = MapUtils.getString(query, "endDate");
        Integer page = MapUtils.getInteger(query, "page");
        Integer pageSize = MapUtils.getInteger(query, "pageSize");
        String userPhone = MapUtils.getString(query, "userPhone");
        String nickName = MapUtils.getString(query, "nickName");
        String modeName = MapUtils.getString(query, "modeName");
        // 获取车辆编号
        /*Props props = super.getProps();
        String carId = null;
        if (props != null) {
            if (props != null) {
                carId = props.get("carId", String.class);
            }
        }*/

        logger.info("--> 查询参数为： [{}]", JsonUtils.toJson(query).toString());
        state.set("job", bulbousQuoteService.exportExcel(carId, userPhone, nickName, modeName, beginDate, endDate, page, pageSize));
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "导出进度")
    public void progress() {
        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("job");
        String jobId = MapUtils.getString(query, "jobId");
        state.set("job", bulbousQuoteService.getProgressByJobId(jobId));
    }

}
