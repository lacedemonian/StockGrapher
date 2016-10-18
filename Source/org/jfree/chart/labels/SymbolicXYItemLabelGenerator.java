/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XisSymbolic;
import org.jfree.data.xy.YisSymbolic;
import org.jfree.util.PublicCloneable;

public class SymbolicXYItemLabelGenerator
implements XYItemLabelGenerator,
XYToolTipGenerator,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 3963400354475494395L;

    public String generateToolTip(XYDataset data, int series, int item) {
        String yStr;
        String xStr;
        if (data instanceof YisSymbolic) {
            yStr = ((YisSymbolic)((Object)data)).getYSymbolicValue(series, item);
        } else {
            double y = data.getYValue(series, item);
            yStr = Double.toString(SymbolicXYItemLabelGenerator.round(y, 2));
        }
        if (data instanceof XisSymbolic) {
            xStr = ((XisSymbolic)((Object)data)).getXSymbolicValue(series, item);
        } else if (data instanceof TimeSeriesCollection) {
            RegularTimePeriod p = ((TimeSeriesCollection)data).getSeries(series).getTimePeriod(item);
            xStr = p.toString();
        } else {
            double x = data.getXValue(series, item);
            xStr = Double.toString(SymbolicXYItemLabelGenerator.round(x, 2));
        }
        return "X: " + xStr + ", Y: " + yStr;
    }

    public String generateLabel(XYDataset dataset, int series, int category) {
        return null;
    }

    private static double round(double value, int nb) {
        if (nb <= 0) {
            return Math.floor(value + 0.5);
        }
        double p = Math.pow(10.0, nb);
        double tempval = Math.floor(value * p + 0.5);
        return tempval / p;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SymbolicXYItemLabelGenerator) {
            return true;
        }
        return false;
    }
}

