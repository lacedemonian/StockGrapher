/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.category;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class StackedAreaRenderer
extends AreaRenderer
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -3595635038460823663L;

    public Range findRangeBounds(CategoryDataset dataset) {
        return DatasetUtilities.findStackedRangeBounds(dataset);
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
        EntityCollection entities;
        Number previousValue;
        Number value = dataset.getValue(row, column);
        if (value == null) {
            return;
        }
        double x1 = domainAxis.getCategoryMiddle(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double y1 = 0.0;
        double y1Untranslated = value.doubleValue();
        g2.setPaint(this.getItemPaint(row, column));
        g2.setStroke(this.getItemStroke(row, column));
        if (column != 0 && (previousValue = dataset.getValue(row, column - 1)) != null) {
            double x0 = domainAxis.getCategoryMiddle(column - 1, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
            double y0Untranslated = previousValue.doubleValue();
            double previousHeightx0Untranslated = this.getPreviousHeight(dataset, row, column - 1);
            double previousHeightx1Untranslated = this.getPreviousHeight(dataset, row, column);
            RectangleEdge location = plot.getRangeAxisEdge();
            double previousHeightx0 = rangeAxis.valueToJava2D(previousHeightx0Untranslated, dataArea, location);
            double previousHeightx1 = rangeAxis.valueToJava2D(previousHeightx1Untranslated, dataArea, location);
            double y0 = rangeAxis.valueToJava2D(y0Untranslated += previousHeightx0Untranslated, dataArea, location);
            y1 = rangeAxis.valueToJava2D(y1Untranslated += previousHeightx1Untranslated, dataArea, location);
            Polygon p = null;
            PlotOrientation orientation = plot.getOrientation();
            if (orientation == PlotOrientation.HORIZONTAL) {
                p = new Polygon();
                p.addPoint((int)y0, (int)x0);
                p.addPoint((int)y1, (int)x1);
                p.addPoint((int)previousHeightx1, (int)x1);
                p.addPoint((int)previousHeightx0, (int)x0);
            } else if (orientation == PlotOrientation.VERTICAL) {
                p = new Polygon();
                p.addPoint((int)x0, (int)y0);
                p.addPoint((int)x1, (int)y1);
                p.addPoint((int)x1, (int)previousHeightx1);
                p.addPoint((int)x0, (int)previousHeightx0);
            }
            g2.setPaint(this.getItemPaint(row, column));
            g2.setStroke(this.getItemStroke(row, column));
            g2.fill(p);
        }
        if ((entities = state.getEntityCollection()) != null) {
            Rectangle2D.Double shape = new Rectangle2D.Double(x1 - 3.0, y1 - 3.0, 6.0, 6.0);
            this.addItemEntity(entities, dataset, row, column, shape);
        }
    }

    protected double getPreviousHeight(CategoryDataset data, int series, int category) {
        double result = 0.0;
        for (int i = 0; i < series; ++i) {
            Number tmp = data.getValue(i, category);
            if (tmp == null) continue;
            result += tmp.doubleValue();
        }
        return result;
    }
}

