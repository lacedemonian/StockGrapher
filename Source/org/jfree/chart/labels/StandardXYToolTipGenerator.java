/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.labels;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.PublicCloneable;

public class StandardXYToolTipGenerator
extends AbstractXYItemLabelGenerator
implements XYToolTipGenerator,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -3564164459039540784L;
    public static final String DEFAULT_TOOL_TIP_FORMAT = "{0}: ({1}, {2})";

    public static StandardXYToolTipGenerator getTimeSeriesInstance() {
        return new StandardXYToolTipGenerator("{0}: ({1}, {2})", DateFormat.getInstance(), NumberFormat.getInstance());
    }

    public StandardXYToolTipGenerator() {
        this("{0}: ({1}, {2})", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance());
    }

    public StandardXYToolTipGenerator(String formatString, NumberFormat xFormat, NumberFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    public StandardXYToolTipGenerator(String formatString, DateFormat xFormat, NumberFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    public StandardXYToolTipGenerator(String formatString, DateFormat xFormat, DateFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    public String generateToolTip(XYDataset dataset, int series, int item) {
        return this.generateLabelString(dataset, series, item);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof StandardXYToolTipGenerator) {
            return super.equals(obj);
        }
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

