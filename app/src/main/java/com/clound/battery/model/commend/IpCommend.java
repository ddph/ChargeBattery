package com.clound.battery.model.commend;

/**
 * author cowards
 * created on 2018\12\10 0010
 **/
public class IpCommend {

    private static boolean DEBUG = false;       //本地调试是false,线上测是true

    public static String getIp() {
        if (DEBUG) {
            return "39.105.56.18";
        }
        return "192.168.1.192";
    }

    public static int getPort() {
        if (DEBUG) {
            return 18306;
        }
        return 18306;
    }
}