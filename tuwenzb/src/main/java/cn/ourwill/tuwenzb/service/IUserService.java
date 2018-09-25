package cn.ourwill.tuwenzb.service;

import cn.ourwill.tuwenzb.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 　ClassName:IUserService
 * Description：
 * User:hasee
 * CreatedDate:2017/6/30 15:51
 */

public interface IUserService extends IBaseService<User>{
    @Override
    Integer save(User user);
    //根据微信号（openid）查找微信用户的refresh_token
    List<User> selectByWechatNum(String wechatNum);

    //刷新微信用户的refresh_token
    Integer updateRefreshToken(User user);

    Integer updateUserType(Integer userType, Integer id);

    User selectByUsername(String username);

    User selectByUsernameOrPhone(String username);

    int consumeRemain(Integer days, User user);

    int photoConsumeRemain(Integer days, User user);

    List<User> selectByParams(Map params);

    Integer changePWD(Integer userId, String newPassword, String salt);

    /**
     * @author wanghao
     * 修改用户授权信息
     */
    Integer updateAuthorization(User user);

    void unBinding(User user);

    User setectByMobPhone(String mobPhone);

    Integer updatePhone(String newPhone,Integer id);

    void updateLicenseType(Integer userId);

    List<User> getAdminUser(Integer activityId);

    void getCounts(User user,Integer photoLive);

    int updateUnionId(Integer id, String unionId);
}
