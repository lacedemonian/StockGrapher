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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.RendererChangeEvent;
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
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public class HighLowRenderer
extends AbstractXYItemRenderer
implements XYItemRenderer,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -8135673815876552516L;
    private boolean drawOpenTicks = true;
    private boolean drawCloseTicks = true;
    private transient Paint openTickPaint;
    private transient Paint closeTickPaint;

    public boolean getDrawOpenTicks() {
        return this.drawOpenTicks;
    }

    public void setDrawOpenTicks(boolean draw) {
        this.drawOpenTicks = draw;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getDrawCloseTicks() {
        return this.drawCloseTicks;
    }

    public void setDrawCloseTicks(boolean draw) {
        this.drawCloseTicks = draw;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public Paint getOpenTickPaint() {
        return this.openTickPaint;
    }

    public void setOpenTickPaint(Paint paint) {
        this.openTickPaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public Paint getCloseTickPaint() {
        return this.closeTickPaint;
    }

    public void setCloseTickPaint(Paint paint) {
        this.closeTickPaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        Number x = dataset.getX(series, item);
        if (x == null) {
            return;
        }
        double xdouble = x.doubleValue();
        if (!domainAxis.getRange().contains(xdouble)) {
            return;
        }
        double xx = domainAxis.valueToJava2D(xdouble, dataArea, plot.getDomainAxisEdge());
        Rectangle2D.Double entityArea = null;
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }
        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge location = plot.getRangeAxisEdge();
        Paint itemPaint = this.getItemPaint(series, item);
        Stroke itemStroke = this.getItemStroke(series, item);
        g2.setPaint(itemPaint);
        g2.setStroke(itemStroke);
        if (dataset instanceof OHLCDataset) {
            double yOpen;
            double yClose;
            OHLCDataset hld = (OHLCDataset)dataset;
            double yHigh = hld.getHighValue(series, item);
            double yLow = hld.getLowValue(series, item);
            if (!Double.isNaN(yHigh) && !Double.isNaN(yLow)) {
                double yyHigh = rangeAxis.valueToJava2D(yHigh, dataArea, location);
                double yyLow = rangeAxis.valueToJava2D(yLow, dataArea, location);
                if (orientation == PlotOrientation.HORIZONTAL) {
                    g2.draw(new Line2D.Double(yyLow, xx, yyHigh, xx));
                    entityArea = new Rectangle2D.Double(Math.min(yyLow, yyHigh), xx - 1.0, Math.abs(yyHigh - yyLow), 2.0);
                } else if (orientation == PlotOrientation.VERTICAL) {
                    g2.draw(new Line2D.Double(xx, yyLow, xx, yyHigh));
                    entityArea = new Rectangle2D.Double(xx - 1.0, Math.min(yyLow, yyHigh), 2.0, Math.abs(yyHigh - yyLow));
                }
            }
            double delta = 2.0;
            if (domainAxis.isInverted()) {
                delta = - delta;
            }
            if (this.getDrawOpenTicks() && !Double.isNaN(yOpen = hld.getOpenValue(series, item))) {
                double yyOpen = rangeAxis.valueToJava2D(yOpen, dataArea, location);
                if (this.openTickPaint != null) {
                    g2.setPaint(this.openTickPaint);
                } else {
                    g2.setPaint(itemPaint);
                }
                if (orientation == PlotOrientation.HORIZONTAL) {
                    g2.draw(new Line2D.Double(yyOpen, xx + delta, yyOpen, xx));
                } else if (orientation == PlotOrientation.VERTICAL) {
                    g2.draw(new Line2D.Double(xx - delta, yyOpen, xx, yyOpen));
                }
            }
            if (this.getDrawCloseTicks() && !Double.isNaN(yClose = hld.getCloseValue(series, item))) {
                double yyClose = rangeAxis.valueToJava2D(yClose, dataArea, location);
                if (this.closeTickPaint != null) {
                    g2.setPaint(this.closeTickPaint);
                } else {
                    g2.setPaint(itemPaint);
                }
                if (orientation == PlotOrientation.HORIZONTAL) {
                    g2.draw(new Line2D.Double(yyClose, xx, yyClose, xx - delta));
                } else if (orientation == PlotOrientation.VERTICAL) {
                    g2.draw(new Line2D.Double(xx, yyClose, xx + delta, yyClose));
                }
            }
        } else if (item > 0) {
            Number x0 = dataset.getX(series, item - 1);
            Number y0 = dataset.getY(series, item - 1);
            Number y = dataset.getY(series, item);
            if (x0 == null || y0 == null || y == null) {
                return;
            }
            double xx0 = domainAxis.valueToJava2D(x0.doubleValue(), dataArea, plot.getDomainAxisEdge());
            double yy0 = rangeAxis.valueToJava2D(y0.doubleValue(), dataArea, location);
            double yy = rangeAxis.valueToJava2D(y.doubleValue(), dataArea, location);
            if (orientation == PlotOrientation.HORIZONTAL) {
                g2.draw(new Line2D.Double(yy0, xx0, yy, xx));
            } else if (orientation == PlotOrientation.VERTICAL) {
                g2.draw(new Line2D.Double(xx0, yy0, xx, yy));
            }
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
            XYItemEntity entity = new XYItemEntity(entityArea, dataset, series, item, tip, url);
            entities.add(entity);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HighLowRenderer)) {
            return false;
        }
        HighLowRenderer that = (HighLowRenderer)obj;
        if (this.drawOpenTicks != that.drawOpenTicks) {
            return false;
        }
        if (this.drawCloseTicks != that.drawCloseTicks) {
            return false;
        }
        if (!PaintUtilities.equal(this.openTickPaint, that.openTickPaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.closeTickPaint, that.closeTickPaint)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.openTickPaint = SerialUtilities.readPaint(stream);
        this.closeTickPaint = SerialUtilities.readPaint(stream);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.openTickPaint, stream);
        SerialUtilities.writePaint(this.closeTickPaint, stream);
    }
}

