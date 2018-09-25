package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.Activity;
import cn.ourwill.tuwenzb.entity.Comment;
import cn.ourwill.tuwenzb.entity.CommentWithActivity;
import cn.ourwill.tuwenzb.mapper.ActivityMapper;
import cn.ourwill.tuwenzb.mapper.ActivityStatisticsMapper;
import cn.ourwill.tuwenzb.mapper.CommentMapper;
import cn.ourwill.tuwenzb.service.IActivityStatisticsService;
import cn.ourwill.tuwenzb.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements ICommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ActivityStatisticsMapper activityStatisticsMapper;
    @Autowired
    IActivityStatisticsService activityStatisticsService;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Comment> getByParam(Map param) {
        return commentMapper.getByParam(param);
    }

    @Override
    public List<Comment> getByParamWithBack(Map param) {
        return commentMapper.getByParamWithBack(param);
    }

    @Override
    public List<CommentWithActivity> getByUserId(Integer userId,Integer photoLive) {
        return commentMapper.getByUserId(userId,photoLive);
    }

    @Override
    public List<CommentWithActivity> getBackByUserId(Integer userId, Integer photoLive) {
        return commentMapper.getBackByUserId(userId,photoLive);
    }

    @Override
    public Integer save(Comment comment,Integer checkType){
        int count = commentMapper.save(comment);
        //先发后审时 计数
        if(count>0&&checkType.equals(1)) {
            activityStatisticsService.addCommentNumberRedis(comment.getActivityId());
            activityStatisticsService.addRealCommentNumberRedis(comment.getActivityId());
        }
        return count;
    }
    @Override
    public Integer delete(Integer id){
        Map<String,Object> map=new HashMap();
        map.put("id",id);
        Comment comment = commentMapper.getById(id);
        if(comment!=null) {
            Activity activity = activityMapper.getById(comment.getActivityId());
            int count = commentMapper.delete(map);
            if(count>0&&activity.getCheckType().equals(1)) {
                redisTemplate.delete("com:"+comment.getId());
                activityStatisticsService.minusCommentNumberRedis(comment.getActivityId());
                activityStatisticsService.minusRealCommentNumberRedis(comment.getActivityId());
            }
            return count;
        }
        return 0;
    }

    @Override
    public Integer updateCheckBatch(List<Integer> ids, Integer check) {
        int count = commentMapper.updateCheckBatch(ids,check);
        if(check.equals(1)&&count>0){
            redisTemplate.delete(ids.stream().map(id->"com:"+id).collect(Collectors.toList()));
        }
        return count;
    }

    @Override
    public Integer selectCountByCheck(Integer activityId, Integer checkType) {
        return commentMapper.selectCountByCheck(activityId,checkType);
    }

    @Override
    public Integer volumeIncrease(List<Comment> comments) {
        return commentMapper.volumeIncrease(comments);
    }

    @Override
    public boolean addLike(Integer commentId,Integer userId) {
        return redisTemplate.opsForZSet().add("com:"+commentId,userId,System.currentTimeMillis());
    }

    @Override
    public boolean cancelLike(Integer commentId, Integer userId) {
        Long result = redisTemplate.opsForZSet().remove("com:"+commentId,userId);
        if(result>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isLiked(Integer commentId, Integer userId) {
        Double result = redisTemplate.opsForZSet().score("com:"+commentId,userId);
        if(result==null){
            return false;
        }
        return true;
    }

    @Override
    public Integer getLikeNum(Integer commentId) {
        return redisTemplate.opsForZSet().zCard("com:"+commentId).intValue();
    }

    @Override
    public List<Comment> getByPhotoId(Integer photoId,Integer checkType) {
        return commentMapper.getByPhotoId(photoId,checkType);
    }
}
