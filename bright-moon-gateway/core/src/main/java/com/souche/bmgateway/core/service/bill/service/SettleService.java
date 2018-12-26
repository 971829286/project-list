package com.souche.bmgateway.core.service.bill.service;

import com.souche.bmgateway.core.domain.TaskInfo;
import com.souche.bmgateway.core.dto.response.SettleResponse;

public interface SettleService {

    SettleResponse settle(String date, String type, TaskInfo taskInfo);
}
