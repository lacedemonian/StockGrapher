/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.MessageFormat;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.PublicCloneable;

public class StandardXYSeriesLabelGenerator
implements XYSeriesLabelGenerator,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 1916017081848400024L;
    public static final String DEFAULT_LABEL_FORMAT = "{0}";
    private String formatPattern;

    public StandardXYSeriesLabelGenerator() {
        this("{0}");
    }

    public StandardXYSeriesLabelGenerator(String format) {
        if (format == null) {
            throw new IllegalArgumentException("Null 'format' argument.");
        }
        this.formatPattern = format;
    }

    public String generateLabel(XYDataset dataset, int series) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        String label = MessageFormat.format(this.formatPattern, this.createItemArray(dataset, series));
        return label;
    }

    protected Object[] createItemArray(XYDataset dataset, int series) {
        Object[] result = new Object[]{dataset.getSeriesKey(series).toString()};
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardXYSeriesLabelGenerator)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }
}

