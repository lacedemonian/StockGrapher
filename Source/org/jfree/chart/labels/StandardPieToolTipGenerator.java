/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.NumberFormat;
import org.jfree.chart.labels.AbstractPieItemLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.data.general.PieDataset;
import org.jfree.util.PublicCloneable;

public class StandardPieToolTipGenerator
extends AbstractPieItemLabelGenerator
implements PieToolTipGenerator,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 2995304200445733779L;
    public static final String DEFAULT_TOOLTIP_FORMAT = "{0}: ({1}, {2})";
    public static final String DEFAULT_SECTION_LABEL_FORMAT = "{0} = {1}";

    public StandardPieToolTipGenerator() {
        this("{0} = {1}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance());
    }

    public StandardPieToolTipGenerator(String labelFormat) {
        this(labelFormat, NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance());
    }

    public StandardPieToolTipGenerator(String labelFormat, NumberFormat numberFormat, NumberFormat percentFormat) {
        super(labelFormat, numberFormat, percentFormat);
    }

    public String generateToolTip(PieDataset dataset, Comparable key) {
        return this.generateSectionLabel(dataset, key);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

