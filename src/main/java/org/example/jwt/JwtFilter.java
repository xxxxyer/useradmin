package org.example.jwt;

import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.service.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * JWT过滤器，拦截 /secure的请求
 */
@Slf4j
@WebFilter(filterName = "JwtFilter", urlPatterns = {"/user/*", "/role/*"})
public class JwtFilter implements Filter
{
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/user/signin", "/user/create", "/user/online")));

    @Autowired
    private RedisTemplate redisTemplate;
    private final String ONLINE_TOKEN = "online_token_";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        //获取 header里的token
        final String token = request.getHeader("authorization");
        // 获取访问path
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);

        if ("OPTIONS".equals(request.getMethod())) {
//        if (true) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        }
        // Except OPTIONS, other request should be checked by JWT
        else if (allowedPath) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        } else {
            // 无token
            if (token == null) {
                response.getWriter().println(ResultGenerator.genJsonFailResult(401,"没有token！"));
                return;
            }
            // token失效
            if (Boolean.FALSE.equals(redisTemplate.hasKey(ONLINE_TOKEN + token))) {
                response.getWriter().println(ResultGenerator.genJsonFailResult(401,"token失效！"));
                return;
            }

            Map<String, Claim> userData = JwtUtil.verifyToken(token);
            if (userData == null) {
                response.getWriter().println(ResultGenerator.genJsonFailResult(401,"token不合法！"));
                return;
            }
            Integer id = userData.get("id").asInt();
            String username = userData.get("username").asString();
            String nickname = userData.get("nickname") == null ? null :userData.get("nickname").asString();
            String role= userData.get("role") == null ? null : userData.get("role").asString();
            log.info("verify token: id={}, username={}, role={}", id, username, role);
            //拦截器 拿到用户信息，放到request中
            request.setAttribute("id", id);
            request.setAttribute("username", username);
            request.setAttribute("nickname", nickname);
            request.setAttribute("role", role);
            request.setAttribute("token", token);
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }

    public static User getTokenUserInfo(HttpServletRequest request) {
        long id = Long.parseLong(request.getAttribute("id").toString());
        String username = (String) request.getAttribute("username");
        String nickname = (String) request.getAttribute("nickname");
        String role = (String) request.getAttribute("role");
        return new User(id, username, nickname, role);
    }
}