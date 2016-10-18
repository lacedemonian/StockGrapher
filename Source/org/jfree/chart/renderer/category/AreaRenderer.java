/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.category;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AreaRendererEndType;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class AreaRenderer
extends AbstractCategoryItemRenderer
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -4231878281385812757L;
    private AreaRendererEndType endType = AreaRendererEndType.TAPER;

    public AreaRendererEndType getEndType() {
        return this.endType;
    }

    public void setEndType(AreaRendererEndType type) {
        if (type == null) {
            throw new IllegalArgumentException("Null 'type' argument.");
        }
        this.endType = type;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public LegendItem getLegendItem(int datasetIndex, int series) {
        String label;
        CategoryPlot cp = this.getPlot();
        if (cp == null) {
            return null;
        }
        CategoryDataset dataset = cp.getDataset(datasetIndex);
        String description = label = this.getLegendItemLabelGenerator().generateLabel(dataset, series);
        String toolTipText = null;
        if (this.getLegendItemToolTipGenerator() != null) {
            toolTipText = this.getLegendItemToolTipGenerator().generateLabel(dataset, series);
        }
        String urlText = null;
        if (this.getLegendItemURLGenerator() != null) {
            urlText = this.getLegendItemURLGenerator().generateLabel(dataset, series);
        }
        Rectangle2D.Double shape = new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0);
        Paint paint = this.getSeriesPaint(series);
        Paint outlinePaint = this.getSeriesOutlinePaint(series);
        Stroke outlineStroke = this.getSeriesOutlineStroke(series);
        return new LegendItem(label, description, toolTipText, urlText, (Shape)shape, paint, outlineStroke, outlinePaint);
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
        Number value = dataset.getValue(row, column);
        if (value != null) {
            Number n2;
            EntityCollection entities;
            Number n0;
            PlotOrientation orientation = plot.getOrientation();
            RectangleEdge axisEdge = plot.getDomainAxisEdge();
            int count = dataset.getColumnCount();
            float x0 = (float)domainAxis.getCategoryStart(column, count, dataArea, axisEdge);
            float x1 = (float)domainAxis.getCategoryMiddle(column, count, dataArea, axisEdge);
            float x2 = (float)domainAxis.getCategoryEnd(column, count, dataArea, axisEdge);
            x0 = Math.round(x0);
            x1 = Math.round(x1);
            x2 = Math.round(x2);
            if (this.endType == AreaRendererEndType.TRUNCATE) {
                if (column == 0) {
                    x0 = x1;
                } else if (column == this.getColumnCount() - 1) {
                    x2 = x1;
                }
            }
            double yy1 = value.doubleValue();
            double yy0 = 0.0;
            if (column > 0 && (n0 = dataset.getValue(row, column - 1)) != null) {
                yy0 = (n0.doubleValue() + yy1) / 2.0;
            }
            double yy2 = 0.0;
            if (column < dataset.getColumnCount() - 1 && (n2 = dataset.getValue(row, column + 1)) != null) {
                yy2 = (n2.doubleValue() + yy1) / 2.0;
            }
            RectangleEdge edge = plot.getRangeAxisEdge();
            float y0 = (float)rangeAxis.valueToJava2D(yy0, dataArea, edge);
            float y1 = (float)rangeAxis.valueToJava2D(yy1, dataArea, edge);
            float y2 = (float)rangeAxis.valueToJava2D(yy2, dataArea, edge);
            float yz = (float)rangeAxis.valueToJava2D(0.0, dataArea, edge);
            g2.setPaint(this.getItemPaint(row, column));
            g2.setStroke(this.getItemStroke(row, column));
            GeneralPath area = new GeneralPath();
            if (orientation == PlotOrientation.VERTICAL) {
                area.moveTo(x0, yz);
                area.lineTo(x0, y0);
                area.lineTo(x1, y1);
                area.lineTo(x2, y2);
                area.lineTo(x2, yz);
            } else if (orientation == PlotOrientation.HORIZONTAL) {
                area.moveTo(yz, x0);
                area.lineTo(y0, x0);
                area.lineTo(y1, x1);
                area.lineTo(y2, x2);
                area.lineTo(yz, x2);
            }
            area.closePath();
            g2.setPaint(this.getItemPaint(row, column));
            g2.fill(area);
            if (this.isItemLabelVisible(row, column)) {
                this.drawItemLabel(g2, orientation, dataset, row, column, x1, y1, value.doubleValue() < 0.0);
            }
            if ((entities = state.getEntityCollection()) != null) {
                this.addItemEntity(entities, dataset, row, column, area);
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

