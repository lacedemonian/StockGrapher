/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.urls;

import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.chart.urls.XYZURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

public class StandardXYZURLGenerator
extends StandardXYURLGenerator
implements XYZURLGenerator {
    public String generateURL(XYZDataset dataset, int series, int item) {
        return super.generateURL(dataset, series, item);
    }
}

