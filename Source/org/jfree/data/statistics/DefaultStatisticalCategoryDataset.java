/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.statistics;

import java.util.List;
import org.jfree.data.KeyedObjects2D;
import org.jfree.data.Range;
import org.jfree.data.RangeInfo;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.statistics.MeanAndStandardDeviation;
import org.jfree.data.statistics.StatisticalCategoryDataset;

public class DefaultStatisticalCategoryDataset
extends AbstractDataset
implements StatisticalCategoryDataset,
RangeInfo {
    private KeyedObjects2D data = new KeyedObjects2D();
    private double minimumRangeValue = 0.0;
    private double maximumRangeValue = 0.0;
    private Range rangeBounds = new Range(0.0, 0.0);

    public Number getMeanValue(int row, int column) {
        Number result = null;
        MeanAndStandardDeviation masd = (MeanAndStandardDeviation)this.data.getObject(row, column);
        if (masd != null) {
            result = masd.getMean();
        }
        return result;
    }

    public Number getValue(int row, int column) {
        return this.getMeanValue(row, column);
    }

    public Number getValue(Comparable rowKey, Comparable columnKey) {
        return this.getMeanValue(rowKey, columnKey);
    }

    public Number getMeanValue(Comparable rowKey, Comparable columnKey) {
        Number result = null;
        MeanAndStandardDeviation masd = (MeanAndStandardDeviation)this.data.getObject(rowKey, columnKey);
        if (masd != null) {
            result = masd.getMean();
        }
        return result;
    }

    public Number getStdDevValue(int row, int column) {
        Number result = null;
        MeanAndStandardDeviation masd = (MeanAndStandardDeviation)this.data.getObject(row, column);
        if (masd != null) {
            result = masd.getStandardDeviation();
        }
        return result;
    }

    public Number getStdDevValue(Comparable rowKey, Comparable columnKey) {
        Number result = null;
        MeanAndStandardDeviation masd = (MeanAndStandardDeviation)this.data.getObject(rowKey, columnKey);
        if (masd != null) {
            result = masd.getStandardDeviation();
        }
        return result;
    }

    public int getColumnIndex(Comparable key) {
        return this.data.getColumnIndex(key);
    }

    public Comparable getColumnKey(int column) {
        return this.data.getColumnKey(column);
    }

    public List getColumnKeys() {
        return this.data.getColumnKeys();
    }

    public int getRowIndex(Comparable key) {
        return this.data.getRowIndex(key);
    }

    public Comparable getRowKey(int row) {
        return this.data.getRowKey(row);
    }

    public List getRowKeys() {
        return this.data.getRowKeys();
    }

    public int getRowCount() {
        return this.data.getRowCount();
    }

    public int getColumnCount() {
        return this.data.getColumnCount();
    }

    public void add(double mean, double standardDeviation, Comparable rowKey, Comparable columnKey) {
        this.add(new Double(mean), new Double(standardDeviation), rowKey, columnKey);
    }

    public void add(Number mean, Number standardDeviation, Comparable rowKey, Comparable columnKey) {
        MeanAndStandardDeviation item = new MeanAndStandardDeviation(mean, standardDeviation);
        this.data.addObject(item, rowKey, columnKey);
        double m = 0.0;
        double sd = 0.0;
        if (mean != null) {
            m = mean.doubleValue();
        }
        if (standardDeviation != null) {
            sd = standardDeviation.doubleValue();
        }
        if (m + sd > this.maximumRangeValue) {
            this.maximumRangeValue = m + sd;
            this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
        }
        if (m - sd < this.minimumRangeValue) {
            this.minimumRangeValue = m - sd;
            this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
        }
        this.fireDatasetChanged();
    }

    public double getRangeLowerBound(boolean includeInterval) {
        return this.minimumRangeValue;
    }

    public double getRangeUpperBound(boolean includeInterval) {
        return this.maximumRangeValue;
    }

    public Range getRangeBounds(boolean includeInterval) {
        return this.rangeBounds;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DefaultStatisticalCategoryDataset)) {
            return false;
        }
        DefaultStatisticalCategoryDataset that = (DefaultStatisticalCategoryDataset)obj;
        if (!this.data.equals(that.data)) {
            return false;
        }
        return true;
    }
}

