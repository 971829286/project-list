package cn.ourwill.huiyizhan.mapper;

import cn.ourwill.huiyizhan.entity.User;
import cn.ourwill.huiyizhan.entity.UserBase;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class UserSqlProvider {

    String colums="id,uuid,level,nickname,username,avatar,mob_phone,tel_phone,email,qq,company,address,version,info";

    public String insertSelective(User record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("user");
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }

        if (record.getUUID() != null) {
            sql.VALUES("uuid", "#{UUID,jdbcType=CHAR}");
        }

        if (record.getLevel() != null) {
            sql.VALUES("level", "#{level,jdbcType=INTEGER}");
        }

        if (record.getNickname() != null) {
            sql.VALUES("nickname", "#{nickname,jdbcType=VARCHAR}");
        }

        //-------------------------------添加的同步字段 2018年4月9日18:05:14
        if (record.getAddress() != null) {
            sql.VALUES("address", "#{address,jdbcType=VARCHAR}");
        }
        if (record.getCompany() != null) {
            sql.VALUES("company", "#{company,jdbcType=VARCHAR}");
        }
        if (record.getQq() != null) {
            sql.VALUES("qq", "#{qq,jdbcType=VARCHAR}");
        }
        if (record.getEmail() != null) {
            sql.VALUES("email", "#{email,jdbcType=VARCHAR}");
        }
        if (record.getTelPhone() != null) {
            sql.VALUES("tel_phone", "#{telPhone,jdbcType=VARCHAR}");
        }
        if (record.getMobPhone() != null) {
            sql.VALUES("mob_phone", "#{mobPhone,jdbcType=VARCHAR}");
        }
        if (record.getAvatar() != null) {
            sql.VALUES("avatar", "#{avatar,jdbcType=VARCHAR}");
        }
        if (record.getUsername() != null) {
            sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        if (record.getVersion() != null) {
            sql.VALUES("version", "#{version,jdbcType=INTEGER}");
        }
        if (record.getInfo() != null) {
            sql.VALUES("info", "#{info,jdbcType=VARCHAR}");
        }
        if (record.getUnionid() != null) {
            sql.VALUES("unionid", "#{unionid,jdbcType=VARCHAR}");
        }
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(User record) {
        SQL sql = new SQL();
        sql.UPDATE("user");

        if (record.getUUID() != null) {
            sql.SET("uuid = #{UUID,jdbcType=CHAR}");
        }

        if (record.getLevel() != null) {
            sql.SET("level = #{level,jdbcType=INTEGER}");
        }

        if (record.getNickname() != null) {
            sql.SET("nickname = #{nickname,jdbcType=VARCHAR}");
        }

        //-------------------------------添加的同步字段 2018年4月9日18:05:14
        if (record.getAddress() != null) {
            sql.SET("address = #{address,jdbcType=VARCHAR}");
        }
        if (record.getCompany() != null) {
            sql.SET("company = #{company,jdbcType=VARCHAR}");
        }
        if (record.getQq() != null) {
            sql.SET("qq = #{qq,jdbcType=VARCHAR}");
        }
        if (record.getEmail() != null) {
            sql.SET("email = #{email,jdbcType=VARCHAR}");
        }
        if (record.getTelPhone() != null) {
            sql.SET("tel_phone = #{telPhone,jdbcType=VARCHAR}");
        }
        if (record.getMobPhone() != null) {
            sql.SET("mob_phone = #{mobPhone,jdbcType=VARCHAR}");
        }
        if (record.getAvatar() != null) {
            sql.SET("avatar = #{avatar,jdbcType=VARCHAR}");
        }
        if (record.getUsername() != null) {
            sql.SET("username = #{username,jdbcType=VARCHAR}");
        }
        if (record.getVersion() != null) {
            sql.SET("version = #{version,jdbcType=INTEGER}");
        }
        if (record.getInfo() != null) {
            sql.SET("info = #{info,jdbcType=VARCHAR}");
        }

        sql.WHERE("id = #{id,jdbcType=INTEGER}");

        return sql.toString();
    }

    public String updateUserBase(UserBase record) {
        SQL sql = new SQL();
        sql.UPDATE("user");
        if (record.getLevel() != null) {
            sql.VALUES("level", "#{level,jdbcType=INTEGER}");
        }

        if (record.getNickname() != null) {
            sql.VALUES("nickname", "#{nickname,jdbcType=VARCHAR}");
        }

        //-------------------------------添加的同步字段 2018年4月9日18:05:14
        if (record.getAddress() != null) {
            sql.VALUES("address", "#{address,jdbcType=VARCHAR}");
        }
        if (record.getCompany() != null) {
            sql.VALUES("company", "#{company,jdbcType=VARCHAR}");
        }
        if (record.getQq() != null) {
            sql.VALUES("qq", "#{qq,jdbcType=VARCHAR}");
        }
        if (record.getEmail() != null) {
            sql.VALUES("email", "#{email,jdbcType=VARCHAR}");
        }
        if (record.getTelPhone() != null) {
            sql.VALUES("tel_phone", "#{telPhone,jdbcType=VARCHAR}");
        }
        if (record.getMobPhone() != null) {
            sql.VALUES("mob_phone", "#{mobPhone,jdbcType=VARCHAR}");
        }
        if (record.getAvatar() != null) {
            sql.VALUES("avatar", "#{avatar,jdbcType=VARCHAR}");
        }
        if (record.getUsername() != null) {
            sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        if (record.getVersion() != null) {
            sql.VALUES("version", "#{version,jdbcType=INTEGER}");
        }
        if (record.getInfo() != null) {
            sql.VALUES("info", "#{info,jdbcType=VARCHAR}");
        }
        return sql.toString();
    }

    //根据属性查找(使用Map参数)
    public String selectByParams(final Map<String,Object> param){
        return new SQL(){
            {
                SELECT(colums);
                FROM("user");
                if(param.get("id")!=null&&param.get("id")!=""){
                    WHERE("id=#{id}");
                }
                if(param.get("username")!=null&&param.get("username")!=""){
                    WHERE("username like CONCAT('%',#{username},'%')");
                }
                if(param.get("nickname")!=null&&param.get("nickname")!=""){
                    WHERE("nickname=#{nickname}");
                }
                if(param.get("mobPhone")!=null&&param.get("mobPhone")!=""){
                    WHERE("mob_phone like CONCAT('%',#{mobPhone},'%')");
                }
                if(param.get("telPhone")!=null&&param.get("telPhone")!=""){
                    WHERE("tel_phone=#{telPhone}");
                }
                if(param.get("email")!=null&&param.get("email")!=""){
                    WHERE("email=#{email}");
                }
            }
        }.toString();
    }

    //查找所有
    public String findAll() {
        return new SQL() {
            {
                SELECT("id,uuid,level,nickname,username,avatar," +
                        "mob_phone,tel_phone,email,qq,company,address,version,info,unionid");
                FROM("user");
            }
        }.toString();
    }
}