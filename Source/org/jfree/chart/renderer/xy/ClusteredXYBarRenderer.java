/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class ClusteredXYBarRenderer
extends XYBarRenderer
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 5864462149177133147L;
    private boolean centerBarAtStartValue;

    public ClusteredXYBarRenderer() {
        this(0.0, false);
    }

    public ClusteredXYBarRenderer(double margin, boolean centerBarAtStartValue) {
        super(margin);
        this.centerBarAtStartValue = centerBarAtStartValue;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        double value0;
        EntityCollection entities;
        double value1;
        IntervalXYDataset intervalDataset = (IntervalXYDataset)dataset;
        Paint seriesPaint = this.getItemPaint(series, item);
        if (this.getUseYInterval()) {
            value0 = intervalDataset.getStartYValue(series, item);
            value1 = intervalDataset.getEndYValue(series, item);
        } else {
            value0 = this.getBase();
            value1 = intervalDataset.getYValue(series, item);
        }
        if (Double.isNaN(value0) || Double.isNaN(value1)) {
            return;
        }
        double translatedValue0 = rangeAxis.valueToJava2D(value0, dataArea, plot.getRangeAxisEdge());
        double translatedValue1 = rangeAxis.valueToJava2D(value1, dataArea, plot.getRangeAxisEdge());
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        double x1 = intervalDataset.getStartXValue(series, item);
        double translatedX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double x2 = intervalDataset.getEndXValue(series, item);
        double translatedX2 = domainAxis.valueToJava2D(x2, dataArea, xAxisLocation);
        double translatedWidth = Math.max(1.0, Math.abs(translatedX2 - translatedX1));
        double translatedHeight = Math.abs(translatedValue0 - translatedValue1);
        if (this.centerBarAtStartValue) {
            translatedX1 -= translatedWidth / 2.0;
        }
        if (this.getMargin() > 0.0) {
            double cut = translatedWidth * this.getMargin();
            translatedWidth -= cut;
            translatedX1 += cut / 2.0;
        }
        int numSeries = dataset.getSeriesCount();
        double seriesBarWidth = translatedWidth / (double)numSeries;
        Rectangle2D.Double bar = null;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            bar = new Rectangle2D.Double(Math.min(translatedValue0, translatedValue1), translatedX1 - seriesBarWidth * (double)(numSeries - series), translatedHeight, seriesBarWidth);
        } else if (orientation == PlotOrientation.VERTICAL) {
            bar = new Rectangle2D.Double(translatedX1 + seriesBarWidth * (double)series, Math.min(translatedValue0, translatedValue1), seriesBarWidth, translatedHeight);
        }
        g2.setPaint(seriesPaint);
        g2.fill(bar);
        if (this.isDrawBarOutline() && Math.abs(translatedX2 - translatedX1) > 3.0) {
            g2.setStroke(this.getItemOutlineStroke(series, item));
            g2.setPaint(this.getItemOutlinePaint(series, item));
            g2.draw(bar);
        }
        if (this.isItemLabelVisible(series, item)) {
            this.drawItemLabel(g2, orientation, dataset, series, item, bar.getCenterX(), bar.getY(), value1 < 0.0);
        }
        if (info != null && (entities = info.getOwner().getEntityCollection()) != null) {
            String tip = null;
            XYToolTipGenerator generator = this.getToolTipGenerator(series, item);
            if (generator != null) {
                tip = generator.generateToolTip(dataset, series, item);
            }
            String url = null;
            if (this.getURLGenerator() != null) {
                url = this.getURLGenerator().generateURL(dataset, series, item);
            }
            XYItemEntity entity = new XYItemEntity(bar, dataset, series, item, tip, url);
            entities.add(entity);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ClusteredXYBarRenderer)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        ClusteredXYBarRenderer that = (ClusteredXYBarRenderer)obj;
        if (this.centerBarAtStartValue != that.centerBarAtStartValue) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

