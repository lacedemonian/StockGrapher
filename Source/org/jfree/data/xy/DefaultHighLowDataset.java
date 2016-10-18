/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.xy;

import java.util.Date;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.OHLCDataset;

public class DefaultHighLowDataset
extends AbstractXYDataset
implements OHLCDataset {
    private Comparable seriesKey;
    private Date[] date;
    private Number[] high;
    private Number[] low;
    private Number[] open;
    private Number[] close;
    private Number[] volume;

    public DefaultHighLowDataset(Comparable seriesKey, Date[] date, double[] high, double[] low, double[] open, double[] close, double[] volume) {
        this.seriesKey = seriesKey;
        this.date = date;
        this.high = DefaultHighLowDataset.createNumberArray(high);
        this.low = DefaultHighLowDataset.createNumberArray(low);
        this.open = DefaultHighLowDataset.createNumberArray(open);
        this.close = DefaultHighLowDataset.createNumberArray(close);
        this.volume = DefaultHighLowDataset.createNumberArray(volume);
    }

    public Comparable getSeriesKey(int i) {
        return this.seriesKey;
    }

    public Number getX(int series, int item) {
        return new Long(this.date[item].getTime());
    }

    public Date getXDate(int series, int item) {
        return this.date[item];
    }

    public Number getY(int series, int item) {
        return this.getClose(series, item);
    }

    public Number getHigh(int series, int item) {
        return this.high[item];
    }

    public double getHighValue(int series, int item) {
        double result = Double.NaN;
        Number high = this.getHigh(series, item);
        if (high != null) {
            result = high.doubleValue();
        }
        return result;
    }

    public Number getLow(int series, int item) {
        return this.low[item];
    }

    public double getLowValue(int series, int item) {
        double result = Double.NaN;
        Number low = this.getLow(series, item);
        if (low != null) {
            result = low.doubleValue();
        }
        return result;
    }

    public Number getOpen(int series, int item) {
        return this.open[item];
    }

    public double getOpenValue(int series, int item) {
        double result = Double.NaN;
        Number open = this.getOpen(series, item);
        if (open != null) {
            result = open.doubleValue();
        }
        return result;
    }

    public Number getClose(int series, int item) {
        return this.close[item];
    }

    public double getCloseValue(int series, int item) {
        double result = Double.NaN;
        Number close = this.getClose(series, item);
        if (close != null) {
            result = close.doubleValue();
        }
        return result;
    }

    public Number getVolume(int series, int item) {
        return this.volume[item];
    }

    public double getVolumeValue(int series, int item) {
        double result = Double.NaN;
        Number volume = this.getVolume(series, item);
        if (volume != null) {
            result = volume.doubleValue();
        }
        return result;
    }

    public int getSeriesCount() {
        return 1;
    }

    public int getItemCount(int series) {
        return this.date.length;
    }

    public static Number[] createNumberArray(double[] data) {
        Number[] result = new Number[data.length];
        for (int i = 0; i < data.length; ++i) {
            result[i] = new Double(data[i]);
        }
        return result;
    }
}

