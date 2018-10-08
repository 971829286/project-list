import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';
import moment from 'moment';

import {Form, Input, Button, Table, Pagination, DatePicker, Row, Col, Modal, Carousel, Progres} from 'antd';
import ChemiHeader from '../components/ChemiHeader';

const FormItem = Form.Item;

class CarPage extends SADPage {

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
        console.log("我要下载报价表了。。。");
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
        this.exprot().then(() => {
            request();
        });
    }

    render() {

        if (!this.stateAlready) {
            return null;
        }

        // 定义数据列
        const columns = [
            {title: '发布人昵称', dataIndex: 'nickName', width: 200, key: 'nickName'},
            {title: '发布人手机', dataIndex: 'userPhone', width: 150, key: 'userPhone'},
            {title: '车辆标题', dataIndex: 'modeName', width: 200, key: 'modeName'},
            {
                title: '发布时间',
                dataIndex: 'dateSell',
                key: 'dateSell'
            },
            {
                title: '车辆价格',
                dataIndex: 'carPrice',
                key: 'carPrice',
                render: (text, record) => {
                    return record.carPrice + "万元"
                }
            },

            {title: '售卖状态', dataIndex: 'status', key: 'status'},
            {
                title: '报价数',
                dataIndex: 'quoteCount',
                key: 'quoteCount',
                width: 120,
                render: (text, record) => {
                    return <div>
                        <Row>
                            <Col className="gutter-row" span={12}>
                                <div className="gutter-box"><a
                                    href={'/QuotePage.html?carId=' + record.carId}>{record.quoteCount}</a>
                                </div>
                            </Col>
                        </Row>
                    </div>
                },
            }
        ];

        return (
            <ChemiHeader description="我是车辆列表页" selectedKey={"CarPage"}>

                <div>
                    <Form layout="inline">
                        <FormItem>
                            <Input placeholder="发布人手机" onChange={(e) => (this.state.query.userPhone = e.target.value)}/>
                        </FormItem>
                        <FormItem>
                            <Input placeholder="发布人昵称" onChange={(e) => (this.state.query.nickName = e.target.value)}/>
                        </FormItem>
                        <FormItem>
                            <Input placeholder="车辆标题" onChange={(e) => (this.state.query.modeName = e.target.value)}/>
                        </FormItem>

                        <FormItem>
                            <DatePicker placeholder="开始时间" onChange={(beginDate) => {
                                if (null == beginDate){
                                    this.state.query.beginDate = null
                                } else {
                                    this.state.query.beginDate = beginDate.format("YYYY/MM/DD")
                                }
                            }}/>
                        </FormItem>
                        <FormItem>
                            <DatePicker placeholder="结束时间" onChange={(endDate) => {
                                if (null == endDate){
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
            </ChemiHeader>
        )
    }
}

ReactDom.render(
    <div><CarPage></CarPage></div>,
    document.querySelector("#content")
);
