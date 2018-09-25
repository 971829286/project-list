package com.souche.bulbous.web.json;

import com.google.common.collect.Maps;
import com.souche.bulbous.api.CarService;
import com.souche.bulbous.excel.ExcelConstant;
import com.souche.bulbous.exception.CustomException;
import com.souche.bulbous.service.BulbousCarService;
import com.souche.bulbous.vo.CarStatisticBean;
import com.souche.optimus.common.page.Page;
import com.souche.optimus.common.util.JsonUtils;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.sad.AbstractReactAction;
import com.souche.optimus.core.sad.Props;
import com.souche.optimus.core.sad.ReactAction;
import com.souche.optimus.core.sad.State;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 车辆数据交互类
 *
 * @author XadomGreen
 * @since 2018-08-17
 */
@View("CarPage")
public class CarPage extends AbstractReactAction {

    @Autowired
    private BulbousCarService bulbousCarService;

    private Logger logger = LoggerFactory.getLogger(CarPage.class);

    @Autowired
    private CarService carServciceImpl;

    /**
     * 初始化车辆数据
     *
     * @param props
     * @return
     */
    @Override
    public Map<String, Object> init(Props props) {
        logger.info("--> 初始化车辆数据...");

        Map<String, Object> result = Maps.newHashMap();
        if (carServciceImpl == null) {
            logger.error("--> 车辆服务不可用[can not found car Service provided!]...");
            result.put("result", new Page<CarStatisticBean>(0, 1, ExcelConstant.PAGE_SIZE, null));
            return result;
        }

        List<CarStatisticBean> list = carServciceImpl.getCatList(null, null, null, null, null, 1, ExcelConstant.PAGE_SIZE);
        if (list != null && list.size() >= 0) {
            result.put("result", new Page<CarStatisticBean>(carServciceImpl.getCarListCount(null, null, null, null, null),
                    1, ExcelConstant.PAGE_SIZE, list));
        }
        return result;
    }

    /**
     * 获取车辆列表
     */
    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取列表")
    public void getList() {
        logger.info("--> 获取车辆数据列表");

        // 查询参数
        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("query");
        String beginDate = MapUtils.getString(query, "beginDate");
        String endDate = MapUtils.getString(query, "endDate");
        Integer page = MapUtils.getInteger(query, "page");
        Integer pageSize = MapUtils.getInteger(query, "pageSize");
        String userPhone = MapUtils.getString(query, "userPhone");
        String nickName = MapUtils.getString(query, "nickName");
        String modeName = MapUtils.getString(query, "modeName");

        logger.info("--> 查询参数为： [{}]", JsonUtils.toJson(query).toString());

        // 默认为第一页，每页显示50条
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = ExcelConstant.PAGE_SIZE;
        }

        if (carServciceImpl == null) {
            logger.error("--> 车辆服务不可用[can not found car Service provided!]...");
            state.set("result", new Page<CarStatisticBean>(0, 1, ExcelConstant.PAGE_SIZE, null));
        } else {
            List<CarStatisticBean> list = carServciceImpl.getCatList(userPhone, nickName, modeName, beginDate, endDate, page, pageSize);
            int totalNumber = carServciceImpl.getCarListCount(userPhone, nickName, modeName, beginDate, endDate);
            state.set("result", new Page<CarStatisticBean>(totalNumber, page, ExcelConstant.PAGE_SIZE, list));
            logger.info("--> 数据总数为[{}]条，data为:{}", totalNumber, JsonUtils.toJson(state.get("result")).toString());
        }

    }

    /**
     * 导出列表数据到Excel
     */
    @SuppressWarnings("unchecked")
    @ReactAction(desc = "导出Excel")
    public void exprot() {
        logger.info("--> 导出列表数据到Excel");

        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("query");
        String beginDate = MapUtils.getString(query, "beginDate");
        String endDate = MapUtils.getString(query, "endDate");
        Integer page = MapUtils.getInteger(query, "page");
        Integer pageSize = MapUtils.getInteger(query, "pageSize");
        String userPhone = MapUtils.getString(query, "userPhone");
        String nickName = MapUtils.getString(query, "nickName");
        String modeName = MapUtils.getString(query, "modeName");

        // 默认为第一页，每页显示50条
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = ExcelConstant.PAGE_SIZE;
        }

        // 服务是否可用
        if (carServciceImpl == null) {
            logger.error("--> 车辆服务不可用[can not found car Service provided!]...");
            throw new CustomException("车辆服务不可用！");
        }

        state.set("job", bulbousCarService.exportExcel(userPhone, nickName, modeName, beginDate, endDate, page, pageSize));
    }

    /**
     * 导出进度查询
     */
    @SuppressWarnings("unchecked")
    @ReactAction(desc = "导出进度")
    public void progress() {
        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("job");
        String jobId = MapUtils.getString(query, "jobId");
        state.set("job", bulbousCarService.getProgressByJobId(jobId));
    }
}
