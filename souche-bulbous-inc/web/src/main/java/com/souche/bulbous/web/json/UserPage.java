package com.souche.bulbous.web.json;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.souche.bulbous.api.UserService;
import com.souche.bulbous.excel.ExcelConstant;
import com.souche.bulbous.vo.UserManagerBean;
import com.souche.bulbous.service.BulbousUserService;
import com.souche.bulbous.utils.MockDataUtil;
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

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户数据交互类
 *
 * @author XadomGreen
 * @since 2018-08-17
 */
@View("UserPage")
public class UserPage extends AbstractReactAction {

    private Logger logger = LoggerFactory.getLogger(QuotePage.class);

    @Resource
    private UserService userService;
    @Autowired
    private BulbousUserService bulbousUserService;

    @Override
    public Map<String, Object> init(Props props) {
        logger.info("初始化用户列表😙😍");
        List<UserManagerBean> list = Lists.newArrayList();
        Map<String, Object> result = Maps.newHashMap();
        if (userService != null) {
            result.put("result", new Page<UserManagerBean>(userService.getUserManagerListCount(null, null, null, null), 1, ExcelConstant.PAGE_SIZE, userService.getUserManagerList(null, null, null, null, 1, ExcelConstant.PAGE_SIZE)));
        } else {
            result.put("result", new Page<UserManagerBean>());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取列表")
    public void getList() {
        logger.info("--> 获取用户数据列表List");
        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("query");
        String beginDate = MapUtils.getString(query, "beginDate");
        String endDate = MapUtils.getString(query, "endDate");
        Integer page = MapUtils.getInteger(query, "page");
        Integer pageSize = MapUtils.getInteger(query, "pageSize");
        String userPhone = MapUtils.getString(query, "userPhone");
        String nickName = MapUtils.getString(query, "nickName");

        logger.info("--> 查询参数为： [{}]", JsonUtils.toJson(query).toString());
        // 查询总记录数
        int totalNumber = userService.getUserManagerListCount(userPhone, nickName, beginDate, endDate);
        // 查询记录列表
        List<UserManagerBean> list = null;
        if (userService != null) {
            list = userService.getUserManagerList(userPhone, nickName, beginDate, endDate,
                    page, pageSize);
        }

        state.set("result", new Page<UserManagerBean>(totalNumber, page, ExcelConstant.PAGE_SIZE, list));
        logger.info("--> 查询结果为：共计[{}]条记录 [{}]", totalNumber, JsonUtils.toJson(state.get("result")).toString());

    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "导出报价Excel")
    public void exportExcel() {
        logger.info("获取报价数据列表List");
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

        state.set("job", bulbousUserService.exportExcel(userPhone, nickName, beginDate, endDate));
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "导出进度")
    public void progress() {
        State state = super.getState();
        Map<String, Object> query = (Map<String, Object>) state.get("job");
        String jobId = MapUtils.getString(query, "jobId");
        state.set("job", bulbousUserService.getProgressByJobId(jobId));
    }

}
