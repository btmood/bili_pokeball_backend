package com.lark.userservice.config;

import com.fasterxml.jackson.core.filter.TokenFilter;
import com.lark.usercommon.shiro.filter.OptionFilter;
import com.lark.usercommon.shiro.realm.PokeballRealm;
import com.lark.usercommon.shiro.session.CustomSessionManager;
import com.lark.userservice.shiro.realm.UserRealm;
import jdk.nashorn.internal.parser.Token;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //1. 创建realm
    @Bean
    public PokeballRealm getRealm(){
        return new UserRealm();
    }

    //2. 创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(PokeballRealm realm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realm);

        /*配置会话管理需要的内容----开始--------*/
        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(cacheManager());

        /*---------会话、缓存、配置结束-----------*/

        return securityManager;
    }

    //3. 配置shiro过滤器
//    @Bean
//    public TokenFilter tokenFilter(){
//        return new TokenFilter();
//    }

    /**
     * 在web程序中，shiro进行权限控制全部是通过一组过滤器集合进行控制
     *  通过map来对不同的地址进行过滤，通过value来决定用哪种方式来过滤
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        //1. 创建过滤器工厂
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        //2. 设置安全管理器
        filterFactory.setSecurityManager(securityManager);

        //补充：自定义过滤器
        Map<String, Filter> filters = filterFactory.getFilters();
//        filters.put("hasToken", tokenFilter());
        filters.put("authc", new OptionFilter());
        filterFactory.setFilters(filters);

        //3. 通用配置（跳转登录页面，为授权跳转的页面）
        filterFactory.setLoginUrl("/autherror?code=1");//未登录跳转到url地址
        filterFactory.setUnauthorizedUrl("/autherror?code=2");//未授权
        //4. 设置过滤器集合

        /**
         * 设置所有的过滤器：有顺序map
         *      key = 拦截的url地址
         *      value = 过滤器类型
         *
         * 权限
         *      anon：匿名访问
         *      authc：认证之后访问
         *      perms：具有某种权限才能访问（在controller中用注解来做，不用这个配置文件）
         */
        Map<String,String> filterMap = new LinkedHashMap<>();
        //swagger
        filterMap.put("/swagger-ui.html","anon");
        filterMap.put("/swagger-resources/**","anon");
        filterMap.put("/v2/api-docs/**","anon");
        filterMap.put("/webjars/springfox-swagger-ui/**","anon");
        //登录
        filterMap.put("/userservice/user/login", "anon");
        //测试专用
        filterMap.put("/permission/test", "anon");
        //未授权或者未登录需要跳转的接口
        filterMap.put("/autherror?code=1", "anon");
        filterMap.put("/autherror?code=2", "anon");
        //所有地址必须认证（登录）以后才能访问
        filterMap.put("/**", "authc");
        //权限、角色：使用注解来授权


        filterFactory.setFilterChainDefinitionMap(filterMap);

        return filterFactory;
    }

    //配置shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /*-------------------session管理---------------*/

    /**
     * 为什么要把session放到redis中去呢？
     *  1. 如果不放到redis中去，session就保存在当前服务的内存中，其它服务访问不到
     *  2. 很明显了，未了用于分布式微服务，每个服务都能同步数据
     *  3. 如果不放到reids中去，就相当于是一个普通的session
     */
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    //1. redis的控制器，操作redis
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword("123456");
        return redisManager;
    }

    //2. sessionDao
    public RedisSessionDAO reidsSessionDao(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        //看shiro架构图，可以看到reidsDao使用RedisManager管理
        redisSessionDAO.setRedisManager(redisManager());
//        redisSessionDAO.setExpire(12000);
        return redisSessionDAO;
    }

    //3. 会话管理
    public DefaultWebSessionManager sessionManager(){
        CustomSessionManager sessionManager = new CustomSessionManager();
        //这一步其实和上面差不多逻辑
        sessionManager.setSessionDAO(reidsSessionDao());
        //禁用cookie(可选)
        sessionManager.setSessionIdCookieEnabled(true);
        //禁用url重写（可选）
        sessionManager.setSessionIdUrlRewritingEnabled(false);
//        sessionManager.setGlobalSessionTimeout(60);
        return sessionManager;
    }


    //4. 缓存管理器
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /*-------------会话管理配置结束---------------------------*/

}
