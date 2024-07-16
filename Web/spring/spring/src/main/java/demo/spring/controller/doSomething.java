package demo.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class doSomething {
    @GetMapping(value = "")
    public String dosth(HttpServletRequest request, HttpServletResponse response) {
        return request.getSession().getId();
    }
}
