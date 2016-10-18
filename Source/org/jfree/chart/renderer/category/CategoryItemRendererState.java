/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.category;

import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.RendererState;

public class CategoryItemRendererState
extends RendererState {
    private double barWidth = 0.0;
    private double seriesRunningTotal = 0.0;

    public CategoryItemRendererState(PlotRenderingInfo info) {
        super(info);
    }

    public double getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(double width) {
        this.barWidth = width;
    }

    public double getSeriesRunningTotal() {
        return this.seriesRunningTotal;
    }

    void setSeriesRunningTotal(double total) {
        this.seriesRunningTotal = total;
    }
}

