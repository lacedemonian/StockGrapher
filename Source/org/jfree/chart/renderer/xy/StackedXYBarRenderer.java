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
import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

public class StackedXYBarRenderer
extends XYBarRenderer
implements Serializable {
    private static final long serialVersionUID = -7049101055533436444L;
    static /* synthetic */ Class class$org$jfree$data$xy$IntervalXYDataset;
    static /* synthetic */ Class class$org$jfree$data$xy$TableXYDataset;

    public StackedXYBarRenderer() {
    }

    public StackedXYBarRenderer(double margin) {
        super(margin);
    }

    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset data, PlotRenderingInfo info) {
        return new XYBarRenderer.XYBarRendererState(this, info);
    }

    public Range findRangeBounds(XYDataset dataset) {
        if (dataset != null) {
            return DatasetUtilities.findStackedRangeBounds((TableXYDataset)dataset);
        }
        return null;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        EntityCollection entities;
        double translatedBase;
        double translatedValue;
        if (!(dataset instanceof IntervalXYDataset) || !(dataset instanceof TableXYDataset)) {
            String message = "dataset (type " + dataset.getClass().getName() + ") has wrong type:";
            boolean and = false;
            Class class_ = class$org$jfree$data$xy$IntervalXYDataset == null ? (StackedXYBarRenderer.class$org$jfree$data$xy$IntervalXYDataset = StackedXYBarRenderer.class$("org.jfree.data.xy.IntervalXYDataset")) : class$org$jfree$data$xy$IntervalXYDataset;
            if (!class_.isAssignableFrom(dataset.getClass())) {
                message = message + " it is no IntervalXYDataset";
                and = true;
            }
            Class class_2 = class$org$jfree$data$xy$TableXYDataset == null ? (StackedXYBarRenderer.class$org$jfree$data$xy$TableXYDataset = StackedXYBarRenderer.class$("org.jfree.data.xy.TableXYDataset")) : class$org$jfree$data$xy$TableXYDataset;
            if (!class_2.isAssignableFrom(dataset.getClass())) {
                if (and) {
                    message = message + " and";
                }
                message = message + " it is no TableXYDataset";
            }
            throw new IllegalArgumentException(message);
        }
        IntervalXYDataset intervalDataset = (IntervalXYDataset)dataset;
        double value = intervalDataset.getYValue(series, item);
        if (Double.isNaN(value)) {
            return;
        }
        double positiveBase = 0.0;
        double negativeBase = 0.0;
        for (int i = 0; i < series; ++i) {
            double v = dataset.getYValue(i, item);
            if (Double.isNaN(v)) continue;
            if (v > 0.0) {
                positiveBase += v;
                continue;
            }
            negativeBase += v;
        }
        RectangleEdge edgeR = plot.getRangeAxisEdge();
        if (value > 0.0) {
            translatedBase = rangeAxis.valueToJava2D(positiveBase, dataArea, edgeR);
            translatedValue = rangeAxis.valueToJava2D(positiveBase + value, dataArea, edgeR);
        } else {
            translatedBase = rangeAxis.valueToJava2D(negativeBase, dataArea, edgeR);
            translatedValue = rangeAxis.valueToJava2D(negativeBase + value, dataArea, edgeR);
        }
        RectangleEdge edgeD = plot.getDomainAxisEdge();
        double startX = intervalDataset.getStartXValue(series, item);
        if (Double.isNaN(startX)) {
            return;
        }
        double translatedStartX = domainAxis.valueToJava2D(startX, dataArea, edgeD);
        double endX = intervalDataset.getEndXValue(series, item);
        if (Double.isNaN(endX)) {
            return;
        }
        double translatedEndX = domainAxis.valueToJava2D(endX, dataArea, edgeD);
        double translatedWidth = Math.max(1.0, Math.abs(translatedEndX - translatedStartX));
        double translatedHeight = Math.abs(translatedValue - translatedBase);
        if (this.getMargin() > 0.0) {
            double cut = translatedWidth * this.getMargin();
            translatedWidth -= cut;
            translatedStartX += cut / 2.0;
        }
        Rectangle2D.Double bar = null;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            bar = new Rectangle2D.Double(Math.min(translatedBase, translatedValue), translatedEndX, translatedHeight, translatedWidth);
        } else if (orientation == PlotOrientation.VERTICAL) {
            bar = new Rectangle2D.Double(translatedStartX, Math.min(translatedBase, translatedValue), translatedWidth, translatedHeight);
        }
        g2.setPaint(this.getItemPaint(series, item));
        g2.fill(bar);
        if (this.isDrawBarOutline() && Math.abs(translatedEndX - translatedStartX) > 3.0) {
            g2.setStroke(this.getItemStroke(series, item));
            g2.setPaint(this.getItemOutlinePaint(series, item));
            g2.draw(bar);
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

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

