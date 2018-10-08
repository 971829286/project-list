package com.souche.niu.spi.adapter;

import com.souche.niu.spi.VINParserSPI;
import com.souche.vinquery.api.SoucheVINParseService;
import com.souche.vinquery.api.bean.ParseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * vin码匹配车型
 * Created by sid on 2018/9/6.
 */
@Component
public class VINParserSPIAdapter implements VINParserSPI {

    private static final Logger logger = LoggerFactory.getLogger(VINParserSPIAdapter.class);

    @Autowired
    private SoucheVINParseService soucheVINParseService;

    @Override
    public List<ParseModel> getCarModelInfo(String vin) {
        try {
            return soucheVINParseService.getNewSoucheModelInfo(vin);
        } catch (Exception e) {
            logger.error("根据vin码查询车型车系品牌出错", e);
            return new ArrayList<>();
        }
    }
}
