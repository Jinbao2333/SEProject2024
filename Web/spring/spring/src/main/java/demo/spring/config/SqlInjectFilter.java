package demo.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@WebFilter(urlPatterns = "/*", filterName = "SQLInjection", initParams = { @WebInitParam(name = "regex", value = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" +
        "(\\b(and|exec|execute|insert|select|delete|update|count|drop|%|chr|mid|master|truncate|char|declare|sitename|net user|xp_cmdshell|or|like'|and|exec|execute|insert|create|drop|table|from|grant|use|group_concat|column_name|information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|chr|mid|master|truncate|char|declare|or|--|like)\\b)") })
public class SqlInjectFilter implements Filter {
    private String regx;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.regx = filterConfig.getInitParameter("regex");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
            Map parametersMap = servletRequest.getParameterMap();
            Iterator it = parametersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String[] value = (String[]) entry.getValue();
                for (int i = 0; i < value.length; i++) {
                    if (null != value[i] && this.regx != null) {
                        Pattern p = Pattern.compile(this.regx);
                        Matcher m = p.matcher(value[i]);
                        if (m.find()) {
                            log.error("您输入的参数有非法字符，请输入正确的参数！");
                            servletRequest.setAttribute("err", "您输入的参数有非法字符，请输入正确的参数！");
                            servletRequest.setAttribute("pageUrl", req.getRequestURI());
                            servletRequest.getRequestDispatcher("/error").forward(servletRequest, servletResponse);
                            return;
                        }
                    }
                }
            }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}