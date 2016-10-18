/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;

public class Second
extends RegularTimePeriod
implements Serializable {
    private static final long serialVersionUID = -6536564190712383466L;
    public static final int FIRST_SECOND_IN_MINUTE = 0;
    public static final int LAST_SECOND_IN_MINUTE = 59;
    private Minute minute;
    private int second;

    public Second() {
        this(new Date());
    }

    public Second(int second, Minute minute) {
        if (minute == null) {
            throw new IllegalArgumentException("Null 'minute' argument.");
        }
        this.minute = minute;
        this.second = second;
    }

    public Second(int second, int minute, int hour, int day, int month, int year) {
        this(second, new Minute(minute, hour, day, month, year));
    }

    public Second(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE);
    }

    public Second(Date time, TimeZone zone) {
        this.minute = new Minute(time, zone);
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(time);
        this.second = calendar.get(13);
    }

    public int getSecond() {
        return this.second;
    }

    public Minute getMinute() {
        return this.minute;
    }

    public RegularTimePeriod previous() {
        Second result = null;
        if (this.second != 0) {
            result = new Second(this.second - 1, this.minute);
        } else {
            Minute previous = (Minute)this.minute.previous();
            if (previous != null) {
                result = new Second(59, previous);
            }
        }
        return result;
    }

    public RegularTimePeriod next() {
        Second result = null;
        if (this.second != 59) {
            result = new Second(this.second + 1, this.minute);
        } else {
            Minute next = (Minute)this.minute.next();
            if (next != null) {
                result = new Second(0, next);
            }
        }
        return result;
    }

    public long getSerialIndex() {
        return this.minute.getSerialIndex() * 60 + (long)this.second;
    }

    public long getFirstMillisecond(Calendar calendar) {
        return this.minute.getFirstMillisecond(calendar) + (long)this.second * 1000;
    }

    public long getLastMillisecond(Calendar calendar) {
        return this.minute.getFirstMillisecond(calendar) + (long)this.second * 1000 + 999;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Second) {
            Second s = (Second)obj;
            return this.second == s.getSecond() && this.minute.equals(s.getMinute());
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.second;
        result = 37 * result + this.minute.hashCode();
        return result;
    }

    public int compareTo(Object o1) {
        int result;
        if (o1 instanceof Second) {
            Second s = (Second)o1;
            result = this.minute.compareTo(s.minute);
            if (result == 0) {
                result = this.second - s.second;
            }
        } else {
            result = o1 instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    public static Second parseSecond(String s) {
        Second result = null;
        String daystr = (s = s.trim()).substring(0, Math.min(10, s.length()));
        Day day = Day.parseDay(daystr);
        if (day != null) {
            int minute;
            String hmsstr = s.substring(Math.min(daystr.length() + 1, s.length()), s.length());
            hmsstr = hmsstr.trim();
            int l = hmsstr.length();
            String hourstr = hmsstr.substring(0, Math.min(2, l));
            String minstr = hmsstr.substring(Math.min(3, l), Math.min(5, l));
            String secstr = hmsstr.substring(Math.min(6, l), Math.min(8, l));
            int hour = Integer.parseInt(hourstr);
            if (hour >= 0 && hour <= 23 && (minute = Integer.parseInt(minstr)) >= 0 && minute <= 59) {
                Minute m = new Minute(minute, new Hour(hour, day));
                int second = Integer.parseInt(secstr);
                if (second >= 0 && second <= 59) {
                    result = new Second(second, m);
                }
            }
        }
        return result;
    }
}

