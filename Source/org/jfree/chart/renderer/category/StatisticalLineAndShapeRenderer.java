/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.category;

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
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.StatisticalCategoryDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

public class StatisticalLineAndShapeRenderer
extends LineAndShapeRenderer
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -3557517173697777579L;
    private transient Paint errorIndicatorPaint = null;

    public StatisticalLineAndShapeRenderer() {
        this(true, true);
    }

    public StatisticalLineAndShapeRenderer(boolean linesVisible, boolean shapesVisible) {
        super(true, true);
    }

    public Paint getErrorIndicatorPaint() {
        return this.errorIndicatorPaint;
    }

    public void setErrorIndicatorPaint(Paint paint) {
        this.errorIndicatorPaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
        Number previousValue;
        EntityCollection entities;
        Number v = dataset.getValue(row, column);
        if (v == null) {
            return;
        }
        StatisticalCategoryDataset statData = (StatisticalCategoryDataset)dataset;
        Number meanValue = statData.getMeanValue(row, column);
        PlotOrientation orientation = plot.getOrientation();
        double x1 = domainAxis.getCategoryMiddle(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double y1 = rangeAxis.valueToJava2D(meanValue.doubleValue(), dataArea, plot.getRangeAxisEdge());
        Shape shape = this.getItemShape(row, column);
        if (orientation == PlotOrientation.HORIZONTAL) {
            shape = ShapeUtilities.createTranslatedShape(shape, y1, x1);
        } else if (orientation == PlotOrientation.VERTICAL) {
            shape = ShapeUtilities.createTranslatedShape(shape, x1, y1);
        }
        if (this.getItemShapeVisible(row, column)) {
            if (this.getItemShapeFilled(row, column)) {
                g2.setPaint(this.getItemPaint(row, column));
                g2.fill(shape);
            } else {
                if (this.getUseOutlinePaint()) {
                    g2.setPaint(this.getItemOutlinePaint(row, column));
                } else {
                    g2.setPaint(this.getItemPaint(row, column));
                }
                g2.setStroke(this.getItemOutlineStroke(row, column));
                g2.draw(shape);
            }
        }
        if (this.getItemLineVisible(row, column) && column != 0 && (previousValue = statData.getValue(row, column - 1)) != null) {
            double previous = previousValue.doubleValue();
            double x0 = domainAxis.getCategoryMiddle(column - 1, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
            double y0 = rangeAxis.valueToJava2D(previous, dataArea, plot.getRangeAxisEdge());
            Line2D.Double line = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                line = new Line2D.Double(y0, x0, y1, x1);
            } else if (orientation == PlotOrientation.VERTICAL) {
                line = new Line2D.Double(x0, y0, x1, y1);
            }
            g2.setPaint(this.getItemPaint(row, column));
            g2.setStroke(this.getItemStroke(row, column));
            g2.draw(line);
        }
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        double rectX = domainAxis.getCategoryStart(column, this.getColumnCount(), dataArea, xAxisLocation);
        rectX += (double)row * state.getBarWidth();
        g2.setPaint(this.getItemPaint(row, column));
        double valueDelta = statData.getStdDevValue(row, column).doubleValue();
        double highVal = meanValue.doubleValue() + valueDelta > rangeAxis.getRange().getUpperBound() ? rangeAxis.valueToJava2D(rangeAxis.getRange().getUpperBound(), dataArea, yAxisLocation) : rangeAxis.valueToJava2D(meanValue.doubleValue() + valueDelta, dataArea, yAxisLocation);
        double lowVal = meanValue.doubleValue() + valueDelta < rangeAxis.getRange().getLowerBound() ? rangeAxis.valueToJava2D(rangeAxis.getRange().getLowerBound(), dataArea, yAxisLocation) : rangeAxis.valueToJava2D(meanValue.doubleValue() - valueDelta, dataArea, yAxisLocation);
        if (this.errorIndicatorPaint != null) {
            g2.setPaint(this.errorIndicatorPaint);
        } else {
            g2.setPaint(this.getItemPaint(row, column));
        }
        Line2D.Double line = null;
        line = new Line2D.Double(x1, lowVal, x1, highVal);
        g2.draw(line);
        line = new Line2D.Double(x1 - 5.0, highVal, x1 + 5.0, highVal);
        g2.draw(line);
        line = new Line2D.Double(x1 - 5.0, lowVal, x1 + 5.0, lowVal);
        g2.draw(line);
        if (this.isItemLabelVisible(row, column)) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                this.drawItemLabel(g2, orientation, dataset, row, column, y1, x1, meanValue.doubleValue() < 0.0);
            } else if (orientation == PlotOrientation.VERTICAL) {
                this.drawItemLabel(g2, orientation, dataset, row, column, x1, y1, meanValue.doubleValue() < 0.0);
            }
        }
        if (state.getInfo() != null && (entities = state.getEntityCollection()) != null && shape != null) {
            String tip = null;
            CategoryToolTipGenerator tipster = this.getToolTipGenerator(row, column);
            if (tipster != null) {
                tip = tipster.generateToolTip(dataset, row, column);
            }
            String url = null;
            if (this.getItemURLGenerator(row, column) != null) {
                url = this.getItemURLGenerator(row, column).generateURL(dataset, row, column);
            }
            CategoryItemEntity entity = new CategoryItemEntity(shape, tip, url, dataset, row, dataset.getColumnKey(column), column);
            entities.add(entity);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StatisticalLineAndShapeRenderer)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        StatisticalLineAndShapeRenderer that = (StatisticalLineAndShapeRenderer)obj;
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

