package demo.spring.controller;

import demo.spring.entity.MyUser;
import demo.spring.entity.Permission;
import demo.spring.entity.Result;
import demo.spring.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserService myUserService;

    private MyUser myUser;

    @CrossOrigin
    @PostMapping("/api/login")
    public Result login(@RequestBody LoginRequest loginRequest, HttpSession session) {
       System.out.println(new BCryptPasswordEncoder().encode("123456"));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            return Result.fail("用户名或密码错误",null);
        }

        myUser= myUserService.findUserbyUsername(loginRequest.getUsername());

        List<Integer> permissionList=myUserService.findPermissionID(myUser.getRole());
        session.setAttribute("user",permissionList.contains(7));
        session.setAttribute("field",permissionList.contains(8));
        session.setAttribute("role",permissionList.contains(9));
        session.setAttribute("admin",myUser.getRole());

        return Result.success("success",myUser);
    }

    @CrossOrigin
    @PostMapping("/api/register")
    public Result register(@RequestBody RegisterRequest registerRequest) {
        if(myUserService.findPwdbyUsername(registerRequest.getUsername())!=null){
            return Result.fail("用户已存在",null);
        }

        resultMap=VerifyCode.queryvCode(registerRequest.getUsername());
        //判断验证码是否正确
        if(resultMap==null){
            return Result.fail("请先接收验证码",null);
        }
        String requestHash = resultMap.get("hash").toString();

        String tamp = resultMap.get("tamp").toString();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");//当前时间
        Calendar c = Calendar.getInstance();
        String currentTime = sf.format(c.getTime());
        if (tamp.compareTo(currentTime) > 0) {
            String hash =  MD5Utils.code(registerRequest.getIdentify());//生成MD5值
            if (!hash.equalsIgnoreCase(requestHash)){
                return Result.success("验证码错误",null);
            }
        }
        else{
            return Result.fail("验证码过期",null);
        }


        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(myUserService.findmaxUser_id());
        int user_id_next=myUserService.findmaxUser_id()+1;
        System.out.println(user_id_next);
        myUser=new MyUser();
        myUser.setUser_id(user_id_next);
        myUser.setUserName(registerRequest.getUsername());
        myUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        myUser.setEmail(registerRequest.getEmail());
        myUser.setRole(2);

        System.out.println(myUserService.addUser(myUser));

        return Result.success("注册成功",null);
    }

    @CrossOrigin
    @PostMapping("/api/modifypwd")
    public Result modifypwd(@RequestBody RegisterRequest registerRequest) {

        resultMap=VerifyCode.queryvCode(registerRequest.getUsername());
        //判断验证码是否正确
        if(resultMap==null){
            return Result.fail("请先接收验证码",null);
        }
        String requestHash = resultMap.get("hash").toString();

        String tamp = resultMap.get("tamp").toString();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");//当前时间
        Calendar c = Calendar.getInstance();
        String currentTime = sf.format(c.getTime());
        if (tamp.compareTo(currentTime) > 0) {
            String hash =  MD5Utils.code(registerRequest.getIdentify());//生成MD5值
            if (!hash.equalsIgnoreCase(requestHash)){
                return Result.success("验证码错误",null);
            }
        }
        else{
            return Result.fail("验证码过期",null);
        }


        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(myUserService.findmaxUser_id());
        int user_id_next=myUserService.findmaxUser_id()+1;
        System.out.println(user_id_next);
        myUser=new MyUser();
        myUser.setUser_id(user_id_next);
        myUser.setUserName(registerRequest.getUsername());
        myUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        myUser.setEmail(registerRequest.getEmail());
        try {
            System.out.println(myUserService.modifyPwd(myUser));
        }catch (Exception e){
            return Result.fail("修改失败",null);
        }
        return Result.success("修改成功",null);
    }

    @CrossOrigin
    @GetMapping(value="/session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result sessioninvalid(){
        return Result.fail("会话过期",null);
    }

    @Autowired
    private JavaMailSender jms;

    @Value("${spring.mail.username}")
    private String from;

    private HashMap<String, Object> resultMap = new HashMap<>();
    @CrossOrigin
    @PostMapping("/api/register/sendemail")
    public Result registersendemail(@RequestBody RegisterRequest registerRequest){
        SimpleMailMessage message = new SimpleMailMessage();

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 6; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        String code = sb.toString();//随机数生成6位验证码

        message.setFrom(from);
        message.setTo(registerRequest.getEmail());
        message.setSubject(registerRequest.getUsername()+"LabPaperShare注册");// 标题
        message.setText("你的验证码为："+code+"，有效时间为5分钟(若不是本人操作，可忽略该条邮件)");
        try {
            jms.send(message);
            saveCode(registerRequest.getUsername(),code);
            return Result.success("邮件发送成功",null);
        }catch (MailSendException e){
            return Result.fail("目标邮箱不存在",null);
        } catch (Exception e) {
            return Result.fail("文本邮件发送异常",null);
        }
    }

    private void saveCode(String username,String code){
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 5);
        String currentTime = sf.format(c.getTime());// 生成5分钟后时间，用户校验是否过期

        String hash =  MD5Utils.code(code);//生成MD5值
        resultMap.put("hash", hash);
        resultMap.put("tamp", currentTime);

        VerifyCode.insertvCode(username,resultMap);
    }

    @CrossOrigin
    @PostMapping("/api/permission")
    public List<Permission> permission(@RequestParam("user_id") Integer user_id) {
        return this.myUserService.findPermission(user_id);
    }

    @CrossOrigin
    @PostMapping("/api/logout")
    public Result logout(HttpSession session, SessionStatus sessionStatus) {
        session.invalidate();
        sessionStatus.setComplete();
        return Result.success("成功",null);
    }
}
