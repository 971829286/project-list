package com.souche.bulbous.web.json;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.manager.ServiceToolManager;
import com.souche.bulbous.spi.EntranceSPI;
import com.souche.bulbous.vo.ServiceToolVo;
import com.souche.niu.model.EntranceDto;
import com.souche.niu.result.PageResult;
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
 * @remark: Created by wujingtao in 2018/9/10
 **/
@View("ServiceToolScreen")
public class ServiceToolScreen extends AbstractReactAction {

    private static final Logger logger = LoggerFactory.getLogger(ServiceToolScreen.class);

    @Autowired
    private ServiceToolManager serviceToolManager;

    @Autowired
    private EntranceSPI entranceSPI;

    /**
     * @param props 初始化时 page:1 pageSize:10 status:'1'
     * @return
     */
    @Override
    public Map<String, Object> init(Props props) {
        Map<String, Object> result = new HashMap<>();
        //初始化时 page:1 pageSize:10 status:'1'
        PageResult<ServiceToolVo> vos = this.serviceToolManager.findByPage(1, 10);
        result.put("rowData", vos);
        EntranceDto dto=this.entranceSPI.findOne();
        result.put("entranceData",dto==null?new EntranceDto():dto);
        return result;
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "入口配置保存事件")
    public void saveEntrance() {
        State state = super.getState();
        String toolProtocol = (String) state.get("toolProtocol");
        String toolTitle = (String) state.get("toolTitle");
        Integer toolId = (Integer) state.get("toolId");
        if (StringUtil.isEmpty(toolProtocol)) {
            logger.info("保存入口配置失败 参数为空 toolProtocol");
            throw new OptimusExceptionBase("保存入口配置失败 参数为空 toolProtocol");
        }
        if (StringUtil.isEmpty(toolTitle)) {
            logger.info("保存入口配置失败 参数为空 toolTitle");
            throw new OptimusExceptionBase("保存入口配置失败 参数为空 toolTitle");
        }
        try {
            EntranceDto dto = new EntranceDto();
            dto.setTitle(toolTitle);
            dto.setProtocol(toolProtocol);
            if (toolId!=null && toolId!=0) {
                dto.setId(toolId);
            }
            this.entranceSPI.save(dto);
            logger.info("保存入口配置成功 {}",JSON.toJSONString(dto));
            state.set("saveStatus", true);
            state.set("savemsg", "保存成功");
        } catch (OptimusExceptionBase e) {
            logger.info("保存入口配置失败 {}", e.toString());
            state.set("saveStatus", false);
            state.set("savemsg", "保存失败");
        }
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取列表")
    public void getList() {
        // 查询参数
        State state = super.getState();
        Integer page = (Integer) state.get("page");
        Integer pageSize = (Integer) state.get("pageSize");
        PageResult<ServiceToolVo> vos = this.serviceToolManager.findByPage(page, pageSize);
        state.set("rowData", vos);
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "获取详情")
    public void edit() {
        State state = super.getState();
        String id = (String) state.get("openId");
        try {
            ServiceToolVo vo = this.serviceToolManager.findById(Integer.parseInt(id));
            state.set("editData", vo);
        } catch (OptimusExceptionBase e) {
            logger.info("根据ID获取服务工具记录失败 {}", e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "根据ID删除")
    public void delete() {
        State state = super.getState();
        String id = (String) state.get("openId");
        try {
            this.serviceToolManager.deleteById(Integer.parseInt(id));
        } catch (OptimusExceptionBase e) {
            logger.info("删除服务工具记录失败 {}", e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "保存")
    public void saveInfo() {
        State state = super.getState();
        String id = (String) state.get("openId");
        String title = (String) state.get("title");
        String protocol = (String) state.get("protocol");
        String clickPoint = (String) state.get("clickPoint");
        String url2X = (String) state.get("url2X");
        String url3X = (String) state.get("url3X");
        Boolean isShow = (Boolean) state.get("isShow");
        String orderNum = (String) state.get("orderNum");
        ServiceToolVo vo = new ServiceToolVo(id, title, protocol, url2X, url3X, clickPoint, isShow + "", orderNum);
        try {
            int count = this.serviceToolManager.save(vo);
            state.set("success", true);//关闭弹窗
            state.set("msg", "保存成功");
        } catch (OptimusExceptionBase e) {
            logger.info("保存服务工具记录失败 {}", e.toString());
            state.set("success", false);
            state.set("msg", "保存失败"+e.toString());
        }
    }


}
