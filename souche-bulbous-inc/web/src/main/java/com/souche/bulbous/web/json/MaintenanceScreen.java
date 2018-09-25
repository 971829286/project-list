package com.souche.bulbous.web.json;

import com.alibaba.fastjson.JSON;
import com.souche.bulbous.manager.BannerCfgManager;
import com.souche.bulbous.manager.MoneyCfgManager;
import com.souche.bulbous.spi.MoneyCfgSPI;
import com.souche.bulbous.vo.BannerCfgVo;
import com.souche.niu.model.MoneyCfgDto;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.sad.AbstractReactAction;
import com.souche.optimus.core.sad.Props;
import com.souche.optimus.core.sad.ReactAction;
import com.souche.optimus.core.sad.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description：维保查询而配置
 * @remark: Created by wujingtao in 2018/9/11
 **/
@View("MaintenanceScreen")
public class MaintenanceScreen extends AbstractReactAction {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceScreen.class);

    @Autowired
    private BannerCfgManager bannerCfgManager;
    @Autowired
    private MoneyCfgSPI moneyCfgSPI;
    @Autowired
    private MoneyCfgManager moneyCfgManager;

    @Override
    public Map<String, Object> init(Props props) {
        State state = super.getState();
        Map<String, Object> result = new HashMap<>();
        BannerCfgVo bannerVo = this.bannerCfgManager.findOne();
        logger.info("获取banner配置记录 {}", JSON.toJSONString(bannerVo));
        state.set("bannerId", bannerVo.getId());
        state.set("fileList", bannerVo.getPhoto());
        state.set("banner", bannerVo.getBannerTitle());
        state.set("bannerUrl", bannerVo.getUrl());
        state.set("bannerProtocol", bannerVo.getProtocol());

        MoneyCfgDto moneyCfgDto = this.moneyCfgSPI.findOne();
        logger.info("获取金额配置记录 {}", JSON.toJSONString(moneyCfgDto));
        state.set("moneyId", moneyCfgDto.getId());
        state.set("noCertification", moneyCfgDto.getNoCertification());
        state.set("certification", moneyCfgDto.getCertification());

        result.put("bannerData", bannerVo);
        result.put("moneyData", moneyCfgDto);
        return result;
    }


    @SuppressWarnings("unchecked")
    @ReactAction(desc = "保存banner配置")
    public void saveBanner() {
        Map<String, Object> result = new HashMap<>();
        State state = super.getState();
        try {
            Integer bannerId = (Integer) state.get("bannerId");
            String banner = (String) state.get("banner");
            String bannerUrl = (String) state.get("bannerUrl");
            String bannerProtocol = (String) state.get("bannerProtocol");
            result = this.bannerCfgManager.save(bannerId, banner, bannerUrl, bannerProtocol);
            state.set("result:", result);
        } catch (Exception e) {
            logger.info("保存banner配置失败 {}", e.toString());
            result.put("success", false);
            result.put("msg", "保存失败");
            state.set("result", result);
        }
    }

    @SuppressWarnings("unchecked")
    @ReactAction(desc = "保存金额配置")
    public void saveMoney() {
        Map<String, Object> result = new HashMap<>();
        State state = super.getState();
        try {
            String noCertification = (String) state.get("noCertification");
            String certification = (String) state.get("certification");
            Integer moneyId = (Integer) state.get("moneyId");
            BigDecimal b_certification = new BigDecimal(certification.trim());
            BigDecimal b_noCertification = new BigDecimal(noCertification.trim());
            result = this.moneyCfgManager.save(moneyId,
                    b_certification.setScale(2, BigDecimal.ROUND_DOWN),
                    b_noCertification.setScale(2, BigDecimal.ROUND_DOWN));
            state.set("result:", result);
        } catch (Exception e) {
            logger.info("保存金额配置失败 {}", e.toString());
            result.put("success", true);
            result.put("msg", "保存失败");
            state.set("result:", result);
        }
    }

}
