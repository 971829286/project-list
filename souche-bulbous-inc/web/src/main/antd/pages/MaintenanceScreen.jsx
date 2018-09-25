import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';

import CMSHeader from '../components/CMSHeader';

import {Button, Modal, Icon, Upload} from 'antd';


class MaintenanceScreen extends SADPage {

    /**
     * @author wujingtao
     *
     *
     * @date 2018年09月11日
     */

    constructor() {
        super();
        //todo 设置初始弹窗的显示状态 以及初始数据
        this.state = {
            //todo banner 配置所需字段
            bannerSuccess: false,   //banner配置成功与否时的状态判断
            bannerMsg: '',         //保存失败时表单提示信息
            bannerId: '',
            banner: '',
            bannerUrl: '',
            bannerProtocol: '',
            banner_msg: '',         //banner配置为空时提示信息
            bannerUrlMsg: '',      //banner配置URL为空时提示信息
            bannerProtocolMsg: '',   //跳转协议

            //todo 金额配置所需字段
            moneySuccess: false,   //金额配置成功与否的的状态信息
            moneyMsg: '',         //保存操作的提示信息
            moneyId: '',
            noCertification: '',   //非认证金额
            certification: '',   //认证金额
            noCertificationMsg: '',
            certificationMsg: '',
            fileList: [],
            previewVisible: false,
            previewImage: '',

            bannerToast: false,    //确认保存banner提示
            moneyToast:false     //确认保存monner
        }
        console.log("页面构建方法....")
        //setTimeout(() => {this.getDataRecord()}, 100);
        setTimeout(() => {
            let photo = this.state.bannerData.photo;
            if (photo.length === 0) {
                photo = []
            }
            this.setState({
                bannerId: this.state.bannerData.id,
                banner: this.state.bannerData.bannerTitle,
                bannerUrl: this.state.bannerData.url,
                bannerProtocol: this.state.bannerData.protocol,
                fileList: photo,
                moneyId: this.state.moneyData.id,
                noCertification: this.state.moneyData.noCertification,
                certification: this.state.moneyData.certification,
            });
        }, 1000);
    }


    //todo 保存banner配置
    bannerSave = () => {
        /*if (!this.state.banner) {
            this.setState({banner_msg: 'BANNER配置不允许为空'});
            return;
        }

        if (!this.state.bannerUrl) {
            this.setState({bannerUrlMsg: '照片不允许为空'});
            return;
        }
        if (!this.state.bannerProtocol) {
            this.setState({bannerProtocolMsg: '跳转协议地址不允许为空'});
            return;
        }*/
        //调用后端保存接口
        this.setState({
                bannerId: this.state.bannerData.id,
                bannerToast:false,
            },
            () => this.saveBanner());
        //验证保存成功与否
        //alert("保存成功！");
        location.reload(true);

    }

    //todo 保存金额配置
    moneySave = () => {

       /* if (!this.state.noCertification && this.state.noCertification !== 0) {
            this.setState({noCertificationMsg: '非认证金额不允许为空'});
            return;
        }
        if (isNaN(this.state.noCertification)) {
            this.setState({noCertificationMsg: '非认证金额不允许输入非数值类型'});
            return;
        }
        if (parseFloat(this.state.noCertification) <= 0) {
            this.setState({noCertificationMsg: '非认证金额不允许<=0'});
            return;
        }
        if (!this.state.certification && this.state.certification !== 0) {
            this.setState({certificationMsg: '认证金额不允许为空'});
            return;
        }
        if (isNaN(this.state.certification)) {
            this.setState({certificationMsg: '认证金额不允许输入非数值类型'});
            return;
        }
        if (parseFloat(this.state.certification) <= 0) {
            this.setState({certificationMsg: '认证金额不允许为空且不允许<=0'});
            return;
        }*/

        //调用后端接口
        this.setState({
            moneyId: this.state.moneyData.id,
            noCertification: this.state.noCertification + '',
            certification: this.state.certification + '',
            moneyToast:false,
        }, () => this.saveMoney());

        //验证保存成功与否
        //alert("保存成功！");
        location.reload(true);

    }

    //todo 上传图片
    handleChange = (info) => {
        let fileList = info.fileList;
        if (fileList.length === 0) {
            this.setState({bannerUrl: ''});
            this.state.bannerData.photo = []
        }

        // 1. Limit the number of uploaded files
        // Only to show two recent uploaded files, and old ones will be replaced by the new
        fileList = fileList.slice(-1);

        // 2. Read from response and show file link
        fileList = fileList.map((file) => {
            if (file.response) {
                // Component will show file.url as link
                file.url = file.response.data;
                this.state.bannerUrl = file.response.data;
                this.state.banner_msg = '';
            }
            return file;
        });

        // 3. Filter successfully uploaded files according to response from server
        fileList = fileList.filter((file) => {
            if (file.response) {
                console.log("file.response.status:" + file.response.success);
                return file.response.success === true;
            }
            return true;
        });

        this.setState({fileList: fileList});

    }

    //todo 照片大图展示事件
    handlePreview = (file) => {
        this.setState({
            previewImage: file.url || file.thumbUrl,
            previewVisible: true,
        });
    }

    //todo 照片大图关闭事件
    handleCancel = () => {
        this.setState({previewVisible: false})
    }

    initPhoto = () => {
        console.log("初始化照片...")
        let photo = this.state.bannerData.photo;
        return this.state.fileList;
    }


    render() {

        if (!this.stateAlready) {
            return null;
        }

        const uploadButton = (
            <div>
                <Icon type="plus"/>
                <div style={{marginTop: '8px', color: '#666'}}>上传图片</div>
            </div>
        );

        const uploadFile = {
            action: 'uploadAPI/uploadPic.json',
            onChange: this.handleChange.bind(this),
            multiple: true,
            accept: 'image/jpeg,image/jpeg,image/x-png,image/gif',
            listType: "picture-card",
            onPreview: this.handlePreview.bind(this),
        };


        return (
            <CMSHeader>
                <div style={{margin: '100px 300px'}}>
                    <div style={{
                        border: '2px solid #ccc',
                        borderRadius: "15px",
                        width: '100%'
                    }}>
                        {/*banner配置*/}
                        <table style={{
                            padding: "10px 10px 10px 20px",
                            width: '100%'
                        }}>
                            <tbody>
                            <tr>
                                <td style={{width: '25%', fontWeight: '900', fontSize: 'large', paddingTop: '20px'}}>
                                    <a style={{color: 'black', paddingLeft: '10px'}}>banner配置</a>
                                </td>
                                <td style={{width: '75%'}}></td>
                            </tr>
                            <tr style={{paddingTop: '10px'}}>
                                <td style={{width: '15%'}}>
                                    <label style={{paddingLeft: '10px'}}>BANNER标题：</label>
                                </td>
                                <td style={{width: '85%'}}>
                                    <input style={{width: '99%', float: 'left', paddingRight: '20px'}}
                                           type={'banner'}
                                           id={'banner'}
                                           onChange={(e) => {
                                               this.setState({
                                                   banner: e.target.value,
                                                   banner_msg: '',
                                               })
                                           }}
                                           value={this.state.banner}
                                    />
                                </td>
                            </tr>
                            <tr>
                                <td style={{width: '15%'}}></td>
                                <td style={{width: '85%'}}>
                                    <label style={{color: 'red'}}>{this.state.banner_msg}</label>
                                </td>
                            </tr>
                            <tr>
                                <td style={{width: '15%'}}>
                                    <label style={{paddingLeft: '10px'}}>图片：</label>
                                </td>
                                <td style={{width: '85%'}}>
                                    <Upload {...uploadFile} fileList={this.state.fileList}>
                                        {this.state.fileList.length >= 1 ? null : uploadButton}
                                    </Upload>
                                </td>
                            </tr>
                            <tr>
                                <td style={{width: '15%'}}></td>
                                <td style={{width: '85%'}}>
                                    <label style={{color: 'red'}}>{this.state.bannerUrlMsg}</label>
                                </td>
                            </tr>
                            {/*跳转协议*/}
                            <tr>
                                <td style={{width: '15%'}}>
                                    <label style={{paddingLeft: '10px'}}>跳转协议地址：</label>
                                </td>
                                <td style={{width: '85%'}}>
                                    <input style={{width: '99%', float: 'left', paddingRight: '20px'}}
                                           type={'protocol'}
                                           id={'protocol'}
                                           onChange={(e) => {
                                               this.setState({
                                                   bannerProtocol: e.target.value,
                                                   bannerProtocolMsg: '',
                                               })
                                           }}
                                           value={this.state.bannerProtocol}
                                    />
                                </td>
                            </tr>
                            <tr>
                                <td style={{width: '15%'}}></td>
                                <td style={{width: '85%'}}>
                                    <label style={{color: 'red'}}>{this.state.bannerProtocolMsg}</label>
                                </td>
                            </tr>
                            <tr>
                                <td style={{width: '15%'}}></td>
                                <td style={{width: '85%'}}>
                                    <label style={{color: 'red'}}>*支持http协议和本地协议跳转协议</label>
                                </td>
                            </tr>
                            <tr>
                                <td style={{width: '15%'}}></td>
                                <td style={{width: '85%', paddingRight: '10px'}}>
                                    <Button type="primary" style={{float: "right", paddingRight: "20px"}}
                                            onClick={() => {
                                                if (!this.state.banner) {
                                                    this.setState({banner_msg: 'BANNER标题不允许为空'});
                                                    return;
                                                }
                                                if (!this.state.bannerUrl) {
                                                    this.setState({bannerUrlMsg: '照片不允许为空'});
                                                    return;
                                                }
                                                /*if (!this.state.bannerProtocol) {
                                                    this.setState({bannerProtocolMsg: '跳转协议地址不允许为空'});
                                                    return;
                                                }*/
                                                this.setState({
                                                    bannerToast: true
                                                })
                                            }}>保存</Button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <br/>
                    <div style={{
                        border: '2px solid #ccc',
                        borderRadius: "15px",
                    }}>
                        {/*金额配置*/}
                        <table style={{
                            padding: "10px 10px 10px 20px",
                            width: '100%'
                        }}>
                            <tbody>
                            <tr>
                                <td style={{width: '25%', fontWeight: '900', fontSize: 'large', paddingTop: '20px'}}>
                                    <a style={{color: 'black', paddingLeft: '10px'}}>金额配置</a>
                                </td>
                                <td style={{width: '25%'}}></td>
                                <td style={{width: '25%'}}></td>
                                <td style={{width: '25%'}}></td>
                            </tr>
                            <tr>
                                <td style={{width: '25%'}}>
                                    <label style={{paddingLeft: '10px'}}>非认证车行金额（元）：</label>
                                </td>
                                <td style={{width: '25%'}}>
                                    <input style={{width: '90%', float: 'left'}}
                                           type={'noCertification'}
                                           id={'noCertification'}
                                           onChange={(e) => {
                                               this.setState({
                                                   noCertification: e.target.value,
                                                   noCertificationMsg: '',
                                               })
                                           }}
                                           value={this.state.noCertification}
                                           placeholder={'金额为非负数'}
                                    />
                                </td>
                                <td style={{width: '25%'}}>
                                    <label style={{paddingLeft: '10px'}}>认证车行金额（元）：</label>
                                </td>
                                <td style={{width: '25%'}}>
                                    <input style={{width: '90%', float: 'left'}}
                                           type={'certification'}
                                           id={'certification'}
                                           onChange={(e) => {
                                               this.setState({
                                                   certification: e.target.value,
                                                   certificationMsg: '',
                                               })
                                           }}
                                           value={this.state.certification}
                                           placeholder={'金额为非负数'}
                                    />
                                </td>
                            </tr>


                            <tr>
                                <td style={{width: '25%'}}></td>
                                <td style={{width: '25%'}}>
                                    <label style={{color: 'red'}}>{this.state.noCertificationMsg}</label>
                                </td>
                                <td style={{width: '25%'}}></td>
                                <td style={{width: '25%'}}>
                                    <label style={{color: 'red'}}>{this.state.certificationMsg}</label>
                                </td>
                            </tr>
                            <tr>
                                <td style={{width: '25%'}}></td>
                                <td style={{width: '25%'}}></td>
                                <td style={{width: '25%'}}></td>
                                <td style={{width: '25%', paddingRight: '10px'}}>
                                    <Button type="primary" style={{float: "right", paddingRight: "20px"}}
                                            onClick={() => {
                                                if (!this.state.noCertification && this.state.noCertification !== 0) {
                                                    this.setState({noCertificationMsg: '非认证金额不允许为空'});
                                                    return;
                                                }
                                                if (isNaN(this.state.noCertification)) {
                                                    this.setState({noCertificationMsg: '非认证金额不允许输入非数值类型'});
                                                    return;
                                                }
                                                if (parseFloat(this.state.noCertification) <= 0) {
                                                    this.setState({noCertificationMsg: '非认证金额不允许<=0'});
                                                    return;
                                                }
                                                if (!this.state.certification && this.state.certification !== 0) {
                                                    this.setState({certificationMsg: '认证金额不允许为空'});
                                                    return;
                                                }
                                                if (isNaN(this.state.certification)) {
                                                    this.setState({certificationMsg: '认证金额不允许输入非数值类型'});
                                                    return;
                                                }
                                                if (parseFloat(this.state.certification) <= 0) {
                                                    this.setState({certificationMsg: '认证金额不允许为空且不允许<=0'});
                                                    return;
                                                }
                                                this.setState({moneyToast:true})
                                            }}>保存</Button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
                <div>
                    <Modal visible={this.state.previewVisible} footer={null} onCancel={this.handleCancel}>
                        <img alt="example" style={{width: '100%'}} src={this.state.previewImage}/>
                    </Modal>
                </div>
                {/*banner保存的确认弹框*/}
                <div>
                    <Modal title={'确定保存提示'}
                           okText={'确定'}
                           cancelText={'取消'}
                           visible={this.state.bannerToast}
                           onOk={() => this.bannerSave()}
                           onCancel={() => {
                               this.setState({
                                   bannerToast: false
                               });
                               location.reload(true);
                           }}
                    >
                        <label>确定保存banner配置吗？</label>
                    </Modal>
                </div>
                {/*维保金额保存的确认弹框*/}
                <div>
                    <Modal title={'确定保存提示'}
                           okText={'确定'}
                           cancelText={'取消'}
                           visible={this.state.moneyToast}
                           onOk={() => this.moneySave()}
                           onCancel={() => {
                               this.setState({
                                   moneyToast: false
                               });
                               location.reload(true);
                           }}
                    >
                        <label>确定保存金额配置吗？</label>
                    </Modal>
                </div>
            </CMSHeader>
        )
    }
}

ReactDom.render(
    <MaintenanceScreen></MaintenanceScreen>,
    document.querySelector("#content")
);
