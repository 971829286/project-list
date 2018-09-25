package cn.ourwill.huiyizhan.service;

import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBase;
import cn.ourwill.huiyizhan.entity.UserBasicInfo;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/3/21 18:09
 * @Description
 */
public interface IUserService extends IBaseService<User> {
    User selectByUuid(String uuid);

    User SyncFormCenter(UserBase user);
    User SyncFormCenter(UserBase user,String uuid);

    UserBase getWillCenterUserByUUID(String uuid);

    User selectUserByUsername(String username);
    User selectUserByMobPhone(String mobPhone);

    List<User> selectByParams(Map<String, String> param);

    int updateUserByUUID(UserBase user);

    UserBasicInfo getBasicInfoById(Integer id);

    /**
     * <pre>
     *
     *
     *   更具userId ,获取其信息，(包括统计信息)
     *
     *   判断其与当前登录人的关系
     *   </pre>
     */
    UserBasicInfo getBasicInfoById(Integer userId, Integer loginUserId);

    UserBase selectByUnionId(String unionid);

    public Integer changeUnionIdByUuid(String unionid,String uuid);

}
