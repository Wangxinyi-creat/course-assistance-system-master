package com.zhedian.admin.config;

import com.zhedian.provide.security.exception.AccessDeniedHandlerImpl;
import com.zhedian.provide.security.exception.AuthenticationEntryPointImpl;
import com.zhedian.provide.security.filter.JwtAuthenticationTokenFilter;
import com.zhedian.provide.security.handle.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 认证失败处理器
     */
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    /**
     * 授权失败处理器
     */
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    /**
     * 退出成功处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable().and()
                // 认证认证授权失败处理类
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login  允许匿名访问
                .antMatchers("/user/login").permitAll()
                // 静态资源，可匿名访问   放行文件上传 /file/upload   swagger放行
                .antMatchers(HttpMethod.GET, "/**/*.css", "/**/*.js", "/file/**", "/img/**").permitAll()
                .antMatchers("/file/upload","/file/uploadLocal", "/swagger-ui/index.html", "/swagger-ui.html",
                        "/swagger-resources/**", "/v2/**", "/api/**", "/doc.html","/user/captcha","/druid/*",
                        "/dept/get","/dict/getByDictType","/user/register","/file/upload").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        // 添加Logout filter
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        http.cors();
    }


    /**
     * new BCryptPasswordEncoder();
     * bCryptPasswordEncoder.encode();//加密
     * bCryptPasswordEncoder.matches()//判断加密密码与原密码是否一致
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
