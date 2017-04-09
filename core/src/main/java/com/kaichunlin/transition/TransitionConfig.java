package com.kaichunlin.transition;

/**
 * Framework-wide debug configuration.
 */
public class TransitionConfig {
    static boolean _debug;
    static boolean _printDebug;

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

    /**
     * @param printDebug set auto printing transition detail messages on transition/animation end
     */
    public static void setPrintDebug(boolean printDebug) {
        _printDebug = printDebug;
    }

    /**
     * @return is auto printing transition detail messages on transition/animation end enabled
     */
    public static boolean isPrintDebug() {
        return _printDebug;
    }
}