/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter
extends FileFilter {
    private String description;
    private String extension;

    public ExtensionFileFilter(String description, String extension) {
        this.description = description;
        this.extension = extension;
    }

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String name = file.getName().toLowerCase();
        if (name.endsWith(this.extension)) {
            return true;
        }
        return false;
    }

    public String getDescription() {
        return this.description;
    }
}

