package demo.spring.config;

import demo.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.Arrays;
import java.util.List;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private MyUserDetailsService myUserDetailsService;

        @Autowired
        private MySessionExpiredStrategy mySessionExpiredStrategy;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http
                        //禁用baisc和form认证，在Controller中自己实现认证逻
                        .httpBasic().disable()
                        .formLogin().disable()
                        .csrf().disable()
                        .logout().disable()
                        .exceptionHandling()
                        .and()
                        .authorizeRequests() // 授权配置
                        .antMatchers("/authentication/require","/api/login","/test","/api/register/sendemail",
                                "/api/register","/session/invalid","/user/login").permitAll()
                        .anyRequest()  // 所有请求
                        .authenticated() // 都需要认证
                        .and()
                        .sessionManagement() // 添加 Session管理器
                        .invalidSessionStrategy(new MyInvalidSessionStrategy()) // Session失效后跳转到这个链接
                        .maximumSessions(1)
                        .expiredSessionStrategy(new MySessionExpiredStrategy());
                ;
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
                return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        }

        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:9877")
                        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                        .maxAge(3600)
                        .allowCredentials(true);
        }

}
