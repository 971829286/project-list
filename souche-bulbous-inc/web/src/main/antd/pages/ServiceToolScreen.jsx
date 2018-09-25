import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';

import CMSHeader from '../components/CMSHeader';

import {Form, Table, Button, Pagination, Popconfirm, Input, Modal, Icon, Upload, Checkbox} from 'antd';


/**
 * @Description：服务工具配置页
 *
 * @remark: Created by wujingtao in 2018/9/10
 *
 *  谨以此页面献给各位老师，祝各位老师节日快乐
 **/


class ServiceToolScreen extends SADPage {

    constructor() {
        super();
        //todo 设置弹窗的初始状态及初始数据
        this.state = {
            //todo 入口配置所需参数
            toolId:0,
            toolProtocol: '',  //工具页面打开协议
            toolTitle: '',    //工具页面标题
            toolProtocolMsg: '',
            toolTitleMsg: '',
            saveStatus: false,
            savemsg: '',

            //todo 各项目配置所需参数
            page: 1,
            pageSize: 10,
            openId: '',
            success: true,   //用作保存信息成功与否时的状态判断
            msg: '',         //保存失败时表单提示信息
            operateType: '',  //区别新增or编辑
            visible: false,
            toast: false,    //取消按钮的二次确认弹框
            toastmsg: '',
            title: '',
            protocol: '',
            url2X: '',
            url3X: '',
            clickPoint: '',  //点击事件埋点
            orderNum: '',    //排序值
            isShow: false,       //是否在首页显示
            fileList2X: [],
            fileList3X: [],
            titlemsg: '',    //标题框提示信息
            protocolmsg: '',
            fileListmsg2X: '',
            fileListmsg3X: '',
            orderNumMsg: ''
        }

        setTimeout(() => {
            this.setState({
                toolId:this.state.entranceData.id,
                toolProtocol:this.state.entranceData.protocol,
                toolTitle:this.state.entranceData.title,
            })
        }, 1000);
        console.log("页面构建方法....")
    }

    saveButton = () => {
        if (!this.state.toolProtocol) {
            this.setState({toolProtocolMsg: '工具页面打开协议不允许为空'});
            return;
        }
        if (!this.state.toolTitle) {
            this.setState({toolTitleMsg: '工具页面标题不允许为空'});
            return;
        }
        //todo 调后端保存入口配置的接口
        this.saveEntrance();
        setTimeout(() => {
            alert(this.state.savemsg);
            if (this.state.saveStatus === true) {
                this.setState({
                    toolId:0,
                    toolProtocol: '',
                    toolTitle: ''
                })
                return;
            }
            //拿到后端的状态弹出提示信息
        }, 500);
        location.reload(true);
    }

    //删除
    onDelete = (id) => {
        //todo 调用后端删除接口
        this.setState({
            openId: id
        }, () => this.delete());
        location.reload(true);
    }

    //编辑
    onEdit = (id) => {
        //todo 调用后端回显接口
        //todo 在setState时回调后端接口
        this.setState({
                openId: id
            },
            () => this.edit());
        setTimeout(() => {
            let data = this.state.editData;
            let photo2X = this.state.editData.photo2X;
            let photo3X = this.state.editData.photo3X;
            this.setState({
                operateType: '编辑',
                visible: true,
                title: data.title,
                protocol: data.protocol,
                url2X: data.url2X,
                url3X: data.url3X,
                clickPoint: data.clickPoint,
                orderNum: data.orderNum,
                isShow: (data.isShow === '否' ? false : true),
                fileList2X: [{
                    uid: '-1',
                    name: photo2X.name,
                    status: photo2X.status,
                    url: photo2X.url
                }],
                fileList3X: [{
                    uid: '-2',
                    name: photo3X.name,
                    status: photo3X.status,
                    url: photo3X.url
                }],
                titlemsg: '',
                protocolmsg: '',
                fileListmsg: '',
                orderNumMsg: ''
            })
        }, 500)
    }

    //todo 设置显示状态及所传参数
    addButton = () => {
        this.setState(() => ({
            operateType: '新增',
            title: '',
            protocol: '',
            clickPoint: '',  //目标用户 多个之间用逗号隔开 为空表示所有用户
            orderNum: '',
            fileList2X: [],
            fileList3X: [],
            isShow: false,
            visible: true,
            titlemsg: '',    //标题框提示信息
            protocolmsg: '',
            fileListmsg: '',
            orderNumMsg: ''
        }));
    }

    //todo 点击对话框OK按钮触发的事件
    handleOk = () => {
        if (!this.state.title) {
            this.setState({titlemsg: '标题不允许为空'});
            return;
        }
        if (!this.state.protocol) {
            this.setState({protocolmsg: '跳转协议不允许为空'});
            return;
        }
        if (!this.state.url2X) {
            this.setState({fileListmsg2X: '图标2X不允许为空'});
            return;
        }
        if (!this.state.url3X) {
            this.setState({fileListmsg3X: '图标3X不允许为空'});
            return;
        }
        if (!this.state.orderNum) {
            this.setState({orderNumMsg: '排序值不允许为空'});
            return;
        }
        if (isNaN(this.state.orderNum)) {
            this.setState({orderNumMsg: '排序值请输入1-1000的整数'});
            return;
        }
        if (parseFloat(this.state.orderNum) < 1) {
            this.setState({orderNumMsg: '排序值请输入1-1000的整数'});
            return;
        }
        if (parseFloat(this.state.orderNum) > 1000) {
            this.setState({orderNumMsg: '排序值请输入1-1000的整数'});
            return;
        }
        if(!/^\d+$/.test(this.state.orderNum)){
            this.setState({orderNumMsg: '排序值请输入1-1000的整数'});
            return;
        }
        this.saveInfo();
        //验证是否保存成功
        setTimeout(() => {
            if (this.state.success === true) {
                alert(this.state.msg);
                return;
            }
            this.setState({visible: false})
        }, 500);
        location.reload(true);
    }

    //todo 点击对话框取消按钮触发的事件
    handleCancel = () => {
        if (this.state.operateType === '新增') {
            if (
                this.state.title != '' ||
                this.state.protocol != '' ||
                this.state.url2X != '' ||
                this.state.url3X != '' ||
                this.state.clickPoint != '' ||
                this.state.orderNum != '' ||
                this.state.isShow != false
            ) {
                this.setState(() => ({
                    toast: true,
                    toastmsg: '内容已添加'
                }));
                return;
            }
            //未添加任何内容 直接取消
            this.setState(() => ({
                visible: false,
            }));
        }

        if (this.state.operateType === '编辑') {
            let data = this.state.editData;
            let flag = (this.state.isShow === false ? '否' : '是')
            if (
                data.title != this.state.title ||
                data.protocol != this.state.protocol ||
                data.url2X != this.state.url2X ||
                data.url3X != this.state.url3X ||
                data.clickPoint != this.state.clickPoint ||
                data.isShow != flag
            ) {
                this.setState({
                    toast: true,
                    toastmsg: '内容已修改'
                })
                return;
            }
            //内容未发生变更 直接取消
            this.setState(() => ({
                visible: false,
            }));
        }

    }


    //todo 上传图片
    handleChange2X = (info) => {
        let fileList = info.fileList;
        //移除照片的动作
        if (fileList.length === 0) {
            this.setState({url2X: ''})
        }
        // 1. Limit the number of uploaded files
        // Only to show two recent uploaded files, and old ones will be replaced by the new
        fileList = fileList.slice(-1);

        // 2. Read from response and show file link
        fileList = fileList.map((file) => {
            if (file.response) {
                // Component will show file.url as link
                file.url = file.response.data;
                this.state.url2X = file.response.data;
                this.state.fileListmsg2X = '';
            }
            return file;
        });

        // 3. Filter successfully uploaded files according to response from server
        fileList = fileList.filter((file) => {
            if (file.response) {
                return file.response.success ===true;
            }
            return true;
        });

        this.setState({fileList2X: fileList});
    }

    //todo 上传图片
    handleChange3X = (info) => {
        let fileList = info.fileList;
        //移除照片的动作
        if (fileList.length === 0) {
            this.setState({url3X: ''})
        }

        // 1. Limit the number of uploaded files
        // Only to show two recent uploaded files, and old ones will be replaced by the new
        fileList = fileList.slice(-1);

        // 2. Read from response and show file link
        fileList = fileList.map((file) => {
            if (file.response) {
                // Component will show file.url as link
                file.url = file.response.data;
                this.state.url3X = file.response.data;
                this.state.fileListmsg3X = '';
            }
            return file;
        });

        // 3. Filter successfully uploaded files according to response from server
        fileList = fileList.filter((file) => {
            if (file.response) {
                return file.response.success === true;
            }
            return true;
        });

        this.setState({fileList3X: fileList});
    }


    render() {

        if (!this.stateAlready) {
            return null;
        }

        const uploadFile2X = {
            action: 'uploadAPI/uploadPic.json',
            onChange: this.handleChange2X.bind(this),
            multiple: true,
            accept: 'image/jpeg,image/jpeg,image/x-png,image/gif',
        }

        const uploadFile3X = {
            action: 'uploadAPI/uploadPic.json',
            onChange: this.handleChange3X.bind(this),
            multiple: true,
            accept: 'image/jpeg,image/jpeg,image/x-png,image/gif'
        }

        //列表标题
        const columns = [
            {title: '标题', dataIndex: 'title', width: '20%', key: 'title'},
            {
                title: '图片', dataIndex: 'url2X', width: '15%', key: 'url2X',
                render: (text, record) => {
                    return (
                        <img src={record.url2X} width={'40px'} height={'60px'}></img>
                    );
                }
            },
            {title: '是否显示在首页', dataIndex: 'isShow', width: '20%', key: 'isShow'},
            {title: '排序值', dataIndex: 'orderNum', width: '25%', key: 'orderNum'},
            {
                title: '操作',
                key: 'operating',
                width: '20%',
                dataIndex: 'operating',
                render: (text, record) => {
                    return (
                        <span>
                            <a href={'#'} onClick={() => this.onEdit(record.id)}>编辑</a>
                            <label> | </label>
                            <Popconfirm title="确定删除?" okText="确定" cancelText="取消"
                                        onConfirm={() => this.onDelete(record.id)}>
                            <a href={'#'}>删除</a>
                            </Popconfirm>
                        </span>
                    );
                }
            }
        ];

        return (
            <CMSHeader>
                <div style={{
                    margin: '10px 10px',
                    border: '2px solid #ccc',
                    borderRadius: "15px",
                }}>
                    <table style={{
                        padding: "10px 10px 10px 20px",
                        width: '100%'
                    }}>
                        <tbody>
                        {/*标题行*/}
                        <tr>
                            <td style={{width: '16%', fontWeight: '900', fontSize: 'large',paddingLeft:'20px'}}>
                                <a style={{color: 'black'}}>入口配置</a>
                            </td>
                            <td style={{width: '84%'}}></td>
                        </tr>

                        {/*我的-工作页面打开协议行*/}
                        <tr>
                            <td style={{width: '16%',paddingLeft:'20px'}}>
                                <label>我的-所有工具页面打开协议：</label>
                            </td>
                            <td style={{width: '84%', paddingRight: '10px'}}>
                                <input style={{width: '99%', float: 'right'}}
                                       type={'text'}
                                       id={'toolTitle'}
                                       onChange={(e) => {
                                           this.setState({
                                               toolProtocol: e.target.value,
                                               toolProtocolMsg: ''
                                           })
                                       }}
                                       value={this.state.toolProtocol}
                                />
                            </td>
                        </tr>
                        <tr>
                            <td style={{width: '16%'}}></td>
                            <td style={{width: '84%',paddingLeft:'15px'}}>
                                <lable style={{color: 'red'}}>{this.state.toolProtocolMsg}</lable>
                            </td>
                        </tr>

                        {/*我的-工作页面标题行*/}
                        <tr>
                            <td style={{width: '16%',paddingLeft:'20px'}}>
                                <label>我的-所有工具页面标题：</label>
                            </td>
                            <td style={{width: '84%', paddingRight: '10px'}}>
                                <input style={{float: 'right', width: '99%'}}
                                       type={'text'}
                                       id={'toolTitle'}
                                       onChange={(e) => {
                                           this.setState({
                                               toolTitle: e.target.value,
                                               toolTitleMsg: ''
                                           })
                                       }}
                                       value={this.state.toolTitle}
                                />
                            </td>
                        </tr>
                        <tr>
                            <td style={{width: '16%'}}>
                            </td>
                            <td style={{width: '84%',paddingLeft:'15px'}}>
                                <lable style={{color: 'red'}}>{this.state.toolTitleMsg}</lable>
                            </td>
                        </tr>
                        <tr>
                            <td style={{width: '16%'}}></td>
                            <td style={{width: '84%', paddingRight: '10px'}}>
                                <Button type="primary" style={{float: "right", paddingRight: "20px"}}
                                        onClick={() => this.saveButton()}>保存</Button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div style={{margin: '10px 10px'}}>
                    <div style={{
                        border: '2px solid #ccc',
                        borderRadius: "15px",
                        padding: "10px 10px 10px 20px",
                        width: '100%'
                    }}>
                        <div>
                            <table style={{
                                padding: "10px 10px 10px 20px",
                                width: '100%'
                            }}>
                                <tbody>
                                <tr>
                                    <td style={{width: '16%', fontWeight: '900', fontSize: 'large'}}>
                                        <a style={{color: 'black'}}>各项目配置</a>
                                    </td>
                                    <td style={{width: '84%'}}>
                                        <Button type="primary" style={{float: "right", paddingRight: "20px"}}
                                                onClick={() => this.addButton()}>新增</Button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div>
                            <div className="table-content">
                                <Table columns={columns} dataSource={this.state.rowData.item} pagination={false}/>
                            </div>
                            <div className="table-pagination">
                                <Pagination showQuickJumper defaultCurrent={1}
                                            total={this.state.rowData.total}
                                            defaultPageSize={10} onChange={(page) => {
                                    (this.state.page = page) && this.getList()
                                }}/>
                            </div>
                        </div>
                    </div>
                    {/*弹框*/}
                    <div>
                        <Modal
                            onOk={this.handleOk}
                            onCancel={this.handleCancel}
                            okText={'保存'}
                            cancelText={'取消'}
                            visible={this.state.visible}
                            title={this.state.operateType + '|服务工具'}>
                            <Form>
                                <table>
                                    <tbody>
                                    {/* 标题行*/}
                                    <tr>
                                        <td style={{width: '21%'}}><label>标题：</label></td>
                                        <td style={{width: '78%'}}><Input type={'text'} id={'title'}
                                                                          onChange={(e) => {
                                                                              this.setState({
                                                                                  title: e.target.value,
                                                                                  titlemsg: ''
                                                                              })
                                                                          }} value={this.state.title}/>
                                        </td>
                                        <td style={{width: '1%'}}>
                                            <lable style={{color: 'red'}}>*</lable>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <lable style={{color: 'red'}}>{this.state.titlemsg}</lable>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>

                                    {/*协议地址行*/}
                                    <tr>
                                        <td style={{width: '21%'}}><label>协议地址：</label></td>
                                        <td style={{width: '78%'}}><Input type={'text'} id={'protocol'}
                                                                          onChange={(e) => {
                                                                              this.setState({
                                                                                  protocol: e.target.value,
                                                                                  protocolmsg: ''
                                                                              })
                                                                          }} value={this.state.protocol}/>
                                        </td>
                                        <td style={{width: '1%'}}>
                                            <lable style={{color: 'red'}}>*</lable>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <lable style={{color: 'red'}}>{this.state.protocolmsg}</lable>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <lable style={{color: 'red'}}>*支持http协议和本地跳转协议</lable>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>

                                    {/*图标2X*/}
                                    <tr>
                                        <td style={{width: '21%'}}>
                                            <label> 图标2X：</label>
                                        </td>
                                        <td style={{width: '78%'}}>
                                            <Upload {...uploadFile2X} fileList={this.state.fileList2X}>
                                                <Button>
                                                    <Icon type="upload"/> 上传图片
                                                </Button>
                                            </Upload>
                                        </td>
                                        <td style={{width: '1%'}}>
                                            <lable style={{color: 'red'}}>*</lable>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <lable style={{color: 'red'}}>{this.state.fileListmsg2X}</lable>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>

                                    {/*图标3X*/}
                                    <tr>
                                        <td style={{width: '21%'}}>
                                            <label> 图标3X：</label>
                                        </td>
                                        <td style={{width: '78%'}}>
                                            <Upload {...uploadFile3X} fileList={this.state.fileList3X}>
                                                <Button>
                                                    <Icon type="upload"/> 上传图片
                                                </Button>
                                            </Upload>
                                        </td>
                                        <td style={{width: '1%'}}>
                                            <lable style={{color: 'red'}}>*</lable>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <lable style={{color: 'red'}}>{this.state.fileListmsg3X}</lable>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>

                                    {/*点击事件埋点行*/}
                                    <tr>
                                        <td style={{width: '21%'}}>
                                            <label>点击事件埋点：</label>
                                        </td>
                                        <td style={{width: '78%'}}>
                                            <Input type={'text'} id={'clickPoint'} onChange={(e) => {
                                                this.setState({clickPoint: e.target.value})
                                            }} value={this.state.clickPoint}/>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <label style={{color: 'red'}}>*点击事件打点KEY</label>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>

                                    {/*排序值行*/}
                                    <tr>
                                        <td style={{width: '21%'}}>
                                            <label> 排序值：</label>
                                        </td>
                                        <td style={{width: '78%'}}>
                                            <Input type={'text'} id={'orderNum'} onChange={(e) => {
                                                this.setState({
                                                    orderNum: e.target.value,
                                                    orderNumMsg: ''
                                                })
                                            }} value={this.state.orderNum}/>
                                        </td>
                                        <td style={{width: '1%'}}>
                                            <lable style={{color: 'red'}}>*</lable>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <lable style={{color: 'red'}}>{this.state.orderNumMsg}</lable>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '21%'}}></td>
                                        <td style={{width: '78%'}}>
                                            <lable style={{color: 'red'}}>*输入1-1000的数，数字越大，排序越靠前</lable>
                                        </td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>
                                    <tr>
                                        <td style={{width: '25%'}}>
                                            <Checkbox checked={this.state.isShow} onChange={(e) => {
                                                this.setState({isShow: e.target.checked})
                                            }}>是否在首页显示</Checkbox>
                                        </td>
                                        <td style={{width: '74%'}}></td>
                                        <td style={{width: '1%'}}></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </Form>
                        </Modal>
                    </div>
                    {/*取消按钮的确认弹框*/}
                    <div>
                        <Modal title={'确认取消提示'}
                               okText={'确定'}
                               cancelText={'取消'}
                               visible={this.state.toast}
                               onOk={() => {
                                   this.setState({
                                       toast: false,
                                       visible: false
                                   })
                               }}
                               onCancel={() => {
                                   this.setState({
                                       toast: false
                                   })
                               }}
                        >
                            <label>{this.state.toastmsg}，确定取消吗？</label>
                        </Modal>
                    </div>
                </div>
            </CMSHeader>
        )
    }
}

ReactDom.render(
    <ServiceToolScreen></ServiceToolScreen>,
    document.querySelector("#content")
);
