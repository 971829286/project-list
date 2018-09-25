package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.*;
import cn.ourwill.tuwenzb.mapper.*;
import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.service.IUserService;
import cn.ourwill.tuwenzb.service.IVoteService;
import cn.ourwill.tuwenzb.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class VoteServiceImpl extends BaseServiceImpl<Vote> implements IVoteService {

    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private VoteOptionsMapper voteOptionsMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Integer createVote(Vote vote) {

        List<VoteOptions> voteOptions = vote.getVoteOptions();
        voteMapper.save(vote);
        for(VoteOptions vo : voteOptions){
            vo.setVoteId(vote.getId());
        }
        voteOptionsMapper.saveList(voteOptions);
        return vote.getId();
    }

    @Override
    public List<Vote> getVoteList(Integer userId,Integer activityId) {

        List<Vote> votes = voteMapper.getVoteList(activityId);
        for(Vote vote:votes){
            List<VoteOptions> voteOptionses =  voteOptionsMapper.getByVoteId(vote.getId());
            vote.setVoteOptions(voteOptionses);
            Integer votedTotal = 0;
            Integer isVoted = 0;
            for(VoteOptions voteOptions:voteOptionses){
                //选项投票数
                Integer optionTotal = redisTemplate.opsForZSet().zCard("voteOption:"+voteOptions.getId()).intValue();
                Double result = redisTemplate.opsForZSet().score("voteOption:"+voteOptions.getId(),userId);
                if (result != null) {
                    voteOptions.setIsVoted(1);
                    isVoted = 1;
                }else{
                    voteOptions.setIsVoted(0);
                }
                votedTotal += optionTotal;
                voteOptions.setOptionTotal(optionTotal);
            }
            //投票总数+
            vote.setVotedTotal(votedTotal);
            vote.setIsVoted(isVoted);
            for(VoteOptions voteOptions:voteOptionses){
                if(votedTotal > 0){
                    Double percent = Double.valueOf(voteOptions.getOptionTotal() )/ votedTotal*100;
                    DecimalFormat df   = new DecimalFormat("######0.00");
                    //投票占比
                    voteOptions.setOptionPercent(df.format(percent)+"%");
                }else{
                    voteOptions.setOptionPercent("0%");
                }
            }
            vote.setVoteOptions(voteOptionses);
            Date endTime = vote.getEndTime();
            Integer status = vote.getStatus();

            if(endTime.after(new Date()) && status !=  -1){
                //投票进行中
                vote.setStatus(1);
            }else{
                //投票已关闭
                vote.setStatus(-1);
             }
        }
        return votes;
    }

    @Transactional
    @Override
    public Integer deleteVotes(List<Integer> voteIds) {
        Integer count = voteMapper.deleteVotes(voteIds);
        voteOptionsMapper.deleteOptionsMapperes(voteIds);
        return  count;
    }

    @Transactional
    @Override
    public Integer updateVote(Vote vote) {
        Integer count = voteMapper.updateVote(vote);
        List<VoteOptions> voteOptions = vote.getVoteOptions();
        voteOptionsMapper.updateVoteOptions(voteOptions);
        return count;
    }

    @Transactional
    @Override
    public boolean addVoted(List<Integer> voteOptionIds, Integer userId) {
        Boolean boo = true;
        for(Integer voteOptionId :voteOptionIds){
            boo = redisTemplate.opsForZSet().add("voteOption:"+voteOptionId,userId,System.currentTimeMillis());
            if (boo == false) {
                return boo;
            }
        }
        return boo;
    }

    @Override
    public Vote getVoteById(Integer voteId) {
        Vote vote = voteMapper.getById(voteId);
        List<VoteOptions> voteOptionses = voteOptionsMapper.getByVoteId(voteId);
        Integer votedTotal = 0;

        for(VoteOptions voteOptions:voteOptionses){
            //选项投票数
            Integer optionTotal = redisTemplate.opsForZSet().zCard("voteOption:"+voteOptions.getId()).intValue();
            votedTotal += optionTotal;
            voteOptions.setOptionTotal(optionTotal);
        }
        //投票总数+
        vote.setVotedTotal(votedTotal);
        for(VoteOptions voteOptions:voteOptionses){
            Double percent = Double.valueOf(voteOptions.getOptionTotal() )/ votedTotal*100;
            DecimalFormat df   = new DecimalFormat("######0.00");
            //投票占比
            voteOptions.setOptionPercent(df.format(percent)+"%");
        }
        vote.setVoteOptions(voteOptionses);
        Date endTime = vote.getEndTime();
        if(endTime.after(new Date()) && vote.getStatus() != -1){
            //投票进行中
            vote.setStatus(1);
        }else{
            //投票已关闭
            vote.setStatus(-1);
        }
        return vote;
    }

    @Override
    public Integer closedById(Integer voteId) {
        return voteMapper.closedById(voteId);
    }

    @Override
    public Integer deleteOptionById(Integer optionId) {
        return voteOptionsMapper.deleteOptionById(optionId);
    }

    @Override
    public int getVoteCount(Integer activityId) {
        return voteMapper.getVoteCount(activityId);
    }
}
