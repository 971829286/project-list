package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.BulletScreen;
import cn.ourwill.tuwenzb.entity.SignWall;
import cn.ourwill.tuwenzb.mapper.BulletScreenMapper;
import cn.ourwill.tuwenzb.mapper.SignWallMapper;
import cn.ourwill.tuwenzb.service.ISignWallService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/1/16 18:42
 * @Description
 */
@Repository
public class SignWallServiceImpl extends BaseServiceImpl<SignWall> implements ISignWallService {

    @Autowired
    private SignWallMapper signWallMapper;
    @Autowired
    private BulletScreenMapper bulletScreenMapper;

    @Override
    @Transactional
    public Integer save(SignWall signWall){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BulletScreen bulletScreen = new BulletScreen();
        bulletScreen.setRoomId(signWall.getRoomId());
        bulletScreen.setAvatar(signWall.getAvatar());
        bulletScreen.setNickname(signWall.getNickname());
        bulletScreen.setUserId(signWall.getUserId());
        bulletScreen.setCheckStatus(1);
        bulletScreen.setContent("签到成功");
        bulletScreenMapper.save(bulletScreen);
        return signWallMapper.save(signWall);
    }

    @Override
    public List<SignWall> getByUserId(Integer userId,Integer roomId) {
        return signWallMapper.getByUserId(userId,roomId);
    }

    @Override
    public List<SignWall> getByRoomId(Integer roomId,Date time) {
        return signWallMapper.getByRoomId(roomId,time);
    }

    @Override
    public Integer getRanking(Integer roomId, Date cTime) {
        return signWallMapper.getRanking(roomId,cTime);
    }
}
