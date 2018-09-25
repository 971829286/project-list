package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.TicketsRecord;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/23 18:34
 * @Description
 */
public interface ITicketsRecordService extends IBaseService<TicketsRecord> {
    boolean checkIsSell(Integer id);

    List<TicketsRecord> getByActivityIdUserId(Integer id, Integer userId, Boolean isValid);

    int updateUserInfo(TicketsRecord ticketsRecord);

    List<TicketsRecord> selectByParamsWithOrder(Map params);

    int checkTicket(Integer id, Integer activityId,boolean isPass);

    int checkTicketBatch(List<Integer> ids, Integer activityId,boolean isPass);

    int refundTicket(Integer id, Integer activityId);

    /**
     *<pre>
     *          根据票是否到期状态 ：
     *                  0 失效，
     *                  1 有效
     *       获取当前登录人 参与的活动 ，
     *
     * </pre>
     */
    List<TicketsRecord> getParticipation(Integer id, Integer status);

    List<TicketsRecord> selectSignedByActivityId(Integer activityId);

    int updateSignStatus(Integer id, Integer activityId, int index);

    TicketsRecord selectByAuthCodeOrSignCode(Integer activityId,String code);

    Map selectStatisticsByActivityId(Integer activityId);


    int updateTicketLink(String ticketLink,Integer id);

    List<TicketsRecord> selectTicketsByopenIdAndActivityId(String openId, Integer activityId);
    Integer statisticsMyTicket(int userId,boolean isValid);
}
