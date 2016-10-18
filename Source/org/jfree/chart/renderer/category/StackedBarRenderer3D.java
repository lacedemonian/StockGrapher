/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.category;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class StackedBarRenderer3D
extends BarRenderer3D
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -5832945916493247123L;

    public StackedBarRenderer3D() {
    }

    public StackedBarRenderer3D(double xOffset, double yOffset) {
        super(xOffset, yOffset);
    }

    public Range findRangeBounds(CategoryDataset dataset) {
        return DatasetUtilities.findStackedRangeBounds(dataset);
    }

    protected void calculateBarWidth(CategoryPlot plot, Rectangle2D dataArea, int rendererIndex, CategoryItemRendererState state) {
        CategoryAxis domainAxis = this.getDomainAxis(plot, rendererIndex);
        CategoryDataset data = plot.getDataset(rendererIndex);
        if (data != null) {
            PlotOrientation orientation = plot.getOrientation();
            double space = 0.0;
            if (orientation == PlotOrientation.HORIZONTAL) {
                space = dataArea.getHeight();
            } else if (orientation == PlotOrientation.VERTICAL) {
                space = dataArea.getWidth();
            }
            double maxWidth = space * this.getMaximumBarWidth();
            int columns = data.getColumnCount();
            double categoryMargin = 0.0;
            if (columns > 1) {
                categoryMargin = domainAxis.getCategoryMargin();
            }
            double used = space * (1.0 - domainAxis.getLowerMargin() - domainAxis.getUpperMargin() - categoryMargin);
            if (columns > 0) {
                state.setBarWidth(Math.min(used / (double)columns, maxWidth));
            } else {
                state.setBarWidth(Math.min(used, maxWidth));
            }
        }
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
        double positiveBase;
        double translatedValue;
        double translatedBase;
        CategoryItemLabelGenerator generator;
        Number dataValue = dataset.getValue(row, column);
        if (dataValue == null) {
            return;
        }
        double value = dataValue.doubleValue();
        Rectangle2D.Double adjusted = new Rectangle2D.Double(dataArea.getX(), dataArea.getY() + this.getYOffset(), dataArea.getWidth() - this.getXOffset(), dataArea.getHeight() - this.getYOffset());
        PlotOrientation orientation = plot.getOrientation();
        double barW0 = domainAxis.getCategoryMiddle(column, this.getColumnCount(), adjusted, plot.getDomainAxisEdge()) - state.getBarWidth() / 2.0;
        double negativeBase = positiveBase = this.getBase();
        for (int i = 0; i < row; ++i) {
            Number v = dataset.getValue(i, column);
            if (v == null) continue;
            double d = v.doubleValue();
            if (d > 0.0) {
                positiveBase += d;
                continue;
            }
            negativeBase += d;
        }
        RectangleEdge location = plot.getRangeAxisEdge();
        if (value > 0.0) {
            translatedBase = rangeAxis.valueToJava2D(positiveBase, adjusted, location);
            translatedValue = rangeAxis.valueToJava2D(positiveBase + value, adjusted, location);
        } else {
            translatedBase = rangeAxis.valueToJava2D(negativeBase, adjusted, location);
            translatedValue = rangeAxis.valueToJava2D(negativeBase + value, adjusted, location);
        }
        double barL0 = Math.min(translatedBase, translatedValue);
        double barLength = Math.max(Math.abs(translatedValue - translatedBase), this.getMinimumBarLength());
        Rectangle2D.Double bar = null;
        bar = orientation == PlotOrientation.HORIZONTAL ? new Rectangle2D.Double(barL0, barW0, barLength, state.getBarWidth()) : new Rectangle2D.Double(barW0, barL0, state.getBarWidth(), barLength);
        Paint itemPaint = this.getItemPaint(row, column);
        g2.setPaint(itemPaint);
        g2.fill(bar);
        if (pass == 0) {
            EntityCollection entities;
            double x0 = bar.getMinX();
            double x1 = x0 + this.getXOffset();
            double x2 = bar.getMaxX();
            double x3 = x2 + this.getXOffset();
            double y0 = bar.getMinY() - this.getYOffset();
            double y1 = bar.getMinY();
            double y2 = bar.getMaxY() - this.getYOffset();
            double y3 = bar.getMaxY();
            GeneralPath bar3dRight = null;
            GeneralPath bar3dTop = null;
            if (value > 0.0 || orientation == PlotOrientation.VERTICAL) {
                bar3dRight = new GeneralPath();
                bar3dRight.moveTo((float)x2, (float)y3);
                bar3dRight.lineTo((float)x2, (float)y1);
                bar3dRight.lineTo((float)x3, (float)y0);
                bar3dRight.lineTo((float)x3, (float)y2);
                bar3dRight.closePath();
                if (itemPaint instanceof Color) {
                    g2.setPaint(((Color)itemPaint).darker());
                }
                g2.fill(bar3dRight);
            }
            if (value > 0.0 || orientation == PlotOrientation.HORIZONTAL) {
                bar3dTop = new GeneralPath();
                bar3dTop.moveTo((float)x0, (float)y1);
                bar3dTop.lineTo((float)x1, (float)y0);
                bar3dTop.lineTo((float)x3, (float)y0);
                bar3dTop.lineTo((float)x2, (float)y1);
                bar3dTop.closePath();
                g2.fill(bar3dTop);
            }
            if (this.isDrawBarOutline() && state.getBarWidth() > 3.0) {
                g2.setStroke(this.getItemOutlineStroke(row, column));
                g2.setPaint(this.getItemOutlinePaint(row, column));
                g2.draw(bar);
                if (bar3dRight != null) {
                    g2.draw(bar3dRight);
                }
                if (bar3dTop != null) {
                    g2.draw(bar3dTop);
                }
            }
            if ((entities = state.getEntityCollection()) != null) {
                this.addItemEntity(entities, dataset, row, column, bar);
            }
        } else if (pass == 1 && (generator = this.getItemLabelGenerator(row, column)) != null && this.isItemLabelVisible(row, column)) {
            this.drawItemLabel(g2, dataset, row, column, plot, generator, bar, value < 0.0);
        }
    }

    public int getPassCount() {
        return 2;
    }
}

