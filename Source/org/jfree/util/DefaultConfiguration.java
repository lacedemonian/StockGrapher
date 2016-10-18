/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import org.jfree.util.Configuration;

public class DefaultConfiguration
extends Properties
implements Configuration {
    public String getConfigProperty(String key) {
        return this.getProperty(key);
    }

    public String getConfigProperty(String key, String defaultValue) {
        return this.getProperty(key, defaultValue);
    }

    public Iterator findPropertyKeys(String prefix) {
        TreeSet<String> collector = new TreeSet<String>();
        Enumeration enum1 = this.keys();
        while (enum1.hasMoreElements()) {
            String key = (String)enum1.nextElement();
            if (!key.startsWith(prefix) || collector.contains(key)) continue;
            collector.add(key);
        }
        return Collections.unmodifiableSet(collector).iterator();
    }

    public Enumeration getConfigProperties() {
        return this.keys();
    }
}

