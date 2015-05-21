package com.kaichunlin.transition;

/**
 * Configuration that affects the whole framework
 * <p/>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public class TransitionConfig {
    static boolean _debug;

    /**
     * @param debug set debugging
     */
    public static void setDebug(boolean debug) {
        _debug = debug;
    }

    /**
     * @return is debugging enabled
     */
    public static boolean isDebug() {
        return _debug;
    }
}
