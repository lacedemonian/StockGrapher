/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.base.log;

import org.jfree.base.log.DefaultLog;
import org.jfree.base.log.LogConfiguration;
import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.LogTarget;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PrintStreamLogTarget;

public class DefaultLogModule
extends AbstractModule {
    static /* synthetic */ Class class$org$jfree$util$PrintStreamLogTarget;

    public DefaultLogModule() throws ModuleInitializeException {
        this.loadModuleInfo();
    }

    public void initialize(SubSystem subSystem) throws ModuleInitializeException {
        if (LogConfiguration.isDisableLogging()) {
            return;
        }
        Class class_ = class$org$jfree$util$PrintStreamLogTarget == null ? (DefaultLogModule.class$org$jfree$util$PrintStreamLogTarget = DefaultLogModule.class$("org.jfree.util.PrintStreamLogTarget")) : class$org$jfree$util$PrintStreamLogTarget;
        if (LogConfiguration.getLogTarget().equals(class_.getName())) {
            DefaultLog.installDefaultLog();
            Log.getInstance().addTarget(new PrintStreamLogTarget());
            Log.info("Default log target started ... previous log messages could have been ignored.");
        } else if ("true".equals(subSystem.getGlobalConfig().getConfigProperty("org.jfree.base.LogAutoInit"))) {
            DefaultLog.installDefaultLog();
            LogTarget lt = (LogTarget)ObjectUtilities.loadAndInstantiate(LogConfiguration.getLogTarget(), this.getClass());
            Log.getInstance().addTarget(lt);
        }
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

