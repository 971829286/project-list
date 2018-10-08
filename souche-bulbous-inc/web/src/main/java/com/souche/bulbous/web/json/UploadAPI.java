package com.souche.bulbous.web.json;

import com.souche.optimus.common.upload.FileInfo;
import com.souche.optimus.common.util.CollectionUtils;
import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.controller.Context;
import com.souche.optimus.core.web.Result;
import com.wordnik.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/6
 **/
@View(value="uploadAPI",desc="图片上传接口")
public class UploadAPI {

    @ApiOperation("上传图片")
    public Result uploadPic(Context context){
        Result result = new Result();
        List<FileInfo> list = context.getAttachments();
        if (CollectionUtils.isEmpty(list)) {
            result.setData(null);
            result.setCode("500");
            result.setMsg("图片上传失败");
            result.setSuccess(false);
            return result;
        }
        FileInfo fileInfo = list.get(0);
        String path = fileInfo.getFullFilePath();
        result.setSuccess(true);
        result.setMsg("上传成功");
        result.setCode("200");
        result.setData(path);
        return result;
    }

}
