import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';

import moment from 'moment';

import {Form, Input, Button, Table, Pagination, DatePicker, Row, Col, Modal, Carousel, Progres} from 'antd';
import ChemiHeader from '../components/ChemiHeader';

const FormItem = Form.Item;

class QuotePage extends SADPage {
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
        console.log("我要下载报价表了。。。");
        console.log(this.state.query.carId);
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
            {title: '报价人昵称', dataIndex: 'nickName', width: 200, key: 'nickName'},
            {title: '报价人手机', dataIndex: 'userPhone', width: 150, key: 'userPhone'},
            {title: '车辆标题', dataIndex: 'modeName', width: 200, key: 'modeName'},
            {
                title: '报价时间',
                dataIndex: 'quoteTime',
                key: 'quoteTime'
            },
            {
                title: '车辆价格',
                dataIndex: 'salePrice',
                key: 'salePrice',
                render: (text, record) => {
                    return record.salePrice + "万元"
                }
            },
            {
                title: '车辆报价',
                dataIndex: 'quotePrice',
                key: 'quotePrice',
                render: (text, record) => {
                    return record.quotePrice / 10000 + "万元"
                }
            }
        ];

        return (
            <ChemiHeader description="我是报价列表页" selectedKey={"QuotePage"}>
                <div>
                    <Form layout="inline">
                        <FormItem>
                            <Input placeholder="报价人手机" onChange={(e) => (this.state.query.userPhone = e.target.value)}/>
                        </FormItem>
                        <FormItem>
                            <Input placeholder="报价人昵称" onChange={(e) => (this.state.query.nickName = e.target.value)}/>
                        </FormItem>
                        <FormItem>
                            <Input placeholder="车辆标题" onChange={(e) => (this.state.query.modeName = e.target.value)}/>
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
            </ChemiHeader>
        )
    }
}

ReactDom.render(
    <div><QuotePage></QuotePage></div>,
    document.querySelector("#content")
);
