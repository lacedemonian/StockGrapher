/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.axis;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnitSource;

public class StandardTickUnitSource
implements TickUnitSource {
    private static final double LOG_10_VALUE = Math.log(10.0);

    public TickUnit getLargerTickUnit(TickUnit unit) {
        double x = unit.getSize();
        double log = Math.log(x) / LOG_10_VALUE;
        double higher = Math.ceil(log);
        return new NumberTickUnit(Math.pow(10.0, higher), new DecimalFormat("0.0E0"));
    }

    public TickUnit getCeilingTickUnit(TickUnit unit) {
        return this.getLargerTickUnit(unit);
    }

    public TickUnit getCeilingTickUnit(double size) {
        double log = Math.log(size) / LOG_10_VALUE;
        double higher = Math.ceil(log);
        return new NumberTickUnit(Math.pow(10.0, higher), new DecimalFormat("0.0E0"));
    }
}

