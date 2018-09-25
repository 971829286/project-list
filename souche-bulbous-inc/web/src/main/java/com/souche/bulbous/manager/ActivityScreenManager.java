package com.souche.bulbous.manager;

import com.souche.bulbous.vo.ActivityScreenVo;
import com.souche.niu.result.PageResult;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/15
 **/
public interface ActivityScreenManager {

    PageResult<ActivityScreenVo> findByPage(int page, int pageSize, String status);

    ActivityScreenVo findById(String id);
}
