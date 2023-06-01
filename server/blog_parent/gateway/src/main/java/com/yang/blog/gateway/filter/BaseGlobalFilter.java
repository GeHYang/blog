package com.yang.blog.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yang.blog.gateway.utils.JwtUtil;
import com.yang.blog.gateway.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author: Yang
 * @create: 2023-05-02
 * @Description: 自定义全局过滤器
 */
@Order(-1)
@Component
public class BaseGlobalFilter implements GlobalFilter {
    @Resource
    private RedisCache redisCache;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 路径校验，区分是否是需要验证的接口
        String path = exchange.getRequest().getPath().toString();
        if(pathMatcher(path)){
            return chain.filter(exchange);
        }
        // 规定token需要存放在header里面，并且键名为Authorization
        // 1、获取token信息
        List<String> tokenList = exchange.getRequest().getHeaders().get("Authorization");
        // 没有token直接放行
        if(tokenList == null || tokenList.size() == 0){
            return chain.filter(exchange);
        }
        // 2、获取token
        String token = tokenList.get(0);
        // 3、校验token
        try {
            // 3.1、解析token
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            // 3.2、从redis中查询该用户的token，判断token是否一致
            String redisToken = redisCache.getCacheObject("blog-login-token:" + userId);
            if(!StringUtils.hasText(redisToken) || !redisToken.equals(token)){
                return res(exchange.getResponse(), 40000, "登录校验失败，请重新登录");// 不放行
            }
            ServerHttpRequest httpRequest = exchange.getRequest().mutate().header("user-id", userId).build();

            return chain.filter(exchange.mutate().request(httpRequest).build());
        } catch (Exception e) {
            e.printStackTrace();
            // token已失效
            return res(exchange.getResponse(), 40000, "登录校验失败，请重新登录");// 不放行
        }
    }

    /**
     * 路径校验器，用来判断是否需要验证才能进入接口
     * @param path 请求路径
     * @return true：不需要校验，false：需要登录校验
     */
    private boolean pathMatcher(String path) {
        PathMatcher matcher = new AntPathMatcher();
        String[] anyPath = {"/front/dynamic/page", "/front/user/login", "/front/comment/*"};
        for (String any : anyPath) {
            if(matcher.match(any, path)){
                return true;
            }
        }
        return false;
    }

    private Mono<Void> res(ServerHttpResponse response, int code, String msg) {
        // 1、构建响应信息
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        // 2、构建响应数据
        byte[] bytes = json.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        // 3、设置响应数据编码格式
        response.getHeaders().add("Content-Type", "application/json;charset=utf8");
        return response.writeWith(Mono.just(buffer));
    }
}
