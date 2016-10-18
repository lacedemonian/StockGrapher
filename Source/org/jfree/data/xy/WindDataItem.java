/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.xy;

class WindDataItem
implements Comparable {
    private Number x;
    private Number windDir;
    private Number windForce;

    public WindDataItem(Number x, Number windDir, Number windForce) {
        this.x = x;
        this.windDir = windDir;
        this.windForce = windForce;
    }

    public Number getX() {
        return this.x;
    }

    public Number getWindDirection() {
        return this.windDir;
    }

    public Number getWindForce() {
        return this.windForce;
    }

    public int compareTo(Object object) {
        if (object instanceof WindDataItem) {
            WindDataItem item = (WindDataItem)object;
            if (this.x.doubleValue() > item.x.doubleValue()) {
                return 1;
            }
            if (this.x.equals(item.x)) {
                return 0;
            }
            return -1;
        }
        throw new ClassCastException("WindDataItem.compareTo(error)");
    }
}

