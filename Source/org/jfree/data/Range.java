/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data;

import java.io.Serializable;

public strictfp class Range
implements Serializable {
    private static final long serialVersionUID = -906333695431863380L;
    private double lower;
    private double upper;

    public Range(double lower, double upper) {
        if (lower > upper) {
            String msg = "Range(double, double): require lower (" + lower + ") <= upper (" + upper + ").";
            throw new IllegalArgumentException(msg);
        }
        this.lower = lower;
        this.upper = upper;
    }

    public double getLowerBound() {
        return this.lower;
    }

    public double getUpperBound() {
        return this.upper;
    }

    public double getLength() {
        return this.upper - this.lower;
    }

    public double getCentralValue() {
        return this.lower / 2.0 + this.upper / 2.0;
    }

    public boolean contains(double value) {
        return value >= this.lower && value <= this.upper;
    }

    public boolean intersects(double b0, double b1) {
        if (b0 <= this.lower) {
            return b1 > this.lower;
        }
        return b0 < this.upper && b1 >= b0;
    }

    public double constrain(double value) {
        double result = value;
        if (!this.contains(value)) {
            if (value > this.upper) {
                result = this.upper;
            } else if (value < this.lower) {
                result = this.lower;
            }
        }
        return result;
    }

    public static Range combine(Range range1, Range range2) {
        if (range1 == null) {
            return range2;
        }
        if (range2 == null) {
            return range1;
        }
        double l = Math.min(range1.getLowerBound(), range2.getLowerBound());
        double u = Math.max(range1.getUpperBound(), range2.getUpperBound());
        return new Range(l, u);
    }

    public static Range expandToInclude(Range range, double value) {
        if (range == null) {
            return new Range(value, value);
        }
        if (value < range.getLowerBound()) {
            return new Range(value, range.getUpperBound());
        }
        if (value > range.getUpperBound()) {
            return new Range(range.getLowerBound(), value);
        }
        return range;
    }

    public static Range expand(Range range, double lowerMargin, double upperMargin) {
        if (range == null) {
            throw new IllegalArgumentException("Null 'range' argument.");
        }
        double length = range.getLength();
        double lower = length * lowerMargin;
        double upper = length * upperMargin;
        return new Range(range.getLowerBound() - lower, range.getUpperBound() + upper);
    }

    public static Range shift(Range base, double delta) {
        return Range.shift(base, delta, false);
    }

    public static Range shift(Range base, double delta, boolean allowZeroCrossing) {
        if (allowZeroCrossing) {
            return new Range(base.getLowerBound() + delta, base.getUpperBound() + delta);
        }
        return new Range(Range.shiftWithNoZeroCrossing(base.getLowerBound(), delta), Range.shiftWithNoZeroCrossing(base.getUpperBound(), delta));
    }

    private static double shiftWithNoZeroCrossing(double value, double delta) {
        if (value > 0.0) {
            return Math.max(value + delta, 0.0);
        }
        if (value < 0.0) {
            return Math.min(value + delta, 0.0);
        }
        return value + delta;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Range)) {
            return false;
        }
        Range range = (Range)obj;
        if (this.lower != range.lower) {
            return false;
        }
        if (this.upper != range.upper) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.lower);
        int result = (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.upper);
        result = 29 * result + (int)(temp ^ temp >>> 32);
        return result;
    }

    public String toString() {
        return "Range[" + this.lower + "," + this.upper + "]";
    }
}

