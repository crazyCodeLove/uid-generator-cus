package com.sse.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author pczhao
 * @email
 * @date 2018-12-06 14:05
 */

public class IpUtil {

    public static String getRequestIpAddr(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String ipAddress = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipAddress) || StringUtils.equalsIgnoreCase("unknown", ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddress) || StringUtils.equalsIgnoreCase("unknown", ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddress) || StringUtils.equalsIgnoreCase("unknown", ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (StringUtils.equals(ipAddress, "127.0.0.1") || StringUtils.equals(ipAddress, "0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    return null;
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个非unknown的有效IP字符串为客户端真实IP,多个IP按照','分割
        if (!StringUtils.isBlank(ipAddress) && ipAddress.length() > 15) {
            // "***.***.***.***".length() = 15
            String[] ipArrs = StringUtils.split(ipAddress, ",");
            for (String ipArr : ipArrs) {
                if (!StringUtils.isBlank(ipArr) && !StringUtils.equalsIgnoreCase("unknown", ipArr)) {
                    ipAddress = ipArr;
                    break;
                }
            }
        }
        return ipAddress;
    }

    public static String getLocalIpAddr() {
        // enumerates all network interfaces
        try {
            Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();
            while (enu.hasMoreElements()) {
                NetworkInterface ni = enu.nextElement();
                if (ni.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    InetAddress address = addressEnumeration.nextElement();
                    // ignores all invalidated addresses
                    if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                        continue;
                    }
                    return address.getHostAddress();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("No validated local address!");
    }
}
