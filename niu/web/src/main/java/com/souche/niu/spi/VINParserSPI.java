package com.souche.niu.spi;

import com.souche.vinquery.api.bean.ParseModel;

import java.util.List;

/**
 * Created by sid on 2018/9/6.
 */
public interface VINParserSPI {

    /**
     * 根据vin码查询
     *
     * @param vin
     * @return
     */
    List<ParseModel> getCarModelInfo(String vin);
}
