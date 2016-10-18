/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.base;

import org.jfree.JCommon;
import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.base.modules.PackageManager;
import org.jfree.ui.about.ProjectInfo;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

public class BaseBoot
extends AbstractBoot {
    private static BaseBoot singleton;
    private BootableProjectInfo bootableProjectInfo = JCommon.INFO;
    static /* synthetic */ Class class$org$jfree$base$log$DefaultLogModule;

    private BaseBoot() {
    }

    public static ModifiableConfiguration getConfiguration() {
        return (ModifiableConfiguration)BaseBoot.getInstance().getGlobalConfig();
    }

    protected synchronized Configuration loadConfiguration() {
        return this.createDefaultHierarchicalConfiguration("/org/jfree/base/jcommon.properties", "/jcommon.properties", true);
    }

    public static synchronized AbstractBoot getInstance() {
        if (singleton == null) {
            singleton = new BaseBoot();
        }
        return singleton;
    }

    protected void performBoot() {
        Class class_ = class$org$jfree$base$log$DefaultLogModule == null ? (BaseBoot.class$org$jfree$base$log$DefaultLogModule = BaseBoot.class$("org.jfree.base.log.DefaultLogModule")) : class$org$jfree$base$log$DefaultLogModule;
        this.getPackageManager().addModule(class_.getName());
        this.getPackageManager().load("org.jfree.base.");
        this.getPackageManager().initializeModules();
    }

    protected BootableProjectInfo getProjectInfo() {
        return this.bootableProjectInfo;
    }

    public static void main(String[] args) {
        BaseBoot.getInstance().start();
        Log.debug("Hello world");
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

