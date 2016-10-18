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
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.BooleanList;
import org.jfree.util.BooleanUtilities;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

public class LineAndShapeRenderer
extends AbstractCategoryItemRenderer
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -197749519869226398L;
    private Boolean linesVisible = null;
    private BooleanList seriesLinesVisible = new BooleanList();
    private boolean baseLinesVisible;
    private Boolean shapesVisible;
    private BooleanList seriesShapesVisible;
    private boolean baseShapesVisible;
    private Boolean shapesFilled;
    private BooleanList seriesShapesFilled;
    private boolean baseShapesFilled;
    private boolean useFillPaint;
    private boolean drawOutlines;
    private boolean useOutlinePaint;

    public LineAndShapeRenderer() {
        this(true, true);
    }

    public LineAndShapeRenderer(boolean lines, boolean shapes) {
        this.baseLinesVisible = lines;
        this.shapesVisible = null;
        this.seriesShapesVisible = new BooleanList();
        this.baseShapesVisible = shapes;
        this.shapesFilled = null;
        this.seriesShapesFilled = new BooleanList();
        this.baseShapesFilled = true;
        this.useFillPaint = false;
        this.drawOutlines = true;
        this.useOutlinePaint = false;
    }

    public boolean getItemLineVisible(int series, int item) {
        Boolean flag = this.linesVisible;
        if (flag == null) {
            flag = this.getSeriesLinesVisible(series);
        }
        if (flag != null) {
            return flag;
        }
        return this.baseLinesVisible;
    }

    public Boolean getLinesVisible() {
        return this.linesVisible;
    }

    public void setLinesVisible(Boolean visible) {
        this.linesVisible = visible;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public void setLinesVisible(boolean visible) {
        this.setLinesVisible(BooleanUtilities.valueOf(visible));
    }

    public Boolean getSeriesLinesVisible(int series) {
        return this.seriesLinesVisible.getBoolean(series);
    }

    public void setSeriesLinesVisible(int series, Boolean flag) {
        this.seriesLinesVisible.setBoolean(series, flag);
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public void setSeriesLinesVisible(int series, boolean visible) {
        this.setSeriesLinesVisible(series, BooleanUtilities.valueOf(visible));
    }

    public boolean getBaseLinesVisible() {
        return this.baseLinesVisible;
    }

    public void setBaseLinesVisible(boolean flag) {
        this.baseLinesVisible = flag;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getItemShapeVisible(int series, int item) {
        Boolean flag = this.shapesVisible;
        if (flag == null) {
            flag = this.getSeriesShapesVisible(series);
        }
        if (flag != null) {
            return flag;
        }
        return this.baseShapesVisible;
    }

    public Boolean getShapesVisible() {
        return this.shapesVisible;
    }

    public void setShapesVisible(Boolean visible) {
        this.shapesVisible = visible;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public void setShapesVisible(boolean visible) {
        this.setShapesVisible(BooleanUtilities.valueOf(visible));
    }

    public Boolean getSeriesShapesVisible(int series) {
        return this.seriesShapesVisible.getBoolean(series);
    }

    public void setSeriesShapesVisible(int series, boolean visible) {
        this.setSeriesShapesVisible(series, BooleanUtilities.valueOf(visible));
    }

    public void setSeriesShapesVisible(int series, Boolean flag) {
        this.seriesShapesVisible.setBoolean(series, flag);
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getBaseShapesVisible() {
        return this.baseShapesVisible;
    }

    public void setBaseShapesVisible(boolean flag) {
        this.baseShapesVisible = flag;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getDrawOutlines() {
        return this.drawOutlines;
    }

    public void setDrawOutlines(boolean flag) {
        this.drawOutlines = flag;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getUseOutlinePaint() {
        return this.useOutlinePaint;
    }

    public void setUseOutlinePaint(boolean use) {
        this.useOutlinePaint = use;
    }

    public boolean getItemShapeFilled(int series, int item) {
        return this.getSeriesShapesFilled(series);
    }

    public boolean getSeriesShapesFilled(int series) {
        if (this.shapesFilled != null) {
            return this.shapesFilled;
        }
        Boolean flag = this.seriesShapesFilled.getBoolean(series);
        if (flag != null) {
            return flag;
        }
        return this.baseShapesFilled;
    }

    public Boolean getShapesFilled() {
        return this.shapesFilled;
    }

    public void setShapesFilled(boolean filled) {
        if (filled) {
            this.setShapesFilled(Boolean.TRUE);
        } else {
            this.setShapesFilled(Boolean.FALSE);
        }
    }

    public void setShapesFilled(Boolean filled) {
        this.shapesFilled = filled;
    }

    public void setSeriesShapesFilled(int series, Boolean filled) {
        this.seriesShapesFilled.setBoolean(series, filled);
    }

    public void setSeriesShapesFilled(int series, boolean filled) {
        this.seriesShapesFilled.setBoolean(series, BooleanUtilities.valueOf(filled));
    }

    public boolean getBaseShapesFilled() {
        return this.baseShapesFilled;
    }

    public void setBaseShapesFilled(boolean flag) {
        this.baseShapesFilled = flag;
    }

    public boolean getUseFillPaint() {
        return this.useFillPaint;
    }

    public void setUseFillPaint(boolean flag) {
        this.useFillPaint = flag;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public LegendItem getLegendItem(int datasetIndex, int series) {
        CategoryPlot cp = this.getPlot();
        if (cp == null) {
            return null;
        }
        if (this.isSeriesVisible(series) && this.isSeriesVisibleInLegend(series)) {
            String label;
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
            Shape shape = this.getSeriesShape(series);
            Paint paint = this.getSeriesPaint(series);
            Paint fillPaint = this.useFillPaint ? this.getItemFillPaint(series, 0) : paint;
            boolean shapeOutlineVisible = this.drawOutlines;
            Paint outlinePaint = this.useOutlinePaint ? this.getItemOutlinePaint(series, 0) : paint;
            Stroke outlineStroke = this.getSeriesOutlineStroke(series);
            boolean lineVisible = this.getItemLineVisible(series, 0);
            boolean shapeVisible = this.getItemShapeVisible(series, 0);
            return new LegendItem(label, description, toolTipText, urlText, shapeVisible, shape, this.getItemShapeFilled(series, 0), fillPaint, shapeOutlineVisible, outlinePaint, outlineStroke, lineVisible, (Shape)new Line2D.Double(-7.0, 0.0, 7.0, 0.0), this.getItemStroke(series, 0), this.getItemPaint(series, 0));
        }
        return null;
    }

    public int getPassCount() {
        return 2;
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
        Number previousValue;
        if (!this.getItemVisible(row, column)) {
            return;
        }
        Number v = dataset.getValue(row, column);
        if (v == null) {
            return;
        }
        PlotOrientation orientation = plot.getOrientation();
        double x1 = domainAxis.getCategoryMiddle(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double value = v.doubleValue();
        double y1 = rangeAxis.valueToJava2D(value, dataArea, plot.getRangeAxisEdge());
        if (pass == 0 && this.getItemLineVisible(row, column) && column != 0 && (previousValue = dataset.getValue(row, column - 1)) != null) {
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
        if (pass == 1) {
            EntityCollection entities;
            Shape shape = this.getItemShape(row, column);
            if (orientation == PlotOrientation.HORIZONTAL) {
                shape = ShapeUtilities.createTranslatedShape(shape, y1, x1);
            } else if (orientation == PlotOrientation.VERTICAL) {
                shape = ShapeUtilities.createTranslatedShape(shape, x1, y1);
            }
            if (this.getItemShapeVisible(row, column)) {
                if (this.getItemShapeFilled(row, column)) {
                    if (this.useFillPaint) {
                        g2.setPaint(this.getItemFillPaint(row, column));
                    } else {
                        g2.setPaint(this.getItemPaint(row, column));
                    }
                    g2.fill(shape);
                }
                if (this.drawOutlines) {
                    if (this.useOutlinePaint) {
                        g2.setPaint(this.getItemOutlinePaint(row, column));
                    } else {
                        g2.setPaint(this.getItemPaint(row, column));
                    }
                    g2.setStroke(this.getItemOutlineStroke(row, column));
                    g2.draw(shape);
                }
            }
            if (this.isItemLabelVisible(row, column)) {
                if (orientation == PlotOrientation.HORIZONTAL) {
                    this.drawItemLabel(g2, orientation, dataset, row, column, y1, x1, value < 0.0);
                } else if (orientation == PlotOrientation.VERTICAL) {
                    this.drawItemLabel(g2, orientation, dataset, row, column, x1, y1, value < 0.0);
                }
            }
            if ((entities = state.getEntityCollection()) != null) {
                this.addItemEntity(entities, dataset, row, column, shape);
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LineAndShapeRenderer)) {
            return false;
        }
        LineAndShapeRenderer that = (LineAndShapeRenderer)obj;
        if (this.baseLinesVisible != that.baseLinesVisible) {
            return false;
        }
        if (!ObjectUtilities.equal(this.seriesLinesVisible, that.seriesLinesVisible)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.linesVisible, that.linesVisible)) {
            return false;
        }
        if (this.baseShapesVisible != that.baseShapesVisible) {
            return false;
        }
        if (!ObjectUtilities.equal(this.seriesShapesVisible, that.seriesShapesVisible)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.shapesVisible, that.shapesVisible)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.shapesFilled, that.shapesFilled)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.seriesShapesFilled, that.seriesShapesFilled)) {
            return false;
        }
        if (this.baseShapesFilled != that.baseShapesFilled) {
            return false;
        }
        if (this.useOutlinePaint != that.useOutlinePaint) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        LineAndShapeRenderer clone = (LineAndShapeRenderer)super.clone();
        clone.seriesLinesVisible = (BooleanList)this.seriesLinesVisible.clone();
        clone.seriesShapesVisible = (BooleanList)this.seriesLinesVisible.clone();
        clone.seriesShapesFilled = (BooleanList)this.seriesShapesFilled.clone();
        return clone;
    }
}

