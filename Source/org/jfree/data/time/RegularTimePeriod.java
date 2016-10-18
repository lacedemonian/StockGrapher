/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jfree.data.time.TimePeriod;
import org.jfree.date.MonthConstants;

public abstract class RegularTimePeriod
implements TimePeriod,
Comparable,
MonthConstants {
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
    public static final Calendar WORKING_CALENDAR = Calendar.getInstance(DEFAULT_TIME_ZONE);
    static /* synthetic */ Class class$java$util$Date;
    static /* synthetic */ Class class$java$util$TimeZone;
    static /* synthetic */ Class class$org$jfree$data$time$Year;
    static /* synthetic */ Class class$org$jfree$data$time$Quarter;
    static /* synthetic */ Class class$org$jfree$data$time$Month;
    static /* synthetic */ Class class$org$jfree$data$time$Day;
    static /* synthetic */ Class class$org$jfree$data$time$Hour;
    static /* synthetic */ Class class$org$jfree$data$time$Minute;
    static /* synthetic */ Class class$org$jfree$data$time$Second;
    static /* synthetic */ Class class$org$jfree$data$time$Millisecond;

    public static RegularTimePeriod createInstance(Class c, Date millisecond, TimeZone zone) {
        RegularTimePeriod result = null;
        try {
            Class[] arrclass = new Class[2];
            Class class_ = class$java$util$Date == null ? (RegularTimePeriod.class$java$util$Date = RegularTimePeriod.class$("java.util.Date")) : class$java$util$Date;
            arrclass[0] = class_;
            Class class_2 = class$java$util$TimeZone == null ? (RegularTimePeriod.class$java$util$TimeZone = RegularTimePeriod.class$("java.util.TimeZone")) : class$java$util$TimeZone;
            arrclass[1] = class_2;
            Constructor constructor = c.getDeclaredConstructor(arrclass);
            result = (RegularTimePeriod)constructor.newInstance(millisecond, zone);
        }
        catch (Exception e) {
            // empty catch block
        }
        return result;
    }

    public static Class downsize(Class c) {
        Class class_ = class$org$jfree$data$time$Year == null ? (RegularTimePeriod.class$org$jfree$data$time$Year = RegularTimePeriod.class$("org.jfree.data.time.Year")) : class$org$jfree$data$time$Year;
        if (c.equals(class_)) {
            Class class_2 = class$org$jfree$data$time$Quarter == null ? (RegularTimePeriod.class$org$jfree$data$time$Quarter = RegularTimePeriod.class$("org.jfree.data.time.Quarter")) : class$org$jfree$data$time$Quarter;
            return class_2;
        }
        Class class_3 = class$org$jfree$data$time$Quarter == null ? (RegularTimePeriod.class$org$jfree$data$time$Quarter = RegularTimePeriod.class$("org.jfree.data.time.Quarter")) : class$org$jfree$data$time$Quarter;
        if (c.equals(class_3)) {
            Class class_4 = class$org$jfree$data$time$Month == null ? (RegularTimePeriod.class$org$jfree$data$time$Month = RegularTimePeriod.class$("org.jfree.data.time.Month")) : class$org$jfree$data$time$Month;
            return class_4;
        }
        Class class_5 = class$org$jfree$data$time$Month == null ? (RegularTimePeriod.class$org$jfree$data$time$Month = RegularTimePeriod.class$("org.jfree.data.time.Month")) : class$org$jfree$data$time$Month;
        if (c.equals(class_5)) {
            Class class_6 = class$org$jfree$data$time$Day == null ? (RegularTimePeriod.class$org$jfree$data$time$Day = RegularTimePeriod.class$("org.jfree.data.time.Day")) : class$org$jfree$data$time$Day;
            return class_6;
        }
        Class class_7 = class$org$jfree$data$time$Day == null ? (RegularTimePeriod.class$org$jfree$data$time$Day = RegularTimePeriod.class$("org.jfree.data.time.Day")) : class$org$jfree$data$time$Day;
        if (c.equals(class_7)) {
            Class class_8 = class$org$jfree$data$time$Hour == null ? (RegularTimePeriod.class$org$jfree$data$time$Hour = RegularTimePeriod.class$("org.jfree.data.time.Hour")) : class$org$jfree$data$time$Hour;
            return class_8;
        }
        Class class_9 = class$org$jfree$data$time$Hour == null ? (RegularTimePeriod.class$org$jfree$data$time$Hour = RegularTimePeriod.class$("org.jfree.data.time.Hour")) : class$org$jfree$data$time$Hour;
        if (c.equals(class_9)) {
            Class class_10 = class$org$jfree$data$time$Minute == null ? (RegularTimePeriod.class$org$jfree$data$time$Minute = RegularTimePeriod.class$("org.jfree.data.time.Minute")) : class$org$jfree$data$time$Minute;
            return class_10;
        }
        Class class_11 = class$org$jfree$data$time$Minute == null ? (RegularTimePeriod.class$org$jfree$data$time$Minute = RegularTimePeriod.class$("org.jfree.data.time.Minute")) : class$org$jfree$data$time$Minute;
        if (c.equals(class_11)) {
            Class class_12 = class$org$jfree$data$time$Second == null ? (RegularTimePeriod.class$org$jfree$data$time$Second = RegularTimePeriod.class$("org.jfree.data.time.Second")) : class$org$jfree$data$time$Second;
            return class_12;
        }
        Class class_13 = class$org$jfree$data$time$Second == null ? (RegularTimePeriod.class$org$jfree$data$time$Second = RegularTimePeriod.class$("org.jfree.data.time.Second")) : class$org$jfree$data$time$Second;
        if (c.equals(class_13)) {
            Class class_14 = class$org$jfree$data$time$Millisecond == null ? (RegularTimePeriod.class$org$jfree$data$time$Millisecond = RegularTimePeriod.class$("org.jfree.data.time.Millisecond")) : class$org$jfree$data$time$Millisecond;
            return class_14;
        }
        Class class_15 = class$org$jfree$data$time$Millisecond == null ? (RegularTimePeriod.class$org$jfree$data$time$Millisecond = RegularTimePeriod.class$("org.jfree.data.time.Millisecond")) : class$org$jfree$data$time$Millisecond;
        return class_15;
    }

    public abstract RegularTimePeriod previous();

    public abstract RegularTimePeriod next();

    public abstract long getSerialIndex();

    public Date getStart() {
        return new Date(this.getFirstMillisecond());
    }

    public Date getEnd() {
        return new Date(this.getLastMillisecond());
    }

    public long getFirstMillisecond() {
        return this.getFirstMillisecond(DEFAULT_TIME_ZONE);
    }

    public long getFirstMillisecond(TimeZone zone) {
        WORKING_CALENDAR.setTimeZone(zone);
        return this.getFirstMillisecond(WORKING_CALENDAR);
    }

    public abstract long getFirstMillisecond(Calendar var1);

    public long getLastMillisecond() {
        return this.getLastMillisecond(DEFAULT_TIME_ZONE);
    }

    public long getLastMillisecond(TimeZone zone) {
        WORKING_CALENDAR.setTimeZone(zone);
        return this.getLastMillisecond(WORKING_CALENDAR);
    }

    public abstract long getLastMillisecond(Calendar var1);

    public long getMiddleMillisecond() {
        long m1 = this.getFirstMillisecond();
        long m2 = this.getLastMillisecond();
        return m1 + (m2 - m1) / 2;
    }

    public long getMiddleMillisecond(TimeZone zone) {
        long m1 = this.getFirstMillisecond(zone);
        long m2 = this.getLastMillisecond(zone);
        return m1 + (m2 - m1) / 2;
    }

    public long getMiddleMillisecond(Calendar calendar) {
        long m1 = this.getFirstMillisecond(calendar);
        long m2 = this.getLastMillisecond(calendar);
        return m1 + (m2 - m1) / 2;
    }

    public String toString() {
        return String.valueOf(this.getStart());
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

