/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.urls;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;

public class TimeSeriesURLGenerator
implements XYURLGenerator,
Serializable {
    private static final long serialVersionUID = -9122773175671182445L;
    private DateFormat dateFormat = DateFormat.getInstance();
    private String prefix = "index.html";
    private String seriesParameterName = "series";
    private String itemParameterName = "item";

    public TimeSeriesURLGenerator() {
    }

    public TimeSeriesURLGenerator(DateFormat dDateFormat, String sPrefix, String sSeriesParameterName, String sItemParameterName) {
        this.dateFormat = dDateFormat;
        this.prefix = sPrefix;
        this.seriesParameterName = sSeriesParameterName;
        this.itemParameterName = sItemParameterName;
    }

    public String generateURL(XYDataset dataset, int series, int item) {
        String result = this.prefix;
        boolean firstParameter = result.indexOf("?") == -1;
        Comparable seriesKey = dataset.getSeriesKey(series);
        if (seriesKey != null) {
            result = result + (firstParameter ? "?" : "&amp;");
            result = result + this.seriesParameterName + "=" + seriesKey.toString();
            firstParameter = false;
        }
        long x = dataset.getX(series, item).longValue();
        String xValue = this.dateFormat.format(new Date(x));
        result = result + (firstParameter ? "?" : "&amp;");
        result = result + this.itemParameterName + "=" + xValue;
        return result;
    }
}

