/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.base;

import java.util.ArrayList;
import org.jfree.base.BasicProjectInfo;

public class BootableProjectInfo
extends BasicProjectInfo {
    private ArrayList dependencies = new ArrayList();
    private String bootClass;
    private boolean autoBoot = true;

    public BootableProjectInfo() {
    }

    public BootableProjectInfo(String name, String version, String licence, String info) {
        this();
        this.setName(name);
        this.setVersion(version);
        this.setLicenceName(licence);
        this.setInfo(info);
    }

    public BootableProjectInfo(String name, String version, String info, String copyright, String licenceName) {
        this();
        this.setName(name);
        this.setVersion(version);
        this.setLicenceName(licenceName);
        this.setInfo(info);
        this.setCopyright(copyright);
    }

    public BootableProjectInfo[] getDependencies() {
        return this.dependencies.toArray(new BootableProjectInfo[this.dependencies.size()]);
    }

    public void addDependency(BootableProjectInfo projectInfo) {
        if (projectInfo == null) {
            throw new NullPointerException();
        }
        this.dependencies.add(projectInfo);
    }

    public String getBootClass() {
        return this.bootClass;
    }

    public void setBootClass(String bootClass) {
        this.bootClass = bootClass;
    }

    public boolean isAutoBoot() {
        return this.autoBoot;
    }

    public void setAutoBoot(boolean autoBoot) {
        this.autoBoot = autoBoot;
    }
}

