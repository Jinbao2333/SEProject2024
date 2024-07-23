package demo.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MySessionExpiredStrategy implements SessionInformationExpiredStrategy {

    String[] puburi={"/authentication/require","/api/login","/test","/api/register/sendemail",
            "/api/register","/session/invalid","/user/login"};
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        Map<Object, Object> map = new LinkedHashMap<>();
        map.put("code", 3);
        map.put("msg", "你还没有登陆");

        String jsonMap = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(jsonMap);
    }
}