package com.souche.bulbous.dto;

import com.souche.bulbous.bean.Job;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class ProgressDto {

    @ApiModelProperty("导出任务ID")
    private String jobId;
    @ApiModelProperty("进度，0-100")
    private int progress;
    @ApiModelProperty("下载URL")
    private String url;

    public ProgressDto() {
    }

    public ProgressDto(Job job) {
        this.jobId = job.getId();
        this.progress = job.getProgress();
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
