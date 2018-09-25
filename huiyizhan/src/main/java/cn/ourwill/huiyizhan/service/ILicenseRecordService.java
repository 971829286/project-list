package cn.ourwill.huiyizhan.service;


import cn.ourwill.huiyizhan.entity.LicenseRecord;

import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/22 0022 17:21
 * @Version1.0
 */
public interface ILicenseRecordService extends IBaseService<LicenseRecord>{

    //上传
    public Integer save(LicenseRecord licenseRecord);
    //更新
    public Integer update(LicenseRecord licenseRecord);
    //根据ID查找
    public LicenseRecord selectById(Integer id);
    //获取所有数据
    public List<LicenseRecord> findAll(Integer photoLive);
    //通过属性查询
    public List<LicenseRecord> getByParam(Map map);
    //批量删除
    public Integer deletePatch(List<Integer> ids);

    Map addLicense(Integer userId, Map map);

    List<LicenseRecord> selectByUserId(Integer userId, Integer photoLive);

}
