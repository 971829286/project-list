import React from 'react';
import '../styles/index.less';

import {Layout} from 'antd';

const {Content, Footer} = Layout;

/**
 * @author wujingtao
 *
 * @desc CMS 配置页通用组件
 *
 * @date  2018年08月31日14:59:26
 */
class CMSHeader extends React.Component{

    render(){
        //todo 设置入参
        const {children} = this.props;
        return(
            <Layout style={{height:'100%'}}>
                <Content>
                    <div>
                        {children}
                    </div>
                </Content>
               {/* <Footer style={{textAlign: 'center'}}>
                    车牛小程序V1.1.0
                </Footer>*/}
            </Layout>
        );
    }

}
export default CMSHeader;
