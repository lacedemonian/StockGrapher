/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.jfree.data.time.RegularTimePeriod;

public class FixedMillisecond
extends RegularTimePeriod
implements Serializable {
    private static final long serialVersionUID = 7867521484545646931L;
    private Date time;

    public FixedMillisecond() {
        this(new Date());
    }

    public FixedMillisecond(long millisecond) {
        this(new Date(millisecond));
    }

    public FixedMillisecond(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return this.time;
    }

    public RegularTimePeriod previous() {
        FixedMillisecond result = null;
        long t = this.time.getTime();
        if (t != Long.MIN_VALUE) {
            result = new FixedMillisecond(t - 1);
        }
        return result;
    }

    public RegularTimePeriod next() {
        FixedMillisecond result = null;
        long t = this.time.getTime();
        if (t != Long.MAX_VALUE) {
            result = new FixedMillisecond(t + 1);
        }
        return result;
    }

    public boolean equals(Object object) {
        if (object instanceof FixedMillisecond) {
            FixedMillisecond m = (FixedMillisecond)object;
            return this.time.equals(m.getTime());
        }
        return false;
    }

    public int hashCode() {
        return this.time.hashCode();
    }

    public int compareTo(Object o1) {
        int result;
        if (o1 instanceof FixedMillisecond) {
            FixedMillisecond t1 = (FixedMillisecond)o1;
            long difference = this.time.getTime() - t1.time.getTime();
            result = difference > 0 ? 1 : (difference < 0 ? -1 : 0);
        } else {
            result = o1 instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    public long getFirstMillisecond() {
        return this.time.getTime();
    }

    public long getFirstMillisecond(Calendar calendar) {
        return this.time.getTime();
    }

    public long getLastMillisecond() {
        return this.time.getTime();
    }

    public long getLastMillisecond(Calendar calendar) {
        return this.time.getTime();
    }

    public long getMiddleMillisecond() {
        return this.time.getTime();
    }

    public long getMiddleMillisecond(Calendar calendar) {
        return this.time.getTime();
    }

    public long getSerialIndex() {
        return this.time.getTime();
    }
}

