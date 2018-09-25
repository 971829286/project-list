package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBase;
import cn.ourwill.huiyizhan.entity.UserBasicInfo;
import cn.ourwill.huiyizhan.entity.UserStatistics;
import cn.ourwill.huiyizhan.mapper.UserMapper;
import cn.ourwill.huiyizhan.service.IUserService;
import cn.ourwill.huiyizhan.service.IUserStatisticsService;
import cn.ourwill.huiyizhan.service.IWatchListPeopleService;
import cn.ourwill.huiyizhan.utils.WillCenterConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/21 18:10
 * @Description
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    //    @Value("${WillCenter.API.User.getUserInfoByUUID}")
//    private String getUserInfoByUUID;
    private WillCenterConstants willCenter = WillCenterConstants.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserStatisticsService userStatisticsService;

    @Override
    public User selectByUuid(String uuid) {
        return userMapper.selectByUuid(uuid);
    }

    @Override
    public User SyncFormCenter(UserBase user) {
        User localUser = new User();
//        String uuid = user.getUUID();
        localUser.setUUID(user.getUUID());
        localUser.setLevel(user.getLevel());
        localUser.setAddress(StringUtils.isEmpty(user.getAddress()) ? null : user.getAddress());
        localUser.setAvatar(StringUtils.isEmpty(user.getAvatar()) ? null : user.getAvatar());
        localUser.setCompany(StringUtils.isEmpty(user.getCompany()) ? null : user.getCompany());
        localUser.setUsername(StringUtils.isEmpty(user.getUsername()) ? null : user.getUsername());
        localUser.setNickname(StringUtils.isEmpty(user.getNickname()) ? null : user.getNickname());
        localUser.setEmail(StringUtils.isEmpty(user.getEmail()) ? null : user.getEmail());
        localUser.setTelPhone(StringUtils.isEmpty(user.getTelPhone()) ? null : user.getTelPhone());
        localUser.setQq(StringUtils.isEmpty(user.getQq()) ? null : user.getQq());
        localUser.setMobPhone(StringUtils.isEmpty(user.getMobPhone()) ? null : user.getMobPhone());
        localUser.setVersion(user.getVersion());
        localUser.setUnionid(StringUtils.isEmpty(user.getUnionid()) ? null : user.getUnionid());
        if (userMapper.insertSelective(localUser) > 0) {
            return localUser;
        }
        return null;
    }

    @Override
    public User SyncFormCenter(UserBase userWillCenter, String uuid) {
//        if(isUpdate){
//            //String uuid = user.getUUID();
//            UserBase userWillCenter = (UserBase) getWillCenterUserByUUID(uuid);
        User localUser = selectByUuid(userWillCenter.getUUID());
//            log.info("======================="+userHYZ.toString());
        if (localUser != null) {
            Integer versionWillCenter = userWillCenter.getVersion();
            Integer versionLocal = localUser.getVersion();
            if (versionWillCenter != versionLocal) {
                localUser.setUUID(uuid);
                localUser.setUsername(userWillCenter.getUsername());
                localUser.setAddress(StringUtils.isEmpty(userWillCenter.getAddress()) ? null : userWillCenter.getAddress());
                localUser.setAvatar(StringUtils.isEmpty(userWillCenter.getAvatar()) ? null : userWillCenter.getAvatar());
                localUser.setCompany(StringUtils.isEmpty(userWillCenter.getCompany()) ? null : userWillCenter.getCompany());
                localUser.setUsername(StringUtils.isEmpty(userWillCenter.getUsername()) ? null : userWillCenter.getUsername());
                localUser.setEmail(StringUtils.isEmpty(userWillCenter.getEmail()) ? null : userWillCenter.getEmail());
                localUser.setTelPhone(StringUtils.isEmpty(userWillCenter.getTelPhone()) ? null : userWillCenter.getTelPhone());
                localUser.setQq(StringUtils.isEmpty(userWillCenter.getQq()) ? null : userWillCenter.getQq());
                localUser.setMobPhone(StringUtils.isEmpty(userWillCenter.getMobPhone()) ? null : userWillCenter.getMobPhone());
                localUser.setInfo(StringUtils.isEmpty(userWillCenter.getInfo()) ? null : userWillCenter.getInfo());
                localUser.setVersion(userWillCenter.getVersion());
                localUser.setNickname(StringUtils.isEmpty(userWillCenter.getNickname()) ? null : userWillCenter.getNickname());
                localUser.setUnionid(StringUtils.isEmpty(userWillCenter.getUnionid()) ? null : userWillCenter.getUnionid());
                userMapper.updateUserByUUID(localUser);
            }
        }
        return localUser;
    }

    /**
     * 通过uuid获取willcenter的用户对象
     *
     * @param uuid
     * @return
     */
    @Override
    public UserBase getWillCenterUserByUUID(String uuid) {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(willCenter.getGetUserInfoByUUIDUrl());
        postMethod.addParameter("uuid", uuid);
        try {
            httpClient.executeMethod(postMethod);
            String userString = postMethod.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userString);
            String data = jsonNode.get("data").toString();
            UserBase user = mapper.readValue(data, UserBase.class);
            return user;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User selectUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public User selectUserByMobPhone(String mobPhone) {
        return userMapper.selectUserByMobPhone(mobPhone);
    }

    @Override
    public List<User> selectByParams(Map<String, String> param) {
        return userMapper.selectByParams(param);
    }

    @Override
    public int updateUserByUUID(UserBase user) {
        return userMapper.updateUserByUUID(user);
    }

    @Override
    public UserBasicInfo getBasicInfoById(Integer id) {
        UserBasicInfo userBasicInfo = userMapper.getBasicInfoById(id);
        if(userBasicInfo!=null) {
            UserStatistics userStatistics = userStatisticsService.getUserStatisticsFromRedis(id);
            userBasicInfo.setUserStatistics(userStatistics);
        }
        return userBasicInfo;
    }

    @Autowired
    private IWatchListPeopleService watchListPeopleService;

    @Override
    public UserBasicInfo getBasicInfoById(Integer userId, Integer loginUserId) {
        UserBasicInfo userBasicInfo = this.getBasicInfoById(userId);
        if (userBasicInfo != null) {
            userBasicInfo.setMeToHim(watchListPeopleService.checkWatchStatus(userId, loginUserId) ? 1 : 0);
            userBasicInfo.setHimTome(watchListPeopleService.checkWatchStatus(loginUserId, userId) ? 1 : 0);
            userBasicInfo.setMutualFansStatus(watchListPeopleService.isMutualfans(userId, loginUserId) ? 1 : 0);
        }
        return userBasicInfo;
    }

    @Override
    public UserBase selectByUnionId(String unionid){
        UserBase user =  userMapper.getUserByUnionid(unionid);
        return user;
    }

    @Override
    public Integer changeUnionIdByUuid(String unionid,String uuid) {
        return userMapper.changeUnionIdByUuid(unionid,uuid);
    }
}
