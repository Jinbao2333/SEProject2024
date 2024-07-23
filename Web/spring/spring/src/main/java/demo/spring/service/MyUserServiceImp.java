package demo.spring.service;

import demo.spring.entity.*;
import demo.spring.mapper.MyUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("myUserService")
public class MyUserServiceImp implements  MyUserService{
    @Autowired
    private MyUserMapper myUserMapper;

    @Override
    public String findPwdbyUsername(String uname) {
        return this.myUserMapper.findPwdbyUsername(uname);
    }

    @Override
    public MyUser findUserbyUsername(String uname){
        return this.myUserMapper.findUserbyUsername(uname);
    }

    @Override
    public int addUser(MyUser user){
        return this.myUserMapper.add(user.getUser_id(),user.getUserName(),user.getPassword(),user.getEmail());
    }

    @Override
    public int findmaxUser_id(){
        return this.myUserMapper.findmaxUser_id();
    }

    @Override
    public List<Permission> findPermission(int user_id){
        return this.myUserMapper.findPermisssion(user_id);
    }

    public List<MyUser> findallUser() {
        return this.myUserMapper.findallUser();
    }

    public int deleteUser(int user_id){
        return this.myUserMapper.deleteUser(user_id);
    }

    public int changeUserRole(int user_id,int role_id){
        return this.myUserMapper.changeUserRole(user_id,role_id);
    }

    public List<Role> findallRole(){
        return this.myUserMapper.findallRole();
    }

    public List<Permission> findallPermission(){
        return this.myUserMapper.findallPermission();
    }

    public List<RoleInfo> findallRoleInfo(){
        List<Role> rolelist=this.findallRole();
        List<RoleInfo> res=new ArrayList<RoleInfo>();
        RoleInfo roleInfo;
        for(int i=0;i<rolelist.size();i++) {
            roleInfo=new RoleInfo();
            roleInfo.setRole_id(rolelist.get(i).getRole_id());
            roleInfo.setComment(rolelist.get(i).getComment());
            roleInfo.setName(rolelist.get(i).getName());
            roleInfo.setPermissions(this.myUserMapper.findRolePermission(rolelist.get(i).getRole_id()));
            res.add(roleInfo);
        }
        return res;
    }

    public int addRole(Role role) {
        return this.myUserMapper.addRole(role.getRole_id(),role.getName(),role.getComment());
    }

    public int RoleaddPermission(int role_id,int[] permission_id){
        this.myUserMapper.deleteRolePermission(role_id);
        for(int i=0;i<permission_id.length;i++) {
            this.myUserMapper.RoleaddPermission(role_id, permission_id[i]);
        }
        return 0;
    }

    public List<Integer> findPermissionID(int role_id){
        return this.myUserMapper.findPermissionID(role_id);
    }

    public int modifyPwd(MyUser myUser){return this.myUserMapper.modifyPwd(myUser);}
}