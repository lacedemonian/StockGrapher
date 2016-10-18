/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.util.PublicCloneable;

public class StandardCategoryItemLabelGenerator
extends AbstractCategoryItemLabelGenerator
implements CategoryItemLabelGenerator,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 3499701401211412882L;
    public static final String DEFAULT_LABEL_FORMAT_STRING = "{2}";

    public StandardCategoryItemLabelGenerator() {
        super("{2}", NumberFormat.getInstance());
    }

    public StandardCategoryItemLabelGenerator(String labelFormat, NumberFormat formatter) {
        super(labelFormat, formatter);
    }

    public StandardCategoryItemLabelGenerator(String labelFormat, DateFormat formatter) {
        super(labelFormat, formatter);
    }

    public String generateLabel(CategoryDataset dataset, int row, int column) {
        return this.generateLabelString(dataset, row, column);
    }
}

