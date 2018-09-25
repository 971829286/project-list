package cn.ourwill.huiyizhan.service.impl;

import cn.ourwill.huiyizhan.baseEnum.TicketStatus;
import cn.ourwill.huiyizhan.entity.TicketsRecord;
import cn.ourwill.huiyizhan.mapper.ActivityTicketsMapper;
import cn.ourwill.huiyizhan.mapper.TicketsRecordMapper;
import cn.ourwill.huiyizhan.service.ITicketsRecordService;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/23 18:36
 * @Description
 */
@Service
public class TicketsRecordServiceImpl extends BaseServiceImpl<TicketsRecord> implements ITicketsRecordService {
    @Autowired
    private TicketsRecordMapper ticketsRecordMapper;
    @Autowired
    private ActivityTicketsMapper activityTicketsMapper;

    @Override
    public boolean checkIsSell(Integer ticketsId) {
        int count = ticketsRecordMapper.getCountByTicketsId(ticketsId);
        if(count>0) return true;
        return false;
    }

    @Override
    public List<TicketsRecord> getByActivityIdUserId(Integer activityId, Integer userId, Boolean isValid) {
        return ticketsRecordMapper.getByActivityIdUserId(activityId,userId,isValid);
    }

    @Override
    public int updateUserInfo(TicketsRecord ticketsRecord) {
        return ticketsRecordMapper.updateUserInfoById(ticketsRecord);
    }

    @Override
    public List<TicketsRecord> selectByParamsWithOrder(Map params) {
        return ticketsRecordMapper.selectByParamsWithOrder(params);
    }

    @Override
    @Transactional
    public int checkTicket(Integer id, Integer activityId, boolean isPass) {
        int status = TicketStatus.SIGN_NOT.getIndex();
        if(!isPass) {
            status = TicketStatus.CHECK_NOT_PASS.getIndex();
        }
        int count = ticketsRecordMapper.checkTicket(id,activityId,status);
        if(!isPass) {
            ticketsRecordMapper.refreshStockNumByTicketId(id);
        }
        return count;
    }

    @Override
    @Transactional
    public int checkTicketBatch(List<Integer> ids, Integer activityId,boolean isPass) {
        int status = TicketStatus.SIGN_NOT.getIndex();
        if(!isPass) {
            status = TicketStatus.CHECK_NOT_PASS.getIndex();
        }
        int count = ticketsRecordMapper.checkTicketBatch(ids,activityId,status);
        if(!isPass) {
            //根据门票记录更新库存
            ticketsRecordMapper.refreshStockNumByTicketIds(ids);
        }
        return count;
    }

    @Override
    @Transactional
    public int refundTicket(Integer id, Integer activityId) {
        int count = ticketsRecordMapper.refundTicket(id, activityId, TicketStatus.REFUNDED.getIndex());
        if(count>0) {
            ticketsRecordMapper.refreshStockNumByTicketId(id);
        }
        return count;
    }

    @Override
    public List<TicketsRecord> getParticipation(Integer id, Integer status) {
        return ticketsRecordMapper.getParticipation(id,status);
    }

    @Override
    public List<TicketsRecord> selectSignedByActivityId(Integer activityId) {
        return ticketsRecordMapper.selectSignedByActivityId(activityId);
    }

    @Override
    public int updateSignStatus(Integer id, Integer activityId, int ticketStatus) {
        if(ticketStatus == 1){
            return ticketsRecordMapper.cancelSign(id, activityId);
        }
        return ticketsRecordMapper.doSign(id,activityId);
    }

    @Override
    public TicketsRecord selectByAuthCodeOrSignCode(Integer activityId,String code) {
        return ticketsRecordMapper.selectByAuthCodeOrSignCode(activityId,code);
    }

    @Override
    public Map selectStatisticsByActivityId(Integer activityId) {
        return ticketsRecordMapper.selectCountByActivityId(activityId);
    }

    @Override
    public int updateTicketLink(String ticketLink, Integer id) {
        return ticketsRecordMapper.updateTicketLink(ticketLink,id);
    }

    @Override
    public List<TicketsRecord> selectTicketsByopenIdAndActivityId(String openId, Integer activityId) {
        Map<String,Object> map = new HashedMap<>();
        map.put("openId",openId);
        map.put("activityId",activityId);
        return ticketsRecordMapper.selectTicketsByopenIdAndActivityId(map);
    }

    @Override
    public Integer statisticsMyTicket(int userId, boolean isValid) {
        return ticketsRecordMapper.statisticsMyTicket(userId,isValid);
    }


}
