package com.souche.bmgateway.core.repo.impl;

import com.souche.bmgateway.core.dao.TaskInfoMapper;
import com.souche.bmgateway.core.domain.TaskInfo;
import com.souche.bmgateway.core.repo.TaskInfoRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author wkl
 */
@Repository
public class TaskInfoRepositoryImpl implements TaskInfoRepository {

    @Resource
    private TaskInfoMapper taskInfoMapper;

    @Override
    public TaskInfo getTaskInfo(String taskCode) {
        return taskInfoMapper.selectByTaskCode(taskCode);
    }
}
