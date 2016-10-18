/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class XYBubbleRenderer
extends AbstractXYItemRenderer
implements XYItemRenderer,
Cloneable,
PublicCloneable,
Serializable {
    public static final long serialVersionUID = -5221991598674249125L;
    public static final int SCALE_ON_BOTH_AXES = 0;
    public static final int SCALE_ON_DOMAIN_AXIS = 1;
    public static final int SCALE_ON_RANGE_AXIS = 2;
    private int scaleType;

    public XYBubbleRenderer() {
        this(0);
    }

    public XYBubbleRenderer(int scaleType) {
        if (scaleType < 0 || scaleType > 2) {
            throw new IllegalArgumentException("Invalid 'scaleType'.");
        }
        this.scaleType = scaleType;
    }

    public int getScaleType() {
        return this.scaleType;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        PlotOrientation orientation = plot.getOrientation();
        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double z = Double.NaN;
        if (dataset instanceof XYZDataset) {
            XYZDataset xyzData = (XYZDataset)dataset;
            z = xyzData.getZValue(series, item);
        }
        if (!Double.isNaN(z)) {
            RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
            double transX = domainAxis.valueToJava2D(x, dataArea, domainAxisLocation);
            double transY = rangeAxis.valueToJava2D(y, dataArea, rangeAxisLocation);
            double transDomain = 0.0;
            double transRange = 0.0;
            switch (this.getScaleType()) {
                case 1: {
                    double zero = domainAxis.valueToJava2D(0.0, dataArea, domainAxisLocation);
                    transRange = transDomain = domainAxis.valueToJava2D(z, dataArea, domainAxisLocation) - zero;
                    break;
                }
                case 2: {
                    double zero = rangeAxis.valueToJava2D(0.0, dataArea, rangeAxisLocation);
                    transDomain = transRange = zero - rangeAxis.valueToJava2D(z, dataArea, rangeAxisLocation);
                    break;
                }
                default: {
                    double zero1 = domainAxis.valueToJava2D(0.0, dataArea, domainAxisLocation);
                    double zero2 = rangeAxis.valueToJava2D(0.0, dataArea, rangeAxisLocation);
                    transDomain = domainAxis.valueToJava2D(z, dataArea, domainAxisLocation) - zero1;
                    transRange = zero2 - rangeAxis.valueToJava2D(z, dataArea, rangeAxisLocation);
                }
            }
            transDomain = Math.abs(transDomain);
            transRange = Math.abs(transRange);
            Ellipse2D.Double circle = null;
            if (orientation == PlotOrientation.VERTICAL) {
                circle = new Ellipse2D.Double(transX - transDomain / 2.0, transY - transRange / 2.0, transDomain, transRange);
            } else if (orientation == PlotOrientation.HORIZONTAL) {
                circle = new Ellipse2D.Double(transY - transRange / 2.0, transX - transDomain / 2.0, transRange, transDomain);
            }
            g2.setPaint(this.getItemPaint(series, item));
            g2.fill(circle);
            g2.setStroke(new BasicStroke(1.0f));
            g2.setPaint(Color.lightGray);
            g2.draw(circle);
            if (this.isItemLabelVisible(series, item)) {
                if (orientation == PlotOrientation.VERTICAL) {
                    this.drawItemLabel(g2, orientation, dataset, series, item, transX, transY, false);
                } else if (orientation == PlotOrientation.HORIZONTAL) {
                    this.drawItemLabel(g2, orientation, dataset, series, item, transY, transX, false);
                }
            }
            EntityCollection entities = null;
            if (info != null) {
                entities = info.getOwner().getEntityCollection();
            }
            if (entities != null) {
                String tip = null;
                XYToolTipGenerator generator = this.getToolTipGenerator(series, item);
                if (generator != null) {
                    tip = generator.generateToolTip(dataset, series, item);
                }
                String url = null;
                if (this.getURLGenerator() != null) {
                    url = this.getURLGenerator().generateURL(dataset, series, item);
                }
                XYItemEntity entity = new XYItemEntity(circle, dataset, series, item, tip, url);
                entities.add(entity);
            }
            this.updateCrosshairValues(crosshairState, x, y, transX, transY, orientation);
        }
    }

    public LegendItem getLegendItem(int datasetIndex, int series) {
        XYDataset dataset;
        LegendItem result = null;
        XYPlot xyplot = this.getPlot();
        if (xyplot != null && (dataset = xyplot.getDataset(datasetIndex)) != null && this.getItemVisible(series, 0)) {
            String label;
            String description = label = this.getLegendItemLabelGenerator().generateLabel(dataset, series);
            String toolTipText = null;
            if (this.getLegendItemToolTipGenerator() != null) {
                toolTipText = this.getLegendItemToolTipGenerator().generateLabel(dataset, series);
            }
            String urlText = null;
            if (this.getLegendItemURLGenerator() != null) {
                urlText = this.getLegendItemURLGenerator().generateLabel(dataset, series);
            }
            Ellipse2D.Double shape = new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0);
            Paint paint = this.getSeriesPaint(series);
            Paint outlinePaint = this.getSeriesOutlinePaint(series);
            Stroke outlineStroke = this.getSeriesOutlineStroke(series);
            result = new LegendItem(label, description, toolTipText, urlText, (Shape)shape, paint, outlineStroke, outlinePaint);
        }
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

