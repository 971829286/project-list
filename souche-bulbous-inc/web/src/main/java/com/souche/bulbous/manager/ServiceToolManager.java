package com.souche.bulbous.manager;

import com.souche.bulbous.vo.ServiceToolVo;
import com.souche.niu.result.PageResult;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
public interface ServiceToolManager {

    PageResult<ServiceToolVo> findByPage(int page, int pageSize);

    int save(ServiceToolVo vo);

    ServiceToolVo findById(Integer id);

    void deleteById(Integer id);
}
