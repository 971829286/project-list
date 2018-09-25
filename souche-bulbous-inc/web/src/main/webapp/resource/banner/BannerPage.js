webpackJsonp([4],{39:function(module,exports,__webpack_require__){"use strict";eval("\n\nObject.defineProperty(exports, \"__esModule\", {\n    value: true\n});\n\nvar _jsx2 = __webpack_require__(19);\n\nvar _jsx3 = _interopRequireDefault(_jsx2);\n\nvar _react = __webpack_require__(0);\n\nvar _react2 = _interopRequireDefault(_react);\n\n__webpack_require__(53);\n\nvar _antd = __webpack_require__(20);\n\nfunction _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }\n\nvar Content = _antd.Layout.Content,\n    Footer = _antd.Layout.Footer;\n\n/**\n * @author wujingtao\n *\n * @desc CMS 配置页通用组件\n *\n * @date  2018年08月31日14:59:26\n */\n\nvar CMSHeader = function () {\n    function CMSHeader(props) {\n        //todo 设置入参\n        var children = props.children;\n\n        return (0, _jsx3.default)(_antd.Layout, {\n            style: { height: '100%' }\n        }, void 0, (0, _jsx3.default)(Content, {}, void 0, (0, _jsx3.default)('div', {}, void 0, children)));\n    }\n\n    return CMSHeader;\n}();\n\nexports.default = CMSHeader;\nmodule.exports = exports['default'];\n\n//////////////////\n// WEBPACK FOOTER\n// ./components/CMSHeader.jsx\n// module id = 39\n// module chunks = 4 5 6 7 8\n\n//# sourceURL=webpack:///./components/CMSHeader.jsx?")},776:function(module,exports,__webpack_require__){"use strict";eval("\n\nvar _extends2 = __webpack_require__(4);\n\nvar _extends3 = _interopRequireDefault(_extends2);\n\nvar _jsx2 = __webpack_require__(19);\n\nvar _jsx3 = _interopRequireDefault(_jsx2);\n\nvar _classCallCheck2 = __webpack_require__(1);\n\nvar _classCallCheck3 = _interopRequireDefault(_classCallCheck2);\n\nvar _possibleConstructorReturn2 = __webpack_require__(3);\n\nvar _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);\n\nvar _inherits2 = __webpack_require__(2);\n\nvar _inherits3 = _interopRequireDefault(_inherits2);\n\nvar _react = __webpack_require__(0);\n\nvar _react2 = _interopRequireDefault(_react);\n\nvar _reactDom = __webpack_require__(9);\n\nvar _reactDom2 = _interopRequireDefault(_reactDom);\n\nvar _sad = __webpack_require__(26);\n\nvar _sad2 = _interopRequireDefault(_sad);\n\nvar _CMSHeader = __webpack_require__(39);\n\nvar _CMSHeader2 = _interopRequireDefault(_CMSHeader);\n\nvar _antd = __webpack_require__(20);\n\nfunction _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }\n\nvar Option = _antd.Select.Option;\nvar RadioButton = _antd.Radio.Button;\nvar RadioGroup = _antd.Radio.Group;\nvar FormItem = _antd.Form.Item;\n\nvar _ref = (0, _jsx3.default)(_antd.Icon, {\n    type: 'plus'\n});\n\nvar _ref2 = (0, _jsx3.default)(_antd.Divider, {\n    type: 'vertical'\n});\n\nvar _ref3 = (0, _jsx3.default)('a', {\n    href: '#'\n}, void 0, '\\u5220\\u9664');\n\nvar _ref4 = (0, _jsx3.default)(_antd.Divider, {\n    type: 'vertical'\n});\n\nvar _ref5 = (0, _jsx3.default)(RadioButton, {\n    value: 3\n}, void 0, '\\u5168\\u90E8');\n\nvar _ref6 = (0, _jsx3.default)(_antd.Input, {\n    type: 'text'\n});\n\nvar _ref7 = (0, _jsx3.default)(_antd.Input, {\n    type: 'text'\n});\n\nvar Page = _antd.Form.create()(function (_SADPage) {\n    (0, _inherits3.default)(BannerPage, _SADPage);\n\n    /**\n     * @author wujingtao\n     *\n     * @desc 开屏配置展示页\n     *\n     * @date 2018年09月03日11:34:30\n     */\n\n    function BannerPage() {\n        (0, _classCallCheck3.default)(this, BannerPage);\n\n        //todo 设置初始弹窗的显示状态 以及初始数据\n        var _this = (0, _possibleConstructorReturn3.default)(this, _SADPage.call(this));\n\n        _this.onDelete = function (id) {\n            _this.state.bannerId = id;\n            _this.deleteById().then(function () {\n                if (_this.state.isDelete == true || _this.state.isDelete == 'true') {\n                    alert(\"删除成功\");\n                    _this.setState({ visible: false });\n                    // this.forceUpdate();\n                    location.reload(true);\n                } else {\n                    alert(\"删除失败\");\n                }\n            });\n        };\n\n        _this.handleOk = function () {\n            var regex = new RegExp(\"([http|https]+://[^\\\\s]*)|(^/|^./)\", 'i');\n\n            //获取所有form表单的值\n            var value = _this.props.form.getFieldsValue();\n            console.log(regex.test(value.address));\n            if (typeof value.title == 'undefined' || value.title == null || value.title == '') {\n                _this.error(\"错误\", \"请输入banner标题\");\n                return;\n            } else if (typeof value.address == 'undefined' || !regex.test(value.address)) {\n                _this.error(\"错误\", \"请输入正确的跳转协议地址\");\n                return;\n            } else if (typeof value.image == 'undefined') {\n                _this.error(\"错误\", \"请上传图片\");\n                return;\n            } else {\n                _this.state.confirmLoading = true;\n                _this.setState({ confirmLoading: true });\n                _this.setState({\n                    ModalText: 'The modal will be closed after two seconds',\n                    confirmLoading: true\n                });\n                _this.state.itemData.address = value.address;\n                _this.state.itemData.title = value.title;\n                var str = _this.state.fileList[0].response.data || _this.state.fileList[0].url;\n                var splits = str.split('/');\n                var url = \"/\" + splits[3] + \"/\" + splits[4] + \"/\" + splits[5];\n                _this.state.itemData.image = url;\n                //如果是新增\n                if (_this.state.operateType == '新增') {\n                    _this.saveInfo().then(function () {\n                        // this.state.confirmLoading = false;\n                        // this.state.visible = false;\n                        _this.setState({ confirmLoading: false });\n                        _this.setState({ visible: false });\n                        if (_this.state.isSave == true || _this.state.isSave == 'true') {\n                            alert(\"保存成功\");\n                            location.reload(true);\n                        } else {\n                            alert(\"保存失败\");\n                            location.reload(true);\n                        }\n                    });\n                    //如果是编辑\n                } else {\n                    _this.updateBanner().then(function () {\n                        _this.setState({ confirmLoading: false });\n                        _this.setState({ visible: false });\n                        if (_this.state.isUpdate == true || _this.state.isUpdate == 'true') {\n                            alert(\"更新成功!\");\n                            location.reload(true);\n                        } else {\n                            alert(\"更新失败\");\n                        }\n                    });\n                }\n            }\n            _this.setState({ refresh: Math.random() });\n        };\n\n        _this.handleCancel = function () {\n            _this.setState(function () {\n                return {\n                    visible: false\n                };\n            });\n        };\n\n        _this.handleChange = function (info) {\n            var fileList = info.fileList;\n            if (fileList.length === 0) {\n                _this.setState({ url: '' });\n            }\n            // 1. Limit the number of uploaded files\n            // Only to show two recent uploaded files, and old ones will be replaced by the new\n            fileList = fileList.slice(-1);\n\n            // 2. Read from response and show file link\n            fileList = fileList.map(function (file) {\n                if (file.response) {\n                    // Component will show file.url as link\n                    file.url = file.response.data;\n                    _this.state.bannerUrl = file.response.data;\n                    _this.state.banner_msg = '';\n                }\n                return file;\n            });\n\n            // 3. Filter successfully uploaded files according to response from server\n            fileList = fileList.filter(function (file) {\n                if (file.response) {\n                    return file.response.success === true;\n                }\n                return true;\n            });\n            _this.setState({ fileList: fileList });\n        };\n\n        _this.handlePreview = function (file) {\n            _this.setState({\n                previewImage: file.url || file.thumbUrl,\n                previewVisible: true\n            });\n        };\n\n        _this.handleCancelPreview = function () {\n            _this.setState({ previewVisible: false });\n        };\n\n        _this.onChangePage = function (page, pageSize) {\n            // this.state = {\n            //     ...this.state,\n            //     query:{\n            //         status:e,\n            //         page:page\n            //     }\n            // }\n            _this.state.query.page = page;\n            _this.state.query.pageSize = pageSize;\n            _this.getList();\n        };\n\n        _this.onChangeRadio = function (e) {\n            _this.state.query.status = e.target.value;\n            //this.getList();\n        };\n\n        _this.displayStatus = function (status) {\n            if (status == 0) {\n                return '未上架';\n            } else if (status == 1) {\n                return '已上架';\n            } else if (status == 2) {\n                return '已下架';\n            }\n        };\n\n        _this.displayOperators = function (status) {\n            if (status == 0) {\n                return '上架';\n            } else if (status == 1) {\n                return '下架';\n            } else if (status == 2) {\n                return '重新上架';\n            }\n        };\n\n        _this.getOperator = function (status) {\n            if (status == 0 || status == 2) {\n                //未上架->上架\n                //下架->上架\n                return 1;\n            } else if (status == 1) {\n                //上架->下架\n                return 2;\n            }\n        };\n\n        _this.onChangeStatus = function (id, status) {\n            _this.state.bannerId = id;\n            _this.state.status = status;\n            _this.changeStatus();\n        };\n\n        _this.showDialog = function (bannerId) {\n            if (bannerId != null) {\n                _this.setState(function () {\n                    return {\n                        operateType: '编辑',\n                        visible: true,\n                        bannerId: bannerId\n                    };\n                });\n                _this.state.visible = true;\n                _this.state.bannerId = bannerId;\n                _this.state.operateType = '编辑';\n                _this.getBannerById().then(function () {\n                    //设置初始值\n                    _this.props.form.setFieldsValue(_this.state.itemData);\n                    var image = [{\n                        uid: 1,\n                        name: '',\n                        status: 'done',\n                        url: _this.state.itemData.image\n                    }];\n                    _this.setState({ fileList: image });\n                });\n            } else {\n                _this.props.form.resetFields();\n                _this.state.fileList = [];\n                _this.setState(function () {\n                    return {\n                        operateType: '新增',\n                        visible: true\n                    };\n                });\n            }\n        };\n\n        _this.error = function (t, c) {\n            _antd.Modal.error({\n                title: t,\n                content: c\n            });\n        };\n\n        _this.handleBigPicCancel = function () {\n            _this.setState({\n                listImgVisible: false\n            });\n        };\n\n        _this.state = {\n            query: {\n                page: 1,\n                pageSize: 10,\n                openId: '',\n                status: 3 //查找标志  默认为查全部\n            },\n            previewImage: '',\n            previewVisible: false,\n            operateType: '', //区别新增or编辑\n            visible: false,\n            confirmLoading: false, //确认加载按钮\n            bannerId: '',\n            refresh: '',\n            validate: false,\n            itemData: {\n                title: '',\n                address: '',\n                image: ''\n            },\n            fileList: [],\n\n            listImgVisible: false, //列表查看大图\n            listImgUrl: ''\n        };\n        return _this;\n    }\n\n    //删除\n\n\n    //编辑\n    // onEdit = (id) => {\n    //\n    //     this.edit();\n    //     this.setState(() => (\n    //         {\n    //             operateType:'编辑',\n    //             visible:true,\n    //             query: {\n    //                 openId: id\n    //             }\n    //         }\n    //     ))\n    // };\n    //\n    //\n    // addButton = () => {\n    //     this.setState(()=>({\n    //         operateType:'新增',\n    //         visible:true\n    //     }));\n    // }\n\n    //照片大图展示事件\n\n\n    //照片大图关闭事件\n\n\n    //翻页\n\n    //状态显示\n\n\n    //操作显示\n\n    //操作码转换\n\n\n    //状态改变事件\n\n\n    //todo 关闭查看大图\n\n\n    BannerPage.prototype.render = function render() {\n        var _this2 = this;\n\n        //const regEx = /(^((https|http)?:\\/\\/)[^\\s]+) || (^(\\/|(.\\/)|(a-zA-Z)))/gi;\n        if (!this.stateAlready) {\n            return null;\n        }\n\n        var form = this.props.form;\n        var getFieldDecorator = form.getFieldDecorator;\n\n        var children = [];\n\n        var uploadButton = (0, _jsx3.default)('div', {}, void 0, _ref, (0, _jsx3.default)('div', {\n            style: { marginTop: '8px', color: '#666' }\n        }, void 0, '\\u4E0A\\u4F20\\u56FE\\u7247'));\n        var uploadFile = {\n            //TODO  URL\n            action: '/uploadAPI/uploadPic.json',\n            onChange: this.handleChange.bind(this),\n            multiple: true,\n            accept: 'image/jpeg,image/jpeg,image/x-png,image/gif',\n            listType: \"picture-card\",\n            onPreview: this.handlePreview.bind(this)\n        };\n\n        for (var i = 10; i < 36; i++) {\n            children.push((0, _jsx3.default)(Option, {}, i.toString(36) + i, i.toString(36) + i));\n        }\n\n        //列表标题\n        var columns = [{ title: '序号', dataIndex: 'bannerId', key: 'bannerId' }, { title: '标题', dataIndex: 'title', key: 'title' }, {\n            title: '图片', dataIndex: 'pic', key: 'pic',\n            render: function render(text, record) {\n                return (0, _jsx3.default)('img', {\n                    src: record.image,\n                    onClick: function onClick() {\n                        _this2.setState({\n                            listImgVisible: true,\n                            listImgUrl: record.image\n                        });\n                    },\n                    height: '100px'\n                });\n            }\n        }, { title: '创建时间', dataIndex: 'dateCreate', key: 'dateCreate' }, { title: '排序值', dataIndex: 'orderNum', key: 'orderNum' }, {\n            title: '状态',\n            dataIndex: 'status',\n            key: 'status',\n            render: function render(text, record) {\n                // record.status == 0 ? \"未上架\" : record.status == 1 ? \"已上架\" : \"已下架\";\n                // return this.displayStatus(record.status);\n                return '已生效';\n            }\n        }, {\n            title: '操作',\n            dataIndex: 'operator',\n            render: function render(text, record) {\n                return (0, _jsx3.default)('span', {}, void 0, _ref2, (0, _jsx3.default)(_antd.Popconfirm, {\n                    title: '\\u786E\\u5B9A\\u5220\\u9664?',\n                    okText: '\\u786E\\u5B9A',\n                    cancelText: '\\u53D6\\u6D88',\n                    onConfirm: function onConfirm() {\n                        return _this2.onDelete(record.bannerId);\n                    }\n                }, void 0, _ref3), _ref4, (0, _jsx3.default)('a', {\n                    href: '#',\n                    onClick: function onClick() {\n                        return _this2.showDialog(record.bannerId);\n                    }\n                }, void 0, '\\u7F16\\u8F91'));\n            }\n        }];\n\n        return (0, _jsx3.default)(_CMSHeader2.default, {}, void 0, (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)(_antd.Form, {\n            layout: 'inline'\n        }, void 0, (0, _jsx3.default)(FormItem, {}, void 0, (0, _jsx3.default)(RadioGroup, {\n            defaultValue: 3,\n            onChange: function onChange(e) {\n                _this2.onChangeRadio(e);\n            }\n        }, void 0, _ref5)), (0, _jsx3.default)(FormItem, {}, void 0, (0, _jsx3.default)(_antd.Button, {\n            type: 'primary',\n            onClick: function onClick() {\n                return _this2.showDialog(null);\n            }\n        }, void 0, '\\u65B0\\u589E'))), (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)('div', {\n            className: 'table-content'\n        }, void 0, (0, _jsx3.default)(_antd.Table, {\n            columns: columns,\n            dataSource: this.state.result.items,\n            pagination: false\n        })))), (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)(_antd.Modal, {\n            title: this.state.operateType + '|首页banner',\n            okText: '\\u4FDD\\u5B58',\n            cancelText: '\\u53D6\\u6D88',\n            onOk: this.handleOk,\n            onCancel: this.handleCancel,\n            visible: this.state.visible,\n            confirmLoading: this.state.confirmLoading\n        }, void 0, (0, _jsx3.default)(_antd.Form, {\n            layout: 'vertical'\n        }, void 0, (0, _jsx3.default)(FormItem, {\n            label: 'BANNER\\u6807\\u9898'\n        }, void 0, getFieldDecorator('title', {\n            rules: [{ required: true, whiteSpace: true, message: '请输入标题!' }]\n        })(_ref6)), (0, _jsx3.default)(FormItem, {\n            label: '\\u8DF3\\u8F6C\\u534F\\u8BAE\\u5730\\u5740',\n            extra: '*\\u652F\\u6301http\\u534F\\u8BAE\\u548C\\u672C\\u5730\\u8DF3\\u8F6C\\u534F\\u8BAE'\n        }, void 0, getFieldDecorator('address', {\n            rules: [{ required: true, whiteSpace: true, pattern: \"([http|https]+://[^\\\\s]*)|(^/|^./)\", message: '请输入正确的跳转协议地址!' }]\n        })(_ref7)), (0, _jsx3.default)(FormItem, {\n            label: '\\u56FE\\u7247'\n        }, void 0, getFieldDecorator('image', {\n            rules: [{ required: true, message: '请选择要上传的图片!' }]\n        })((0, _jsx3.default)('div', {}, void 0, _react2.default.createElement(\n            _antd.Upload,\n            (0, _extends3.default)({}, uploadFile, { fileList: this.state.fileList }),\n            this.state.fileList.length >= 1 ? null : uploadButton\n        )))))), (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)(_antd.Modal, {\n            visible: this.state.previewVisible,\n            footer: null,\n            onCancel: this.handleCancelPreview\n        }, void 0, (0, _jsx3.default)('img', {\n            alt: 'example',\n            style: { width: '100%' },\n            src: this.state.previewImage\n        }))), (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)(_antd.Modal, {\n            style: { width: '100%', height: '100%' },\n            visible: this.state.previewVisible,\n            footer: null,\n            onCancel: this.handleCancelPreview\n        }, void 0, (0, _jsx3.default)('img', {\n            alt: 'example',\n            style: { width: '100%' },\n            src: this.state.previewImage\n        })))), (0, _jsx3.default)('div', {}, void 0, (0, _jsx3.default)(_antd.Modal, {\n            visible: this.state.listImgVisible,\n            footer: null,\n            onCancel: this.handleBigPicCancel\n        }, void 0, (0, _jsx3.default)('img', {\n            alt: 'example',\n            style: { width: '100%', height: '100%' },\n            src: this.state.listImgUrl\n        }))));\n    };\n\n    return BannerPage;\n}(_sad2.default));\n_reactDom2.default.render((0, _jsx3.default)(Page, {}), document.querySelector(\"#content\"));\n\n//////////////////\n// WEBPACK FOOTER\n// ./pages/banner/BannerPage.jsx\n// module id = 776\n// module chunks = 4\n\n//# sourceURL=webpack:///./pages/banner/BannerPage.jsx?")}},[776]);