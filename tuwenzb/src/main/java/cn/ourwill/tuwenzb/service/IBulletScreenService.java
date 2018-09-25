package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.BulletScreen;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:44
 * @Description
 */
public interface IBulletScreenService extends IBaseService<BulletScreen>{
    List<BulletScreen> getByRoomId(Integer roomId, Date date);
}
