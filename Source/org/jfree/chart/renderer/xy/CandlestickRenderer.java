/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
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
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class CandlestickRenderer
extends AbstractXYItemRenderer
implements XYItemRenderer,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 50390395841817121L;
    public static final int WIDTHMETHOD_AVERAGE = 0;
    public static final int WIDTHMETHOD_SMALLEST = 1;
    public static final int WIDTHMETHOD_INTERVALDATA = 2;
    private int autoWidthMethod = 0;
    private double autoWidthFactor = 0.6428571428571429;
    private double autoWidthGap = 0.0;
    private double candleWidth;
    private double maxCandleWidthInMilliseconds = 7.2E7;
    private double maxCandleWidth;
    private transient Paint upPaint;
    private transient Paint downPaint;
    private boolean drawVolume;
    private transient double maxVolume;

    public CandlestickRenderer() {
        this(-1.0);
    }

    public CandlestickRenderer(double candleWidth) {
        this(candleWidth, true, new HighLowItemLabelGenerator());
    }

    public CandlestickRenderer(double candleWidth, boolean drawVolume, XYToolTipGenerator toolTipGenerator) {
        this.setToolTipGenerator(toolTipGenerator);
        this.candleWidth = candleWidth;
        this.drawVolume = drawVolume;
        this.upPaint = Color.green;
        this.downPaint = Color.red;
    }

    public double getCandleWidth() {
        return this.candleWidth;
    }

    public void setCandleWidth(double width) {
        if (width != this.candleWidth) {
            this.candleWidth = width;
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public double getMaxCandleWidthInMilliseconds() {
        return this.maxCandleWidthInMilliseconds;
    }

    public void setMaxCandleWidthInMilliseconds(double millis) {
        this.maxCandleWidthInMilliseconds = millis;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public int getAutoWidthMethod() {
        return this.autoWidthMethod;
    }

    public void setAutoWidthMethod(int autoWidthMethod) {
        if (this.autoWidthMethod != autoWidthMethod) {
            this.autoWidthMethod = autoWidthMethod;
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public double getAutoWidthFactor() {
        return this.autoWidthFactor;
    }

    public void setAutoWidthFactor(double autoWidthFactor) {
        if (this.autoWidthFactor != autoWidthFactor) {
            this.autoWidthFactor = autoWidthFactor;
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public double getAutoWidthGap() {
        return this.autoWidthGap;
    }

    public void setAutoWidthGap(double autoWidthGap) {
        if (this.autoWidthGap != autoWidthGap) {
            this.autoWidthGap = autoWidthGap;
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getUpPaint() {
        return this.upPaint;
    }

    public void setUpPaint(Paint paint) {
        this.upPaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public Paint getDownPaint() {
        return this.downPaint;
    }

    public void setDownPaint(Paint paint) {
        this.downPaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean drawVolume() {
        return this.drawVolume;
    }

    public void setDrawVolume(boolean flag) {
        if (this.drawVolume != flag) {
            this.drawVolume = flag;
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset dataset, PlotRenderingInfo info) {
        ValueAxis axis = plot.getDomainAxis();
        double x1 = axis.getLowerBound();
        double x2 = x1 + this.maxCandleWidthInMilliseconds;
        RectangleEdge edge = plot.getDomainAxisEdge();
        double xx1 = axis.valueToJava2D(x1, dataArea, edge);
        double xx2 = axis.valueToJava2D(x2, dataArea, edge);
        this.maxCandleWidth = Math.abs(xx2 - xx1);
        if (this.drawVolume) {
            OHLCDataset highLowDataset = (OHLCDataset)dataset;
            this.maxVolume = 0.0;
            for (int series = 0; series < highLowDataset.getSeriesCount(); ++series) {
                for (int item = 0; item < highLowDataset.getItemCount(series); ++item) {
                    double volume = highLowDataset.getVolumeValue(series, item);
                    if (volume <= this.maxVolume) continue;
                    this.maxVolume = volume;
                }
            }
        }
        return new XYItemRendererState(info);
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        double stickWidth;
        boolean horiz;
        double volumeWidth;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            horiz = true;
        } else if (orientation == PlotOrientation.VERTICAL) {
            horiz = false;
        } else {
            return;
        }
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }
        OHLCDataset highLowData = (OHLCDataset)dataset;
        Number x = highLowData.getX(series, item);
        Number yHigh = highLowData.getHigh(series, item);
        Number yLow = highLowData.getLow(series, item);
        Number yOpen = highLowData.getOpen(series, item);
        Number yClose = highLowData.getClose(series, item);
        RectangleEdge domainEdge = plot.getDomainAxisEdge();
        double xx = domainAxis.valueToJava2D(x.doubleValue(), dataArea, domainEdge);
        RectangleEdge edge = plot.getRangeAxisEdge();
        double yyHigh = rangeAxis.valueToJava2D(yHigh.doubleValue(), dataArea, edge);
        double yyLow = rangeAxis.valueToJava2D(yLow.doubleValue(), dataArea, edge);
        double yyOpen = rangeAxis.valueToJava2D(yOpen.doubleValue(), dataArea, edge);
        double yyClose = rangeAxis.valueToJava2D(yClose.doubleValue(), dataArea, edge);
        if (this.candleWidth > 0.0) {
            volumeWidth = this.candleWidth;
            stickWidth = this.candleWidth;
        } else {
            double xxWidth = 0.0;
            switch (this.autoWidthMethod) {
                int itemCount;
                case 0: {
                    itemCount = highLowData.getItemCount(series);
                    if (horiz) {
                        xxWidth = dataArea.getHeight() / (double)itemCount;
                        break;
                    }
                    xxWidth = dataArea.getWidth() / (double)itemCount;
                    break;
                }
                case 1: {
                    itemCount = highLowData.getItemCount(series);
                    double lastPos = -1.0;
                    xxWidth = dataArea.getWidth();
                    for (int i = 0; i < itemCount; ++i) {
                        double pos = domainAxis.valueToJava2D(highLowData.getXValue(series, i), dataArea, domainEdge);
                        if (lastPos != -1.0) {
                            xxWidth = Math.min(xxWidth, Math.abs(pos - lastPos));
                        }
                        lastPos = pos;
                    }
                    break;
                }
                case 2: {
                    IntervalXYDataset intervalXYData = (IntervalXYDataset)dataset;
                    double startPos = domainAxis.valueToJava2D(intervalXYData.getStartXValue(series, item), dataArea, plot.getDomainAxisEdge());
                    double endPos = domainAxis.valueToJava2D(intervalXYData.getEndXValue(series, item), dataArea, plot.getDomainAxisEdge());
                    xxWidth = Math.abs(endPos - startPos);
                }
            }
            xxWidth -= 2.0 * this.autoWidthGap;
            xxWidth *= this.autoWidthFactor;
            xxWidth = Math.min(xxWidth, this.maxCandleWidth);
            volumeWidth = Math.max(Math.min(1.0, this.maxCandleWidth), xxWidth);
            stickWidth = Math.max(Math.min(3.0, this.maxCandleWidth), xxWidth);
        }
        Paint p = this.getItemPaint(series, item);
        Stroke s = this.getItemStroke(series, item);
        g2.setStroke(s);
        if (this.drawVolume) {
            double min;
            double max;
            int volume = (int)highLowData.getVolumeValue(series, item);
            double volumeHeight = (double)volume / this.maxVolume;
            if (horiz) {
                min = dataArea.getMinX();
                max = dataArea.getMaxX();
            } else {
                min = dataArea.getMinY();
                max = dataArea.getMaxY();
            }
            double zzVolume = volumeHeight * (max - min);
            g2.setPaint(Color.gray);
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(3, 0.3f));
            if (horiz) {
                g2.fill(new Rectangle2D.Double(min, xx - volumeWidth / 2.0, zzVolume, volumeWidth));
            } else {
                g2.fill(new Rectangle2D.Double(xx - volumeWidth / 2.0, max - zzVolume, volumeWidth, zzVolume));
            }
            g2.setComposite(originalComposite);
        }
        g2.setPaint(p);
        double yyMaxOpenClose = Math.max(yyOpen, yyClose);
        double yyMinOpenClose = Math.min(yyOpen, yyClose);
        double maxOpenClose = Math.max(yOpen.doubleValue(), yClose.doubleValue());
        double minOpenClose = Math.min(yOpen.doubleValue(), yClose.doubleValue());
        if (yHigh.doubleValue() > maxOpenClose) {
            if (horiz) {
                g2.draw(new Line2D.Double(yyHigh, xx, yyMaxOpenClose, xx));
            } else {
                g2.draw(new Line2D.Double(xx, yyHigh, xx, yyMaxOpenClose));
            }
        }
        if (yLow.doubleValue() < minOpenClose) {
            if (horiz) {
                g2.draw(new Line2D.Double(yyLow, xx, yyMinOpenClose, xx));
            } else {
                g2.draw(new Line2D.Double(xx, yyLow, xx, yyMinOpenClose));
            }
        }
        Rectangle2D.Double body = null;
        body = horiz ? new Rectangle2D.Double(yyMinOpenClose, xx - stickWidth / 2.0, yyMaxOpenClose - yyMinOpenClose, stickWidth) : new Rectangle2D.Double(xx - stickWidth / 2.0, yyMinOpenClose, stickWidth, yyMaxOpenClose - yyMinOpenClose);
        if (yClose.doubleValue() > yOpen.doubleValue()) {
            if (this.upPaint != null) {
                g2.setPaint(this.upPaint);
                g2.fill(body);
            }
        } else {
            if (this.downPaint != null) {
                g2.setPaint(this.downPaint);
            }
            g2.fill(body);
        }
        g2.setPaint(p);
        g2.draw(body);
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
            XYItemEntity entity = new XYItemEntity(body, dataset, series, item, tip, url);
            entities.add(entity);
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof CandlestickRenderer) {
            CandlestickRenderer renderer = (CandlestickRenderer)obj;
            boolean result = super.equals(obj);
            result = result && this.candleWidth == renderer.getCandleWidth();
            result = result && this.upPaint.equals(renderer.getUpPaint());
            result = result && this.downPaint.equals(renderer.getDownPaint());
            result = result && this.drawVolume == renderer.drawVolume;
            return result;
        }
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.upPaint, stream);
        SerialUtilities.writePaint(this.downPaint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.upPaint = SerialUtilities.readPaint(stream);
        this.downPaint = SerialUtilities.readPaint(stream);
    }
}

