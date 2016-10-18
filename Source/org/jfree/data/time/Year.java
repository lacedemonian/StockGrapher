/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimePeriodFormatException;

public class Year
extends RegularTimePeriod
implements Serializable {
    private static final long serialVersionUID = -7659990929736074836L;
    private int year;

    public Year() {
        this(new Date());
    }

    public Year(int year) {
        if (year < 1900 || year > 9999) {
            throw new IllegalArgumentException("Year constructor: year (" + year + ") outside valid range.");
        }
        this.year = year;
    }

    public Year(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE);
    }

    public Year(Date time, TimeZone zone) {
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(time);
        this.year = calendar.get(1);
    }

    public int getYear() {
        return this.year;
    }

    public RegularTimePeriod previous() {
        if (this.year > 1900) {
            return new Year(this.year - 1);
        }
        return null;
    }

    public RegularTimePeriod next() {
        if (this.year < 9999) {
            return new Year(this.year + 1);
        }
        return null;
    }

    public long getSerialIndex() {
        return this.year;
    }

    public long getFirstMillisecond(Calendar calendar) {
        Day jan1 = new Day(1, 1, this.year);
        return jan1.getFirstMillisecond(calendar);
    }

    public long getLastMillisecond(Calendar calendar) {
        Day dec31 = new Day(31, 12, this.year);
        return dec31.getLastMillisecond(calendar);
    }

    public boolean equals(Object object) {
        if (object != null) {
            if (object instanceof Year) {
                Year target = (Year)object;
                return this.year == target.getYear();
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        int c = this.year;
        result = 37 * result + c;
        return result;
    }

    public int compareTo(Object o1) {
        int result;
        if (o1 instanceof Year) {
            Year y = (Year)o1;
            result = this.year - y.getYear();
        } else {
            result = o1 instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    public String toString() {
        return Integer.toString(this.year);
    }

    public static Year parseYear(String s) {
        int y;
        try {
            y = Integer.parseInt(s.trim());
        }
        catch (NumberFormatException e) {
            throw new TimePeriodFormatException("Cannot parse string.");
        }
        try {
            return new Year(y);
        }
        catch (IllegalArgumentException e) {
            throw new TimePeriodFormatException("Year outside valid range.");
        }
    }
}

