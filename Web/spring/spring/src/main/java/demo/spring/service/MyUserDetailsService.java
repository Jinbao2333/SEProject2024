package demo.spring.service;

import demo.spring.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserService myUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        MyUser user=new MyUser();
        user.setUserName(username);
        user.setPassword(myUserService.findPwdbyUsername(username));
        // 输出加密后的密码
        System.out.println(user.getUserName());
        System.out.println(user.getPassword());

        return new User(username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}