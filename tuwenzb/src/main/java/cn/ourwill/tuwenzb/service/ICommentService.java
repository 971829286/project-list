package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.Comment;
import cn.ourwill.tuwenzb.entity.CommentWithActivity;

import java.util.List;
import java.util.Map;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface ICommentService extends IBaseService<Comment>{
    List<Comment> getByParam(Map param);
    List<Comment> getByParamWithBack(Map param);
    List<CommentWithActivity> getByUserId(Integer userId,Integer photoLive);
    Integer save(Comment comment,Integer checkType);
    @Override
    Integer delete(Integer id);

    Integer updateCheckBatch(List<Integer> ids, Integer check);

    Integer selectCountByCheck(Integer activityId,Integer checkType);

    Integer volumeIncrease(List<Comment> comments);

    boolean addLike(Integer commentId,Integer userId);

    boolean cancelLike(Integer commentId, Integer userId);

    boolean isLiked(Integer commentId, Integer userId);

    Integer getLikeNum(Integer commentId);

    List<Comment> getByPhotoId(Integer photoId,Integer checkType);

    List<CommentWithActivity> getBackByUserId(Integer userId, Integer photoLive);
}
