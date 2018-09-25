import React from 'react';
import {Upload, Button, Icon} from 'antd';

class UploadPic extends React.Component {

    state = {
        fileList: [{
            uid: '',
            name: '',
            status: '',
            url: '',
        }
        ],
    }


    handleChange = (info) => {
        let fileList = info.fileList;

        // 1. Limit the number of uploaded files
        // Only to show two recent uploaded files, and old ones will be replaced by the new
        fileList = fileList.slice(-2);

        // 2. Read from response and show file link
        fileList = fileList.map((file) => {
            if (file.response) {
                // Component will show file.url as link
                file.url = file.response.data;
            }
            return file;
        });

        // 3. Filter successfully uploaded files according to response from server
        fileList = fileList.filter((file) => {
            if (file.response) {
                return file.response.success === 'success';
            }
            return true;
        });

        this.setState({fileList});

    }


    render() {
        const uploadFile = {
            action: '/uploadAPI/uploadPic.json',
            onChange: this.handleChange.bind(),
            multiple: true,
            type: 'file',
            accept: 'image/jpeg,image/jpeg,image/x-png,image/gif'
        };

        return (
            <Upload {...uploadFile} fileList={this.state.fileList}>
                <Button>
                    <Icon type="upload"/> 上传图片
                </Button>
            </Upload>
        );
    }
}

export default UploadPic;

