/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimePeriodFormatException;
import org.jfree.data.time.Year;

public class Week
extends RegularTimePeriod
implements Serializable {
    private static final long serialVersionUID = 1856387786939865061L;
    public static final int FIRST_WEEK_IN_YEAR = 1;
    public static final int LAST_WEEK_IN_YEAR = 53;
    private Year year;
    private int week;

    public Week() {
        this(new Date());
    }

    public Week(int week, int year) {
        this(week, new Year(year));
    }

    public Week(int week, Year year) {
        if (week < 1 && week > 53) {
            throw new IllegalArgumentException("The 'week' argument must be in the range 1 - 53.");
        }
        this.week = week;
        this.year = year;
    }

    public Week(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE);
    }

    public Week(Date time, TimeZone zone) {
        if (time == null) {
            throw new IllegalArgumentException("Null 'time' argument.");
        }
        if (zone == null) {
            throw new IllegalArgumentException("Null 'zone' argument.");
        }
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(time);
        int tempWeek = calendar.get(3);
        if (tempWeek == 1 && calendar.get(2) == 11) {
            this.week = 1;
            this.year = new Year(calendar.get(1) + 1);
        } else {
            this.week = Math.min(tempWeek, 53);
            this.year = new Year(calendar.get(1));
        }
    }

    public Year getYear() {
        return this.year;
    }

    public int getYearValue() {
        return this.year.getYear();
    }

    public int getWeek() {
        return this.week;
    }

    public RegularTimePeriod previous() {
        Week result;
        if (this.week != 1) {
            result = new Week(this.week - 1, this.year);
        } else {
            Year prevYear = (Year)this.year.previous();
            if (prevYear != null) {
                int yy = prevYear.getYear();
                Calendar prevYearCalendar = Calendar.getInstance();
                prevYearCalendar.set(yy, 11, 31);
                result = new Week(prevYearCalendar.getActualMaximum(3), prevYear);
            } else {
                result = null;
            }
        }
        return result;
    }

    public RegularTimePeriod next() {
        Week result;
        if (this.week < 52) {
            result = new Week(this.week + 1, this.year);
        } else {
            Year nextYear;
            Calendar calendar = Calendar.getInstance();
            calendar.set(this.year.getYear(), 11, 31);
            int actualMaxWeek = calendar.getActualMaximum(3);
            result = this.week != actualMaxWeek ? new Week(this.week + 1, this.year) : ((nextYear = (Year)this.year.next()) != null ? new Week(1, nextYear) : null);
        }
        return result;
    }

    public long getSerialIndex() {
        return (long)this.year.getYear() * 53 + (long)this.week;
    }

    public long getFirstMillisecond(Calendar calendar) {
        Calendar c = (Calendar)calendar.clone();
        c.clear();
        c.set(1, this.year.getYear());
        c.set(3, this.week);
        c.set(7, c.getFirstDayOfWeek());
        c.set(10, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTime().getTime();
    }

    public long getLastMillisecond(Calendar calendar) {
        RegularTimePeriod next = this.next();
        return next.getFirstMillisecond(calendar) - 1;
    }

    public String toString() {
        return "Week " + this.week + ", " + this.year;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Week)) {
            return false;
        }
        Week that = (Week)obj;
        if (this.week != that.week) {
            return false;
        }
        if (!this.year.equals(that.year)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.week;
        result = 37 * result + this.year.hashCode();
        return result;
    }

    public int compareTo(Object o1) {
        int result;
        if (o1 instanceof Week) {
            Week w = (Week)o1;
            result = this.year.getYear() - w.getYear().getYear();
            if (result == 0) {
                result = this.week - w.getWeek();
            }
        } else {
            result = o1 instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Week parseWeek(String s) {
        Week result = null;
        if (s == null) return result;
        int i = Week.findSeparator(s = s.trim());
        if (i == -1) throw new TimePeriodFormatException("Could not find separator.");
        String s1 = s.substring(0, i).trim();
        String s2 = s.substring(i + 1, s.length()).trim();
        Year y = Week.evaluateAsYear(s1);
        if (y != null) {
            int w = Week.stringToWeek(s2);
            if (w != -1) return new Week(w, y);
            throw new TimePeriodFormatException("Can't evaluate the week.");
        }
        y = Week.evaluateAsYear(s2);
        if (y == null) throw new TimePeriodFormatException("Can't evaluate the year.");
        int w = Week.stringToWeek(s1);
        if (w != -1) return new Week(w, y);
        throw new TimePeriodFormatException("Can't evaluate the week.");
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

    private static int stringToWeek(String s) {
        int result = -1;
        s = s.replace('W', ' ');
        s = s.trim();
        try {
            result = Integer.parseInt(s);
            if (result < 1 || result > 53) {
                result = -1;
            }
        }
        catch (NumberFormatException e) {
            // empty catch block
        }
        return result;
    }
}

