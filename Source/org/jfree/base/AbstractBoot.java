/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.base;

import java.lang.reflect.Method;
import org.jfree.base.BootableProjectInfo;
import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.PropertyFileConfiguration;
import org.jfree.base.config.SystemPropertyConfiguration;
import org.jfree.base.modules.PackageManager;
import org.jfree.base.modules.SubSystem;
import org.jfree.util.Configuration;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.util.ExtendedConfigurationWrapper;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public abstract class AbstractBoot
implements SubSystem {
    private ExtendedConfigurationWrapper extWrapper;
    private PackageManager packageManager;
    private Configuration globalConfig;
    private boolean bootInProgress;
    private boolean bootDone;

    protected AbstractBoot() {
    }

    public synchronized PackageManager getPackageManager() {
        if (this.packageManager == null) {
            this.packageManager = PackageManager.createInstance(this);
        }
        return this.packageManager;
    }

    public synchronized Configuration getGlobalConfig() {
        if (this.globalConfig == null) {
            this.globalConfig = this.loadConfiguration();
            this.start();
        }
        return this.globalConfig;
    }

    public final synchronized boolean isBootInProgress() {
        return this.bootInProgress;
    }

    public final synchronized boolean isBootDone() {
        return this.bootDone;
    }

    protected abstract Configuration loadConfiguration();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void start() {
        BootableProjectInfo[] childs;
        BootableProjectInfo[] arrbootableProjectInfo = this;
        synchronized (arrbootableProjectInfo) {
            if (this.isBootInProgress() || this.isBootDone()) {
                return;
            }
            this.bootInProgress = true;
        }
        BootableProjectInfo info = this.getProjectInfo();
        if (info != null) {
            Log.info(info.getName() + " " + info.getVersion());
            childs = info.getDependencies();
            for (int i = 0; i < childs.length; ++i) {
                AbstractBoot boot = this.loadBooter(childs[i].getBootClass());
                if (boot == null) continue;
                boot.start();
            }
        }
        this.performBoot();
        childs = this;
        synchronized (childs) {
            this.bootInProgress = false;
            this.bootDone = true;
        }
    }

    protected abstract void performBoot();

    protected abstract BootableProjectInfo getProjectInfo();

    protected AbstractBoot loadBooter(String classname) {
        if (classname == null) {
            return null;
        }
        try {
            Class<?> c = ObjectUtilities.getClassLoader(this.getClass()).loadClass(classname);
            Method m = c.getMethod("getInstance", null);
            return (AbstractBoot)m.invoke(null, null);
        }
        catch (Exception e) {
            Log.info("Unable to boot dependent class: " + classname);
            return null;
        }
    }

    protected Configuration createDefaultHierarchicalConfiguration(String staticConfig, String userConfig, boolean addSysProps) {
        HierarchicalConfiguration globalConfig = new HierarchicalConfiguration();
        if (staticConfig != null) {
            PropertyFileConfiguration rootProperty = new PropertyFileConfiguration();
            rootProperty.load(staticConfig);
            globalConfig.insertConfiguration(rootProperty);
            globalConfig.insertConfiguration(this.getPackageManager().getPackageConfiguration());
        }
        if (userConfig != null) {
            PropertyFileConfiguration baseProperty = new PropertyFileConfiguration();
            baseProperty.load(userConfig);
            globalConfig.insertConfiguration(baseProperty);
        }
        SystemPropertyConfiguration systemConfig = new SystemPropertyConfiguration();
        globalConfig.insertConfiguration(systemConfig);
        return globalConfig;
    }

    public synchronized ExtendedConfiguration getExtendedConfig() {
        if (this.extWrapper == null) {
            this.extWrapper = new ExtendedConfigurationWrapper(this.getGlobalConfig());
        }
        return this.extWrapper;
    }
}

