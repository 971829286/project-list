webpackJsonp([1],{57:function(module,exports,__webpack_require__){"use strict";eval("\n\nObject.defineProperty(exports, \"__esModule\", {\n    value: true\n});\n\nvar _jsx2 = __webpack_require__(12);\n\nvar _jsx3 = _interopRequireDefault(_jsx2);\n\nvar _react = __webpack_require__(0);\n\nvar _react2 = _interopRequireDefault(_react);\n\n__webpack_require__(56);\n\nvar _antd = __webpack_require__(11);\n\nfunction _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }\n\nvar Header = _antd.Layout.Header,\n    Content = _antd.Layout.Content,\n    Footer = _antd.Layout.Footer;\n\n// 自定义组件\n\nvar _ref = (0, _jsx3.default)('div', {\n    className: 'logo'\n});\n\nvar _ref2 = (0, _jsx3.default)(_antd.Menu.Item, {}, 'CarPage', '\\u8F66\\u8F86\\u5217\\u8868');\n\nvar _ref3 = (0, _jsx3.default)(_antd.Menu.Item, {}, 'QuotePage', '\\u62A5\\u4EF7\\u5217\\u8868');\n\nvar _ref4 = (0, _jsx3.default)(_antd.Menu.Item, {}, 'UserPage', '\\u7528\\u6237\\u7BA1\\u7406');\n\nvar ChemiHeader = function () {\n    function ChemiHeader(props) {\n        // 设置入参？\n        var selectedKey = props.selectedKey,\n            description = props.description,\n            children = props.children;\n\n        return (0, _jsx3.default)(_antd.Layout, {\n            className: 'layout',\n            style: { height: '900px' }\n        }, void 0, (0, _jsx3.default)(Header, {}, void 0, _ref, (0, _jsx3.default)(_antd.Menu, {\n            theme: 'dark',\n            mode: 'horizontal',\n            defaultSelectedKeys: [selectedKey],\n            style: { lineHeight: '64px' },\n            onSelect: function onSelect(item) {\n                window.location.href = item.key + \".html\";\n            }\n        }, void 0, _ref2, _ref3, _ref4)), (0, _jsx3.default)(Content, {\n            style: { padding: '0 20px' }\n        }, void 0, (0, _jsx3.default)(_antd.Breadcrumb, {\n            style: { margin: '16px 0', \"icon\": \"car\" }\n        }, void 0, (0, _jsx3.default)(_antd.Breadcrumb.Item, {}, void 0, description)), (0, _jsx3.default)('div', {\n            style: { background: '#fff', padding: 24, minHeight: 280 }\n        }, void 0, children)), (0, _jsx3.default)(Footer, {\n            style: { textAlign: 'center' }\n        }, void 0, '\\u8F66\\u725B\\u5C0F\\u7A0B\\u5E8FV1.1.0'));\n    }\n\n    return ChemiHeader;\n}();\n\nexports.default = ChemiHeader;\nmodule.exports = exports['default'];\n\n//////////////////\n// WEBPACK FOOTER\n// ./components/ChemiHeader.jsx\n// module id = 57\n// module chunks = 1 2 3 4\n\n//# sourceURL=webpack:///./components/ChemiHeader.jsx?")},831:function(module,exports,__webpack_require__){"use strict";eval("\n\nvar _jsx2 = __webpack_require__(12);\n\nvar _jsx3 = _interopRequireDefault(_jsx2);\n\nvar _classCallCheck2 = __webpack_require__(1);\n\nvar _classCallCheck3 = _interopRequireDefault(_classCallCheck2);\n\nvar _possibleConstructorReturn2 = __webpack_require__(3);\n\nvar _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);\n\nvar _inherits2 = __webpack_require__(2);\n\nvar _inherits3 = _interopRequireDefault(_inherits2);\n\nvar _react = __webpack_require__(0);\n\nvar _react2 = _interopRequireDefault(_react);\n\nvar _reactDom = __webpack_require__(9);\n\nvar _reactDom2 = _interopRequireDefault(_reactDom);\n\nvar _sad = __webpack_require__(28);\n\nvar _sad2 = _interopRequireDefault(_sad);\n\n__webpack_require__(56);\n\nvar _ChemiHeader = __webpack_require__(57);\n\nvar _ChemiHeader2 = _interopRequireDefault(_ChemiHeader);\n\nfunction _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }\n\n/**\n * 车首页\n * Index 一般是当前页面名称 index.html\n */\nvar _ref = (0, _jsx3.default)(_ChemiHeader2.default, {\n    description: '\\u6211\\u662F\\u9996\\u9875',\n    selectedKey: \"Index\"\n}, void 0, (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)('h2', {}, void 0, '\\u6B22\\u8FCE\\u4F7F\\u7528\\u8F66\\u725B\\u5C0F\\u7A0B\\u5E8F\\u540E\\u53F0\\u7BA1\\u7406\\u7CFB\\u7EDF\\uFF01')));\n\n/*\n * 这里声明要引入的组件\n */\n\n\nvar _ref2 = (0, _jsx3.default)(_ChemiHeader2.default, {\n    description: '\\u6211\\u662F\\u9996\\u9875',\n    selectedKey: \"Index\"\n}, void 0, (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)('h2', {}, void 0, '\\u6B22\\u8FCE\\u4F7F\\u7528\\u8F66\\u725B\\u5C0F\\u7A0B\\u5E8F\\u540E\\u53F0\\u7BA1\\u7406\\u7CFB\\u7EDF\\uFF01')));\n\nvar Index = function (_SADPage) {\n    (0, _inherits3.default)(Index, _SADPage);\n\n    function Index() {\n        (0, _classCallCheck3.default)(this, Index);\n\n        var _this = (0, _possibleConstructorReturn3.default)(this, _SADPage.call(this));\n\n        console.log(\"asdfaasdf\");\n        return _this;\n    }\n\n    Index.prototype.render = function render() {\n        // 定义要显示的列表\n        // const columns = [\n        //     {title: '闪屏标题', dataIndex: 'name', key: 'name'},\n        //     {title: '创建人', dataIndex: 'people', key: 'people'}\n        // ];\n        //\n        // let query = this.state.query;\n\n        if (this.stateAlready) {\n            return _ref;\n        }\n\n        return _ref2;\n    };\n\n    return Index;\n}(_sad2.default);\n\n_reactDom2.default.render((0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)(Index, {})), document.querySelector(\"#content\"));\n\n//////////////////\n// WEBPACK FOOTER\n// ./pages/index.jsx\n// module id = 831\n// module chunks = 1\n\n//# sourceURL=webpack:///./pages/index.jsx?")}},[831]);