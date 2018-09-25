package cn.ourwill.tuwenzb.service.impl;

import cn.ourwill.tuwenzb.entity.LicenseRecord;
import cn.ourwill.tuwenzb.entity.User;
import cn.ourwill.tuwenzb.mapper.LicenseRecordMapper;
import cn.ourwill.tuwenzb.mapper.UserMapper;
import cn.ourwill.tuwenzb.service.ILicenseRecordService;
import cn.ourwill.tuwenzb.utils.GlobalUtils;
import cn.ourwill.tuwenzb.utils.ReturnResult;
import cn.ourwill.tuwenzb.utils.ReturnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/22 0022 17:25
 * @Version1.0
 */
@Service
public class LicenseRecordServiceImpl extends BaseServiceImpl<LicenseRecord> implements ILicenseRecordService{

    @Autowired
    LicenseRecordMapper licenseRecordMapper;
    @Autowired
    UserMapper userMapper;

    //上传
    public Integer save(LicenseRecord licenseRecord){return licenseRecordMapper.save(licenseRecord);}

    //更新
    public Integer update(LicenseRecord licenseRecord){return licenseRecordMapper.save(licenseRecord);}

    //根据ID查找
    public LicenseRecord selectById(Integer id){return licenseRecordMapper.getById(id); }

    //获取所有数据
    public List<LicenseRecord> findAll(Integer photoLive){return licenseRecordMapper.findAll(photoLive);}

    //通过属性查询
    public List<LicenseRecord> getByParam(Map map){return licenseRecordMapper.getByParam(map);}

    //批量删除
    public Integer deletePatch(List<Integer> ids){return licenseRecordMapper.deleteBatch(ids);}

    @Override
    @Transactional
    public Map addLicense(Integer userId, Map map) {
        Integer count =0;
        String username ="";
        Integer licenseType = -1;
        String amount = "";
        String paymentType = "";
        Integer photoLive = 0;

        if(map.get("userId")!=null)
            userId = Integer.parseInt(map.get("userId").toString());
        if(map.get("count")!=null)
            count = Integer.parseInt(map.get("count").toString());
        if(map.get("username")!=null)
            username = map.get("username").toString();
        if(map.get("licenseType")!=null)
            licenseType = Integer.parseInt(map.get("licenseType").toString());
        if(map.get("amount")!=null)
            amount = map.get("amount").toString();
        if (map.get("paymentType")!=null)
            paymentType = map.get("paymentType").toString();
        if(map.get("photoLive")!=null&&map.get("photoLive").equals(1))
            photoLive = 1;
        //count参数为输入的  场数  或   年数
        LicenseRecord licenseRecord = new LicenseRecord();
        licenseRecord.setLicenseType(licenseType);
        licenseRecord.setAmount(amount);
        licenseRecord.setPaymentType(paymentType);
        licenseRecord.setPhotoLive(photoLive);

        //用于更新用户表
        User u = new User();
        //传入LicenseRecord对象
        //根据用户名查询出ID,设置用户ID
        User user = userMapper.selectByUsername(username);
        if(user == null)
            return ReturnResult.errorResult("输入的用户不存在");
        else {
            userId = user.getId();
            licenseRecord.setUserId(userId);
            u.setId(userId);
        }
        if(photoLive.equals(0)&&user.getLicenseType().equals(1)){
            return ReturnResult.errorResult("已有包年授权，无法再次授权！");
        }
        if(photoLive.equals(1)&&user.getPhotoLicenseType().equals(1)){
            return ReturnResult.errorResult("已有包年授权，无法再次授权！");
        }
        //判断授权类型 （1:包年  2:包时长  9:永久）
        if(licenseRecord.getLicenseType() == 1){
            //包年
            licenseRecord.setSessionsTotal(100);
            //计算
            Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR, count);
            //授权截止日期
            licenseRecord.setDueDate(c.getTime());
            if(photoLive.equals(0)) {
                u.setDueDate(c.getTime());
                u.setPackYearsDays(100);
            }else{
                u.setPhotoDueDate(c.getTime());
                u.setPhotoPackYearsDays(100);
            }
        }else if(licenseRecord.getLicenseType() == 2){
            //包时长
            licenseRecord.setSessionsTotal(count);
            if(photoLive.equals(0)) {
                Integer remainingDays = user.getRemainingDays() == null ? 0 : user.getRemainingDays();
                u.setRemainingDays(remainingDays + count);
            }else{
                Integer remainingDays = user.getPhotoRemainingDays() == null ? 0 : user.getPhotoRemainingDays();
                u.setPhotoRemainingDays(remainingDays + count);
            }
        }else if(licenseRecord.getLicenseType() == 9){
            //包永久，数据表不存
        }else{
            //授权类型传递错误
            return ReturnResult.errorResult("授权类型不存在");
        }
        //创建人ID
        licenseRecord.setCId(userId);
        //创建时间
        licenseRecord.setCTime(new Date());
        //交易时间
        licenseRecord.setTransactionDate(new Date());

        u.setUId(userId);
        u.setUTime(new Date());
        if(photoLive.equals(0)) {
            u.setLicenseType(licenseRecord.getLicenseType());
        }else{
            u.setPhotoLicenseType(licenseRecord.getLicenseType());
        }
        if(userMapper.updateAuthorization(u)<=0)
            return ReturnResult.errorResult("提交失败");
        //授权记录信息保存   保存记录
        if(licenseRecordMapper.save(licenseRecord)>0)
            return ReturnResult.successResult(ReturnType.ADD_SUCCESS);
        return ReturnResult.errorResult(ReturnType.ADD_ERROR);
    }

    @Override
    public List<LicenseRecord> selectByUserId(Integer userId,Integer photoLive) {
        return licenseRecordMapper.selectByUserId(userId,photoLive);
    }

}
