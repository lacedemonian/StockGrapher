/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class XYStepRenderer
extends XYLineAndShapeRenderer
implements XYItemRenderer,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -8918141928884796108L;

    public XYStepRenderer() {
        this(null, null);
    }

    public XYStepRenderer(XYToolTipGenerator toolTipGenerator, XYURLGenerator urlGenerator) {
        this.setBaseToolTipGenerator(toolTipGenerator);
        this.setURLGenerator(urlGenerator);
        this.setShapesVisible(false);
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        EntityCollection entities;
        if (!this.getItemVisible(series, item)) {
            return;
        }
        PlotOrientation orientation = plot.getOrientation();
        Paint seriesPaint = this.getItemPaint(series, item);
        Stroke seriesStroke = this.getItemStroke(series, item);
        g2.setPaint(seriesPaint);
        g2.setStroke(seriesStroke);
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        if (Double.isNaN(y1)) {
            return;
        }
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
        if (item > 0) {
            double x0 = dataset.getXValue(series, item - 1);
            double y0 = dataset.getYValue(series, item - 1);
            if (!Double.isNaN(y0)) {
                double transX0 = domainAxis.valueToJava2D(x0, dataArea, xAxisLocation);
                double transY0 = rangeAxis.valueToJava2D(y0, dataArea, yAxisLocation);
                Line2D line = state.workingLine;
                if (orientation == PlotOrientation.HORIZONTAL) {
                    if (transY0 == transY1) {
                        line.setLine(transY0, transX0, transY1, transX1);
                        g2.draw(line);
                    } else {
                        line.setLine(transY0, transX0, transY1, transX0);
                        g2.draw(line);
                        line.setLine(transY1, transX0, transY1, transX1);
                        g2.draw(line);
                    }
                } else if (orientation == PlotOrientation.VERTICAL) {
                    if (transY0 == transY1) {
                        line.setLine(transX0, transY0, transX1, transY1);
                        g2.draw(line);
                    } else {
                        line.setLine(transX0, transY0, transX1, transY0);
                        g2.draw(line);
                        line.setLine(transX1, transY0, transX1, transY1);
                        g2.draw(line);
                    }
                }
            }
        }
        this.updateCrosshairValues(crosshairState, x1, y1, transX1, transY1, orientation);
        if (state.getInfo() != null && (entities = state.getEntityCollection()) != null) {
            Rectangle2D.Double shape;
            int r = this.getDefaultEntityRadius();
            Rectangle2D.Double double_ = shape = orientation == PlotOrientation.VERTICAL ? new Rectangle2D.Double(transX1 - (double)r, transY1 - (double)r, 2 * r, 2 * r) : new Rectangle2D.Double(transY1 - (double)r, transX1 - (double)r, 2 * r, 2 * r);
            if (shape != null) {
                String tip = null;
                XYToolTipGenerator generator = this.getToolTipGenerator(series, item);
                if (generator != null) {
                    tip = generator.generateToolTip(dataset, series, item);
                }
                String url = null;
                if (this.getURLGenerator() != null) {
                    url = this.getURLGenerator().generateURL(dataset, series, item);
                }
                XYItemEntity entity = new XYItemEntity(shape, dataset, series, item, tip, url);
                entities.add(entity);
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

