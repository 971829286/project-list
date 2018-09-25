package cn.ourwill.huiyizhan.utils;

/**
 * Created by thomasong on 2/29/16.
 */
public enum ReturnType {
    ADD_SUCCESS(0,"添加成功"),ADD_ERROR(1,"添加失败"),
    UPDATE_SUCCESS(2,"修改成功"),UPDATE_ERROR(3,"修改失败"),
    GET_SUCCESS(4,"获取数据成功"),GET_ERROR(5,"获取数据失败"),
    LIST_SUCCESS(6,"获取列表数据成功"),LIST_ERROR(7,"获取列表数据失败"),
    DELETE_SUCCESS(8,"删除成功"),DELETE_ERROR(9,"删除失败"),
    SERVER_ERROR(10,"server error");
    ;

    private int code;
    private String name;

    ReturnType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
