package cn.ourwill.willcenter.service;

import cn.ourwill.willcenter.entity.User;
import com.qiniu.common.QiniuException;

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

    Integer updateByUuid(User user) throws QiniuException;

    Integer updateUserType(Integer userType, Integer id);

    User selectByUsername(String username);

    User selectByUsernameOrPhone(String username);

    List<User> selectByParams(Map params);

    Integer changePWD(Integer userId, String newPassword, String salt);

    Integer changePWDByUuid(String uuid, String newPassword, String salt);

    User setectByMobPhone(String mobPhone);

    Integer updatePhone(String newPhone,Integer id);

    Integer updateUserVersion(String uuid);

    Integer getUserVersion(String uuid);

    User getUserByUUID( String uuid);

    User selectByUnionId(String unionid);

    User selectByEmailOrPhone(String username);

    Integer changeUnionIdByUuid(String unionid , String uuid);
    @Override
    Integer update(User user);

    Integer updateEmail(Integer id,String email);

    List<User> selectWithOutUuid();

    int batchUpdateUUID(List<User> users);
}
