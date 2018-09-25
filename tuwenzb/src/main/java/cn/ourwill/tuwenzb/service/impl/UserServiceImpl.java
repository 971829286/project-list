package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.ActivityAlbum;
import cn.ourwill.tuwenzb.entity.ActivityStatistics;
import cn.ourwill.tuwenzb.mapper.*;
import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ourwill.tuwenzb.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private WatchListMapper watchListMapper;
    @Autowired
    private ActivityAlbumMapper activityAlbumMapper;
    @Autowired
    private IActivityStatisticsService activityStatisticsService;

    @Override
    @Transactional
    public Integer save(User user){
        Integer count = userMapper.save(user);
        if(count>0 && user.getUserfromType()!=null&&user.getUserfromType().equals(1)) {
            Activity activity = this.getExampleActivity(user);
            Activity photoActivity = this.getExamplePhotoActivity(user);
            if (activityMapper.save(activity) > 0) {
                ActivityStatistics statistics = new ActivityStatistics();
                statistics.setActivityId(activity.getId());
                statistics.setParticipantsNum(0);
                statistics.setCommentNum(0);
                statistics.setLikeNum(0);
                if (activityStatisticsService.save(statistics) > 0) {
                    //添加redis
                    int participantsNum = 0;
                    int likeNum = 0;
                    if (activity.getParticipantsNum() != null) {
                        participantsNum = activity.getParticipantsNum();
                    }
                    if (activity.getLikeNum() != null) {
                        likeNum = activity.getLikeNum();
                    }
                    RedisUtils.addActivityHash(activity.getId(), statistics.getId(), participantsNum, likeNum);
                }
            }
            if(activityMapper.save(photoActivity)>0){
                ActivityStatistics statistics = new ActivityStatistics();
                statistics.setActivityId(photoActivity.getId());
                statistics.setParticipantsNum(0);
                statistics.setCommentNum(0);
                statistics.setLikeNum(0);
                if (activityStatisticsService.save(statistics) > 0) {
                    //照片直播添加分区
                    ActivityAlbum album = new ActivityAlbum();
                    album.setActivityId(photoActivity.getId());
                    album.setAlbumName("默认分区");
                    album.setCTime(new Date());
                    album.setDefaultFlag(1);
                    album.setUserId(photoActivity.getUserId());
                    activityAlbumMapper.save(album);
                    //添加redis
                    int participantsNum = 0;
                    int likeNum = 0;
                    if (photoActivity.getParticipantsNum() != null) {
                        participantsNum = photoActivity.getParticipantsNum();
                    }
                    if (photoActivity.getLikeNum() != null) {
                        likeNum = photoActivity.getLikeNum();
                    }
                    RedisUtils.addActivityHash(photoActivity.getId(), statistics.getId(), participantsNum, likeNum);
                }
            }
        }
        return count;
    }

    private Activity getExampleActivity(User user) {
        Activity activity = new Activity();
        activity.setIntroduction("图文直播新用户首次使用时，享有免费使用一次价值299元直播的专属特权，并可以免费发布10条图文直播，超值钜惠，等你体验！");
        activity.setBanner("app/static/baner170915.jpg");
        activity.setPhotoLive(0);
        activity.setUserId(user.getId());
        activity.setTitle("北京韦尔科技有限公司");
        activity.setSite("北京市海淀区上地信息路7号弘源首著1号楼2001室");
        activity.setOrganizer("北京韦尔科技有限公司");
        activity.setPublisher("will编辑部");
        activity.setCTime(new Date());
        activity.setStartTime(new Date());
        activity.setCheckType(1);
        activity.setStatus(4);
        return activity;
    }

    private Activity getExamplePhotoActivity(User user) {
        Activity activity = new Activity();
        activity.setIntroduction("照片直播新用户首次使用时，享有免费使用一次价值399元直播的专属特权，并可以免费发布20张照片，超值钜惠，等你体验！");
        activity.setBanner("app/static/baner170915.jpg");
        activity.setPhotoLive(1);
        activity.setUserId(user.getId());
        activity.setTitle("北京韦尔科技有限公司");
        activity.setSite("北京市海淀区上地信息路7号弘源首著1号楼2001室");
        activity.setOrganizer("北京韦尔科技有限公司");
        activity.setPublisher("will编辑部");
        activity.setCTime(new Date());
        activity.setStartTime(new Date());
        activity.setCheckType(1);
        activity.setStatus(4);
        return activity;
    }

    @Override
    public List<User> selectByWechatNum(String wechatNum) {
        return userMapper.selectByWechatNum(wechatNum);
    }

    @Override
    public Integer updateRefreshToken(User user) {
        return userMapper.updateRefreshToken(user);
    }

    @Override
    public Integer updateUserType(Integer userType, Integer id) {
        User user=new User();
        user.setUserType(userType);
        user.setId(id);
        return userMapper.updateUserType(user);
    }
    //根据用户名查找
    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    //根据用户名查找
    @Override
    public User selectByUsernameOrPhone(String username) {
        return userMapper.selectByUsernameOrPhone(username);
    }

    @Override
    public int consumeRemain(Integer days, User user) {
        if(user.getLicenseType().equals(1)) {
            return userMapper.consumeRemainYear(days,user.getId());
        }else if(user.getLicenseType().equals(2)){
            return userMapper.consumeRemain(days,user.getId());
        }
        return 0;
    }

    @Override
    public int photoConsumeRemain(Integer days, User user) {
        if(user.getPhotoLicenseType().equals(1)) {
            return userMapper.photoConsumeRemainYear(days,user.getId());
        }else if(user.getPhotoLicenseType().equals(2)){
            return userMapper.photoConsumeRemain(days,user.getId());
        }
        return 0;
    }

    @Override
    public List<User> selectByParams(Map params) {
        return userMapper.selectByParams(params);
    }

    @Override
    public Integer changePWD(Integer userId, String newPassword, String salt) {
        return userMapper.changePWD(userId,newPassword,salt);
    }

    //wanghao
    @Override
    public Integer updateAuthorization(User user) { return userMapper.updateAuthorization(user); }

    @Override
    @Transactional
    public void unBinding(User user) {
        userMapper.updateBoundId(user.getBoundId());
        userMapper.updateBoundId(user.getId());
    }

    @Override
    public User setectByMobPhone(String mobPhone) {
        return userMapper.selectByMobPhone(mobPhone);
    }

    @Override
    public Integer updatePhone(String newPhone,Integer id) {
        return userMapper.updatePhone(newPhone,id);
    }

    @Override
    public void updateLicenseType(Integer userId) {
        User user = userMapper.getById(userId);
        if(user.getLicenseType().equals(1)&&new Date().after(user.getDueDate())){
            if(user.getRemainingDays()!=null&&user.getRemainingDays()>0){
                User newUser = new User();
                newUser.setLicenseType(2);
                userMapper.updateAuthorization(user);
            }else{
                User newUser = new User();
                newUser.setLicenseType(0);
                userMapper.updateAuthorization(user);
            }
        }
    }

    @Override
    public List<User> getAdminUser(Integer activityId) {
        return userMapper.getAdminUser(activityId);
    }

    @Override
    public void getCounts(User user,Integer photoLive) {
        user.setActivityCount(activityMapper.selectCountByUserId(user.getId(),photoLive));
        user.setCommentCount(commentMapper.selectCountByUserId(user.getId(),photoLive));
        user.setAttentionCount(watchListMapper.selectCountByUserId(user.getId(),photoLive));
    }

    @Override
    public int updateUnionId(Integer id, String unionId) {
        return userMapper.updateUnionId(id,unionId);
    }
}
