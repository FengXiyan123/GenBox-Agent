package com.baidu.fsg.uid.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 工具类。
 */
public abstract class AbstractNetUtils {

    public static InetAddress localAddress;

    static {
        try {
            localAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new RuntimeException("fail to get local ip.");
        }
    }

    public static InetAddress getLocalInetAddress() throws SocketException {

        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();

        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            if (ni.isLoopback()) {
                continue;
            }

            Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress address = addressEnumeration.nextElement();

                if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                    continue;
                }

                return address;
            }
        }

        throw new RuntimeException("No validated local address!");
    }

    public static String getLocalAddress() {
        return localAddress.getHostAddress();
    }

}
