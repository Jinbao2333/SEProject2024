package demo.spring.config;

import demo.spring.entity.Permission;
import demo.spring.mapper.MyUserMapper;
import demo.spring.service.MyUserService;
import demo.spring.service.MyUserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    String[] fieldURI={"/api/admin/deletefield","/api/admin/modifyfieldname","/api/admin/addfield"};
    String[] roleURI={"/api/admin/user/changerole","/api/admin/addrole","/api/admin/modifypermission"};
    String[] userURI={"/api/admin/user/delete"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        System.out.println(session.getAttribute("role"));
        String uri=request.getRequestURI();
        boolean role=(Boolean) session.getAttribute("role");
        boolean field=(Boolean) session.getAttribute("field");
        boolean user=(Boolean) session.getAttribute("user");

        if(!field&& Arrays.asList(fieldURI).contains(uri))
            return false;
        if(!role&&Arrays.asList(roleURI).contains(uri))
            return false;
        if(!user&&Arrays.asList(userURI).contains(uri))
            return false;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
