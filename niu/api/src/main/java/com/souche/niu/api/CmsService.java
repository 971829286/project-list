package com.souche.niu.api;

import com.souche.niu.model.Banner;
import com.souche.niu.model.BannerCfgDto;
import com.souche.niu.model.MaintenanceConfigDO;
import com.souche.niu.model.MoneyCfgDto;

import java.util.List;

/**
 * 车牛 cms管理
 * Created by sid on 2018/9/5.
 */
public interface CmsService {

    /**
     * 查询维保设置
     *
     * @return
     */
    MaintenanceConfigDO queryMaintenance();


    /**
     * 按照状态获取banner列表
     * @param status 状态码
     * @param page  当前页
     * @param pageSize 页的大小
     * @return
     */
    List<Banner> getBannerListByStatus(Integer status, Integer page, Integer pageSize);

    /**
     * 按照状态获取banner数量
     * @param status
     * @return
     */
    Integer getBannerListCountByStatus(Integer status);


    /**
     * 按照id获取banner
     * @param bannerId
     * @return
     */
    Banner getBannerById(Integer bannerId);


    /**
     * 按照id删除banner
     * @param bannerId
     * @return
     */
    Boolean deleteById(Integer bannerId);

    /**
     * 更新banner
     * @param bannerId
     * @param banner
     * @return
     */
    Boolean updateBanner(Integer bannerId, Banner banner);

    /**
     * 保存banner
     * @param banner
     * @return
     */
    Integer saveBanner(Banner banner);

    /**
     * 获取唯一记录
     * @return
     */
    BannerCfgDto findOneBannerCfgDto();

    /**
     *保存banner配置
     * ID为空为新增
     * ID不为空做修改
     * @param bannerCfgDto
     * @return
     */
    int saveBannerCfg(BannerCfgDto bannerCfgDto);

    /**
     *查询金额配置唯一记录
     * @return
     */
    MoneyCfgDto findOneMoneyCfgDto();

    /**
     * 保存金额配置
     *
     * @param moneyCfgDto
     * @return
     */
    int saveMoneyCfg(MoneyCfgDto moneyCfgDto);

}
