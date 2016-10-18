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
import org.jfree.data.time.RegularTimePeriod;

public class Minute
extends RegularTimePeriod
implements Serializable {
    private static final long serialVersionUID = 2144572840034842871L;
    public static final int FIRST_MINUTE_IN_HOUR = 0;
    public static final int LAST_MINUTE_IN_HOUR = 59;
    private Hour hour;
    private int minute;

    public Minute() {
        this(new Date());
    }

    public Minute(int minute, Hour hour) {
        if (hour == null) {
            throw new IllegalArgumentException("Null 'hour' argument.");
        }
        this.minute = minute;
        this.hour = hour;
    }

    public Minute(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE);
    }

    public Minute(Date time, TimeZone zone) {
        int min;
        if (time == null) {
            throw new IllegalArgumentException("Null 'time' argument.");
        }
        if (zone == null) {
            throw new IllegalArgumentException("Null 'zone' argument.");
        }
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(time);
        this.minute = min = calendar.get(12);
        this.hour = new Hour(time, zone);
    }

    public Minute(int minute, int hour, int day, int month, int year) {
        this(minute, new Hour(hour, new Day(day, month, year)));
    }

    public Hour getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public RegularTimePeriod previous() {
        Hour prevHour;
        Minute result = this.minute != 0 ? new Minute(this.minute - 1, this.hour) : ((prevHour = (Hour)this.hour.previous()) != null ? new Minute(59, prevHour) : null);
        return result;
    }

    public RegularTimePeriod next() {
        Hour nextHour;
        Minute result = this.minute != 59 ? new Minute(this.minute + 1, this.hour) : ((nextHour = (Hour)this.hour.next()) != null ? new Minute(0, nextHour) : null);
        return result;
    }

    public long getSerialIndex() {
        return this.hour.getSerialIndex() * 60 + (long)this.minute;
    }

    public long getFirstMillisecond(Calendar calendar) {
        int year = this.hour.getDay().getYear();
        int month = this.hour.getDay().getMonth() - 1;
        int day = this.hour.getDay().getDayOfMonth();
        calendar.clear();
        calendar.set(year, month, day, this.hour.getHour(), this.minute, 0);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }

    public long getLastMillisecond(Calendar calendar) {
        int year = this.hour.getDay().getYear();
        int month = this.hour.getDay().getMonth() - 1;
        int day = this.hour.getDay().getDayOfMonth();
        calendar.clear();
        calendar.set(year, month, day, this.hour.getHour(), this.minute, 59);
        calendar.set(14, 999);
        return calendar.getTime().getTime();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Minute)) {
            return false;
        }
        Minute that = (Minute)obj;
        if (this.minute != that.minute) {
            return false;
        }
        if (!this.hour.equals(that.hour)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.minute;
        result = 37 * result + this.hour.hashCode();
        return result;
    }

    public int compareTo(Object o1) {
        int result;
        if (o1 instanceof Minute) {
            Minute m = (Minute)o1;
            result = this.getHour().compareTo(m.getHour());
            if (result == 0) {
                result = this.minute - m.getMinute();
            }
        } else {
            result = o1 instanceof RegularTimePeriod ? 0 : 1;
        }
        return result;
    }

    public static Minute parseMinute(String s) {
        Minute result = null;
        String daystr = (s = s.trim()).substring(0, Math.min(10, s.length()));
        Day day = Day.parseDay(daystr);
        if (day != null) {
            String minstr;
            int minute;
            String hmstr = s.substring(Math.min(daystr.length() + 1, s.length()), s.length());
            String hourstr = (hmstr = hmstr.trim()).substring(0, Math.min(2, hmstr.length()));
            int hour = Integer.parseInt(hourstr);
            if (hour >= 0 && hour <= 23 && (minute = Integer.parseInt(minstr = hmstr.substring(Math.min(hourstr.length() + 1, hmstr.length()), hmstr.length()))) >= 0 && minute <= 59) {
                result = new Minute(minute, new Hour(hour, day));
            }
        }
        return result;
    }
}

