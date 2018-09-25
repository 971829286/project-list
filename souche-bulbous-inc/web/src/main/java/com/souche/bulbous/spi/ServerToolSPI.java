package com.souche.bulbous.spi;

import com.souche.niu.model.ServerToolDto;
import com.souche.niu.result.PageResult;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/15
 **/
public interface ServerToolSPI {

    PageResult<ServerToolDto> findByPage(int page, int pageSize);

    ServerToolDto findById(Integer id);

    void deleteById(Integer id);

    int update(Integer id,ServerToolDto dto);

    int save(ServerToolDto dto);
}
