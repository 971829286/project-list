package com.souche.niu.dao.userInformation;

import com.souche.niu.model.userInformation.SubscribeDo;
import com.souche.niu.model.userInformation.TbKvValueDo;

import java.util.List;

public interface UserInformationDao {

    List<SubscribeDo> queryByUserID(String userID);

    List<TbKvValueDo> getKvList(String groupId);

    /**
     * 根据用户ID统计是否有订阅
     * @param userId
     * @return
     */
    Integer countSubscribeByUserId(String userId);
}
