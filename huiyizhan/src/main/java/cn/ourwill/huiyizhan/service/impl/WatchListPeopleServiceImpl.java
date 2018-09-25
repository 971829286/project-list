package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.PeopleDynamic;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBasicInfo;
import cn.ourwill.huiyizhan.entity.WatchListPeople;
import cn.ourwill.huiyizhan.mapper.WatchListPeopleMapper;
import cn.ourwill.huiyizhan.service.IUserStatisticsService;
import cn.ourwill.huiyizhan.service.IWatchListPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 关注人
 */
@Service
public class WatchListPeopleServiceImpl extends BaseServiceImpl<WatchListPeople> implements IWatchListPeopleService {

    @Autowired
    private WatchListPeopleMapper watchListPeopleMapper;
    @Autowired
    private IUserStatisticsService userStatisticsService;

    @Override
    public Boolean checkWatchStatus(Integer watchedUserId, Integer userId) {
        WatchListPeople watchListPeoples = watchListPeopleMapper.selectByWatchUserAndUser(watchedUserId, userId);
        if (watchListPeoples != null) {
            return true;//已关注
        }
        return false;//未关注
    }

    @Override
    public int addWatch(Integer watchedUserId, Integer userId) {
        WatchListPeople watchListPeople = new WatchListPeople();
        watchListPeople.setWatchUserId(watchedUserId);
        watchListPeople.setUserId(userId);
        watchListPeople.setWatchDate(new Date());
        return watchListPeopleMapper.save(watchListPeople);
    }

    @Override
    public int cancelWatchById(Integer watchedUserId, Integer userId) {
        WatchListPeople watchListPeople = new WatchListPeople();
        watchListPeople.setWatchUserId(watchedUserId);
        watchListPeople.setUserId(userId);
        return watchListPeopleMapper.delete(watchListPeople);
    }

    @Override
    public List<UserBasicInfo> getWatchPeopleInfo(Integer userId) {
        List<UserBasicInfo> watchPeopleInfos = watchListPeopleMapper.getWatchPeopleInfo(userId);
        watchPeopleInfos.stream().forEach(entity-> {
            //从redis 中查询 用户统计信息
            entity.setUserStatistics(userStatisticsService.getUserStatisticsFromRedis(entity.getId()));
            Integer id = entity.getId();
            boolean isMutualfans = this.isMutualfans(id, userId);
            if (isMutualfans) {
                entity.setMutualFansStatus(1);
            } else {
                entity.setMutualFansStatus(0);
            }
        });
        return watchPeopleInfos;
    }




    @Override
    public List<UserBasicInfo> getFansInfo(Integer userId) {
        List<UserBasicInfo> fansInfos = watchListPeopleMapper.getFansInfo(userId);
        fansInfos.stream().forEach(entity-> {
            //从redis 中查询 用户统计信息
            entity.setUserStatistics(userStatisticsService.getUserStatisticsFromRedis(entity.getId()));
            Integer id = entity.getId();
            boolean isMutualfans = this.isMutualfans(id, userId);
            if (isMutualfans) {
                entity.setMutualFansStatus(1);
            } else {
                entity.setMutualFansStatus(0);
            }
        });

        return fansInfos;
    }

    @Override
    public User getWatchAll(Integer userId) {
        return watchListPeopleMapper.getWatchAll(userId);
    }

    @Override
    public boolean isMutualfans(Integer firstId, Integer twoId) {

        WatchListPeople watchListPeopleOne = watchListPeopleMapper.selectByWatchUserAndUser(firstId, twoId);
        if (watchListPeopleOne != null) {
            WatchListPeople watchListPeopleTwo = watchListPeopleMapper.selectByWatchUserAndUser(twoId, firstId);
            if (watchListPeopleTwo != null) {
                return true;// 互粉
            }
        }
        return false;
    }

    @Override
    public List<PeopleDynamic> getPeopleDynamic(Integer userId) {
        List<PeopleDynamic> peopleDynamics = watchListPeopleMapper.getPeopleDynamic(userId);
        peopleDynamics.stream().forEach(entity -> {
            if (entity.getUserId() - userId == 0) {
                entity.setStatus(0);
            } else {
                entity.setStatus(1);
            }
        });
        return peopleDynamics;
    }

    @Override
    public Integer getFansCount(int userId) {
        return watchListPeopleMapper.selectCountByWatchUserId(userId);
    }

    @Override
    public Integer getWatchCount(int userId) {
        return watchListPeopleMapper.selectCountByUserId(userId);
    }
}
