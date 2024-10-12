package com.zhedian.common.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


public class IpUtils {


    public static String getIpAddr(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (org.springframework.util.StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (org.springframework.util.StringUtils.isEmpty(ip) || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (org.springframework.util.StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (org.springframework.util.StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            throw e;
        }

        return ip;
    }
}
