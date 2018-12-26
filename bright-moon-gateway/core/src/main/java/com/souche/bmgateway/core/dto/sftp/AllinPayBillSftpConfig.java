package com.souche.bmgateway.core.dto.sftp;

import com.souche.optimus.common.config.OptimusConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author zhaojian
 * @date 18/7/13
 */
@Component
@Getter
@Setter
@ToString
public class AllinPayBillSftpConfig {


    private String ip = OptimusConfig.getValue("allinpay.bill.sftp.ip");

    private Integer port = OptimusConfig.getValue("allinpay.bill.sftp.port", Integer.class);

    private String username = OptimusConfig.getValue("allinpay.bill.sftp.username");

    private String password = OptimusConfig.getValue("allinpay.bill.sftp.password");

    private String directory = OptimusConfig.getValue("allinpay.bill.sftp.directory");

    private String savePath = OptimusConfig.getValue("allinpay.aliyun.bill.save.path");

}
