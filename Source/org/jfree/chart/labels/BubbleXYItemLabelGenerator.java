/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.util.ObjectUtilities;

public class BubbleXYItemLabelGenerator
extends AbstractXYItemLabelGenerator
implements XYItemLabelGenerator,
Serializable {
    static final long serialVersionUID = -8458568928021240922L;
    public static final String DEFAULT_FORMAT_STRING = "{3}";
    private NumberFormat zFormat;
    private DateFormat zDateFormat;

    public BubbleXYItemLabelGenerator() {
        this("{3}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
    }

    public BubbleXYItemLabelGenerator(String formatString, NumberFormat xFormat, NumberFormat yFormat, NumberFormat zFormat) {
        super(formatString, xFormat, yFormat);
        if (zFormat == null) {
            throw new IllegalArgumentException("Null 'zFormat' argument.");
        }
        this.zFormat = zFormat;
    }

    public BubbleXYItemLabelGenerator(String formatString, DateFormat xFormat, DateFormat yFormat, DateFormat zFormat) {
        super(formatString, xFormat, yFormat);
        if (zFormat == null) {
            throw new IllegalArgumentException("Null 'zFormat' argument.");
        }
        this.zDateFormat = zFormat;
    }

    public NumberFormat getZFormat() {
        return this.zFormat;
    }

    public DateFormat getZDateFormat() {
        return this.zDateFormat;
    }

    public String generateLabel(XYDataset dataset, int series, int item) {
        return this.generateLabelString(dataset, series, item);
    }

    public String generateLabelString(XYDataset dataset, int series, int item) {
        String result = null;
        Object[] items = null;
        items = dataset instanceof XYZDataset ? this.createItemArray((XYZDataset)dataset, series, item) : this.createItemArray(dataset, series, item);
        result = MessageFormat.format(this.getFormatString(), items);
        return result;
    }

    protected Object[] createItemArray(XYZDataset dataset, int series, int item) {
        Object[] result = new Object[4];
        result[0] = dataset.getSeriesKey(series).toString();
        Number x = dataset.getX(series, item);
        DateFormat xf = this.getXDateFormat();
        result[1] = xf != null ? xf.format(x) : this.getXFormat().format(x);
        Number y = dataset.getY(series, item);
        DateFormat yf = this.getYDateFormat();
        result[2] = yf != null ? yf.format(y) : this.getYFormat().format(y);
        Number z = dataset.getZ(series, item);
        result[3] = this.zDateFormat != null ? this.zDateFormat.format(z) : this.zFormat.format(z);
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BubbleXYItemLabelGenerator)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        BubbleXYItemLabelGenerator that = (BubbleXYItemLabelGenerator)obj;
        if (!ObjectUtilities.equal(this.zFormat, that.zFormat)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.zDateFormat, that.zDateFormat)) {
            return false;
        }
        return true;
    }
}

