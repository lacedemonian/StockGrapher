/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.util.PublicCloneable;

public class IntervalCategoryToolTipGenerator
extends StandardCategoryToolTipGenerator
implements CategoryToolTipGenerator,
PublicCloneable,
Cloneable,
Serializable {
    private static final long serialVersionUID = -3853824986520333437L;
    public static final String DEFAULT_TOOL_TIP_FORMAT_STRING = "({0}, {1}) = {3} - {4}";

    public IntervalCategoryToolTipGenerator() {
        super("({0}, {1}) = {3} - {4}", NumberFormat.getInstance());
    }

    public IntervalCategoryToolTipGenerator(String labelFormat, NumberFormat formatter) {
        super(labelFormat, formatter);
    }

    public IntervalCategoryToolTipGenerator(String labelFormat, DateFormat formatter) {
        super(labelFormat, formatter);
    }

    protected Object[] createItemArray(CategoryDataset dataset, int row, int column) {
        Object[] result = new Object[5];
        result[0] = dataset.getRowKey(row).toString();
        result[1] = dataset.getColumnKey(column).toString();
        Number value = dataset.getValue(row, column);
        if (this.getNumberFormat() != null) {
            result[2] = this.getNumberFormat().format(value);
        } else if (this.getDateFormat() != null) {
            result[2] = this.getDateFormat().format(value);
        }
        if (dataset instanceof IntervalCategoryDataset) {
            IntervalCategoryDataset icd = (IntervalCategoryDataset)dataset;
            Number start = icd.getStartValue(row, column);
            Number end = icd.getEndValue(row, column);
            if (this.getNumberFormat() != null) {
                result[3] = this.getNumberFormat().format(start);
                result[4] = this.getNumberFormat().format(end);
            } else if (this.getDateFormat() != null) {
                result[3] = this.getDateFormat().format(start);
                result[4] = this.getDateFormat().format(end);
            }
        }
        return result;
    }
}

