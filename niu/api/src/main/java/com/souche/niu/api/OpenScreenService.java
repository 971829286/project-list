package com.souche.niu.api;

import com.souche.niu.model.OpenScreenDto;
import com.souche.niu.result.PageResult;


/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
public interface OpenScreenService {

    int save(OpenScreenDto dto);

    int update(Integer id,OpenScreenDto dto);

    void deleteById(Integer id);

    /**
     * 分页获取开屏记录
     * @param page
     * @param pageSize
     * @param status  有效期状态  1：全部 2：生效 3：未生效 4：已过期
     * @return
     */
    PageResult<OpenScreenDto> findByPage(int page, int pageSize,String status);

    OpenScreenDto findById(Integer id);
}
