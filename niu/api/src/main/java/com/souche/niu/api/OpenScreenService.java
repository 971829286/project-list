package com.souche.niu.api;

import com.souche.niu.model.OpenScreenDto;
import com.souche.niu.result.PageResult;


/**
 * @Description：开屏配置接口
 *
 * @remark: Created by wujingtao in 2018/9/13
 **/
public interface OpenScreenService {

    /**
     * 保存开屏配置
     * @param dto
     * @return
     */
    int save(OpenScreenDto dto);

    /**
     * 修改开平配置
     * @param id
     * @param dto
     * @return
     */
    int update(Integer id,OpenScreenDto dto);

    /**
     * 修改开屏配置
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 分页获取开屏记录
     * @param page
     * @param pageSize
     * @param status  有效期状态  1：全部 2：生效 3：未生效 4：已过期
     * @return
     */
    PageResult<OpenScreenDto> findByPage(int page, int pageSize,String status);

    /**
     * 根据ID获取开屏配置记录
     * @param id
     * @return
     */
    OpenScreenDto findById(Integer id);
}
