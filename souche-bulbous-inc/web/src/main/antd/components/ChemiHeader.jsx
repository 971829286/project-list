import React from 'react';
import '../styles/index.less';

import {Layout, Menu, Breadcrumb, Pagination, Icon} from 'antd';

const {Header, Content, Footer} = Layout;

// 自定义组件
class ChemiHeader extends React.Component {

    render() {
        // 设置入参？
        const {selectedKey, description, children} = this.props;
        return (
            <Layout className="layout" style={{height:'900px'}}>
                <Header>
                    <div className="logo"/>
                    <Menu
                        theme="dark"
                        mode="horizontal"
                        defaultSelectedKeys={[selectedKey]}
                        style={{lineHeight: '64px'}}
                        onSelect={(item) => {
                            window.location.href = item.key + ".html";
                        }}
                    >
                        <Menu.Item key="CarPage">车辆列表</Menu.Item>
                        <Menu.Item key="QuotePage">报价列表</Menu.Item>
                        <Menu.Item key="UserPage">用户管理</Menu.Item>

                    </Menu>
                </Header>
                <Content style={{padding: '0 20px'}}>
                    <Breadcrumb style={{margin: '16px 0', "icon": "car"}}>
                        <Breadcrumb.Item>{description}</Breadcrumb.Item>
                    </Breadcrumb>
                    <div style={{background: '#fff', padding: 24, minHeight: 280}}>
                        {children}
                    </div>
                </Content>
                <Footer style={{textAlign: 'center'}}>
                    车牛小程序V1.1.0
                </Footer>
            </Layout>
        )
            ;
    }
}

export default ChemiHeader;
