import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';

import moment from 'moment';

import {Form, Input, Button, Table, Pagination, DatePicker, Row, Col, Modal, Carousel, Progress} from 'antd';
import ChemiHeader from '../components/ChemiHeader';

const FormItem = Form.Item;

class UserPage extends SADPage {
    // 初始化数据
    constructor() {
        super();
        this.state.query = {
            page: 1,
            pageSize: 50,
        };
        this.state.exprotStatus = true;
    }

    exprotExcel = () => {
        this.state.exprotStatus = false;
        console.log("我要下载用户表了。。。");
        window.request = () => {
            this.progress().then((data) => {
                if (data.data.job.url) {
                    window.location.href = data.data.job.url;
                    this.setState({
                        exprotStatus: true
                    });
                } else {
                    setTimeout(function () {
                        request();
                    }, 1000);
                }
            });
        }
        this.exportExcel().then(() => {
            request();
        });
    }

    render() {
        if (!this.stateAlready) {
            return null;
        }

        // 定义数据列
        const columns = [
            {title: '用户昵称', dataIndex: 'nickName', width: 200, key: 'nickName'},
            {title: '用户手机', dataIndex: 'phone', width: 150, key: 'phone'},
            {
                title: '注册时间',
                dataIndex: 'dateCreate',
                key: 'dateCreate',
                render: (text, record) => {
                    return moment(record.dateCreate).format("YYYY/MM/DD HH:mm")
                }
            },
            {
                title: '发车车辆',
                dataIndex: 'sellNum',
                key: 'sellNum'
            },
            {
                title: '报价次数',
                dataIndex: 'quoteCount',
                key: 'quoteCount'
            }
        ];

        return (
            <ChemiHeader description="我是用户管理页" selectedKey={"UserPage"}>
                <div>
                    <div>
                        <Form layout="inline">
                            <FormItem>
                                <Input placeholder="用户手机"
                                       onChange={(e) => (this.state.query.userPhone = e.target.value)}/>
                            </FormItem>
                            <FormItem>
                                <Input placeholder="用户昵称"
                                       onChange={(e) => (this.state.query.nickName = e.target.value)}/>
                            </FormItem>
                            <FormItem>
                                <DatePicker placeholder="开始时间" onChange={(beginDate) => {
                                    if (null == beginDate) {
                                        this.state.query.beginDate = null
                                    } else {
                                        this.state.query.beginDate = beginDate.format("YYYY/MM/DD")
                                    }
                                }}/>
                            </FormItem>
                            <FormItem>
                                <DatePicker placeholder="结束时间" onChange={(endDate) => {
                                    if (null == endDate) {
                                        this.state.query.endDate = null
                                    } else {
                                        this.state.query.endDate = endDate.format("YYYY/MM/DD")
                                    }
                                }}/>
                            </FormItem>
                            <FormItem>
                                <Button type="primary" onClick={() => this.getList()}>查找</Button>
                            </FormItem>
                            <FormItem>
                                <Button type="primary" onClick={this.exprotExcel}
                                        loading={!this.state.exprotStatus}>导出Excel表</Button>
                            </FormItem>
                        </Form>

                        <div>
                            <div className="table-content">
                                <Table columns={columns} dataSource={this.state.result.items} pagination={false}/>
                            </div>
                            <div className="table-pagination">
                                <Pagination showQuickJumper defaultCurrent={1} total={this.state.result.totalNumber}
                                            defaultPageSize={this.state.result.pageSize} onChange={(page) => {
                                    (this.state.query.page = page) && this.getList()
                                }}/>
                            </div>
                        </div>
                    </div>
                </div>
            </ChemiHeader>
        )
    }
}

ReactDom.render(
    <div><UserPage></UserPage></div>,
    document.querySelector("#content")
);
