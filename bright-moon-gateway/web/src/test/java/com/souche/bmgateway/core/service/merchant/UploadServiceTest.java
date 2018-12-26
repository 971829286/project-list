package com.souche.bmgateway.core.service.merchant;

import com.souche.bmgateway.core.BaseTest;
import com.souche.bmgateway.core.dto.request.UploadPhotoRequest;
import com.souche.bmgateway.core.dto.request.UploadPicRequest;
import com.souche.bmgateway.core.dto.response.HttpBaseResponse;
import com.souche.bmgateway.core.enums.PicInfoEnums;
import com.souche.bmgateway.core.manager.merchant.MerchantManager;
import com.souche.bmgateway.core.util.CommonUtil;
import com.souche.bmgateway.core.constant.Constants;
import com.souche.bmgateway.core.util.UploadUtil;
import com.souche.optimus.common.util.UUIDUtil;
import com.souche.optimus.core.web.Result;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 上传图片
 *
 * @author chenwj
 * @since 2018/8/2
 */
public class UploadServiceTest extends BaseTest {

    @Resource
    private MerchantManager merchantManager;

    @Resource
    private MerchantService merchantService;

    private static final String downloadFilePath = "/Users/bb/Documents/downloadDir/";
    private static final String merchantId = "3512731531873";
    private static final String urlStr = "http://img.souche.com/xc2tjme07dyo66qq2hj0xrvuvhftgcai.jpg";

    @Test
    public void uploadTest() throws Exception {
        String idCardSavePath = downloadFilePath + merchantId + Constants.Purpose.CETIFICATION;
        UploadUtil.download(urlStr, PicInfoEnums.ID_CARD.getFileName(), idCardSavePath);
        String idCard = UploadUtil.getImgStr(idCardSavePath + PicInfoEnums.ID_CARD.getFileName());

        String idCardBackSavePath = downloadFilePath + merchantId + Constants.Purpose.CETIFICATION_BACK;
        UploadUtil.download(urlStr, PicInfoEnums.ID_CARD_BACK.getFileName(), idCardBackSavePath);
        String idCardBack = UploadUtil.getImgStr(idCardBackSavePath + PicInfoEnums.ID_CARD_BACK.getFileName());

        String businessLicensePath = downloadFilePath + merchantId + Constants.Purpose.BUSINESS_LICENSE;
        UploadUtil.download(urlStr, PicInfoEnums.BUSINESS_LICENSE.getFileName(), businessLicensePath);
        String businessLicense = UploadUtil.getImgStr(businessLicensePath + PicInfoEnums.BUSINESS_LICENSE.getFileName());

        // 身份证正面
        UploadPhotoRequest req = new UploadPhotoRequest();
        req.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        req.setPhotoType("01");
        req.setPictureName("idCard.jpg");
        req.setPicture(idCard);
        HttpBaseResponse responseDTO = merchantManager.uploadPhoto(req);
        Assert.assertTrue(responseDTO.isSuccess());

        // 身份证反面
        UploadPhotoRequest req2 = new UploadPhotoRequest();
        req2.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        req2.setPhotoType("02");
        req2.setPictureName("idCardBack.jpg");
        req2.setPicture(idCardBack);
        HttpBaseResponse responseDTO2 = merchantManager.uploadPhoto(req2);
        Assert.assertTrue(responseDTO2.isSuccess());

        // 营业执照
        UploadPhotoRequest req3 = new UploadPhotoRequest();
        req3.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        req3.setPhotoType("03");
        req3.setPictureName("businessLicense.jpg");
        req3.setPicture(businessLicense);
        HttpBaseResponse responseDTO3 = merchantManager.uploadPhoto(req3);
        Assert.assertTrue(responseDTO3.isSuccess());
    }

    /**
     * 身份证正面
     */
    @Test
    public void uploadIdCardTest() throws Exception {
        String idCardSavePath = downloadFilePath + merchantId + Constants.Purpose.CETIFICATION;
        UploadUtil.download(urlStr, PicInfoEnums.ID_CARD.getFileName(), idCardSavePath);
        UploadUtil.compressPicture(idCardSavePath, PicInfoEnums.ID_CARD.getFileName());
        String idCard = UploadUtil.getImgStr(idCardSavePath + PicInfoEnums.ID_CARD.getFileName());

        UploadPhotoRequest req = new UploadPhotoRequest();
        req.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        req.setPhotoType("01");
        req.setPictureName("idCard.jpg");
        req.setPicture(idCard);
        HttpBaseResponse responseDTO = merchantManager.uploadPhoto(req);
        Assert.assertTrue(responseDTO.isSuccess());
    }

    /**
     * 身份证反面
     */
    @Test
    public void uploadIdCardBackTest() throws Exception {
        String idCardSavePath = downloadFilePath + merchantId + Constants.Purpose.CETIFICATION_BACK;
        UploadUtil.download(urlStr, PicInfoEnums.ID_CARD_BACK.getFileName(), idCardSavePath);
        UploadUtil.compressPicture(idCardSavePath, PicInfoEnums.ID_CARD_BACK.getFileName());
        String idCard = UploadUtil.getImgStr(idCardSavePath + PicInfoEnums.ID_CARD_BACK.getFileName());

        UploadPhotoRequest req = new UploadPhotoRequest();
        req.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        req.setPhotoType("02");
        req.setPictureName("idCardBack.jpg");
        req.setPicture(idCard);
        HttpBaseResponse responseDTO = merchantManager.uploadPhoto(req);
        Assert.assertTrue(responseDTO.isSuccess());
    }

    /**
     * 营业执照
     */
    @Test
    public void uploadBusinessLicenseTest() throws Exception {
        String idCardSavePath = downloadFilePath + merchantId + Constants.Purpose.BUSINESS_LICENSE;
        UploadUtil.download(urlStr, PicInfoEnums.BUSINESS_LICENSE.getFileName(), idCardSavePath);
        UploadUtil.compressPicture(idCardSavePath, PicInfoEnums.BUSINESS_LICENSE.getFileName());
        String idCard = UploadUtil.getImgStr(idCardSavePath + PicInfoEnums.BUSINESS_LICENSE.getFileName());

        UploadPhotoRequest req = new UploadPhotoRequest();
        req.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        req.setPhotoType("03");
        req.setPictureName("businessLicense.jpg");
        req.setPicture(idCard);
        HttpBaseResponse responseDTO = merchantManager.uploadPhoto(req);
        Assert.assertTrue(responseDTO.isSuccess());
    }

    /**
     * 开户许可证
     */
    @Test
    public void uploadAcctOpenLicenseTest() throws Exception {
        String idCardSavePath = downloadFilePath + merchantId + Constants.Purpose.ACCOUNT_OPEN_LICENSE;
        UploadUtil.download(urlStr, PicInfoEnums.ACCT_OPEN_LICENSE.getFileName(), idCardSavePath);
        UploadUtil.compressPicture(idCardSavePath, PicInfoEnums.ACCT_OPEN_LICENSE.getFileName());
        String idCard = UploadUtil.getImgStr(idCardSavePath + PicInfoEnums.ACCT_OPEN_LICENSE.getFileName());

        UploadPhotoRequest req = new UploadPhotoRequest();
        req.setOutTradeNo("MS" + CommonUtil.getToday() + UUIDUtil.getID());
        req.setPhotoType("05");
        req.setPictureName("acctOpenLicense.jpg");
        req.setPicture(idCard);
        HttpBaseResponse responseDTO = merchantManager.uploadPhoto(req);
        Assert.assertTrue(responseDTO.isSuccess());
    }

    /**
     * 调用图片上传service
     */
    @Test
    public void uploadPic() {
        UploadPicRequest param = new UploadPicRequest();
        param.setMemberId("200000110035");
        param.setIdCardPic("http://img.souche.com/files/default/b4ffb28971eb44a53e2fa07ea48d620b.png");
        param.setIdCardBackPic("http://img.souche.com/files/default/2060ca24d6d8397e41177f09032bcb80.png");
        param.setBusinessLicensePic("http://img.souche.com/files/default/c0a8c8146fd5e37a52c7ab05576aeadf.png");
        param.setAcctOpenLicensePic(urlStr);
        Result<String> rs = merchantService.uploadPic(param);
        Assert.assertTrue(rs.isSuccess());
    }

}
