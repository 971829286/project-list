import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';

import CMSHeader from '../components/CMSHeader';

import {Form, Table, Radio, Button, Pagination, Popconfirm, Input, Modal, Icon, Upload, DatePicker} from 'antd';

import moment from 'moment';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;
const FormItem = Form.Item;
const {RangePicker} = DatePicker;

class ActivityScreen extends SADPage {

    /**
     * @author wujingtao
     *
     * @desc 开屏配置展示页
     *
     * @date 2018年09月03日11:34:30
     */

    constructor() {
        super();
        //todo 设置初始弹窗的显示状态 以及初始数据
        this.state = {
            page: 1,
            pageSize: 10,
            openId: '',
            status: '1',  //查找标志  默认为查全部
            success: true,   //用作保存信息成功与否时的状态判断
            msg: '',         //保存失败时表单提示信息
            operateType: '',  //区别新增or编辑
            visible: false,
            toast: false,    //取消按钮的二次确认弹框
            toastmsg: '',
            title: '',
            protocol: '',
            url: '',
            targetUser: '',  //目标用户 多个之间用逗号隔开 为空表示所有用户
            startTime: '',   //时间精确到分钟
            endTime: '',
            fileList: [],
            titlemsg: '',    //标题框提示信息
            protocolmsg: '',
            fileListmsg: '',
            datemsg: '',

            previewImageUrl:'',  //查看大图
            previewVisible:false
        }
        console.log("页面构建方法....")
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
            let photo = this.state.editData.photo;
            this.setState({
                operateType: '编辑',
                visible: true,
                title: data.title,
                protocol: data.protocol,
                url: data.url,
                targetUser: data.targetUser,
                startTime: data.startTime,
                endTime: data.endTime,
                fileList: [{
                    uid: '-1',
                    name: photo.name,
                    status: photo.status,
                    url: photo.url
                }],
                titlemsg: '',
                protocolmsg: '',
                fileListmsg: '',
                datemsg: ''
            })
        }, 500)
    }

    //todo 设置显示状态及所传参数
    addButton = () => {
        this.setState(() => ({
            operateType: '新增',
            title: '',
            protocol: '',
            url:'',
            targetUser: '',  //目标用户 多个之间用逗号隔开 为空表示所有用户
            startTime: '',   //时间精确到分钟
            endTime: '',
            fileList: [],
            visible: true,
            titlemsg: '',    //标题框提示信息
            protocolmsg: '',
            fileListmsg: '',
            datemsg: '',
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
        if (!this.state.url) {
            this.setState({fileListmsg: '图片不允许为空'});
            return;
        }
        if (!this.state.startTime) {
            this.setState({datemsg: '有效期不允许为空'});
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
                this.state.title !== '' ||
                this.state.protocol !== '' ||
                this.state.url !== '' ||
                this.state.targetUser !== '' ||
                this.state.startTime!==''
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
            if (
                data.title != this.state.title ||
                data.protocol != this.state.protocol ||
                data.url != this.state.url ||
                data.targetUser != this.state.targetUser ||
                data.startTime != this.state.startTime ||
                data.endTime != this.state.endTime
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
    handleChange = (info) => {
        let fileList = info.fileList;
        if (fileList.length === 0) {
            this.setState({url:''})
        }

        // 1. Limit the number of uploaded files
        // Only to show two recent uploaded files, and old ones will be replaced by the new
        fileList = fileList.slice(-1);

        // 2. Read from response and show file link
        fileList = fileList.map((file) => {
            if (file.response) {
                // Component will show file.url as link
                file.url = file.response.data;
                this.state.url = file.response.data;
                this.state.fileListmsg = '';
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

        this.setState({fileList});
    }

    //todo 关闭查看大图
    handleBigPicCancel=()=>{
        this.setState({
            previewVisible:false
        })
    }


    render() {

        if (!this.stateAlready) {
            return null;
        }

        const uploadFile = {
            action: 'uploadAPI/uploadPic.json',
            onChange: this.handleChange.bind(this),
            multiple: true,
            accept: 'image/jpeg,image/jpeg,image/x-png,image/gif'
        };

        //列表标题
        const columns = [
            {title: '标题', dataIndex: 'title', width: '20%', key: 'title'},
            {
                title: '图片', dataIndex: 'pic', width: '15%', key: 'pic',
                render: (text, record) => {
                    return (
                        <img src={record.url} onClick={()=>{this.setState({
                            previewVisible:true,
                            previewImageUrl:record.url
                        })}} width={'100px'} height={'150px'}></img>
                    );
                }
            },
            {title: '创建时间', dataIndex: 'dateCreat', width: '20%', key: 'dateCreat'},
            {title: '有效期', dataIndex: 'period', width: '25%', key: 'period'},
            {title: '状态', dataIndex: 'status', width: '10%', key: 'status'},
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
                <div>
                    <Form layout="inline">
                        <FormItem>
                            <RadioGroup defaultValue="1" onChange={(e) => {
                                this.setState({
                                    page: 1,
                                    pageSize: 10,
                                    status: e.target.value
                                }, () => this.getList())
                            }}>
                                <RadioButton value="1">全部</RadioButton>
                                <RadioButton value="2">生效</RadioButton>
                                <RadioButton value="3">未生效</RadioButton>
                                <RadioButton value="4">已过期</RadioButton>
                            </RadioGroup>
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    onClick={() => this.addButton()}>新增</Button>
                        </FormItem>
                    </Form>
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
                        title={this.state.operateType + '|活动浮窗'}>
                        <Form>
                            <table>
                                <tbody>
                                {/* 标题行*/}
                                <tr>
                                    <td style={{width: '20%'}}><label>标题：</label></td>
                                    <td style={{width: '79%'}}><Input type={'text'} id={'title'}
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
                                    <td style={{width: '20%'}}></td>
                                    <td style={{width: '79%'}}>
                                        <lable style={{color: 'red'}}>{this.state.titlemsg}</lable>
                                    </td>
                                    <td style={{width: '1%'}}></td>
                                </tr>

                                {/*跳转协议行*/}
                                <tr>
                                    <td style={{width: '20%'}}><label>跳转协议地址：</label></td>
                                    <td style={{width: '79%'}}><Input type={'text'} id={'protocol'}
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
                                    <td style={{width: '20%'}}></td>
                                    <td style={{width: '79%'}}>
                                        <lable style={{color: 'red'}}>{this.state.protocolmsg}</lable>
                                    </td>
                                    <td style={{width: '1%'}}></td>
                                </tr>
                                <tr>
                                    <td style={{width: '20%'}}></td>
                                    <td style={{width: '79%'}}>
                                        <lable style={{color: 'red'}}>*支持http协议和本地跳转协议</lable>
                                    </td>
                                    <td style={{width: '1%'}}></td>
                                </tr>

                                {/*图片行*/}
                                <tr>
                                    <td style={{width: '20%'}}>
                                        <label> 图片：</label>
                                    </td>
                                    <td style={{width: '79%'}}>
                                        <Upload {...uploadFile} fileList={this.state.fileList}>
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
                                    <td style={{width: '20%'}}></td>
                                    <td style={{width: '79%'}}>
                                        <lable style={{color: 'red'}}>{this.state.fileListmsg}</lable>
                                    </td>
                                    <td style={{width: '1%'}}></td>
                                </tr>

                                {/*目标用户行*/}
                                <tr>
                                    <td style={{width: '20%'}}>
                                        <label>目标用户：</label>
                                    </td>
                                    <td style={{width: '79%'}}>
                                        <Input type={'text'} id={'targetUser'} onChange={(e) => {
                                            this.setState({targetUser: e.target.value})
                                        }} value={this.state.targetUser}/>
                                    </td>
                                    <td style={{width: '1%'}}></td>
                                </tr>
                                <tr>
                                    <td style={{width: '20%'}}></td>
                                    <td style={{width: '79%'}}>
                                        <label style={{color: 'red'}}>*用逗号隔开，不填默认为所有用户</label>
                                    </td>
                                    <td style={{width: '1%'}}></td>
                                </tr>

                                {/*有效期行*/}
                                <tr>
                                    <td style={{width: '20%'}}>
                                        <label> 有效期：</label>
                                    </td>
                                    <td style={{width: '79%'}}>
                                        <RangePicker
                                            value={[this.state.startTime?moment(this.state.startTime, 'YYYY-MM-DD HH:mm'):null,this.state.endTime? moment(this.state.endTime, 'YYYY-MM-DD HH:mm'):null]}
                                            showTime={{format: 'HH:mm'}}
                                            showToday={true}
                                            format="YYYY-MM-DD HH:mm"
                                            placeholder={['开始时间', '结束时间']}
                                            onChange={(dates, dateStrings) => {
                                                if (dateStrings.length > 0) {
                                                    this.setState({
                                                        datemsg: '',
                                                        startTime: dateStrings[0],
                                                        endTime: dateStrings[1],
                                                    })
                                                }
                                            }}
                                            onOk={() => {
                                            }}
                                        />
                                    </td>
                                    <td style={{width: '1%'}}>
                                        <lable style={{color: 'red'}}>*</lable>
                                    </td>
                                </tr>
                                <tr>
                                    <td style={{width: '20%'}}></td>
                                    <td style={{width: '79%'}}>
                                        <lable style={{color: 'red'}}>{this.state.datemsg}</lable>
                                    </td>
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
                {/*查看大图*/}
                <div>
                    <Modal visible={this.state.previewVisible} footer={null} onCancel={this.handleBigPicCancel}>
                        <img alt="example" style={{width: '100%',height:'100%'}} src={this.state.previewImageUrl}/>
                    </Modal>
                </div>
            </CMSHeader>
        )
    }
}

ReactDom.render(
    <ActivityScreen></ActivityScreen>,
    document.querySelector("#content")
);
