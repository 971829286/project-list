package com.souche.bulbous.web.json;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.manager.ActivityScreenManager;
import com.souche.bulbous.spi.ActivityScreenSPI;
import com.souche.niu.model.ActivityScreenDto;
import com.souche.optimus.common.util.StringUtil;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.sad.AbstractReactAction;
import com.souche.optimus.core.sad.Props;
import com.souche.optimus.core.sad.ReactAction;
import com.souche.optimus.core.sad.State;
import com.souche.optimus.exception.OptimusExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Description：活动配置页后端接口
 *
 * @remark: Created by wujingtao in 2018/9/10
 **/
@View("ActivityScreen")
public class ActivityScreen extends AbstractReactAction {

    private static final Logger logger = LoggerFactory.getLogger(ActivityScreen.class);

    @Autowired
    private ActivityScreenManager activityScreenManager;
    @Autowired
    private ActivityScreenSPI activityScreenSPI;

    /**
     * @param props 初始化时 page:1 pageSize:10 status:'1'
     * @return
     */
    @Override
    public Map<String, Object> init(Props props) {
        State state=super.getState();
        Map<String, Object> result = new HashMap<>();
        //初始化时 page:1 pageSize:10 status:'1'
        result.put("rowData",activityScreenManager.findByPage(1,10,"1"));
        return result;
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取列表")
    public void getList() {
        // 查询参数
        State state = super.getState();
        Integer page = (Integer) state.get("page");
        Integer pageSize = (Integer) state.get("pageSize");
        String status = (String) state.get("status");
        if (page == null) {
            logger.info("查询列表失败参数异常 page");
            throw new OptimusExceptionBase("参数异常 page");
        }
        if (pageSize == null) {
            logger.info("查询列表失败参数异常 pageSize");
            throw new OptimusExceptionBase("参数异常 pageSize");
        }
        if (StringUtil.isEmpty(status)) {
            logger.info("查询列表失败参数异常 status");
            throw new OptimusExceptionBase("参数异常 status");
        }
        state.set("rowData",activityScreenManager.findByPage(page,pageSize,status));
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取详情")
    public void edit() {
        State state = super.getState();
        String id = (String) state.get("openId");
        if (StringUtil.isEmpty(id)) {
            logger.info("编辑失败参数异常 ID为空");
            throw new OptimusExceptionBase("参数异常 ID为空");
        }
        state.set("editData", activityScreenManager.findById(id));
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "根据ID删除")
    public void delete() {
        State state = super.getState();
        String id = (String) state.get("openId");
        if (StringUtil.isEmpty(id)) {
            logger.info("删除失败参数异常 ID为空");
            throw new OptimusExceptionBase("参数异常 ID为空");
        }
        try {
            this.activityScreenSPI.deleteById(Integer.parseInt(id));
        } catch (OptimusExceptionBase e) {
            logger.info("删除失败活动浮窗失败 {}",e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "保存")
    public void saveInfo() {
        State state = super.getState();
        String openId = (String) state.get("openId");
        String title = (String) state.get("title");
        String protocol = (String) state.get("protocol");
        String targetUser = (String) state.get("targetUser");
        String url = (String) state.get("url");
        String startTime = (String) state.get("startTime");
        String endTime = (String) state.get("endTime");
        ActivityScreenDto dto=new ActivityScreenDto();
        dto.setTitle(title);
        dto.setProtocol(protocol);
        dto.setTargetUser(targetUser);
        dto.setUrl(url);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        try {
            if (StringUtil.isEmpty(openId)) {
                int count = this.activityScreenSPI.save(dto);
                logger.info("保存活动浮窗记录 {}",JSON.toJSONString(dto));
                state.set("success", true);//关闭弹窗
                state.set("msg", "保存成功");
                return;
            }
            dto.setId(Integer.parseInt(openId));
            int count = this.activityScreenSPI.update(dto.getId(), dto);
            logger.info("修改活动浮窗记录 {}",JSON.toJSONString(dto));
            state.set("success", true);//关闭弹窗
            state.set("msg", "保存成功");
            return;
        } catch (OptimusExceptionBase e) {
            logger.info("保存失败 {}",e.toString());
            state.set("success", false);//关闭弹窗
            state.set("msg", "保存失败");
        }
    }

}
