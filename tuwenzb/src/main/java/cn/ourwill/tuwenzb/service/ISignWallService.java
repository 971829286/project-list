package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.SignWall;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:41
 * @Description
 */
public interface ISignWallService extends IBaseService<SignWall>{
    List<SignWall> getByUserId(Integer userId,Integer roomId);

    List<SignWall> getByRoomId(Integer roomId,Date time);

    Integer getRanking(Integer roomId, Date cTime);
}
