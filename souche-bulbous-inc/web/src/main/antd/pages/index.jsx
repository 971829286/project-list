import React from 'react';
import ReactDom from 'react-dom';
import SADPage from '@souche-f2e/sad';
import '../styles/index.less';

/*
 * 这里声明要引入的组件
 */
import ChemiHeader from '../components/ChemiHeader';

/**
 * 车首页
 * Index 一般是当前页面名称 index.html
 */
class Index extends SADPage {
    constructor() {
        super();
        console.log("asdfaasdf");
    }

    render() {
        // 定义要显示的列表
        // const columns = [
        //     {title: '闪屏标题', dataIndex: 'name', key: 'name'},
        //     {title: '创建人', dataIndex: 'people', key: 'people'}
        // ];
        //
        // let query = this.state.query;

        if (this.stateAlready) {
            return (
                <ChemiHeader description="我是首页" selectedKey={"Index"}>
                    <div><h2>欢迎使用车牛小程序后台管理系统！</h2></div>
                </ChemiHeader>
            )
        }

        return (
            <ChemiHeader description="我是首页" selectedKey={"Index"}>
                <div><h2>欢迎使用车牛小程序后台管理系统！</h2></div>
            </ChemiHeader>
        );
    }
}

ReactDom.render(<div><Index></Index></div>, document.querySelector("#content"));
