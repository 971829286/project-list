package com.souche.bulbous.spi;

import com.souche.niu.model.ActivityScreenDto;
import com.souche.niu.result.PageResult;

/**
 * @Description：
 * @remark: Created by wujingtao in 2018/9/15
 **/
public interface ActivityScreenSPI {

    int save(ActivityScreenDto dto);

    int update(Integer id, ActivityScreenDto dto);

    void deleteById(Integer id);

    /**
     * @param page
     * @param pageSize
     * @param status   有效期状态  1：全部 2：生效 3：未生效 4：已过期
     * @return
     */
    PageResult<ActivityScreenDto> findByPage(int page, int pageSize, String status);

    ActivityScreenDto findById(Integer id);
}
