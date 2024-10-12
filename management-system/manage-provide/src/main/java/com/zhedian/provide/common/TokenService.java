package com.zhedian.provide.common;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zhedian.common.domain.CustomExceptionResult;
import com.zhedian.common.domain.ResultCode;
import com.zhedian.provide.system.entity.SysUser;
import com.zhedian.provide.system.model.LoginUser;
import com.zhedian.provide.system.service.SysMenuService;
import com.zhedian.provide.system.service.SysUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * JWT工具类
 */
@Component
public class TokenService {

    //有效期为
    @Value("${token.expireTime}")
    private int JWT_TTL;
    //设置秘钥明文
    @Value("${token.secret}")
    private String JWT_KEY;

    protected static final long MILLIS_MINUTE = 60 * 1000;

    @Resource
    private RedisCache redisService;

    @Value("${spring.redis.enabled}")
    private Boolean enabled;

    @Resource
    @Lazy
    private SysUserService sysUserService;

    @Resource
    @Lazy
    private SysMenuService sysMenuService;


    /**
     * 生成token
     *
     * @param loginUser 登录实体对象
     * @return
     */
    public String createToken(LoginUser loginUser) {
        String userName = loginUser.getUser().getUserName();
        String jwt = createJWT(userName);
        //loginUser设值
        loginUser.setToken(jwt);
        loginUser.setUserId(loginUser.getUser().getId());

        // 如果开启redis 把loginUser存入redis
        if (enabled) redisService.setCacheObject("login:" + userName, loginUser);
        return jwt;
    }


    /**
     * 解析token  从redis中获取LoginUser并返回
     *
     * @param token
     * @return
     */
    public LoginUser parseToken(String token) {
        String userName;
        try {
            Claims claims = parseJWT(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            //token超时  token非法
            //响应告诉前端需要重新登录
            throw new CustomExceptionResult(ResultCode.FORBIDDEN.getCode(), "token失效或过期，请重新登录");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userName;
        LoginUser loginUser;
        // 判断是否开启redis
        if (enabled) {
            loginUser = redisService.getCacheObject(redisKey);
        } else {
            //根据用户名查询用户信息
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getUserName, userName);
            SysUser user = sysUserService.getOne(wrapper);
            //如果查询不到数据就通过抛出异常来给出提示
            if (Objects.isNull(user)) {
                throw new CustomExceptionResult(403, "无此账号,请确认账号名是否正确");
            }
            //根据用户 查询操作权限
            List<String> permissionIdS = sysMenuService.getPermissionIdByUser(user.getUserName());
            loginUser = new LoginUser(user, permissionIdS);
        }
        return loginUser;
    }


    /**
     * 解析token 并删除用户缓存记录
     *
     * @param token
     */
    public void delLoginUser(String token) {
        String userName;
        //解析token
        try {
            Claims claims = parseJWT(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            //token超时  token非法
            //响应告诉前端需要重新登录
            throw new CustomExceptionResult(ResultCode.FORBIDDEN.getCode(), "登录已过期");
        }
        if (enabled) {
            if (ObjectUtils.isNotNull(userName)) redisService.deleteObject("login:" + userName);
        }
    }


    /**
     * 生成jtw
     *
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    private String createJWT(String subject) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        JwtBuilder builder = getJwtBuilder(subject, null, uuid);// 设置过期时间
        return builder.compact();
    }


    /**
     * 生成jtw
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    private String createJWT(String subject, Integer ttlMillis) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, uuid);// 设置过期时间
        return builder.compact();
    }


    /**
     * 创建jwt
     *
     * @param subject
     * @param ttlMillis
     * @param id
     * @return
     */
    private String createJWT(String subject, Integer ttlMillis, String id) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    private Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * jwt具体实现类
     *
     * @param subject   存入数据（用户id）
     * @param ttlMillis 过期时间
     * @param uuid      唯一id
     * @return
     */
    private JwtBuilder getJwtBuilder(String subject, Integer ttlMillis, String uuid) {
        //生成加密秘钥
        SecretKey secretKey = generalKey();

        //获取当前的总毫秒数
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis * MILLIS_MINUTE;

        //创建返回token
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)     // 主题  可以是JSON数据
                .setIssuer("zhedian")   // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(SignatureAlgorithm.HS512, secretKey) //使用HS512对称加密算法签名, 第二个参数为秘钥
                .setExpiration(new Date(expMillis));
    }


    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    private SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
}
