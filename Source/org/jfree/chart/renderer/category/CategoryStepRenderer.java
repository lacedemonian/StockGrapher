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
import java.io.Serializable;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class CategoryStepRenderer
extends AbstractCategoryItemRenderer
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -5121079703118261470L;
    public static final int STAGGER_WIDTH = 5;
    private boolean stagger = false;
    private transient Line2D line = new Line2D.Double(0.0, 0.0, 0.0, 0.0);

    public CategoryStepRenderer() {
        this(false);
    }

    public CategoryStepRenderer(boolean stagger) {
        this.stagger = stagger;
    }

    public boolean getStagger() {
        return this.stagger;
    }

    public void setStagger(boolean shouldStagger) {
        this.stagger = shouldStagger;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    protected void drawLine(Graphics2D g2, PlotOrientation orientation, double x0, double y0, double x1, double y1) {
        if (orientation == PlotOrientation.VERTICAL) {
            this.line.setLine(x0, y0, x1, y1);
            g2.draw(this.line);
        } else if (orientation == PlotOrientation.HORIZONTAL) {
            this.line.setLine(y0, x0, y1, x1);
            g2.draw(this.line);
        }
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
        Number previousValue;
        Number value = dataset.getValue(row, column);
        if (value == null) {
            return;
        }
        PlotOrientation orientation = plot.getOrientation();
        double x1s = domainAxis.getCategoryStart(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double x1 = domainAxis.getCategoryMiddle(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double x1e = 2.0 * x1 - x1s;
        double y1 = rangeAxis.valueToJava2D(value.doubleValue(), dataArea, plot.getRangeAxisEdge());
        g2.setPaint(this.getItemPaint(row, column));
        g2.setStroke(this.getItemStroke(row, column));
        if (column != 0 && (previousValue = dataset.getValue(row, column - 1)) != null) {
            double previous = previousValue.doubleValue();
            double x0s = domainAxis.getCategoryStart(column - 1, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
            double x0 = domainAxis.getCategoryMiddle(column - 1, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
            double x0e = 2.0 * x0 - x0s;
            double y0 = rangeAxis.valueToJava2D(previous, dataArea, plot.getRangeAxisEdge());
            if (this.getStagger()) {
                int xStagger = row * 5;
                if ((double)xStagger > x1s - x0e) {
                    xStagger = (int)(x1s - x0e);
                }
                x1s = x0e + (double)xStagger;
            }
            this.drawLine(g2, orientation, x0e, y0, x1s, y0);
            this.drawLine(g2, orientation, x1s, y0, x1s, y1);
        }
        this.drawLine(g2, orientation, x1s, y1, x1e, y1);
        if (this.isItemLabelVisible(row, column)) {
            this.drawItemLabel(g2, orientation, dataset, row, column, x1, y1, value.doubleValue() < 0.0);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CategoryStepRenderer)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        CategoryStepRenderer that = (CategoryStepRenderer)obj;
        if (this.stagger != that.stagger) {
            return false;
        }
        return true;
    }
}

