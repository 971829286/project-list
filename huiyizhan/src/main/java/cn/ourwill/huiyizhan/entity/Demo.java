package cn.ourwill.huiyizhan.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 10:17
 * @Version1.0
 */
@ApiModel(value="demo", description = "用户实体")
public class Demo {

    @ApiModelProperty(value="id1",name = "id2")
    private Long id;
    @ApiModelProperty(value="用户名1",name = "用户名2")
    private String username;
    @ApiModelProperty(value="密码1",name = "密码2")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
