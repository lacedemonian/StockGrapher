/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.base.log;

import org.jfree.base.BaseBoot;
import org.jfree.util.Configuration;

public class LogConfiguration {
    public static final String DISABLE_LOGGING_DEFAULT = "false";
    public static final String LOGLEVEL = "org.jfree.base.LogLevel";
    public static final String LOGLEVEL_DEFAULT = "Info";
    public static final String LOGTARGET = "org.jfree.base.LogTarget";
    public static final String LOGTARGET_DEFAULT;
    public static final String DISABLE_LOGGING = "org.jfree.base.NoDefaultDebug";
    static /* synthetic */ Class class$org$jfree$util$PrintStreamLogTarget;

    private LogConfiguration() {
    }

    public static String getLogTarget() {
        return BaseBoot.getInstance().getGlobalConfig().getConfigProperty("org.jfree.base.LogTarget", LOGTARGET_DEFAULT);
    }

    public static void setLogTarget(String logTarget) {
        BaseBoot.getConfiguration().setConfigProperty("org.jfree.base.LogTarget", logTarget);
    }

    public static String getLogLevel() {
        return BaseBoot.getInstance().getGlobalConfig().getConfigProperty("org.jfree.base.LogLevel", "Info");
    }

    public static void setLogLevel(String level) {
        BaseBoot.getConfiguration().setConfigProperty("org.jfree.base.LogLevel", level);
    }

    public static boolean isDisableLogging() {
        return BaseBoot.getInstance().getGlobalConfig().getConfigProperty("org.jfree.base.NoDefaultDebug", "false").equalsIgnoreCase("true");
    }

    public static void setDisableLogging(boolean disableLogging) {
        BaseBoot.getConfiguration().setConfigProperty("org.jfree.base.NoDefaultDebug", String.valueOf(disableLogging));
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class class_ = class$org$jfree$util$PrintStreamLogTarget == null ? (LogConfiguration.class$org$jfree$util$PrintStreamLogTarget = LogConfiguration.class$("org.jfree.util.PrintStreamLogTarget")) : class$org$jfree$util$PrintStreamLogTarget;
        LOGTARGET_DEFAULT = class_.getName();
    }
}

