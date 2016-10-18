/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.axis;

import java.io.Serializable;

public abstract class TickUnit
implements Comparable,
Serializable {
    private static final long serialVersionUID = 510179855057013974L;
    private double size;

    public TickUnit(double size) {
        this.size = size;
    }

    public double getSize() {
        return this.size;
    }

    public String valueToString(double value) {
        return String.valueOf(value);
    }

    public int compareTo(Object object) {
        if (object instanceof TickUnit) {
            TickUnit other = (TickUnit)object;
            if (this.size > other.getSize()) {
                return 1;
            }
            if (this.size < other.getSize()) {
                return -1;
            }
            return 0;
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof TickUnit) {
            TickUnit tu = (TickUnit)obj;
            return this.size == tu.size;
        }
        return false;
    }

    public int hashCode() {
        long temp = this.size != 0.0 ? Double.doubleToLongBits(this.size) : 0;
        return (int)(temp ^ temp >>> 32);
    }
}

