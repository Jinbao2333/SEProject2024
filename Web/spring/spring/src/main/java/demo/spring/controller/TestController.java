package demo.spring.controller;

import demo.spring.entity.Result;
import demo.spring.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    MyUserService myUserService;

    SessionRegistry sessionRegistry=new SessionRegistryImpl();
    @GetMapping( value = "/hello")
    public String getpwd(){
        return "hello";
    }

    @GetMapping( value="/test")
    public Object getuser() {
        return myUserService.findUserbyUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
