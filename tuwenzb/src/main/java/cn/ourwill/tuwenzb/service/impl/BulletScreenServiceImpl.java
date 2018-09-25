package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.BulletScreen;
import cn.ourwill.tuwenzb.mapper.BulletScreenMapper;
import cn.ourwill.tuwenzb.service.IBulletScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:44
 * @Description
 */
@Repository
public class BulletScreenServiceImpl extends BaseServiceImpl<BulletScreen> implements IBulletScreenService {
    @Autowired
    private BulletScreenMapper bulletScreenMapper;
    @Override
    public List<BulletScreen> getByRoomId(Integer roomId, Date time) {
        return bulletScreenMapper.getByRoomId(roomId,time);
    }
}
