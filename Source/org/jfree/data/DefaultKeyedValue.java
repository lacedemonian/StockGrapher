/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data;

import java.io.Serializable;
import org.jfree.data.KeyedValue;
import org.jfree.util.PublicCloneable;

public class DefaultKeyedValue
implements KeyedValue,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -7388924517460437712L;
    private Comparable key;
    private Number value;

    public DefaultKeyedValue(Comparable key, Number value) {
        this.key = key;
        this.value = value;
    }

    public Comparable getKey() {
        return this.key;
    }

    public Number getValue() {
        return this.value;
    }

    public synchronized void setValue(Number value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DefaultKeyedValue)) {
            return false;
        }
        DefaultKeyedValue that = (DefaultKeyedValue)obj;
        if (this.key != null ? !this.key.equals(that.key) : that.key != null) {
            return false;
        }
        if (this.value != null ? !this.value.equals(that.value) : that.value != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.key != null ? this.key.hashCode() : 0;
        result = 29 * result + (this.value != null ? this.value.hashCode() : 0);
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        DefaultKeyedValue clone = (DefaultKeyedValue)super.clone();
        return clone;
    }
}

