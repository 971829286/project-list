import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';
import { RJAreaCascader } from '@souche-f2e/sad/components/RJAntd';

import CMSHeader from '../..//components/CMSHeader';

import {Tag, Tooltip, Form, Table, Radio, Button, Popconfirm, Input, Modal, Icon, Upload, Divider} from 'antd';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;
const FormItem = Form.Item;


const Page = Form.create()(
class BannerPage extends SADPage {

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
            query:{
                page: 1,
                pageSize: 10,
                openId: '',
                status: 3,  //查找标志  默认为查全部
            },
            previewImage:'',
            previewVisible:false,
            operateType:'',  //区别新增or编辑
            visible: false,
            confirmLoading:false,//确认加载按钮
            bannerId:'',
            refresh:'',
            validate:false,
            itemData: {
                title: '',
                address:'',
                image: '',
                orderNum:'',
                targetCity:[],
            },
            fileList: [],
            listImgVisible:false,   //列表查看大图
            listImgUrl:''
        }
    }

    //删除
    onDelete = (id) => {
        this.state.bannerId  = id;
        this.deleteById().then(() => {
            if(this.state.isDelete == true || this.state.isDelete == 'true'){
                alert("删除成功");
                this.setState({visible:false})
                // this.forceUpdate();
                location.reload(true);
            }else{
                alert("删除失败")
            }
        });
    };

    // 编辑
    onEdit = (id) => {
        this.edit();
        this.setState(() => (
            {
                operateType:'编辑',
                visible:true,
                query: {
                    openId: id
                }
            }
        ))
    };


    addButton = () => {
        this.setState(()=>({
            operateType:'新增',
            visible:true
        }));
    }

    handleOk = () => {
        let regex = new RegExp("([http|https]+://[^\\s]*)|(^/|^./)",'i');
        let orderNumRegex = new RegExp("^(?:[0-9]{1,3}|1000)$","i");
        //获取所有form表单的值
        let value = this.props.form.getFieldsValue();
        if(typeof value.title == 'undefined'|| value.title == null || value.title == ''){
            this.error("错误", "请输入banner标题");
            return;
        }else if (typeof value.address == 'undefined' || !regex.test(value.address)){
            this.error("错误", "请输入正确的跳转协议地址");
            return;
        }else if(typeof value.image == 'undefined'){
            this.error("错误", "请上传图片");
            return;
        }else if(typeof  value.orderNum == 'undefined' || !orderNumRegex.test(value.orderNum)){
            this.error("错误", "请输入正确的排序值");
            return;
        } else{
            this.state.confirmLoading = true;
            this.setState({confirmLoading:true});
            this.setState({
                ModalText: 'The modal will be closed after two seconds',
                confirmLoading: true,
            });
            this.state.itemData.address = value.address;
            this.state.itemData.title = value.title;
            //this.state.fileList[0].response.data ||
            let str = this.state.fileList[0].url;
            this.state.itemData.image = str;
            this.state.itemData.orderNum = value.orderNum;
            // this.state.itemData.targetCity = value.targetCity;
            //如果是新增
            if(this.state.operateType == '新增') {
                this.saveInfo().then(() => {
                    // this.state.confirmLoading = false;
                    // this.state.visible = false;
                    this.setState({confirmLoading: false});
                    this.setState({visible: false});
                    if (this.state.isSave == true || this.state.isSave == 'true') {
                        alert("保存成功");
                        location.reload(true);
                    } else {
                        alert("保存失败")
                        location.reload(true);
                    }
                });
                //如果是编辑
            }else{
                this.updateBanner().then(() => {
                    this.setState({confirmLoading: false});
                    this.setState({visible: false});
                    if(this.state.isUpdate == true || this.state.isUpdate == 'true'){
                        alert("更新成功!")
                        location.reload(true);
                    }else{
                        alert("更新失败");
                    }
                });
            }
        }
        this.setState({refresh:Math.random()})
    }

    handleCancel = () => {
        this.setState(() => ({
            visible: false
        }));
    }

    handleChange = (info) => {
        let fileList = info.fileList;
        if (fileList.length === 0) {
            this.setState({url: ''})
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
                return file.response.success === true;
            }
            return true;
        });
        this.setState({fileList});
    }

    //照片大图展示事件
    handlePreview = (file) => {
        this.setState({
            previewImage: file.url || file.thumbUrl,
            previewVisible: true,
        });
    }

    //照片大图关闭事件
    handleCancelPreview = () => {
        this.setState({previewVisible: false})
    }


    //翻页
    onChangePage = (page, pageSize) => {
        // this.state = {
        //     ...this.state,
        //     query:{
        //         status:e,
        //         page:page
        //     }
        // }
        this.state.query.page = page;
        this.state.query.pageSize = pageSize;
        this.getList();
    }
    onChangeRadio = (e) => {
        this.state.query.status = e.target.value;
        this.setState({
            query:{
                status:e.target.value,
            }
        })
        this.getList();
    }
    //状态显示
    displayStatus = (status) => {
        if(status == 0){
            return '未上架';
        }else if (status == 1){
            return '已上架';
        }else if (status == 2) {
            return '已下架';
        }
    }

    //操作显示
    displayOperators = (status) => {
        if(status == 0){
            return '上架';
        }else if (status == 1){
            return '下架';
        }else if (status == 2) {
            return '重新上架';
        }
    }
    //操作码转换
    getOperator = (status)=> {
        if(status == 0 || status == 2){
            //未上架->上架
            //下架->上架
            return 1;
        }else if (status == 1){
            //上架->下架
            return 2;
        }
    }

    //状态改变事件
    onChangeStatus = (id, status) =>{
        this.state.bannerId = id;
        this.getBannerById().then(()=> {
            //设置初始值
            this.props.form.setFieldsValue(this.state.itemData)
            this.state.itemData.status = status;
            let image = [{
                uid : 1,
                name : '',
                status: 'done',
                url : this.state.itemData.image
            }];
            this.setState({fileList:image});
            this.updateBanner().then( () => {
                if(this.state.isUpdate == true || this.state.isUpdate == 'true'){
                    alert("更新状态成功!")
                    location.reload(true);
                }else{
                    alert("更新状态失败");
                }
            });
        });
    }
    showDialog = (bannerId) => {
        if(bannerId != null){
            this.setState(()=>({
                operateType:'编辑',
                visible:true,
                bannerId:bannerId
            }));
            this.state.visible = true;
            this.state.bannerId = bannerId;
            this.state.operateType = '编辑';
            this.getBannerById().then(()=> {
                //设置初始值
                console.log(this.state.itemData.targetCity);
                this.props.form.setFieldsValue(this.state.itemData)
                let image = [{
                    uid : 1,
                    name : '',
                    status: 'done',
                    url : this.state.itemData.image
                }];
                this.setState({fileList:image});
            });
        }else{
            this.props.form.resetFields();
            // this.props.form.setFieldsValue({targetCity:[]});
            this.state.itemData.targetCity = [];
            this.state.fileList = [];
            this.setState(()=>({
                operateType:'新增',
                visible:true
            }));
        }
    }
    error = (t, c) => {
        Modal.error({
            title: t,
            content: c,
        });
    }

    //todo 关闭查看大图
    handleBigPicCancel=()=>{
        this.setState({
            listImgVisible:false
        })
    }

    closeTag = (e) => {
        console.log(e);
    }

    pendingTags = (value) => {

        if(value.key != null && value.label != null) {
            //获取原来的值
            // let values = this.props.form.getFieldsValue();
            let inputValue = this.state.itemData.targetCity;
            let item =  value.label + '(' + value.key + ')';
            if(this.unique(inputValue, item)){
                inputValue.push(item);
            }
            this.state.itemData.targetCity = inputValue;
            this.props.form.setFieldsValue({targetCity:inputValue});
            this.setState({refresh:Math.random()})
        }
    }
    unique = (array, element) => {
        for(let i = 0; i < array.length; i ++){
            if(array[i] === element){
                return false;
            }
        }
        return true;
    }
    handleClose = (removedTag) => {
        const tags = this.state.itemData.targetCity.filter(tag => tag !== removedTag);
        this.state.itemData.targetCity = tags;
        this.props.form.setFieldsValue({targetCity:tags});
    }
    render() {
        //const regEx = /(^((https|http)?:\/\/)[^\s]+) || (^(\/|(.\/)|(a-zA-Z)))/gi;
        if (!this.stateAlready) {
            return null;
        }
        const {form} = this.props;
        const { getFieldDecorator} = form;
        const uploadButton = (
            <div>
                <Icon type="plus"/>
                <div style={{marginTop: '8px', color: '#666'}}>上传图片</div>
            </div>
        );
        const uploadFile = {
            //TODO  URL
            action: '/uploadAPI/uploadPic.json',
            onChange: this.handleChange.bind(this),
            multiple: true,
            accept: 'image/jpeg,image/jpeg,image/x-png,image/gif',
            listType: "picture-card",
            onPreview: this.handlePreview.bind(this),
        };

        //列表标题
        const columns = [
            {title: '序号', dataIndex: 'bannerId', key: 'bannerId',},
            {title: '标题', dataIndex: 'title',  key: 'title'},
            {
                title: '图片', dataIndex: 'pic',  key: 'pic',
                render: (text, record) => {
                    return (
                        <img src={record.image} onClick={()=>{this.setState({
                            listImgVisible:true,
                            listImgUrl:record.image
                        })}} height={'100px'}></img>
                    );
                }
            },
            {title: '创建时间', dataIndex: 'dateCreate', key: 'dateCreate'},
            {title: '排序值', dataIndex: 'orderNum',  key: 'orderNum'},
            {
                title: '状态',
                dataIndex: 'status',
                key: 'status',
                render:(text,record) => {
                    //1:已上架, 0:未上架, 2:已下架
                    // record.status == 0 ? "未上架" : record.status == 1 ? "已上架" : "已下架";
                    return this.displayStatus(record.status);
                    return '已生效';
                }
            },
            {
                title: '操作',
                dataIndex: 'operator',
                render: (text, record) => {
                    return (
                        <span>
                            <Popconfirm title={this.displayOperators(record.status)}
                                        okText="确定" cancelText="取消"
                                        onConfirm={() => this.onChangeStatus(record.bannerId, this.getOperator(record.status))}>
                            <a href={'#'}>{record.status == 0 ? "上架" : record.status == 1 ? "下架" : "重新上架"}</a>
                            </Popconfirm>
                            <Divider type="vertical"/>
                            <Popconfirm title="确定删除?" okText="确定" cancelText="取消"
                                        onConfirm={() => this.onDelete(record.bannerId)}>
                            <a href={'#'}>删除</a>
                            </Popconfirm>
                            <Divider type="vertical"/>
                            <a href={'#'} onClick={() => this.showDialog(record.bannerId)}>
                                编辑
                            </a>
                        </span>
                    );
                }
            },
        ];

        return (

            <CMSHeader>
                <div>
                    <Form layout="inline">
                        <FormItem>
                            {/**全部:3    已上架:1   未上架:0   已下架:2**/}
                            <RadioGroup defaultValue={3} onChange={(e) => {this.onChangeRadio(e)}}>
                                <RadioButton value={3}>全部</RadioButton>
                                <RadioButton value={1}>已上架</RadioButton>
                                <RadioButton value={0}>未上架</RadioButton>
                                <RadioButton value={2}>已下架</RadioButton>
                            </RadioGroup>
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    onClick={() => this.showDialog(null)}>新增</Button>
                        </FormItem>
                    </Form>
                    <div>
                        <div className="table-content">
                            <Table columns={columns} dataSource={this.state.result.items}

                                   pagination={{
                                       'pageSize': this.state.result.pageSize,
                                       'showQuickJumper':true,
                                       'defaultCurrent': 1,
                                       'total': this.state.result.totalNumber,
                                       'onChange' : (page, pageSize) => this.onChangePage(page, pageSize)
                                   }}
                                   // pagination={false}
                            />
                            {/*<div className="table-pagination">*/}
                                {/*<Pagination showQuickJumper defaultCurrent={this.state.page}*/}
                                            {/*total={this.state.result.totalNumber}*/}
                                            {/*defaultPageSize={this.state.result.pageSize}*/}
                                            {/*onChange={(page, pageSize) => {*/}
                                                {/*this.onChangePage(page, pageSize)*/}
                                            {/*}}/>*/}
                            {/*</div>*/}
                        </div>

                    </div>
                </div>
                {/*弹框*/}
                <div>
                    <Modal
                        title={this.state.operateType+'|首页banner'}
                        okText="保存"
                        cancelText="取消"
                        onOk={this.handleOk}
                        onCancel={this.handleCancel}
                        visible={this.state.visible}
                        confirmLoading={this.state.confirmLoading}
                    >

                        <Form layout="vertical">
                            <FormItem label="BANNER标题" >
                                {getFieldDecorator('title', {
                                    rules: [{ required: true, whiteSpace: true, message: '请输入标题!' }],
                                })(
                                    <Input type='text' />
                                )}
                            </FormItem>
                            <FormItem label="跳转协议地址" extra="*支持http协议和本地跳转协议">
                                {getFieldDecorator('address', {
                                    rules: [{ required: true, whiteSpace: true,pattern:"([http|https]+://[^\\s]*)|(^/|^./)", message: '请输入正确的跳转协议地址!' }],
                                })(
                                    <Input  type='text' />
                                )}
                            </FormItem>
                            <FormItem label="图片" >

                                {getFieldDecorator('image', {
                                    rules: [{ required: true,  message: '请选择要上传的图片!' }],
                                })(
                                    <div>
                                        <Upload {...uploadFile} fileList={this.state.fileList}>
                                            {this.state.fileList.length >= 1 ? null : uploadButton}
                                        </Upload>
                                    </div>
                                )}
                            </FormItem>
                            <FormItem label="排序值" extra="*输入1-1000的数,数字越大,排序越靠前">
                                {getFieldDecorator('orderNum', {
                                    rules: [{ required: true, whiteSpace: true, pattern: "^(?:[0-9]{1,3}|1000)$", message: '请输入正确的排序值' }],
                                })(
                                    <Input type='number'  />
                                )}
                            </FormItem>
                            <FormItem label="目标城市" extra="*可多选,不填写默认全国显示">
                                {getFieldDecorator('targetCity', {
                                    rules: [{ required:false }],
                                })(
                                    <Input type={'hidden'}/>
                                )}
                            </FormItem>
                        </Form>
                        <RJAreaCascader
                            // onCityChange={(v) => {console.log(v)}}
                            key={1}
                            onCityChange={(value) => {this.pendingTags(value)}}
                            cascaderLevel={2}
                            allowClear={false}
                            showSearch = {true}
                            width={300}
                            defaultCode={[]}>
                        </RJAreaCascader>
                        <br></br>
                        {
                            this.state.itemData.targetCity == null ? null :
                            this.state.itemData.targetCity.map((tag) => {
                                const isLongTag = tag.length > 20;
                                const tagElem = (
                                    <Tag key={tag}
                                         closable={true}
                                         afterClose={() => this.handleClose(tag)}
                                    >
                                        {isLongTag ? `${tag.slice(0, 20)}...` : tag}
                                    </Tag>
                                );
                                return isLongTag ? <Tooltip title={tag} key={tag}>{tagElem}</Tooltip> : tagElem;
                            })
                         }
                    </Modal>
                    <div>
                        <Modal visible={this.state.previewVisible} footer={null} onCancel={this.handleCancelPreview}>
                            <img alt="example" style={{width: '100%'}} src={this.state.previewImage}/>
                        </Modal>
                    </div>
                    <div>
                        <Modal style={{width:'100%', height:'100%'}}
                               visible={this.state.previewVisible}
                               footer={null}
                               onCancel={this.handleCancelPreview}>
                            <img alt="example" style={{width: '100%'}} src={this.state.previewImage}/>
                        </Modal>
                    </div>
                </div>
                {/*查看大图*/}
                <div>
                    <Modal visible={this.state.listImgVisible} footer={null} onCancel={this.handleBigPicCancel}>
                        <img alt="example" style={{width: '100%',height:'100%'}} src={this.state.listImgUrl}/>
                    </Modal>
                </div>
            </CMSHeader>
        )
    }
}
)
ReactDom.render(
    <Page></Page>,
    document.querySelector("#content")
);
