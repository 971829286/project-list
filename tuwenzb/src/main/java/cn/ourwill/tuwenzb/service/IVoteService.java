package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.ActivityContent;
import cn.ourwill.tuwenzb.entity.Vote;
import cn.ourwill.tuwenzb.service.IBaseService;

import java.util.List;

/**
 * 　ClassName:IVoteService
 * Description：
 * User:hasee
 * CreatedDate:20178/2/27 15:51
 */

public interface IVoteService extends IBaseService<Vote>{

    //保存
    public Integer createVote(Vote vote);

    //根据活动id获取投票记录
    List<Vote> getVoteList(Integer userId,Integer activityId);

    //批量删除投票记录
    Integer deleteVotes(List<Integer> voteIds);
    //更新记录
    Integer updateVote(Vote vote);
    //投票
    boolean addVoted(List<Integer> voteOptionIds, Integer userId);

    //根据id获取投票信息
    Vote getVoteById(Integer voteId);

    //根据id关闭投票
    Integer closedById(Integer voteId);

    Integer deleteOptionById(Integer optionId);

    int getVoteCount(Integer activityId);
}
