package demo.spring.service;

import demo.spring.entity.*;

import java.util.List;

public interface MyUserService{

    public String findPwdbyUsername(String uname);

    public MyUser findUserbyUsername(String uname);

    public int addUser(MyUser user);

    public int findmaxUser_id();

    public List<Permission> findPermission(int user_id);

    public List<MyUser> findallUser();

    public int deleteUser(int user_id);

    public int changeUserRole(int user_id,int role_id);

    public List<Role> findallRole();

    public List<Permission> findallPermission();

    public List<RoleInfo> findallRoleInfo();

    public int addRole(Role role);

    public int RoleaddPermission(int role_id,int[] permission_id);

    public List<Integer> findPermissionID(int role_id);

    public int modifyPwd(MyUser myUser);

}