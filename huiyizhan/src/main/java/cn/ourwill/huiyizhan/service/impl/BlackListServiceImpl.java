package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.BlackList;
import cn.ourwill.huiyizhan.mapper.BlackListMapper;
import cn.ourwill.huiyizhan.service.IBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 16:47
 * @Version1.0
 */
@Service
@Slf4j
public class BlackListServiceImpl extends BaseServiceImpl<BlackList> implements IBlackListService {
    @Autowired
    BlackListMapper blackListMapper;

//    @Value("${scheduled.switch}")
//    private Boolean scheduledSwitch;

    @Override
    public Integer save(BlackList entity) {
        Integer type = entity.getType();
        Date now = new Date();
        switch (type){
            case 0:
                entity.setStatus(2);
                break;
            case 1:
                entity.setStatus(1);
                entity.setEndDate(getAroundDate(now,3));
                break;
            case 2:
                entity.setStatus(1);
                entity.setEndDate(getAroundDate(now,30));
                break;
            default:
                entity.setStatus(2);
                break;
        }
        entity.setStartDate(now);
        entity.setCTime(now);
        return blackListMapper.insertSelective(entity);
    }

    @Override
    public Integer update(BlackList entity) {
        Integer type = entity.getType();
        switch (type){
            case 0:
                entity.setStatus(2);
                break;
            case 1:
                entity.setStatus(1);
                entity.setEndDate(getAroundDate(entity.getStartDate(),3));
                break;
            case 2:
                entity.setStatus(1);
                entity.setEndDate(getAroundDate(entity.getStartDate(),30));
                break;
            default:
                entity.setStatus(2);
                break;
        }
        entity.setUTime(new Date());
        return blackListMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<BlackList> selectByParams(Map param) {
        return blackListMapper.getByParam(param);
    }

    @Override
    public Integer unlock(List<Integer> ids) {
        return blackListMapper.unlock(ids);
    }

    @Override
    public boolean isInBlackList(Integer userId) {
        if(blackListMapper.getOperantByUserId(userId) != null && blackListMapper.getOperantByUserId(userId)>0)
            return true;
        return false;
    }

    private Date getAroundDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,day);
        return calendar.getTime();
    }

//    @Scheduled(cron = "${blacklist.refresh.scheduled}")
//    public void refreshBlack(){
//        if(scheduledSwitch) {
//            log.info("===================================refresh BlackList start======================================");
//            int count = blackListMapper.refreshBlack();
//            log.info("update num: " + count);
//            log.info("===================================refresh BlackList end======================================");
//        }
//    }
}
