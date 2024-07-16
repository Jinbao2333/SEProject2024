package demo.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class MyInvalidSessionStrategy implements InvalidSessionStrategy {
    String[] puburi={"/authentication/require","/api/login","/test","/api/register/sendemail",
            "/api/register","/session/invalid","/user/login"};
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String requestUri = request.getRequestURI();
        System.out.println(requestUri);
        if(isPublicUri(requestUri)){
            request.getSession(true).invalidate();
            RequestDispatcher dispatcher=request.getRequestDispatcher(requestUri);
            dispatcher.forward(request,response);
        }
        else {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("code", 3);
            map.put("msg", "你还没有登陆");

            String jsonMap = new ObjectMapper().writeValueAsString(map);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(jsonMap);
        }
    }

    public boolean isPublicUri(String uri){
        return Arrays.asList(puburi).contains(uri);
    }
}
