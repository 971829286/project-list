package com.souche.bulbous.manager;

import com.souche.bulbous.vo.OpenScreenVo;
import com.souche.niu.result.PageResult;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/14
 **/
public interface OpenScreenManager {

    PageResult<OpenScreenVo> findByPage(int page, int pageSize, String status);

    OpenScreenVo findById(String id);
}
