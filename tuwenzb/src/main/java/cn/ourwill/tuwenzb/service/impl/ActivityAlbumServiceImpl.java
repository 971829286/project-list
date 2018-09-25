package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import cn.ourwill.tuwenzb.mapper.ActivityAlbumMapper;
import cn.ourwill.tuwenzb.service.IActivityAlbumService;
import cn.ourwill.tuwenzb.service.IActivityPhotoTokenService;
import cn.ourwill.tuwenzb.service.IActivityService;
import cn.ourwill.tuwenzb.service.IFacePlusServer;
import cn.ourwill.tuwenzb.utils.FaceDomain;
import cn.ourwill.tuwenzb.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ActivityAlbumServiceImpl extends BaseServiceImpl<ActivityAlbum> implements IActivityAlbumService {
    @Autowired
    private ActivityAlbumMapper activityAlbumMapper;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private FaceDomain faceDomain;

    @Autowired
    IFacePlusServer facePlusServer;
    @Override
    public List<ActivityAlbum> selectByActivity(Integer activityId) {
        return activityAlbumMapper.selectByActivity(activityId);
    }

    @Override
    public List<ActivityAlbum> selectByActivity(Integer activityId, Integer userId, Integer type) {
        return activityAlbumMapper.selectByActivityImpower(activityId, userId, type);
    }

    @Override
    public int batchSave(Integer activityId, Integer userId, List<ActivityAlbum> activityAlbums) {
        activityAlbums.stream().forEach(entity -> {
            entity.setCTime(new Date());
            entity.setUserId(userId);
            entity.setActivityId(activityId);
        });
        int resultCode = activityAlbumMapper.batchSave(activityAlbums);
        Integer displayAlbumId = activityAlbumMapper.getIdByActivityIdAndDefaultFlag(activityId);
        if (displayAlbumId != null && displayAlbumId != 0) {
            activityService.upDateDisplayAlbum(activityId,userId,displayAlbumId);
        }
        return resultCode;
    }

    @Override
    public int batchUpdate(Integer activityId, Integer userId, List<ActivityAlbum> activityAlbums) {
        List<Integer> ids = activityAlbumMapper.findAllId(activityId);
        List<Integer> pageDataList = new ArrayList<>();
        activityAlbums.stream().forEach(entity -> {
            entity.setCTime(new Date());
            entity.setUserId(userId);
            entity.setActivityId(activityId);
            if (entity.getId() != null) {
                pageDataList.add(entity.getId());
            }
        });

        ArrayList<Integer> deleteIds = new ArrayList<>();
        for (Integer id : ids) {
            if (!pageDataList.contains(id)) { // 当前id 所对应的数据已被删除
                deleteIds.add(id);
            }
        }
        if (deleteIds.size() > 0)
            //activityAlbumMapper.batchDelete(deleteIds);
            activityAlbumMapper.updateDeleteStatus(deleteIds);
        return activityAlbumMapper.batchUpdate(activityAlbums);
    }

//    /**
//     * 暂时停用 使用face++
//     * 注册人脸识别的库,如果已经注册则返回标识
//     * @param id
//     * @return
//     */
//    @Override
//    public String getFaceSetToken(int id) {
//        ActivityAlbum activityAlbum = this.activityAlbumMapper.getById(id);
//        if(activityAlbum != null && activityAlbum.getFaceSetToken() == null){
//            String url = faceDomain.getCreate();
//            Map map = HttpUtils.sendPost(url,Map.class,null);
//            String faceSetToken = (String)map.get("faceset_token");
//            if(StringUtils.isNotEmpty(faceSetToken)){
//                activityAlbum.setFaceSetToken(faceSetToken);
//                Integer update = activityAlbumMapper.update(activityAlbum);
//                if(update != null && update > 0)
//                    return faceSetToken;
//            }
//            return null;
//        }
//        return activityAlbum.getFaceSetToken();
//    }
//
//    /**
//     *获取人脸识别库TOken,如果不存在就去注册
//     * @param id
//     * @return
//     */
//    @Override
//    public String getFaceSetTokenByFacePlus(int id){
//        ActivityAlbum activityAlbum = this.activityAlbumMapper.getById(id);
//        if(activityAlbum != null && StringUtils.isEmpty(activityAlbum.getFaceSetToken())){
//            String faceSetToken = facePlusServer.getFaceSetToken();
//            if(StringUtils.isNotEmpty(faceSetToken)){
//                activityAlbum.setFaceSetToken(faceSetToken);
//                activityAlbumMapper.update(activityAlbum);
//                return faceSetToken;
//            }
//            return null;
//        }
//        return activityAlbum.getFaceSetToken();
//    }
}
