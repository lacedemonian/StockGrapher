/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;

public class Millisecond
extends RegularTimePeriod
implements Serializable {
    static final long serialVersionUID = -5316836467277638485L;
    public static final int FIRST_MILLISECOND_IN_SECOND = 0;
    public static final int LAST_MILLISECOND_IN_SECOND = 999;
    private int millisecond;
    private Second second;

    public Millisecond() {
        this(new Date());
    }

    public Millisecond(int millisecond, Second second) {
        this.millisecond = millisecond;
        this.second = second;
    }

    public Millisecond(int millisecond, int second, int minute, int hour, int day, int month, int year) {
        this(millisecond, new Second(second, minute, hour, day, month, year));
    }

    public Millisecond(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE);
    }

    public Millisecond(Date time, TimeZone zone) {
        this.second = new Second(time, zone);
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(time);
        this.millisecond = calendar.get(14);
    }

    public Second getSecond() {
        return this.second;
    }

    public long getMillisecond() {
        return this.millisecond;
    }

    public RegularTimePeriod previous() {
        Millisecond result = null;
        if (this.millisecond != 0) {
            result = new Millisecond(this.millisecond - 1, this.second);
        } else {
            Second previous = (Second)this.second.previous();
            if (previous != null) {
                result = new Millisecond(999, previous);
            }
        }
        return result;
    }

    public RegularTimePeriod next() {
        Millisecond result = null;
        if (this.millisecond != 999) {
            result = new Millisecond(this.millisecond + 1, this.second);
        } else {
            Second next = (Second)this.second.next();
            if (next != null) {
                result = new Millisecond(0, next);
            }
        }
        return result;
    }

    public long getSerialIndex() {
        return this.second.getSerialIndex() * 1000 + (long)this.millisecond;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Millisecond) {
            Millisecond m = (Millisecond)obj;
            return (long)this.millisecond == m.getMillisecond() && this.second.equals(m.getSecond());
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.millisecond;
        result = 37 * result + this.second.hashCode();
        return result;
    }

    public int compareTo(Object obj) {
        int result;
        if (obj instanceof Millisecond) {
            Millisecond ms = (Millisecond)obj;
            long difference = this.getFirstMillisecond() - ms.getFirstMillisecond();
            result = difference > 0 ? 1 : (difference < 0 ? -1 : 0);
        } else {
            result = obj instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    public long getFirstMillisecond() {
        return this.second.getFirstMillisecond() + (long)this.millisecond;
    }

    public long getFirstMillisecond(Calendar calendar) {
        return this.second.getFirstMillisecond(calendar) + (long)this.millisecond;
    }

    public long getLastMillisecond() {
        return this.second.getFirstMillisecond() + (long)this.millisecond;
    }

    public long getLastMillisecond(Calendar calendar) {
        return this.second.getFirstMillisecond(calendar) + (long)this.millisecond;
    }
}

