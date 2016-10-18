/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.io.Serializable;
import java.util.Date;
import org.jfree.data.time.TimePeriod;

public class SimpleTimePeriod
implements TimePeriod,
Comparable,
Serializable {
    private static final long serialVersionUID = 8684672361131829554L;
    private Date start;
    private Date end;

    public SimpleTimePeriod(long start, long end) {
        this(new Date(start), new Date(end));
    }

    public SimpleTimePeriod(Date start, Date end) {
        if (start.getTime() > end.getTime()) {
            throw new IllegalArgumentException("Requires end >= start.");
        }
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return this.start;
    }

    public Date getEnd() {
        return this.end;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TimePeriod)) {
            return false;
        }
        TimePeriod that = (TimePeriod)obj;
        if (!this.start.equals(that.getStart())) {
            return false;
        }
        if (!this.end.equals(that.getEnd())) {
            return false;
        }
        return true;
    }

    public int compareTo(Object obj) {
        long t2;
        long t1;
        long t3;
        long m1;
        TimePeriod that = (TimePeriod)obj;
        long t0 = this.getStart().getTime();
        long m0 = t0 + ((t1 = this.getEnd().getTime()) - t0) / 2;
        if (m0 < (m1 = (t2 = that.getStart().getTime()) + ((t3 = that.getEnd().getTime()) - t2) / 2)) {
            return -1;
        }
        if (m0 > m1) {
            return 1;
        }
        if (t0 < t2) {
            return -1;
        }
        if (t0 > t2) {
            return 1;
        }
        if (t1 < t3) {
            return -1;
        }
        if (t1 > t3) {
            return 1;
        }
        return 0;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.start.hashCode();
        result = 37 * result + this.end.hashCode();
        return result;
    }
}

