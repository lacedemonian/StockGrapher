/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.time;

import java.io.PrintStream;
import java.io.Serializable;
import org.jfree.data.time.TimePeriod;

public class TimePeriodValue
implements Cloneable,
Serializable {
    private static final long serialVersionUID = 3390443360845711275L;
    private TimePeriod period;
    private Number value;

    public TimePeriodValue(TimePeriod period, Number value) {
        this.period = period;
        this.value = value;
    }

    public TimePeriodValue(TimePeriod period, double value) {
        this(period, new Double(value));
    }

    public TimePeriod getPeriod() {
        return this.period;
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TimePeriodValue)) {
            return false;
        }
        TimePeriodValue timePeriodValue = (TimePeriodValue)obj;
        if (this.period != null ? !this.period.equals(timePeriodValue.period) : timePeriodValue.period != null) {
            return false;
        }
        if (this.value != null ? !this.value.equals(timePeriodValue.value) : timePeriodValue.value != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.period != null ? this.period.hashCode() : 0;
        result = 29 * result + (this.value != null ? this.value.hashCode() : 0);
        return result;
    }

    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        }
        catch (CloneNotSupportedException e) {
            System.err.println("Operation not supported.");
        }
        return clone;
    }
}

