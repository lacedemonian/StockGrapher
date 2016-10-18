/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.category;

import java.awt.Color;
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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.StatisticalCategoryDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public class StatisticalBarRenderer
extends BarRenderer
implements CategoryItemRenderer,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -4986038395414039117L;
    private transient Paint errorIndicatorPaint = Color.gray;

    public Paint getErrorIndicatorPaint() {
        return this.errorIndicatorPaint;
    }

    public void setErrorIndicatorPaint(Paint paint) {
        this.errorIndicatorPaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset data, int row, int column, int pass) {
        if (!(data instanceof StatisticalCategoryDataset)) {
            throw new IllegalArgumentException("Requires StatisticalCategoryDataset.");
        }
        StatisticalCategoryDataset statData = (StatisticalCategoryDataset)data;
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            this.drawHorizontalItem(g2, state, dataArea, plot, domainAxis, rangeAxis, statData, row, column);
        } else if (orientation == PlotOrientation.VERTICAL) {
            this.drawVerticalItem(g2, state, dataArea, plot, domainAxis, rangeAxis, statData, row, column);
        }
    }

    protected void drawHorizontalItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, StatisticalCategoryDataset dataset, int row, int column) {
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        double rectY = domainAxis.getCategoryStart(column, this.getColumnCount(), dataArea, xAxisLocation);
        int seriesCount = this.getRowCount();
        int categoryCount = this.getColumnCount();
        if (seriesCount > 1) {
            double seriesGap = dataArea.getHeight() * this.getItemMargin() / (double)(categoryCount * (seriesCount - 1));
            rectY += (double)row * (state.getBarWidth() + seriesGap);
        } else {
            rectY += (double)row * state.getBarWidth();
        }
        Number meanValue = dataset.getMeanValue(row, column);
        double value = meanValue.doubleValue();
        double base = 0.0;
        double lclip = this.getLowerClip();
        double uclip = this.getUpperClip();
        if (uclip <= 0.0) {
            if (value >= uclip) {
                return;
            }
            base = uclip;
            if (value <= lclip) {
                value = lclip;
            }
        } else if (lclip <= 0.0) {
            if (value >= uclip) {
                value = uclip;
            } else if (value <= lclip) {
                value = lclip;
            }
        } else {
            if (value <= lclip) {
                return;
            }
            base = this.getLowerClip();
            if (value >= uclip) {
                value = uclip;
            }
        }
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transY1 = rangeAxis.valueToJava2D(base, dataArea, yAxisLocation);
        double transY2 = rangeAxis.valueToJava2D(value, dataArea, yAxisLocation);
        double rectX = Math.min(transY2, transY1);
        double rectHeight = state.getBarWidth();
        double rectWidth = Math.abs(transY2 - transY1);
        Rectangle2D.Double bar = new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
        Paint seriesPaint = this.getItemPaint(row, column);
        g2.setPaint(seriesPaint);
        g2.fill(bar);
        if (state.getBarWidth() > 3.0) {
            g2.setStroke(this.getItemStroke(row, column));
            g2.setPaint(this.getItemOutlinePaint(row, column));
            g2.draw(bar);
        }
        double valueDelta = dataset.getStdDevValue(row, column).doubleValue();
        double highVal = rangeAxis.valueToJava2D(meanValue.doubleValue() + valueDelta, dataArea, yAxisLocation);
        double lowVal = rangeAxis.valueToJava2D(meanValue.doubleValue() - valueDelta, dataArea, yAxisLocation);
        if (this.errorIndicatorPaint != null) {
            g2.setPaint(this.errorIndicatorPaint);
        } else {
            g2.setPaint(this.getItemOutlinePaint(row, column));
        }
        Line2D.Double line = null;
        line = new Line2D.Double(lowVal, rectY + rectHeight / 2.0, highVal, rectY + rectHeight / 2.0);
        g2.draw(line);
        line = new Line2D.Double(highVal, rectY + rectHeight * 0.25, highVal, rectY + rectHeight * 0.75);
        g2.draw(line);
        line = new Line2D.Double(lowVal, rectY + rectHeight * 0.25, lowVal, rectY + rectHeight * 0.75);
        g2.draw(line);
    }

    protected void drawVerticalItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, StatisticalCategoryDataset dataset, int row, int column) {
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        double rectX = domainAxis.getCategoryStart(column, this.getColumnCount(), dataArea, xAxisLocation);
        int seriesCount = this.getRowCount();
        int categoryCount = this.getColumnCount();
        if (seriesCount > 1) {
            double seriesGap = dataArea.getWidth() * this.getItemMargin() / (double)(categoryCount * (seriesCount - 1));
            rectX += (double)row * (state.getBarWidth() + seriesGap);
        } else {
            rectX += (double)row * state.getBarWidth();
        }
        Number meanValue = dataset.getMeanValue(row, column);
        double value = meanValue.doubleValue();
        double base = 0.0;
        double lclip = this.getLowerClip();
        double uclip = this.getUpperClip();
        if (uclip <= 0.0) {
            if (value >= uclip) {
                return;
            }
            base = uclip;
            if (value <= lclip) {
                value = lclip;
            }
        } else if (lclip <= 0.0) {
            if (value >= uclip) {
                value = uclip;
            } else if (value <= lclip) {
                value = lclip;
            }
        } else {
            if (value <= lclip) {
                return;
            }
            base = this.getLowerClip();
            if (value >= uclip) {
                value = uclip;
            }
        }
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transY1 = rangeAxis.valueToJava2D(base, dataArea, yAxisLocation);
        double transY2 = rangeAxis.valueToJava2D(value, dataArea, yAxisLocation);
        double rectY = Math.min(transY2, transY1);
        double rectWidth = state.getBarWidth();
        double rectHeight = Math.abs(transY2 - transY1);
        Rectangle2D.Double bar = new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
        Paint seriesPaint = this.getItemPaint(row, column);
        g2.setPaint(seriesPaint);
        g2.fill(bar);
        if (state.getBarWidth() > 3.0) {
            g2.setStroke(this.getItemStroke(row, column));
            g2.setPaint(this.getItemOutlinePaint(row, column));
            g2.draw(bar);
        }
        double valueDelta = dataset.getStdDevValue(row, column).doubleValue();
        double highVal = rangeAxis.valueToJava2D(meanValue.doubleValue() + valueDelta, dataArea, yAxisLocation);
        double lowVal = rangeAxis.valueToJava2D(meanValue.doubleValue() - valueDelta, dataArea, yAxisLocation);
        if (this.errorIndicatorPaint != null) {
            g2.setPaint(this.errorIndicatorPaint);
        } else {
            g2.setPaint(this.getItemOutlinePaint(row, column));
        }
        Line2D.Double line = null;
        line = new Line2D.Double(rectX + rectWidth / 2.0, lowVal, rectX + rectWidth / 2.0, highVal);
        g2.draw(line);
        line = new Line2D.Double(rectX + rectWidth / 2.0 - 5.0, highVal, rectX + rectWidth / 2.0 + 5.0, highVal);
        g2.draw(line);
        line = new Line2D.Double(rectX + rectWidth / 2.0 - 5.0, lowVal, rectX + rectWidth / 2.0 + 5.0, lowVal);
        g2.draw(line);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StatisticalBarRenderer)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        StatisticalBarRenderer that = (StatisticalBarRenderer)obj;
        if (!PaintUtilities.equal(this.errorIndicatorPaint, that.errorIndicatorPaint)) {
            return false;
        }
        return true;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.errorIndicatorPaint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.errorIndicatorPaint = SerialUtilities.readPaint(stream);
    }
}

