package com.souche.niu.api;

import com.souche.niu.model.ServerToolDto;
import com.souche.niu.result.PageResult;

/**
 * @Description：服务工具接口
 *
 * @remark: Created by wujingtao in 2018/9/14
 **/
public interface ServerToolService {

    /**
     * 返回受影响行数
     * @param dto
     * @return
     */
    int save(ServerToolDto dto);

    /**
     * 按照id更新记录
     * @param id
     * @param dto
     * @return
     */
    int update(Integer id, ServerToolDto dto);

    /**
     * 按照id查询
     * @param id
     * @return
     */
    ServerToolDto findById(Integer id);

    /**
     * 按照id删除
     * @param id
     * @return
     */
    void deleteById(Integer id);


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    PageResult<ServerToolDto> findByPage(int page, int pageSize);

}
