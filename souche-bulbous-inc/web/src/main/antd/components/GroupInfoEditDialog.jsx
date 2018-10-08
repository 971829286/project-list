import React from 'react';

import '../styles/index.less';
import {Form, Upload, Modal, message, Radio, Input, Button, Switch, Table, Icon} from 'antd';

const Dragger = Upload.Dragger;

const FormItem = Form.Item;


class GroupInfoEditDialog extends React.Component {
    onSubmitFunction = null;
    constructor(props) {
        console.log(props);
        super(props);

        this.onSubmitFunction = props.onSubmit;

        this.state = {
            group_info_visible: false,
            group_info_submit_loading: false,
            picUrl: this.props.picUrl,
            groupName:this.props.groupName
        }

    }

    //编辑按钮的 对话框
    showModal = () => {
        this.setState({
            group_info_visible: true,
        });
    }
    handleOk = () => {
        if(this.state.groupName.length>8){
            Modal.error({title:"检查数据",content:"群名称最长不超过8个字符"});
            return;
        }
        this.setState({group_info_submit_loading: true});
        console.log("提交信息")
        this.onSubmitFunction(this.state.picUrl,this.state.groupName,this.onSubmitSuccess,this.onSubmitFail,this.onSubmitComplate);
    }
    onSubmitSuccess = (resp)=>{
        let self = this;
        Modal.success({title:"操作成功",onOk:()=>{
            self.setState({
                group_info_visible: false,
                group_info_submit_loading: false,
                // picUrl: "",
                // groupName: "",
            });
            this.setState({visible:true})
        }})
    }
    onSubmitFail = (resp)=>{
        var message = resp.data.data.resultMsg;
        Modal.error({title: '操作失败', content: message});
    }
    onSubmitComplate = ()=>{
        this.setState({group_info_submit_loading:false})
    }
    handleCancel = () => {
        this.setState({group_info_visible: false});
    }
    handleInputChange = (event) => {
        console.log(event.target.value);
        if(event.target.value.length>8)
        {
            event.target.value = event.target.value.substring(0,8);
        }
        this.setState({groupName: event.target.value});
    }


    render() {
        var self = this;
        const {formLayout, fileList} = this.state;
        const formItemLayout = formLayout === 'horizontal' ? {
            labelCol: {span: 4},
            wrapperCol: {span: 14},
        } : null;

        const props = {
            name: 'file',
            action: 'http://niu.souche.com/upload/aliyun',
            multiple: false,
            showUploadList: false,
            onChange(info) {
                if (info.file.status !== 'uploading') {
                    console.log(info.file, info.fileList);
                }
                if (info.file.status === 'done') {
                    message.success(`${info.file.name} file uploaded successfully`);
                } else if (info.file.status === 'error') {
                    message.error(`${info.file.name} file upload failed.`);
                }
            },
            onSuccess(ret) {
                console.log(ret);
                if (1 === ret.success) {
                    self.setState({picUrl: ret.path});
                    message.success("文件上传成功");
                } else {
                    message.error("文件上传失败");
                }
            }
        };

        return (

            <div>
                <Button onClick={() => {
                    this.showModal()
                }}>编辑</Button>
                <Modal visible={this.state.group_info_visible} title="群资料编辑" onOk={this.handleOk}
                       onCancel={this.handleCancel}
                       footer={[<Button key="back" size="large" onClick={this.handleCancel}>取消</Button>,
                           <Button key="submit" type="primary" size="large"
                                   loading={this.state.group_info_submit_loading} onClick={this.handleOk}>
                               提交 </Button>,]}>
                    <div style={{marginTop: 16}}>
                        <div>
                            <img src={this.state.picUrl} alt="" style={{width: 100, height: 100}}
                                 className="avatar"/>
                        </div>
                        <Upload {...props}>
                            <Button>
                                <Icon type="upload"/> 点击上传群LOGO
                            </Button>
                        </Upload>
                    </div>
                    <Form layout={formLayout}>
                        <FormItem
                            label="群名字"
                            {...formItemLayout}>
                            <Input placeholder="请输入群名称" value={this.state.groupName}
                                   onChange={this.handleInputChange}/>
                        </FormItem>
                        <Input id="picUrl" value={this.state.picUrl} type="hidden"/>
                    </Form>
                </Modal>

            </div>
        );
    }
}

export default GroupInfoEditDialog;
