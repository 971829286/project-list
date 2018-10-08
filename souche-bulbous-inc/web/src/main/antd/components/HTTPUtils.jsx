import axios from 'axios';
import Cookies from 'js-cookie';
import {Modal} from 'antd';

if (Cookies.get('_security_token_inc')) {
    axios.defaults.headers['Souche-Security-Token-Inc'] = Cookies.get('_security_token_inc');
}

let contextPath = location.pathname
    .replace(/\/[\w\d]+\.html?/, '')
    .substring(1);


export default class HTTPUtils {
    constructor() {
        this.baseUri = window.dev
            ? window.SERVER_URL
            : `${location.protocol}//${location.host}`;
        this.pageName = this._getPageName(location.href);
    }

    /**
     * 获取 [name].html 的 name
     * @returns {string}
     * @private
     */
    _getPageName = function (url = '') {
        let match = url.match(/\/([\w\d]+).html/);
        return match ? match[1] : '';
    }
    checkResponse = function checkResponse(resp) {
        if (resp.status !== 200) {
            console.log(resp.msg);
            // Modal.error({title: 'Error', content: resp.msg});
            return false;
        }

        if (!resp.data.success) {
            console.log(resp);
            // Modal.error({title: 'Error', content: resp.data.msg});
            return false;
        }
        if (!resp.data.data.result) {
            // Modal.error({title: '失败', content: resp.data.data.resultMsg});
            return false;
        }
        return true;
    }
    /**
     * 请求返回错误信息
     */
    post = function (url, params, onSuccess, onFail, onComplate) {
        let uri = contextPath
            ? `${this.baseUri}/${contextPath}/${this.pageName}/` + url
            : `${this.baseUri}/${this.pageName}/` + url;
        var pp = {"state": JSON.stringify(params)};
        console.log(pp);
        axios.post(uri, pp).then(resp => {
            if (!this.checkResponse(resp)) {
                if (onFail) {
                    onFail(resp);
                } else {
                    // Modal.error({title: 'Error', content: resp});
                }
            } else {
                if (onSuccess) {
                    onSuccess(resp);
                } else {
                    // Modal.success({title:"SUCCESS",content:"请求成功"})
                }
            }
            if (onComplate) {
                onComplate();
            }
        }).catch(err => {
            if (onComplate) {
                onComplate();
            }
            Modal.error({title: 'Error', content: "发生异常"});
        })
    }

}


