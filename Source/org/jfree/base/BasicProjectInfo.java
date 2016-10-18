/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.base;

import java.util.ArrayList;
import java.util.List;
import org.jfree.base.Library;

public class BasicProjectInfo
extends Library {
    private String copyright;
    private List libraries = new ArrayList();

    public BasicProjectInfo() {
    }

    public BasicProjectInfo(String name, String version, String licence, String info) {
        this();
        this.setName(name);
        this.setVersion(version);
        this.setLicenceName(licence);
        this.setInfo(info);
    }

    public BasicProjectInfo(String name, String version, String info, String copyright, String licenceName) {
        this(name, version, licenceName, info);
        this.setCopyright(copyright);
    }

    public String getCopyright() {
        return this.copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setInfo(String info) {
        super.setInfo(info);
    }

    public void setLicenceName(String licence) {
        super.setLicenceName(licence);
    }

    public void setName(String name) {
        super.setName(name);
    }

    public void setVersion(String version) {
        super.setVersion(version);
    }

    public Library[] getLibraries() {
        return this.libraries.toArray(new Library[this.libraries.size()]);
    }

    public void addLibrary(Library library) {
        if (library == null) {
            throw new NullPointerException();
        }
        this.libraries.add(library);
    }
}

