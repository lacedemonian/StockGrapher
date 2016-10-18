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
import org.jfree.data.time.Year;
import org.jfree.date.SerialDate;

public class Month
extends RegularTimePeriod
implements Serializable {
    private static final long serialVersionUID = -5090216912548722570L;
    private int month;
    private Year year;

    public Month() {
        this(new Date());
    }

    public Month(int month, int year) {
        this(month, new Year(year));
    }

    public Month(int month, Year year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month outside valid range.");
        }
        this.month = month;
        this.year = year;
    }

    public Month(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE);
    }

    public Month(Date time, TimeZone zone) {
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(time);
        this.month = calendar.get(2) + 1;
        this.year = new Year(calendar.get(1));
    }

    public Year getYear() {
        return this.year;
    }

    public int getYearValue() {
        return this.year.getYear();
    }

    public int getMonth() {
        return this.month;
    }

    public RegularTimePeriod previous() {
        Year prevYear;
        Month result = this.month != 1 ? new Month(this.month - 1, this.year) : ((prevYear = (Year)this.year.previous()) != null ? new Month(12, prevYear) : null);
        return result;
    }

    public RegularTimePeriod next() {
        Year nextYear;
        Month result = this.month != 12 ? new Month(this.month + 1, this.year) : ((nextYear = (Year)this.year.next()) != null ? new Month(1, nextYear) : null);
        return result;
    }

    public long getSerialIndex() {
        return (long)this.year.getYear() * 12 + (long)this.month;
    }

    public String toString() {
        return SerialDate.monthCodeToString(this.month) + " " + this.year;
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Month) {
                Month target = (Month)obj;
                return this.month == target.getMonth() && this.year.equals(target.getYear());
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.month;
        result = 37 * result + this.year.hashCode();
        return result;
    }

    public int compareTo(Object o1) {
        int result;
        if (o1 instanceof Month) {
            Month m = (Month)o1;
            result = this.year.getYear() - m.getYear().getYear();
            if (result == 0) {
                result = this.month - m.getMonth();
            }
        } else {
            result = o1 instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    public long getFirstMillisecond(Calendar calendar) {
        Day first = new Day(1, this.month, this.year.getYear());
        return first.getFirstMillisecond(calendar);
    }

    public long getLastMillisecond(Calendar calendar) {
        int eom = SerialDate.lastDayOfMonth(this.month, this.year.getYear());
        Day last = new Day(eom, this.month, this.year.getYear());
        return last.getLastMillisecond(calendar);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Month parseMonth(String s) {
        Month result = null;
        if (s == null) return result;
        int i = Month.findSeparator(s = s.trim());
        if (i == -1) throw new TimePeriodFormatException("Could not find separator.");
        String s1 = s.substring(0, i).trim();
        String s2 = s.substring(i + 1, s.length()).trim();
        Year year = Month.evaluateAsYear(s1);
        if (year != null) {
            int month = SerialDate.stringToMonthCode(s2);
            if (month != -1) return new Month(month, year);
            throw new TimePeriodFormatException("Can't evaluate the month.");
        }
        year = Month.evaluateAsYear(s2);
        if (year == null) throw new TimePeriodFormatException("Can't evaluate the year.");
        int month = SerialDate.stringToMonthCode(s1);
        if (month != -1) return new Month(month, year);
        throw new TimePeriodFormatException("Can't evaluate the month.");
    }

    private static int findSeparator(String s) {
        int result = s.indexOf(45);
        if (result == -1) {
            result = s.indexOf(44);
        }
        if (result == -1) {
            result = s.indexOf(32);
        }
        if (result == -1) {
            result = s.indexOf(46);
        }
        return result;
    }

    private static Year evaluateAsYear(String s) {
        Year result = null;
        try {
            result = Year.parseYear(s);
        }
        catch (TimePeriodFormatException e) {
            // empty catch block
        }
        return result;
    }
}

