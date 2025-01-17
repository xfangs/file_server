package com.zhongli.fileserver.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class RestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = null;
        if (request instanceof HttpServletRequest) {
            req = (HttpServletRequest) request;
        }
        HttpServletResponse res = null;
        if (response instanceof HttpServletResponse) {
            res = (HttpServletResponse) response;
        }
        String origin = Optional.ofNullable(req.getHeader("Origin")).orElse(req.getHeader("Referer"));
        if (origin == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (req != null && res != null) {
            //设置允许传递的参数
            res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            //设置允许带上cookie
            res.setHeader("Access-Control-Allow-Credentials", "true");
            //设置允许的请求来源
            res.setHeader("Access-Control-Allow-Origin", origin);
            //设置允许的请求方法
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        }
        filterChain.doFilter(request, response);
    }
}
