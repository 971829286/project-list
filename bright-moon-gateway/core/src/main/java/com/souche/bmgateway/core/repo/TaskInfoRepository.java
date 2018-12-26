package com.souche.bmgateway.core.repo;

import com.souche.bmgateway.core.domain.TaskInfo;

public interface TaskInfoRepository {

    TaskInfo getTaskInfo(String taskCode);
}
