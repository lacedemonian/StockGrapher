/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.MessageFormat;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.util.PublicCloneable;

public class StandardCategorySeriesLabelGenerator
implements CategorySeriesLabelGenerator,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 4630760091523940820L;
    public static final String DEFAULT_LABEL_FORMAT = "{0}";
    private String formatPattern;

    public StandardCategorySeriesLabelGenerator() {
        this("{0}");
    }

    public StandardCategorySeriesLabelGenerator(String format) {
        if (format == null) {
            throw new IllegalArgumentException("Null 'format' argument.");
        }
        this.formatPattern = format;
    }

    public String generateLabel(CategoryDataset dataset, int series) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null 'dataset' argument.");
        }
        String label = MessageFormat.format(this.formatPattern, this.createItemArray(dataset, series));
        return label;
    }

    protected Object[] createItemArray(CategoryDataset dataset, int series) {
        Object[] result = new Object[]{dataset.getRowKey(series).toString()};
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardCategorySeriesLabelGenerator)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }
}

