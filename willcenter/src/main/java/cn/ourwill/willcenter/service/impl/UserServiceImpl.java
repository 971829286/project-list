package cn.ourwill.willcenter.service.impl;


import cn.ourwill.willcenter.entity.User;
import cn.ourwill.willcenter.mapper.UserMapper;
import cn.ourwill.willcenter.service.IFdfsImageService;
import cn.ourwill.willcenter.service.IQiniuService;
import cn.ourwill.willcenter.service.IUserService;
import com.qiniu.common.QiniuException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IQiniuService qiniuService;

    @Autowired
    private IFdfsImageService fdfsImageService;

    @Override
    public Integer save(User user){
        Integer count = userMapper.save(user);
        return count;
    }

    @Override
    public Integer updateUserType(Integer userType, Integer id) {
        User user=new User();
        user.setUserType(userType);
        user.setId(id);
        return userMapper.updateUserType(user);
    }
    //根据用户名查找
    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    //根据用户名查找
    @Override
    public User selectByUsernameOrPhone(String username) {
        return userMapper.selectByUsernameOrPhone(username);
    }

    @Override
    public List<User> selectByParams(Map params) {
        return userMapper.selectByParams(params);
    }

    @Override
    public Integer changePWD(Integer userId, String newPassword, String salt) {
        return userMapper.changePWD(userId,newPassword,salt);
    }

    @Override
    public Integer changePWDByUuid(String uuid, String newPassword, String salt) {
        return userMapper.changePWDByUuid(uuid,newPassword,salt);
    }

    @Override
    public User setectByMobPhone(String mobPhone) {
        return userMapper.selectByMobPhone(mobPhone);
    }

    @Override
    public Integer updatePhone(String newPhone,Integer id) {
        return userMapper.updatePhone(newPhone,id);
    }

    @Override
    public Integer updateUserVersion(String uuid) {
        return userMapper.updateUserVersion(uuid);
    }

    @Override
    public Integer getUserVersion(String uuid) {
        return userMapper.getUserVersion(uuid);
    }

    @Override
    public User getUserByUUID(String uuid) {
        return userMapper.getUserByUUID(uuid);
    }

    public List<User> findAll(){
       return userMapper.findAll();
    }

    @Override
    public Integer update(User user) {
        return userMapper.update(user);
    }

    @Override
    public Integer updateEmail(Integer id, String email) {
        return userMapper.updateEmail(id,email);
    }

    @Override
    public List<User> selectWithOutUuid() {
        return userMapper.selectWithOutUuid();
    }

    @Override
    public int batchUpdateUUID(List<User> users) {
        return userMapper.batchUpdateUUID(users);
    }

    @Override
    public Integer updateByUuid(User user) throws QiniuException {
        //图片持久
        if (StringUtils.isNotEmpty(user.getAvatar()) ) {
            if(user.getAvatar().contains("group")){
                Integer count = fdfsImageService.dataPersistence(user.getAvatar());
                if (count<=0){
                    return 0;
                }
            }
        }
        return userMapper.updateByUuid(user);
    }

    @Override
    public User selectByUnionId(String unionid){
        User user =  userMapper.getUserByUnionid(unionid);
        return user;
    }

    @Override
    public User selectByEmailOrPhone(String phoneOrEmail) {
        return userMapper.selectByEmailOrPhone(phoneOrEmail);
    }


    @Override
    public Integer changeUnionIdByUuid(String unionid,String uuid) {
        return userMapper.changeUnionIdByUuid(unionid,uuid);
    }


}
