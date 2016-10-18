/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.xy;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;

public class XYBarDataset
extends AbstractIntervalXYDataset
implements IntervalXYDataset,
DatasetChangeListener {
    private XYDataset underlying;
    private double barWidth;

    public XYBarDataset(XYDataset underlying, double barWidth) {
        this.underlying = underlying;
        this.underlying.addChangeListener(this);
        this.barWidth = barWidth;
    }

    public int getSeriesCount() {
        return this.underlying.getSeriesCount();
    }

    public Comparable getSeriesKey(int series) {
        return this.underlying.getSeriesKey(series);
    }

    public int getItemCount(int series) {
        return this.underlying.getItemCount(series);
    }

    public Number getX(int series, int item) {
        return this.underlying.getX(series, item);
    }

    public Number getY(int series, int item) {
        return this.underlying.getY(series, item);
    }

    public Number getStartX(int series, int item) {
        Double result = null;
        Number xnum = this.underlying.getX(series, item);
        if (xnum != null) {
            result = new Double(xnum.doubleValue() - this.barWidth / 2.0);
        }
        return result;
    }

    public Number getEndX(int series, int item) {
        Double result = null;
        Number xnum = this.underlying.getX(series, item);
        if (xnum != null) {
            result = new Double(xnum.doubleValue() + this.barWidth / 2.0);
        }
        return result;
    }

    public Number getStartY(int series, int item) {
        return this.underlying.getY(series, item);
    }

    public Number getEndY(int series, int item) {
        return this.underlying.getY(series, item);
    }

    public void datasetChanged(DatasetChangeEvent event) {
        this.notifyListeners(event);
    }
}

