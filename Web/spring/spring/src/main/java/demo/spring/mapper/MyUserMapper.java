package demo.spring.mapper;

import demo.spring.entity.MyUser;
import demo.spring.entity.Permission;
import demo.spring.entity.Role;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper
public interface MyUserMapper {
    @Insert("insert into role values(#{role_id},#{name},#{comment})")
    int addRole(int role_id,String name,String comment);

    @Insert("insert into role_permission values(#{role_id},#{permission_id})")
    int RoleaddPermission(int role_id,int permission_id);

    @Insert("insert into user(user_id,username,password,email,role_id,valid) values(#{user_id},#{username},#{password},#{email},2,1)")
    int add(int user_id,String username,String password,String email);
    @Select("select password from user where username=#{uname} and valid=1 and username!='deleteduser'")
    String findPwdbyUsername(String uname);

    @Select("select max(user_id) from user")
    int findmaxUser_id();
    @Select("select max(role_id) from role")
    int findmaxRole_id();

    @Select("select permission_id,name,comment,path,icon from user natural join role_permission natural join permission where user_id=#{user_id}")
    @Results(id="permissionMap",value={
            @Result(property = "permission_id",column = "permission_id",javaType = Integer.class),
            @Result(property = "name",column = "name",javaType = String.class),
            @Result(property = "comment",column = "comment"),
            @Result(property = "path",column = "path"),
            @Result(property = "icon",column = "icon")
    })
    List<Permission> findPermisssion(int user_id);

    @Select("select permission.permission_id,permission.name,permission.comment,path,icon \n" +
            "from role join role_permission join permission\n" +
            "where role.role_id=role_permission.role_id and role_permission.permission_id=permission.permission_id and role.role_id=#{role_id}")
    @Results(id="rolepermissionMap",value={
            @Result(property = "permission_id",column = "permission_id"),
            @Result(property = "name",column = "name"),
            @Result(property = "comment",column = "comment"),
            @Result(property = "path",column = "path"),
            @Result(property = "icon",column = "icon")
    })
    List<Permission> findRolePermission(int role_id);

    @Select("select user_id,username,password,email,role_id from user where username=#{uname}")
    @Results(id="userMap",value={
            @Result(property = "user_id",column = "user_id",javaType = Integer.class),
            @Result(property = "userName",column = "username",javaType = String.class),
            @Result(property = "password",column = "password",javaType = String.class),
            @Result(property = "email",column = "email",javaType = String.class),
            @Result(property = "role",column = "role_id",javaType = Integer.class)
    })
    MyUser findUserbyUsername(String uname);

    @Select("select user_id,username,password,email,role_id from user where valid=1")
    @Results(id="allUserMap",value={
            @Result(property = "user_id",column = "user_id",javaType = Integer.class),
            @Result(property = "userName",column = "username",javaType = String.class),
            @Result(property = "password",column = "password",javaType = String.class),
            @Result(property = "email",column = "email",javaType = String.class),
            @Result(property = "role",column = "role_id",javaType = Integer.class)
    })
    List<MyUser> findallUser();

    @Select("select role_id,name,comment from role")
    @Results(id="allRoleMap",value={
            @Result(property = "role_id",column = "role_id",javaType = Integer.class),
            @Result(property = "name",column = "name",javaType = String.class),
            @Result(property = "comment",column = "comment",javaType = String.class)
    })
    List<Role> findallRole();

    @Select("select permission_id,name,comment,path,icon from permission")
    @Results(id="allPermissionMap",value={
            @Result(property = "permission_id",column = "permission_id",javaType = Integer.class),
            @Result(property = "name",column = "name",javaType = String.class),
            @Result(property = "comment",column = "comment",javaType = String.class),
            @Result(property = "path",column = "path"),
            @Result(property = "icon",column = "path")
    })
    List<Permission> findallPermission();

    @Select("select permission_id from role_permission where role_id=#{role_id}")
    List<Integer> findPermissionID(int role_id);

    @Update("update user set valid=0 where user_id=#{user_id}")
    int deleteUser(int user_id);

    @Update("update user set role_id=#{role_id} where user_id=#{user_id}")
    int changeUserRole(int user_id,int role_id);

    @Update("update user set password=#{password} where username=#{userName}")
    int modifyPwd(MyUser myUser);

    @Delete("delete from role_permission where role_id=#{role_id}")
    int deleteRolePermission(int role_id);

    //@Update("update student set sname=#{name},ssex=#{sex} where sno=#{sno}")
    //int update(Student student);

    //@Delete("delete from  where ")
    //int deleteBysno(String sno);
}
