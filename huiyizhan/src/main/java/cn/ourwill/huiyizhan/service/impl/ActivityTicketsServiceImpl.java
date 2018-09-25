package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.entity.ActivityTickets;
import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.mapper.ActivityTicketsMapper;
import cn.ourwill.huiyizhan.mapper.TicketsRecordMapper;
import cn.ourwill.huiyizhan.service.IActivityTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/23 18:35
 * @Description
 */
@Service
public class ActivityTicketsServiceImpl extends BaseServiceImpl<ActivityTickets> implements IActivityTicketsService {

    @Autowired
    private ActivityTicketsMapper activityTicketsMapper;
    @Autowired
    private TicketsRecordMapper ticketsRecordMapper;

    @Override
    public int batchSave(Integer userId, Integer activityId,List<ActivityTickets> tickets) {
        //添加默认值
        tickets.stream().forEach(entity -> {
            entity.setTicketPrice(entity.getTicketPrice()==null?0:entity.getTicketPrice());
            entity.setIsPublishSell(entity.getIsPublishSell()==null?0:entity.getIsPublishSell());
            entity.setIsCheck(entity.getIsCheck()==null?0:entity.getIsCheck());
            entity.setSellStatus(entity.getSellStatus()==null?0:entity.getSellStatus());
            entity.setSingleLimits(entity.getSingleLimits()==null?0:entity.getSingleLimits());
            entity.setTotalNumber(entity.getTotalNumber()==null?0:entity.getTotalNumber());
            entity.setStockNumber(entity.getTotalNumber()==null?0:entity.getTotalNumber());
            entity.setIsFree(1);
            entity.setActivityId(activityId);
            entity.setUserId(userId);
            entity.setCTime(new Date());
        });
        return activityTicketsMapper.batchSave(tickets);
    }

    @Override
    public List<ActivityTickets> getByActivityId(Integer activityId) {
        return activityTicketsMapper.selectByActivityId(activityId);
    }

    @Override
    @Transactional
    public int batchUpdate(Integer activityId, List<ActivityTickets> activityTickets,User user) {
        List<Integer> ids = activityTicketsMapper.getAllIdByActivityId(activityId);
        List<Integer> updateIds = new ArrayList<>();
        int rank = 0;
        Date now = new Date();
        for (ActivityTickets entity : activityTickets) {
            updateIds.add(entity.getId());
            entity.setRank(rank++);
            entity.setIsFree(1);
            entity.setActivityId(activityId);
            entity.setUserId(user.getId());
            entity.setCTime(now);
            entity.setUId(user.getId());
            entity.setUTime(now);
            //重新计算库存
            resetStockNumber(entity);
        }
//        activityTickets.stream().forEach(entity->{
//            updateIds.add(entity.getId());
////            entity.setRank(rank);
//            rank = rank + 1;
//            entity.setIsFree(1);
//            entity.setActivityId(activityId);
//            entity.setUserId(user.getId());
//            entity.setCTime(now);
//            entity.setUId(user.getId());
//            entity.setUTime(now);
//        });
        List<Integer> deleteIds = ids.stream().filter(id -> !updateIds.contains(id)&&ticketsRecordMapper.getCountByTicketsId(id)<=0).collect(Collectors.toList());
        if(deleteIds.size()>0)
            activityTicketsMapper.batchDelete(deleteIds);
        return activityTicketsMapper.batchUpdate(activityTickets);
    }

    private void resetStockNumber(ActivityTickets tickets){
        ActivityTickets origin = activityTicketsMapper.selectByPrimaryKey(tickets.getId());
        if(origin==null) {
            if(tickets.getTotalNumber()==null) tickets.setTotalNumber(0);
            tickets.setStockNumber(tickets.getTotalNumber());
            return;
        }
        //源票不为空 && 源票总量不为0 && 原票总量!= 现票总量
        if(origin!=null&&!origin.getTotalNumber().equals(0)&&!tickets.getTotalNumber().equals(origin.getTotalNumber())){
            //统计票数计算
            int count = ticketsRecordMapper.selectCountByTicketsId(tickets.getId());
            int stockNumber = tickets.getTotalNumber()-count;
            if(stockNumber>0){
                tickets.setStockNumber(stockNumber);
            }else{
                tickets.setStockNumber(0);
            }
            //前后差值计算
//            int diff = tickets.getTotalNumber() - origin.getTotalNumber();
//            if(tickets.getTotalNumber().equals(0)){
//                tickets.setStockNumber(0);
//            }else if(diff!=0){
//                if((origin.getStockNumber()==null?0:origin.getStockNumber())+diff>0) {
//                    tickets.setStockNumber((origin.getStockNumber() == null ? 0 : origin.getStockNumber()) + diff);
//                }else{
//                    tickets.setStockNumber(0);
//                }
//            }
        }else if(tickets.getTotalNumber().equals(origin.getTotalNumber())){
            tickets.setStockNumber(origin.getStockNumber());
        }else{
            tickets.setStockNumber(origin.getTotalNumber());
        }
    }

    @Override
    public List<ActivityTickets> getValidByActivityId(Integer activityId) {
        return activityTicketsMapper.getValidByActivityId(activityId);
    }
}
