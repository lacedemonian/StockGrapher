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

public class Quarter
extends RegularTimePeriod
implements Serializable {
    private static final long serialVersionUID = 3810061714380888671L;
    public static final int FIRST_QUARTER = 1;
    public static final int LAST_QUARTER = 4;
    public static final int[] FIRST_MONTH_IN_QUARTER = new int[]{0, 1, 4, 7, 10};
    public static final int[] LAST_MONTH_IN_QUARTER = new int[]{0, 3, 6, 9, 12};
    private Year year;
    private int quarter;

    public Quarter() {
        this(new Date());
    }

    public Quarter(int quarter, int year) {
        this(quarter, new Year(year));
    }

    public Quarter(int quarter, Year year) {
        if (quarter < 1 || quarter > 4) {
            throw new IllegalArgumentException("Quarter outside valid range.");
        }
        this.year = year;
        this.quarter = quarter;
    }

    public Quarter(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE);
    }

    public Quarter(Date time, TimeZone zone) {
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(time);
        int month = calendar.get(2) + 1;
        this.quarter = SerialDate.monthCodeToQuarter(month);
        this.year = new Year(calendar.get(1));
    }

    public int getQuarter() {
        return this.quarter;
    }

    public Year getYear() {
        return this.year;
    }

    public RegularTimePeriod previous() {
        Year prevYear;
        Quarter result = this.quarter > 1 ? new Quarter(this.quarter - 1, this.year) : ((prevYear = (Year)this.year.previous()) != null ? new Quarter(4, prevYear) : null);
        return result;
    }

    public RegularTimePeriod next() {
        Year nextYear;
        Quarter result = this.quarter < 4 ? new Quarter(this.quarter + 1, this.year) : ((nextYear = (Year)this.year.next()) != null ? new Quarter(1, nextYear) : null);
        return result;
    }

    public long getSerialIndex() {
        return (long)this.year.getYear() * 4 + (long)this.quarter;
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Quarter) {
                Quarter target = (Quarter)obj;
                return this.quarter == target.getQuarter() && this.year.equals(target.getYear());
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.quarter;
        result = 37 * result + this.year.hashCode();
        return result;
    }

    public int compareTo(Object o1) {
        int result;
        if (o1 instanceof Quarter) {
            Quarter q = (Quarter)o1;
            result = this.year.getYear() - q.getYear().getYear();
            if (result == 0) {
                result = this.quarter - q.getQuarter();
            }
        } else {
            result = o1 instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    public String toString() {
        return "Q" + this.quarter + "/" + this.year;
    }

    public long getFirstMillisecond(Calendar calendar) {
        int month = FIRST_MONTH_IN_QUARTER[this.quarter];
        Day first = new Day(1, month, this.year.getYear());
        return first.getFirstMillisecond(calendar);
    }

    public long getLastMillisecond(Calendar calendar) {
        int month = LAST_MONTH_IN_QUARTER[this.quarter];
        int eom = SerialDate.lastDayOfMonth(month, this.year.getYear());
        Day last = new Day(eom, month, this.year.getYear());
        return last.getLastMillisecond(calendar);
    }

    public static Quarter parseQuarter(String s) {
        int i = s.indexOf("Q");
        if (i == -1) {
            throw new TimePeriodFormatException("Missing Q.");
        }
        if (i == s.length() - 1) {
            throw new TimePeriodFormatException("Q found at end of string.");
        }
        String qstr = s.substring(i + 1, i + 2);
        int quarter = Integer.parseInt(qstr);
        String remaining = s.substring(0, i) + s.substring(i + 2, s.length());
        remaining = remaining.replace('/', ' ');
        remaining = remaining.replace(',', ' ');
        remaining = remaining.replace('-', ' ');
        Year year = Year.parseYear(remaining.trim());
        Quarter result = new Quarter(quarter, year);
        return result;
    }
}

