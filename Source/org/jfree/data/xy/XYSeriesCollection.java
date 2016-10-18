/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.xy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jfree.data.DomainInfo;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.SeriesChangeListener;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.IntervalXYDelegate;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.util.ObjectUtilities;

public class XYSeriesCollection
extends AbstractIntervalXYDataset
implements IntervalXYDataset,
DomainInfo,
Serializable {
    private static final long serialVersionUID = -7590013825931496766L;
    private List data = new ArrayList();
    private IntervalXYDelegate intervalDelegate;

    public XYSeriesCollection() {
        this(null);
    }

    public XYSeriesCollection(XYSeries series) {
        this.intervalDelegate = new IntervalXYDelegate(this, false);
        this.addChangeListener(this.intervalDelegate);
        if (series != null) {
            this.data.add(series);
            series.addChangeListener(this);
        }
    }

    public void addSeries(XYSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Null 'series' argument.");
        }
        this.data.add(series);
        series.addChangeListener(this);
        this.fireDatasetChanged();
    }

    public void removeSeries(int series) {
        if (series < 0 || series > this.getSeriesCount()) {
            throw new IllegalArgumentException("Series index out of bounds.");
        }
        XYSeries ts = (XYSeries)this.data.get(series);
        ts.removeChangeListener(this);
        this.data.remove(series);
        this.fireDatasetChanged();
    }

    public void removeSeries(XYSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Null 'series' argument.");
        }
        if (this.data.contains(series)) {
            series.removeChangeListener(this);
            this.data.remove(series);
            this.fireDatasetChanged();
        }
    }

    public void removeAllSeries() {
        for (int i = 0; i < this.data.size(); ++i) {
            XYSeries series = (XYSeries)this.data.get(i);
            series.removeChangeListener(this);
        }
        this.data.clear();
        this.fireDatasetChanged();
    }

    public int getSeriesCount() {
        return this.data.size();
    }

    public List getSeries() {
        return Collections.unmodifiableList(this.data);
    }

    public XYSeries getSeries(int series) {
        if (series < 0 || series >= this.getSeriesCount()) {
            throw new IllegalArgumentException("Series index out of bounds");
        }
        return (XYSeries)this.data.get(series);
    }

    public Comparable getSeriesKey(int series) {
        return this.getSeries(series).getKey();
    }

    public int getItemCount(int series) {
        return this.getSeries(series).getItemCount();
    }

    public Number getX(int series, int item) {
        XYSeries ts = (XYSeries)this.data.get(series);
        XYDataItem xyItem = ts.getDataItem(item);
        return xyItem.getX();
    }

    public Number getStartX(int series, int item) {
        return this.intervalDelegate.getStartX(series, item);
    }

    public Number getEndX(int series, int item) {
        return this.intervalDelegate.getEndX(series, item);
    }

    public Number getY(int series, int index) {
        XYSeries ts = (XYSeries)this.data.get(series);
        XYDataItem xyItem = ts.getDataItem(index);
        return xyItem.getY();
    }

    public Number getStartY(int series, int item) {
        return this.getY(series, item);
    }

    public Number getEndY(int series, int item) {
        return this.getY(series, item);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYSeriesCollection)) {
            return false;
        }
        XYSeriesCollection that = (XYSeriesCollection)obj;
        return ObjectUtilities.equal(this.data, that.data);
    }

    public int hashCode() {
        return this.data != null ? this.data.hashCode() : 0;
    }

    public double getDomainLowerBound(boolean includeInterval) {
        return this.intervalDelegate.getDomainLowerBound(includeInterval);
    }

    public double getDomainUpperBound(boolean includeInterval) {
        return this.intervalDelegate.getDomainUpperBound(includeInterval);
    }

    public Range getDomainBounds(boolean includeInterval) {
        if (includeInterval) {
            return this.intervalDelegate.getDomainBounds(includeInterval);
        }
        return DatasetUtilities.iterateDomainBounds(this, includeInterval);
    }

    public double getIntervalWidth() {
        return this.intervalDelegate.getIntervalWidth();
    }

    public void setIntervalWidth(double width) {
        if (width < 0.0) {
            throw new IllegalArgumentException("Negative 'width' argument.");
        }
        this.intervalDelegate.setFixedIntervalWidth(width);
        this.fireDatasetChanged();
    }

    public double getIntervalPositionFactor() {
        return this.intervalDelegate.getIntervalPositionFactor();
    }

    public void setIntervalPositionFactor(double factor) {
        this.intervalDelegate.setIntervalPositionFactor(factor);
        this.fireDatasetChanged();
    }

    public boolean isAutoWidth() {
        return this.intervalDelegate.isAutoWidth();
    }

    public void setAutoWidth(boolean b) {
        this.intervalDelegate.setAutoWidth(b);
        this.fireDatasetChanged();
    }
}

