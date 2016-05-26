package com.cip.crane.common.utils;

import com.dianping.lion.EnvZooKeeperConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Author   mingdongli
 * 16/3/17  下午8:13.
 */
public class EnvUtils {

    private static final String env;

    static {
        env = EnvZooKeeperConfig.getEnv().trim();
    }

    public static String getEnv() {

        return env;
    }

    public static boolean isDev() {

        return env.equals("dev");
    }

    public static boolean isAlpha() {

        return env.equals("alpha");
    }

    public static boolean isQa() {

        return env.equals("qa");
    }

    public static boolean isPpe() {

        return env.equals("prelease");
    }

    public static boolean isProduct() {

        return env.equals("product");
    }

    public static Set<String> allEnv() {
        Set<String> envs = new HashSet<String>();
        envs.add("dev");
        envs.add("alpha");
        envs.add("qa");
        envs.add("prelease");
        envs.add("product");
        envs.add("performance");
        return envs;
    }

}
