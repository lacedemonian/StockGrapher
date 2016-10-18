/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;
import javax.swing.event.EventListenerList;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.TextAnchor;
import org.jfree.util.BooleanList;
import org.jfree.util.BooleanUtilities;
import org.jfree.util.ObjectList;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintList;
import org.jfree.util.PaintUtilities;
import org.jfree.util.ShapeList;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.StrokeList;

public abstract class AbstractRenderer
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -828267569428206075L;
    public static final Double ZERO = new Double(0.0);
    public static final Paint DEFAULT_PAINT = Color.blue;
    public static final Paint DEFAULT_OUTLINE_PAINT = Color.gray;
    public static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);
    public static final Stroke DEFAULT_OUTLINE_STROKE = new BasicStroke(1.0f);
    public static final Shape DEFAULT_SHAPE = new Rectangle2D.Double(-3.0, -3.0, 6.0, 6.0);
    public static final Font DEFAULT_VALUE_LABEL_FONT = new Font("SansSerif", 0, 10);
    public static final Paint DEFAULT_VALUE_LABEL_PAINT = Color.black;
    private Boolean seriesVisible = null;
    private BooleanList seriesVisibleList = new BooleanList();
    private boolean baseSeriesVisible = true;
    private Boolean seriesVisibleInLegend = null;
    private BooleanList seriesVisibleInLegendList = new BooleanList();
    private boolean baseSeriesVisibleInLegend = true;
    private transient Paint paint = null;
    private PaintList paintList = new PaintList();
    private transient Paint basePaint = DEFAULT_PAINT;
    private transient Paint fillPaint = null;
    private PaintList fillPaintList = new PaintList();
    private transient Paint baseFillPaint = Color.white;
    private transient Paint outlinePaint = null;
    private PaintList outlinePaintList = new PaintList();
    private transient Paint baseOutlinePaint = DEFAULT_OUTLINE_PAINT;
    private transient Stroke stroke = null;
    private StrokeList strokeList = new StrokeList();
    private transient Stroke baseStroke = DEFAULT_STROKE;
    private transient Stroke outlineStroke = null;
    private StrokeList outlineStrokeList = new StrokeList();
    private transient Stroke baseOutlineStroke = DEFAULT_OUTLINE_STROKE;
    private transient Shape shape = null;
    private ShapeList shapeList = new ShapeList();
    private transient Shape baseShape = DEFAULT_SHAPE;
    private Boolean itemLabelsVisible = null;
    private BooleanList itemLabelsVisibleList = new BooleanList();
    private Boolean baseItemLabelsVisible = Boolean.FALSE;
    private Font itemLabelFont = null;
    private ObjectList itemLabelFontList = new ObjectList();
    private Font baseItemLabelFont = new Font("SansSerif", 0, 10);
    private transient Paint itemLabelPaint = null;
    private PaintList itemLabelPaintList = new PaintList();
    private transient Paint baseItemLabelPaint = Color.black;
    private ItemLabelPosition positiveItemLabelPosition = null;
    private ObjectList positiveItemLabelPositionList = new ObjectList();
    private ItemLabelPosition basePositiveItemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER);
    private ItemLabelPosition negativeItemLabelPosition = null;
    private ObjectList negativeItemLabelPositionList = new ObjectList();
    private ItemLabelPosition baseNegativeItemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_CENTER);
    private double itemLabelAnchorOffset = 2.0;
    private Boolean createEntities = null;
    private BooleanList createEntitiesList = new BooleanList();
    private boolean baseCreateEntities = true;
    private transient EventListenerList listenerList = new EventListenerList();
    private static final double ADJ = Math.cos(0.5235987755982988);
    private static final double OPP = Math.sin(0.5235987755982988);
    static /* synthetic */ Class class$org$jfree$chart$event$RendererChangeListener;

    public abstract DrawingSupplier getDrawingSupplier();

    public boolean getItemVisible(int series, int item) {
        return this.isSeriesVisible(series);
    }

    public boolean isSeriesVisible(int series) {
        boolean result = this.baseSeriesVisible;
        if (this.seriesVisible != null) {
            result = this.seriesVisible;
        } else {
            Boolean b = this.seriesVisibleList.getBoolean(series);
            if (b != null) {
                result = b;
            }
        }
        return result;
    }

    public Boolean getSeriesVisible() {
        return this.seriesVisible;
    }

    public void setSeriesVisible(Boolean visible) {
        this.setSeriesVisible(visible, true);
    }

    public void setSeriesVisible(Boolean visible, boolean notify) {
        this.seriesVisible = visible;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Boolean getSeriesVisible(int series) {
        return this.seriesVisibleList.getBoolean(series);
    }

    public void setSeriesVisible(int series, Boolean visible) {
        this.setSeriesVisible(series, visible, true);
    }

    public void setSeriesVisible(int series, Boolean visible, boolean notify) {
        this.seriesVisibleList.setBoolean(series, visible);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public boolean getBaseSeriesVisible() {
        return this.baseSeriesVisible;
    }

    public void setBaseSeriesVisible(boolean visible) {
        this.setBaseSeriesVisible(visible, true);
    }

    public void setBaseSeriesVisible(boolean visible, boolean notify) {
        this.baseSeriesVisible = visible;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public boolean isSeriesVisibleInLegend(int series) {
        boolean result = this.baseSeriesVisibleInLegend;
        if (this.seriesVisibleInLegend != null) {
            result = this.seriesVisibleInLegend;
        } else {
            Boolean b = this.seriesVisibleInLegendList.getBoolean(series);
            if (b != null) {
                result = b;
            }
        }
        return result;
    }

    public Boolean getSeriesVisibleInLegend() {
        return this.seriesVisibleInLegend;
    }

    public void setSeriesVisibleInLegend(Boolean visible) {
        this.setSeriesVisibleInLegend(visible, true);
    }

    public void setSeriesVisibleInLegend(Boolean visible, boolean notify) {
        this.seriesVisibleInLegend = visible;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Boolean getSeriesVisibleInLegend(int series) {
        return this.seriesVisibleInLegendList.getBoolean(series);
    }

    public void setSeriesVisibleInLegend(int series, Boolean visible) {
        this.setSeriesVisibleInLegend(series, visible, true);
    }

    public void setSeriesVisibleInLegend(int series, Boolean visible, boolean notify) {
        this.seriesVisibleInLegendList.setBoolean(series, visible);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public boolean getBaseSeriesVisibleInLegend() {
        return this.baseSeriesVisibleInLegend;
    }

    public void setBaseSeriesVisibleInLegend(boolean visible) {
        this.setBaseSeriesVisibleInLegend(visible, true);
    }

    public void setBaseSeriesVisibleInLegend(boolean visible, boolean notify) {
        this.baseSeriesVisibleInLegend = visible;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getItemPaint(int row, int column) {
        return this.getSeriesPaint(row);
    }

    public Paint getSeriesPaint(int series) {
        if (this.paint != null) {
            return this.paint;
        }
        Paint seriesPaint = this.paintList.getPaint(series);
        if (seriesPaint == null) {
            DrawingSupplier supplier = this.getDrawingSupplier();
            if (supplier != null) {
                seriesPaint = supplier.getNextPaint();
                this.paintList.setPaint(series, seriesPaint);
            } else {
                seriesPaint = this.basePaint;
            }
        }
        return seriesPaint;
    }

    public void setPaint(Paint paint) {
        this.setPaint(paint, true);
    }

    public void setPaint(Paint paint, boolean notify) {
        this.paint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public void setSeriesPaint(int series, Paint paint) {
        this.setSeriesPaint(series, paint, true);
    }

    public void setSeriesPaint(int series, Paint paint, boolean notify) {
        this.paintList.setPaint(series, paint);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getBasePaint() {
        return this.basePaint;
    }

    public void setBasePaint(Paint paint) {
        this.setBasePaint(paint, true);
    }

    public void setBasePaint(Paint paint, boolean notify) {
        this.basePaint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getItemFillPaint(int row, int column) {
        return this.getSeriesFillPaint(row);
    }

    public Paint getSeriesFillPaint(int series) {
        if (this.fillPaint != null) {
            return this.fillPaint;
        }
        Paint seriesFillPaint = this.fillPaintList.getPaint(series);
        if (seriesFillPaint == null) {
            seriesFillPaint = this.baseFillPaint;
        }
        return seriesFillPaint;
    }

    public void setSeriesFillPaint(int series, Paint paint) {
        this.setSeriesFillPaint(series, paint, true);
    }

    public void setSeriesFillPaint(int series, Paint paint, boolean notify) {
        this.fillPaintList.setPaint(series, paint);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public void setFillPaint(Paint paint) {
        this.setFillPaint(paint, true);
    }

    public void setFillPaint(Paint paint, boolean notify) {
        this.fillPaint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getBaseFillPaint() {
        return this.baseFillPaint;
    }

    public void setBaseFillPaint(Paint paint) {
        this.setBaseFillPaint(paint, true);
    }

    public void setBaseFillPaint(Paint paint, boolean notify) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.baseFillPaint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getItemOutlinePaint(int row, int column) {
        return this.getSeriesOutlinePaint(row);
    }

    public Paint getSeriesOutlinePaint(int series) {
        if (this.outlinePaint != null) {
            return this.outlinePaint;
        }
        Paint seriesOutlinePaint = this.outlinePaintList.getPaint(series);
        if (seriesOutlinePaint == null) {
            DrawingSupplier supplier = this.getDrawingSupplier();
            if (supplier != null) {
                seriesOutlinePaint = supplier.getNextOutlinePaint();
                this.outlinePaintList.setPaint(series, seriesOutlinePaint);
            } else {
                seriesOutlinePaint = this.baseOutlinePaint;
            }
        }
        return seriesOutlinePaint;
    }

    public void setSeriesOutlinePaint(int series, Paint paint) {
        this.setSeriesOutlinePaint(series, paint, true);
    }

    public void setSeriesOutlinePaint(int series, Paint paint, boolean notify) {
        this.outlinePaintList.setPaint(series, paint);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public void setOutlinePaint(Paint paint) {
        this.setOutlinePaint(paint, true);
    }

    public void setOutlinePaint(Paint paint, boolean notify) {
        this.outlinePaint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getBaseOutlinePaint() {
        return this.baseOutlinePaint;
    }

    public void setBaseOutlinePaint(Paint paint) {
        this.setBaseOutlinePaint(paint, true);
    }

    public void setBaseOutlinePaint(Paint paint, boolean notify) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.baseOutlinePaint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Stroke getItemStroke(int row, int column) {
        return this.getSeriesStroke(row);
    }

    public Stroke getSeriesStroke(int series) {
        if (this.stroke != null) {
            return this.stroke;
        }
        Stroke result = this.strokeList.getStroke(series);
        if (result == null) {
            DrawingSupplier supplier = this.getDrawingSupplier();
            if (supplier != null) {
                result = supplier.getNextStroke();
                this.strokeList.setStroke(series, result);
            } else {
                result = this.baseStroke;
            }
        }
        return result;
    }

    public void setStroke(Stroke stroke) {
        this.setStroke(stroke, true);
    }

    public void setStroke(Stroke stroke, boolean notify) {
        this.stroke = stroke;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public void setSeriesStroke(int series, Stroke stroke) {
        this.setSeriesStroke(series, stroke, true);
    }

    public void setSeriesStroke(int series, Stroke stroke, boolean notify) {
        this.strokeList.setStroke(series, stroke);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Stroke getBaseStroke() {
        return this.baseStroke;
    }

    public void setBaseStroke(Stroke stroke) {
        this.setBaseStroke(stroke, true);
    }

    public void setBaseStroke(Stroke stroke, boolean notify) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.baseStroke = stroke;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Stroke getItemOutlineStroke(int row, int column) {
        return this.getSeriesOutlineStroke(row);
    }

    public Stroke getSeriesOutlineStroke(int series) {
        if (this.outlineStroke != null) {
            return this.outlineStroke;
        }
        Stroke result = this.outlineStrokeList.getStroke(series);
        if (result == null) {
            DrawingSupplier supplier = this.getDrawingSupplier();
            if (supplier != null) {
                result = supplier.getNextOutlineStroke();
                this.outlineStrokeList.setStroke(series, result);
            } else {
                result = this.baseOutlineStroke;
            }
        }
        return result;
    }

    public void setOutlineStroke(Stroke stroke) {
        this.setOutlineStroke(stroke, true);
    }

    public void setOutlineStroke(Stroke stroke, boolean notify) {
        this.outlineStroke = stroke;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public void setSeriesOutlineStroke(int series, Stroke stroke) {
        this.setSeriesOutlineStroke(series, stroke, true);
    }

    public void setSeriesOutlineStroke(int series, Stroke stroke, boolean notify) {
        this.outlineStrokeList.setStroke(series, stroke);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Stroke getBaseOutlineStroke() {
        return this.baseOutlineStroke;
    }

    public void setBaseOutlineStroke(Stroke stroke) {
        this.setBaseOutlineStroke(stroke, true);
    }

    public void setBaseOutlineStroke(Stroke stroke, boolean notify) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.baseOutlineStroke = stroke;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Shape getItemShape(int row, int column) {
        return this.getSeriesShape(row);
    }

    public Shape getSeriesShape(int series) {
        if (this.shape != null) {
            return this.shape;
        }
        Shape result = this.shapeList.getShape(series);
        if (result == null) {
            DrawingSupplier supplier = this.getDrawingSupplier();
            if (supplier != null) {
                result = supplier.getNextShape();
                this.shapeList.setShape(series, result);
            } else {
                result = this.baseShape;
            }
        }
        return result;
    }

    public void setShape(Shape shape) {
        this.setShape(shape, true);
    }

    public void setShape(Shape shape, boolean notify) {
        this.shape = shape;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public void setSeriesShape(int series, Shape shape) {
        this.setSeriesShape(series, shape, true);
    }

    public void setSeriesShape(int series, Shape shape, boolean notify) {
        this.shapeList.setShape(series, shape);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Shape getBaseShape() {
        return this.baseShape;
    }

    public void setBaseShape(Shape shape) {
        this.setBaseShape(shape, true);
    }

    public void setBaseShape(Shape shape, boolean notify) {
        if (shape == null) {
            throw new IllegalArgumentException("Null 'shape' argument.");
        }
        this.baseShape = shape;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public boolean isItemLabelVisible(int row, int column) {
        return this.isSeriesItemLabelsVisible(row);
    }

    public boolean isSeriesItemLabelsVisible(int series) {
        if (this.itemLabelsVisible != null) {
            return this.itemLabelsVisible;
        }
        Boolean b = this.itemLabelsVisibleList.getBoolean(series);
        if (b == null) {
            b = this.baseItemLabelsVisible;
        }
        if (b == null) {
            b = Boolean.FALSE;
        }
        return b;
    }

    public void setItemLabelsVisible(boolean visible) {
        this.setItemLabelsVisible(BooleanUtilities.valueOf(visible));
    }

    public void setItemLabelsVisible(Boolean visible) {
        this.setItemLabelsVisible(visible, true);
    }

    public void setItemLabelsVisible(Boolean visible, boolean notify) {
        this.itemLabelsVisible = visible;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public void setSeriesItemLabelsVisible(int series, boolean visible) {
        this.setSeriesItemLabelsVisible(series, BooleanUtilities.valueOf(visible));
    }

    public void setSeriesItemLabelsVisible(int series, Boolean visible) {
        this.setSeriesItemLabelsVisible(series, visible, true);
    }

    public void setSeriesItemLabelsVisible(int series, Boolean visible, boolean notify) {
        this.itemLabelsVisibleList.setBoolean(series, visible);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Boolean getBaseItemLabelsVisible() {
        return this.baseItemLabelsVisible;
    }

    public void setBaseItemLabelsVisible(boolean visible) {
        this.setBaseItemLabelsVisible(BooleanUtilities.valueOf(visible));
    }

    public void setBaseItemLabelsVisible(Boolean visible) {
        this.setBaseItemLabelsVisible(visible, true);
    }

    public void setBaseItemLabelsVisible(Boolean visible, boolean notify) {
        this.baseItemLabelsVisible = visible;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Font getItemLabelFont(int row, int column) {
        Font result = this.itemLabelFont;
        if (result == null && (result = this.getSeriesItemLabelFont(row)) == null) {
            result = this.baseItemLabelFont;
        }
        return result;
    }

    public Font getItemLabelFont() {
        return this.itemLabelFont;
    }

    public void setItemLabelFont(Font font) {
        this.setItemLabelFont(font, true);
    }

    public void setItemLabelFont(Font font, boolean notify) {
        this.itemLabelFont = font;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Font getSeriesItemLabelFont(int series) {
        return (Font)this.itemLabelFontList.get(series);
    }

    public void setSeriesItemLabelFont(int series, Font font) {
        this.setSeriesItemLabelFont(series, font, true);
    }

    public void setSeriesItemLabelFont(int series, Font font, boolean notify) {
        this.itemLabelFontList.set(series, font);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Font getBaseItemLabelFont() {
        return this.baseItemLabelFont;
    }

    public void setBaseItemLabelFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("Null 'font' argument.");
        }
        this.setBaseItemLabelFont(font, true);
    }

    public void setBaseItemLabelFont(Font font, boolean notify) {
        this.baseItemLabelFont = font;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getItemLabelPaint(int row, int column) {
        Paint result = this.itemLabelPaint;
        if (result == null && (result = this.getSeriesItemLabelPaint(row)) == null) {
            result = this.baseItemLabelPaint;
        }
        return result;
    }

    public Paint getItemLabelPaint() {
        return this.itemLabelPaint;
    }

    public void setItemLabelPaint(Paint paint) {
        this.setItemLabelPaint(paint, true);
    }

    public void setItemLabelPaint(Paint paint, boolean notify) {
        this.itemLabelPaint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getSeriesItemLabelPaint(int series) {
        return this.itemLabelPaintList.getPaint(series);
    }

    public void setSeriesItemLabelPaint(int series, Paint paint) {
        this.setSeriesItemLabelPaint(series, paint, true);
    }

    public void setSeriesItemLabelPaint(int series, Paint paint, boolean notify) {
        this.itemLabelPaintList.setPaint(series, paint);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Paint getBaseItemLabelPaint() {
        return this.baseItemLabelPaint;
    }

    public void setBaseItemLabelPaint(Paint paint) {
        this.setBaseItemLabelPaint(paint, true);
    }

    public void setBaseItemLabelPaint(Paint paint, boolean notify) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.baseItemLabelPaint = paint;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public ItemLabelPosition getPositiveItemLabelPosition(int row, int column) {
        return this.getSeriesPositiveItemLabelPosition(row);
    }

    public ItemLabelPosition getPositiveItemLabelPosition() {
        return this.positiveItemLabelPosition;
    }

    public void setPositiveItemLabelPosition(ItemLabelPosition position) {
        this.setPositiveItemLabelPosition(position, true);
    }

    public void setPositiveItemLabelPosition(ItemLabelPosition position, boolean notify) {
        this.positiveItemLabelPosition = position;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public ItemLabelPosition getSeriesPositiveItemLabelPosition(int series) {
        if (this.positiveItemLabelPosition != null) {
            return this.positiveItemLabelPosition;
        }
        ItemLabelPosition position = (ItemLabelPosition)this.positiveItemLabelPositionList.get(series);
        if (position == null) {
            position = this.basePositiveItemLabelPosition;
        }
        return position;
    }

    public void setSeriesPositiveItemLabelPosition(int series, ItemLabelPosition position) {
        this.setSeriesPositiveItemLabelPosition(series, position, true);
    }

    public void setSeriesPositiveItemLabelPosition(int series, ItemLabelPosition position, boolean notify) {
        this.positiveItemLabelPositionList.set(series, position);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public ItemLabelPosition getBasePositiveItemLabelPosition() {
        return this.basePositiveItemLabelPosition;
    }

    public void setBasePositiveItemLabelPosition(ItemLabelPosition position) {
        this.setBasePositiveItemLabelPosition(position, true);
    }

    public void setBasePositiveItemLabelPosition(ItemLabelPosition position, boolean notify) {
        if (position == null) {
            throw new IllegalArgumentException("Null 'position' argument.");
        }
        this.basePositiveItemLabelPosition = position;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public ItemLabelPosition getNegativeItemLabelPosition(int row, int column) {
        return this.getSeriesNegativeItemLabelPosition(row);
    }

    public ItemLabelPosition getNegativeItemLabelPosition() {
        return this.negativeItemLabelPosition;
    }

    public void setNegativeItemLabelPosition(ItemLabelPosition position) {
        this.setNegativeItemLabelPosition(position, true);
    }

    public void setNegativeItemLabelPosition(ItemLabelPosition position, boolean notify) {
        this.negativeItemLabelPosition = position;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public ItemLabelPosition getSeriesNegativeItemLabelPosition(int series) {
        if (this.negativeItemLabelPosition != null) {
            return this.negativeItemLabelPosition;
        }
        ItemLabelPosition position = (ItemLabelPosition)this.negativeItemLabelPositionList.get(series);
        if (position == null) {
            position = this.baseNegativeItemLabelPosition;
        }
        return position;
    }

    public void setSeriesNegativeItemLabelPosition(int series, ItemLabelPosition position) {
        this.setSeriesNegativeItemLabelPosition(series, position, true);
    }

    public void setSeriesNegativeItemLabelPosition(int series, ItemLabelPosition position, boolean notify) {
        this.negativeItemLabelPositionList.set(series, position);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public ItemLabelPosition getBaseNegativeItemLabelPosition() {
        return this.baseNegativeItemLabelPosition;
    }

    public void setBaseNegativeItemLabelPosition(ItemLabelPosition position) {
        this.setBaseNegativeItemLabelPosition(position, true);
    }

    public void setBaseNegativeItemLabelPosition(ItemLabelPosition position, boolean notify) {
        if (position == null) {
            throw new IllegalArgumentException("Null 'position' argument.");
        }
        this.baseNegativeItemLabelPosition = position;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public double getItemLabelAnchorOffset() {
        return this.itemLabelAnchorOffset;
    }

    public void setItemLabelAnchorOffset(double offset) {
        this.itemLabelAnchorOffset = offset;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getItemCreateEntity(int series, int item) {
        if (this.createEntities != null) {
            return this.createEntities;
        }
        Boolean b = this.getSeriesCreateEntities(series);
        if (b != null) {
            return b;
        }
        return this.baseCreateEntities;
    }

    public Boolean getCreateEntities() {
        return this.createEntities;
    }

    public void setCreateEntities(Boolean create) {
        this.setCreateEntities(create, true);
    }

    public void setCreateEntities(Boolean create, boolean notify) {
        this.createEntities = create;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public Boolean getSeriesCreateEntities(int series) {
        return this.createEntitiesList.getBoolean(series);
    }

    public void setSeriesCreateEntities(int series, Boolean create) {
        this.setSeriesCreateEntities(series, create, true);
    }

    public void setSeriesCreateEntities(int series, Boolean create, boolean notify) {
        this.createEntitiesList.setBoolean(series, create);
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    public boolean getBaseCreateEntities() {
        return this.baseCreateEntities;
    }

    public void setBaseCreateEntities(boolean create) {
        this.setBaseCreateEntities(create, true);
    }

    public void setBaseCreateEntities(boolean create, boolean notify) {
        this.baseCreateEntities = create;
        if (notify) {
            this.notifyListeners(new RendererChangeEvent(this));
        }
    }

    protected Point2D calculateLabelAnchorPoint(ItemLabelAnchor anchor, double x, double y, PlotOrientation orientation) {
        Point2D.Double result = null;
        if (anchor == ItemLabelAnchor.CENTER) {
            result = new Point2D.Double(x, y);
        } else if (anchor == ItemLabelAnchor.INSIDE1) {
            result = new Point2D.Double(x + OPP * this.itemLabelAnchorOffset, y - ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE2) {
            result = new Point2D.Double(x + ADJ * this.itemLabelAnchorOffset, y - OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE3) {
            result = new Point2D.Double(x + this.itemLabelAnchorOffset, y);
        } else if (anchor == ItemLabelAnchor.INSIDE4) {
            result = new Point2D.Double(x + ADJ * this.itemLabelAnchorOffset, y + OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE5) {
            result = new Point2D.Double(x + OPP * this.itemLabelAnchorOffset, y + ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE6) {
            result = new Point2D.Double(x, y + this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE7) {
            result = new Point2D.Double(x - OPP * this.itemLabelAnchorOffset, y + ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE8) {
            result = new Point2D.Double(x - ADJ * this.itemLabelAnchorOffset, y + OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE9) {
            result = new Point2D.Double(x - this.itemLabelAnchorOffset, y);
        } else if (anchor == ItemLabelAnchor.INSIDE10) {
            result = new Point2D.Double(x - ADJ * this.itemLabelAnchorOffset, y - OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE11) {
            result = new Point2D.Double(x - OPP * this.itemLabelAnchorOffset, y - ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.INSIDE12) {
            result = new Point2D.Double(x, y - this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE1) {
            result = new Point2D.Double(x + 2.0 * OPP * this.itemLabelAnchorOffset, y - 2.0 * ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE2) {
            result = new Point2D.Double(x + 2.0 * ADJ * this.itemLabelAnchorOffset, y - 2.0 * OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE3) {
            result = new Point2D.Double(x + 2.0 * this.itemLabelAnchorOffset, y);
        } else if (anchor == ItemLabelAnchor.OUTSIDE4) {
            result = new Point2D.Double(x + 2.0 * ADJ * this.itemLabelAnchorOffset, y + 2.0 * OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE5) {
            result = new Point2D.Double(x + 2.0 * OPP * this.itemLabelAnchorOffset, y + 2.0 * ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE6) {
            result = new Point2D.Double(x, y + 2.0 * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE7) {
            result = new Point2D.Double(x - 2.0 * OPP * this.itemLabelAnchorOffset, y + 2.0 * ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE8) {
            result = new Point2D.Double(x - 2.0 * ADJ * this.itemLabelAnchorOffset, y + 2.0 * OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE9) {
            result = new Point2D.Double(x - 2.0 * this.itemLabelAnchorOffset, y);
        } else if (anchor == ItemLabelAnchor.OUTSIDE10) {
            result = new Point2D.Double(x - 2.0 * ADJ * this.itemLabelAnchorOffset, y - 2.0 * OPP * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE11) {
            result = new Point2D.Double(x - 2.0 * OPP * this.itemLabelAnchorOffset, y - 2.0 * ADJ * this.itemLabelAnchorOffset);
        } else if (anchor == ItemLabelAnchor.OUTSIDE12) {
            result = new Point2D.Double(x, y - 2.0 * this.itemLabelAnchorOffset);
        }
        return result;
    }

    public void addChangeListener(RendererChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null 'listener' argument.");
        }
        Class class_ = class$org$jfree$chart$event$RendererChangeListener == null ? (AbstractRenderer.class$org$jfree$chart$event$RendererChangeListener = AbstractRenderer.class$("org.jfree.chart.event.RendererChangeListener")) : class$org$jfree$chart$event$RendererChangeListener;
        this.listenerList.add(class_, listener);
    }

    public void removeChangeListener(RendererChangeListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null 'listener' argument.");
        }
        Class class_ = class$org$jfree$chart$event$RendererChangeListener == null ? (AbstractRenderer.class$org$jfree$chart$event$RendererChangeListener = AbstractRenderer.class$("org.jfree.chart.event.RendererChangeListener")) : class$org$jfree$chart$event$RendererChangeListener;
        this.listenerList.remove(class_, listener);
    }

    public boolean hasListener(EventListener listener) {
        List<Object> list = Arrays.asList(this.listenerList.getListenerList());
        return list.contains(listener);
    }

    public void notifyListeners(RendererChangeEvent event) {
        Object[] ls = this.listenerList.getListenerList();
        for (int i = ls.length - 2; i >= 0; i -= 2) {
            if (ls[i] != (class$org$jfree$chart$event$RendererChangeListener == null ? AbstractRenderer.class$("org.jfree.chart.event.RendererChangeListener") : class$org$jfree$chart$event$RendererChangeListener)) continue;
            ((RendererChangeListener)ls[i + 1]).rendererChanged(event);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractRenderer)) {
            return false;
        }
        AbstractRenderer that = (AbstractRenderer)obj;
        if (!ObjectUtilities.equal(this.seriesVisible, that.seriesVisible)) {
            return false;
        }
        if (!this.seriesVisibleList.equals(that.seriesVisibleList)) {
            return false;
        }
        if (this.baseSeriesVisible != that.baseSeriesVisible) {
            return false;
        }
        if (!ObjectUtilities.equal(this.seriesVisibleInLegend, that.seriesVisibleInLegend)) {
            return false;
        }
        if (!this.seriesVisibleInLegendList.equals(that.seriesVisibleInLegendList)) {
            return false;
        }
        if (this.baseSeriesVisibleInLegend != that.baseSeriesVisibleInLegend) {
            return false;
        }
        if (!PaintUtilities.equal(this.paint, that.paint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.paintList, that.paintList)) {
            return false;
        }
        if (!PaintUtilities.equal(this.basePaint, that.basePaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.fillPaint, that.fillPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.fillPaintList, that.fillPaintList)) {
            return false;
        }
        if (!PaintUtilities.equal(this.baseFillPaint, that.baseFillPaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.outlinePaint, that.outlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.outlinePaintList, that.outlinePaintList)) {
            return false;
        }
        if (!PaintUtilities.equal(this.baseOutlinePaint, that.baseOutlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.stroke, that.stroke)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.strokeList, that.strokeList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseStroke, that.baseStroke)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.outlineStroke, that.outlineStroke)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.outlineStrokeList, that.outlineStrokeList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseOutlineStroke, that.baseOutlineStroke)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.shape, that.shape)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.shapeList, that.shapeList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseShape, that.baseShape)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.itemLabelsVisible, that.itemLabelsVisible)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.itemLabelsVisibleList, that.itemLabelsVisibleList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseItemLabelsVisible, that.baseItemLabelsVisible)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.itemLabelFont, that.itemLabelFont)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.itemLabelFontList, that.itemLabelFontList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseItemLabelFont, that.baseItemLabelFont)) {
            return false;
        }
        if (!PaintUtilities.equal(this.itemLabelPaint, that.itemLabelPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.itemLabelPaintList, that.itemLabelPaintList)) {
            return false;
        }
        if (!PaintUtilities.equal(this.baseItemLabelPaint, that.baseItemLabelPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.positiveItemLabelPosition, that.positiveItemLabelPosition)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.positiveItemLabelPositionList, that.positiveItemLabelPositionList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.basePositiveItemLabelPosition, that.basePositiveItemLabelPosition)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.negativeItemLabelPosition, that.negativeItemLabelPosition)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.negativeItemLabelPositionList, that.negativeItemLabelPositionList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseNegativeItemLabelPosition, that.baseNegativeItemLabelPosition)) {
            return false;
        }
        if (this.itemLabelAnchorOffset != that.itemLabelAnchorOffset) {
            return false;
        }
        if (!ObjectUtilities.equal(this.createEntities, that.createEntities)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.createEntitiesList, that.createEntitiesList)) {
            return false;
        }
        if (this.baseCreateEntities != that.baseCreateEntities) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 193;
        result = 37 * result + ObjectUtilities.hashCode(this.stroke);
        result = 37 * result + ObjectUtilities.hashCode(this.baseStroke);
        result = 37 * result + ObjectUtilities.hashCode(this.outlineStroke);
        result = 37 * result + ObjectUtilities.hashCode(this.baseOutlineStroke);
        return result;
    }

    protected Object clone() throws CloneNotSupportedException {
        AbstractRenderer clone = (AbstractRenderer)super.clone();
        if (this.paintList != null) {
            clone.paintList = (PaintList)this.paintList.clone();
        }
        if (this.fillPaintList != null) {
            clone.fillPaintList = (PaintList)this.fillPaintList.clone();
        }
        if (this.outlinePaintList != null) {
            clone.outlinePaintList = (PaintList)this.outlinePaintList.clone();
        }
        if (this.strokeList != null) {
            clone.strokeList = (StrokeList)this.strokeList.clone();
        }
        if (this.outlineStrokeList != null) {
            clone.outlineStrokeList = (StrokeList)this.outlineStrokeList.clone();
        }
        if (this.shape != null) {
            clone.shape = ShapeUtilities.clone(this.shape);
        }
        if (this.baseShape != null) {
            clone.baseShape = ShapeUtilities.clone(this.baseShape);
        }
        if (this.itemLabelsVisibleList != null) {
            clone.itemLabelsVisibleList = (BooleanList)this.itemLabelsVisibleList.clone();
        }
        if (this.itemLabelFontList != null) {
            clone.itemLabelFontList = (ObjectList)this.itemLabelFontList.clone();
        }
        if (this.itemLabelPaintList != null) {
            clone.itemLabelPaintList = (PaintList)this.itemLabelPaintList.clone();
        }
        if (this.positiveItemLabelPositionList != null) {
            clone.positiveItemLabelPositionList = (ObjectList)this.positiveItemLabelPositionList.clone();
        }
        if (this.negativeItemLabelPositionList != null) {
            clone.negativeItemLabelPositionList = (ObjectList)this.negativeItemLabelPositionList.clone();
        }
        return clone;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.paint, stream);
        SerialUtilities.writePaint(this.basePaint, stream);
        SerialUtilities.writePaint(this.fillPaint, stream);
        SerialUtilities.writePaint(this.baseFillPaint, stream);
        SerialUtilities.writePaint(this.outlinePaint, stream);
        SerialUtilities.writePaint(this.baseOutlinePaint, stream);
        SerialUtilities.writeStroke(this.stroke, stream);
        SerialUtilities.writeStroke(this.baseStroke, stream);
        SerialUtilities.writeStroke(this.outlineStroke, stream);
        SerialUtilities.writeStroke(this.baseOutlineStroke, stream);
        SerialUtilities.writeShape(this.shape, stream);
        SerialUtilities.writeShape(this.baseShape, stream);
        SerialUtilities.writePaint(this.itemLabelPaint, stream);
        SerialUtilities.writePaint(this.baseItemLabelPaint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.paint = SerialUtilities.readPaint(stream);
        this.basePaint = SerialUtilities.readPaint(stream);
        this.fillPaint = SerialUtilities.readPaint(stream);
        this.baseFillPaint = SerialUtilities.readPaint(stream);
        this.outlinePaint = SerialUtilities.readPaint(stream);
        this.baseOutlinePaint = SerialUtilities.readPaint(stream);
        this.stroke = SerialUtilities.readStroke(stream);
        this.baseStroke = SerialUtilities.readStroke(stream);
        this.outlineStroke = SerialUtilities.readStroke(stream);
        this.baseOutlineStroke = SerialUtilities.readStroke(stream);
        this.shape = SerialUtilities.readShape(stream);
        this.baseShape = SerialUtilities.readShape(stream);
        this.itemLabelPaint = SerialUtilities.readPaint(stream);
        this.baseItemLabelPaint = SerialUtilities.readPaint(stream);
        this.listenerList = new EventListenerList();
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}

