/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.RendererChangeEvent;
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
import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

public class XYBarRenderer
extends AbstractXYItemRenderer
implements XYItemRenderer,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 770559577251370036L;
    private double base;
    private boolean useYInterval;
    private double margin;
    private boolean drawBarOutline;
    private GradientPaintTransformer gradientPaintTransformer;
    private transient Shape legendBar;

    public XYBarRenderer() {
        this(0.0);
    }

    public XYBarRenderer(double margin) {
        this.margin = margin;
        this.base = 0.0;
        this.useYInterval = false;
        this.gradientPaintTransformer = new StandardGradientPaintTransformer();
        this.drawBarOutline = true;
        this.legendBar = new Rectangle2D.Double(-3.0, -5.0, 6.0, 10.0);
    }

    public double getBase() {
        return this.base;
    }

    public void setBase(double base) {
        this.base = base;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getUseYInterval() {
        return this.useYInterval;
    }

    public void setUseYInterval(boolean use) {
        this.useYInterval = use;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public double getMargin() {
        return this.margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean isDrawBarOutline() {
        return this.drawBarOutline;
    }

    public void setDrawBarOutline(boolean draw) {
        this.drawBarOutline = draw;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public GradientPaintTransformer getGradientPaintTransformer() {
        return this.gradientPaintTransformer;
    }

    public void setGradientPaintTransformer(GradientPaintTransformer transformer) {
        this.gradientPaintTransformer = transformer;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public Shape getLegendBar() {
        return this.legendBar;
    }

    public void setLegendBar(Shape bar) {
        if (bar == null) {
            throw new IllegalArgumentException("Null 'bar' argument.");
        }
        this.legendBar = bar;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset dataset, PlotRenderingInfo info) {
        XYBarRendererState state = new XYBarRendererState(info);
        ValueAxis rangeAxis = plot.getRangeAxisForDataset(plot.indexOf(dataset));
        state.setG2Base(rangeAxis.valueToJava2D(this.base, dataArea, plot.getRangeAxisEdge()));
        return state;
    }

    public LegendItem getLegendItem(int datasetIndex, int series) {
        XYDataset dataset;
        LegendItem result = null;
        XYPlot xyplot = this.getPlot();
        if (xyplot != null && (dataset = xyplot.getDataset(datasetIndex)) != null) {
            String label;
            XYSeriesLabelGenerator lg = this.getLegendItemLabelGenerator();
            String description = label = lg.generateLabel(dataset, series);
            String toolTipText = null;
            if (this.getLegendItemToolTipGenerator() != null) {
                toolTipText = this.getLegendItemToolTipGenerator().generateLabel(dataset, series);
            }
            String urlText = null;
            if (this.getLegendItemURLGenerator() != null) {
                urlText = this.getLegendItemURLGenerator().generateLabel(dataset, series);
            }
            Shape shape = this.legendBar;
            Paint paint = this.getSeriesPaint(series);
            Paint outlinePaint = this.getSeriesOutlinePaint(series);
            Stroke outlineStroke = this.getSeriesOutlineStroke(series);
            result = new LegendItem(label, description, toolTipText, urlText, shape, paint, outlineStroke, outlinePaint);
        }
        return result;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        EntityCollection entities;
        double value1;
        double value0;
        if (!this.getItemVisible(series, item)) {
            return;
        }
        IntervalXYDataset intervalDataset = (IntervalXYDataset)dataset;
        if (this.useYInterval) {
            value0 = intervalDataset.getStartYValue(series, item);
            value1 = intervalDataset.getEndYValue(series, item);
        } else {
            value0 = this.base;
            value1 = intervalDataset.getYValue(series, item);
        }
        if (Double.isNaN(value0) || Double.isNaN(value1)) {
            return;
        }
        double translatedValue0 = rangeAxis.valueToJava2D(value0, dataArea, plot.getRangeAxisEdge());
        double translatedValue1 = rangeAxis.valueToJava2D(value1, dataArea, plot.getRangeAxisEdge());
        RectangleEdge location = plot.getDomainAxisEdge();
        Number startXNumber = intervalDataset.getStartX(series, item);
        if (startXNumber == null) {
            return;
        }
        double translatedStartX = domainAxis.valueToJava2D(startXNumber.doubleValue(), dataArea, location);
        Number endXNumber = intervalDataset.getEndX(series, item);
        if (endXNumber == null) {
            return;
        }
        double translatedEndX = domainAxis.valueToJava2D(endXNumber.doubleValue(), dataArea, location);
        double translatedWidth = Math.max(1.0, Math.abs(translatedEndX - translatedStartX));
        double translatedHeight = Math.abs(translatedValue1 - translatedValue0);
        if (this.getMargin() > 0.0) {
            double cut = translatedWidth * this.getMargin();
            translatedWidth -= cut;
            translatedStartX += cut / 2.0;
        }
        Rectangle2D.Double bar = null;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            bar = new Rectangle2D.Double(Math.min(translatedValue0, translatedValue1), Math.min(translatedStartX, translatedEndX), translatedHeight, translatedWidth);
        } else if (orientation == PlotOrientation.VERTICAL) {
            bar = new Rectangle2D.Double(Math.min(translatedStartX, translatedEndX), Math.min(translatedValue0, translatedValue1), translatedWidth, translatedHeight);
        }
        Paint itemPaint = this.getItemPaint(series, item);
        if (this.getGradientPaintTransformer() != null && itemPaint instanceof GradientPaint) {
            GradientPaint gp = (GradientPaint)itemPaint;
            itemPaint = this.getGradientPaintTransformer().transform(gp, bar);
        }
        g2.setPaint(itemPaint);
        g2.fill(bar);
        if (this.isDrawBarOutline() && Math.abs(translatedEndX - translatedStartX) > 3.0) {
            Stroke stroke = this.getItemOutlineStroke(series, item);
            Paint paint = this.getItemOutlinePaint(series, item);
            if (stroke != null && paint != null) {
                g2.setStroke(stroke);
                g2.setPaint(paint);
                g2.draw(bar);
            }
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

    public Range findDomainBounds(XYDataset dataset) {
        if (dataset != null) {
            return DatasetUtilities.findDomainBounds(dataset, true);
        }
        return null;
    }

    public Object clone() throws CloneNotSupportedException {
        XYBarRenderer result = (XYBarRenderer)super.clone();
        if (this.gradientPaintTransformer != null) {
            result.gradientPaintTransformer = (GradientPaintTransformer)ObjectUtilities.clone(this.gradientPaintTransformer);
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYBarRenderer)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        XYBarRenderer that = (XYBarRenderer)obj;
        if (this.base != that.base) {
            return false;
        }
        if (this.drawBarOutline != that.drawBarOutline) {
            return false;
        }
        if (this.margin != that.margin) {
            return false;
        }
        if (this.useYInterval != that.useYInterval) {
            return false;
        }
        if (!ObjectUtilities.equal(this.gradientPaintTransformer, that.gradientPaintTransformer)) {
            return false;
        }
        if (!ShapeUtilities.equal(this.legendBar, that.legendBar)) {
            return false;
        }
        return true;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.legendBar = SerialUtilities.readShape(stream);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeShape(this.legendBar, stream);
    }

    protected class XYBarRendererState
    extends XYItemRendererState {
        private double g2Base;

        public XYBarRendererState(PlotRenderingInfo info) {
            super(info);
        }

        public double getG2Base() {
            return this.g2Base;
        }

        public void setG2Base(double value) {
            this.g2Base = value;
        }
    }

}

