package com.example.interceptors;

import com.example.pojo.Result;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        //验证token
        try {
            //redis获取相同token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if(redisToken == null) {
                //token失效
                throw new RuntimeException();
            }
            Map<String,Object> claims = JwtUtil.parseToken(token);
            //存储到ThreadLocal
            ThreadLocalUtil.set(claims);
            return true;
        }catch (Exception e){
            //http响应状态码401
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal数据
        ThreadLocalUtil.remove();
    }
}
