/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.axis;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jfree.chart.axis.TickUnit;
import org.jfree.util.ObjectUtilities;

public class DateTickUnit
extends TickUnit
implements Serializable {
    private static final long serialVersionUID = -7289292157229621901L;
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;
    public static final int MILLISECOND = 6;
    private int unit;
    private int count;
    private int rollUnit;
    private int rollCount;
    private DateFormat formatter;

    public DateTickUnit(int unit, int count) {
        this(unit, count, null);
    }

    public DateTickUnit(int unit, int count, DateFormat formatter) {
        this(unit, count, unit, count, formatter);
    }

    public DateTickUnit(int unit, int count, int rollUnit, int rollCount, DateFormat formatter) {
        super(DateTickUnit.getMillisecondCount(unit, count));
        this.unit = unit;
        this.count = count;
        this.rollUnit = rollUnit;
        this.rollCount = rollCount;
        this.formatter = formatter;
        if (formatter == null) {
            this.formatter = DateFormat.getDateInstance(3);
        }
    }

    public int getUnit() {
        return this.unit;
    }

    public int getCount() {
        return this.count;
    }

    public int getRollUnit() {
        return this.rollUnit;
    }

    public int getRollCount() {
        return this.rollCount;
    }

    public String valueToString(double milliseconds) {
        return this.formatter.format(new Date((long)milliseconds));
    }

    public String dateToString(Date date) {
        return this.formatter.format(date);
    }

    public Date addToDate(Date base) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(base);
        calendar.add(this.getCalendarField(this.unit), this.count);
        return calendar.getTime();
    }

    public Date rollDate(Date base) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(base);
        calendar.add(this.getCalendarField(this.rollUnit), this.rollCount);
        return calendar.getTime();
    }

    public int getCalendarField() {
        return this.getCalendarField(this.unit);
    }

    private int getCalendarField(int tickUnit) {
        switch (tickUnit) {
            case 0: {
                return 1;
            }
            case 1: {
                return 2;
            }
            case 2: {
                return 5;
            }
            case 3: {
                return 11;
            }
            case 4: {
                return 12;
            }
            case 5: {
                return 13;
            }
            case 6: {
                return 14;
            }
        }
        return 14;
    }

    private static long getMillisecondCount(int unit, int count) {
        switch (unit) {
            case 0: {
                return 31536000000L * (long)count;
            }
            case 1: {
                return 2678400000L * (long)count;
            }
            case 2: {
                return 86400000 * (long)count;
            }
            case 3: {
                return 3600000 * (long)count;
            }
            case 4: {
                return 60000 * (long)count;
            }
            case 5: {
                return 1000 * (long)count;
            }
            case 6: {
                return count;
            }
        }
        throw new IllegalArgumentException("DateTickUnit.getMillisecondCount() : unit must be one of the constants YEAR, MONTH, DAY, HOUR, MINUTE, SECOND or MILLISECOND defined in the DateTickUnit class. Do *not* use the constants defined in java.util.Calendar.");
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DateTickUnit)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        DateTickUnit that = (DateTickUnit)obj;
        if (this.unit != that.unit) {
            return false;
        }
        if (this.count != that.count) {
            return false;
        }
        if (!ObjectUtilities.equal(this.formatter, that.formatter)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 19;
        result = 37 * result + this.unit;
        result = 37 * result + this.count;
        result = 37 * result + this.formatter.hashCode();
        return result;
    }
}

