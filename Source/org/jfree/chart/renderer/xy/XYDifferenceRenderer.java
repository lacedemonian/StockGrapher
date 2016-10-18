/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

public class XYDifferenceRenderer
extends AbstractXYItemRenderer
implements XYItemRenderer,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -8447915602375584857L;
    private transient Paint positivePaint;
    private transient Paint negativePaint;
    private boolean shapesVisible;
    private transient Shape legendLine;

    public XYDifferenceRenderer() {
        this(Color.green, Color.red, false);
    }

    public XYDifferenceRenderer(Paint positivePaint, Paint negativePaint, boolean shapes) {
        if (positivePaint == null) {
            throw new IllegalArgumentException("Null 'positivePaint' argument.");
        }
        if (negativePaint == null) {
            throw new IllegalArgumentException("Null 'negativePaint' argument.");
        }
        this.positivePaint = positivePaint;
        this.negativePaint = negativePaint;
        this.shapesVisible = shapes;
        this.legendLine = new Line2D.Double(-7.0, 0.0, 7.0, 0.0);
    }

    public Paint getPositivePaint() {
        return this.positivePaint;
    }

    public void setPositivePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.positivePaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public Paint getNegativePaint() {
        return this.negativePaint;
    }

    public void setNegativePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.negativePaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public boolean getShapesVisible() {
        return this.shapesVisible;
    }

    public void setShapesVisible(boolean flag) {
        this.shapesVisible = flag;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public Shape getLegendLine() {
        return this.legendLine;
    }

    public void setLegendLine(Shape line) {
        if (line == null) {
            throw new IllegalArgumentException("Null 'line' argument.");
        }
        this.legendLine = line;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public XYItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, XYPlot plot, XYDataset data, PlotRenderingInfo info) {
        return super.initialise(g2, dataArea, plot, data, info);
    }

    public int getPassCount() {
        return 2;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        if (pass == 0) {
            this.drawItemPass0(g2, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState);
        } else if (pass == 1) {
            this.drawItemPass1(g2, dataArea, info, plot, domainAxis, rangeAxis, dataset, series, item, crosshairState);
        }
    }

    protected void drawItemPass0(Graphics2D g2, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState) {
        if (series == 0) {
            PlotOrientation orientation = plot.getOrientation();
            RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
            double y0 = dataset.getYValue(0, item);
            double x1 = dataset.getXValue(1, item);
            double y1 = dataset.getYValue(1, item);
            double transY0 = rangeAxis.valueToJava2D(y0, dataArea, rangeAxisLocation);
            double transX1 = domainAxis.valueToJava2D(x1, dataArea, domainAxisLocation);
            double transY1 = rangeAxis.valueToJava2D(y1, dataArea, rangeAxisLocation);
            if (item > 0) {
                Shape negative;
                double prevtransY0;
                double prevtransY1;
                double prevx0 = dataset.getXValue(0, item - 1);
                double prevy0 = dataset.getYValue(0, item - 1);
                double prevy1 = dataset.getYValue(1, item - 1);
                double prevtransX0 = domainAxis.valueToJava2D(prevx0, dataArea, domainAxisLocation);
                Shape positive = this.getPositiveArea((float)prevtransX0, (float)(prevtransY0 = rangeAxis.valueToJava2D(prevy0, dataArea, rangeAxisLocation)), (float)(prevtransY1 = rangeAxis.valueToJava2D(prevy1, dataArea, rangeAxisLocation)), (float)transX1, (float)transY0, (float)transY1, orientation);
                if (positive != null) {
                    g2.setPaint(this.getPositivePaint());
                    g2.fill(positive);
                }
                if ((negative = this.getNegativeArea((float)prevtransX0, (float)prevtransY0, (float)prevtransY1, (float)transX1, (float)transY0, (float)transY1, orientation)) != null) {
                    g2.setPaint(this.getNegativePaint());
                    g2.fill(negative);
                }
            }
        }
    }

    protected void drawItemPass1(Graphics2D g2, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState) {
        Shape entityArea = null;
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }
        Paint seriesPaint = this.getItemPaint(series, item);
        Stroke seriesStroke = this.getItemStroke(series, item);
        g2.setPaint(seriesPaint);
        g2.setStroke(seriesStroke);
        if (series == 0) {
            PlotOrientation orientation = plot.getOrientation();
            RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
            double x0 = dataset.getXValue(0, item);
            double y0 = dataset.getYValue(0, item);
            double x1 = dataset.getXValue(1, item);
            double y1 = dataset.getYValue(1, item);
            double transX0 = domainAxis.valueToJava2D(x0, dataArea, domainAxisLocation);
            double transY0 = rangeAxis.valueToJava2D(y0, dataArea, rangeAxisLocation);
            double transX1 = domainAxis.valueToJava2D(x1, dataArea, domainAxisLocation);
            double transY1 = rangeAxis.valueToJava2D(y1, dataArea, rangeAxisLocation);
            if (item > 0) {
                double prevx0 = dataset.getXValue(0, item - 1);
                double prevy0 = dataset.getYValue(0, item - 1);
                double prevx1 = dataset.getXValue(1, item - 1);
                double prevy1 = dataset.getYValue(1, item - 1);
                double prevtransX0 = domainAxis.valueToJava2D(prevx0, dataArea, domainAxisLocation);
                double prevtransY0 = rangeAxis.valueToJava2D(prevy0, dataArea, rangeAxisLocation);
                double prevtransX1 = domainAxis.valueToJava2D(prevx1, dataArea, domainAxisLocation);
                double prevtransY1 = rangeAxis.valueToJava2D(prevy1, dataArea, rangeAxisLocation);
                Line2D.Double line0 = null;
                Line2D line1 = null;
                if (orientation == PlotOrientation.HORIZONTAL) {
                    line0 = new Line2D.Double(transY0, transX0, prevtransY0, prevtransX0);
                    line1 = new Line2D.Double(transY1, transX1, prevtransY1, prevtransX1);
                } else if (orientation == PlotOrientation.VERTICAL) {
                    line0 = new Line2D.Double(transX0, transY0, prevtransX0, prevtransY0);
                    line1 = new Line2D.Double(transX1, transY1, prevtransX1, prevtransY1);
                }
                if (line0 != null && line0.intersects(dataArea)) {
                    g2.setPaint(this.getItemPaint(series, item));
                    g2.setStroke(this.getItemStroke(series, item));
                    g2.draw(line0);
                }
                if (line1 != null && line1.intersects(dataArea)) {
                    g2.setPaint(this.getItemPaint(1, item));
                    g2.setStroke(this.getItemStroke(1, item));
                    g2.draw(line1);
                }
            }
            if (this.getShapesVisible()) {
                Shape shape0 = this.getItemShape(series, item);
                shape0 = orientation == PlotOrientation.HORIZONTAL ? ShapeUtilities.createTranslatedShape(shape0, transY0, transX0) : ShapeUtilities.createTranslatedShape(shape0, transX0, transY0);
                if (shape0.intersects(dataArea)) {
                    g2.setPaint(this.getItemPaint(series, item));
                    g2.fill(shape0);
                }
                entityArea = shape0;
                if (entities != null) {
                    if (entityArea == null) {
                        entityArea = new Rectangle2D.Double(transX0 - 2.0, transY0 - 2.0, 4.0, 4.0);
                    }
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
                Shape shape1 = this.getItemShape(series + 1, item);
                shape1 = orientation == PlotOrientation.HORIZONTAL ? ShapeUtilities.createTranslatedShape(shape1, transY1, transX1) : ShapeUtilities.createTranslatedShape(shape1, transX1, transY1);
                if (shape1.intersects(dataArea)) {
                    g2.setPaint(this.getItemPaint(series + 1, item));
                    g2.fill(shape1);
                }
                entityArea = shape1;
                if (entities != null) {
                    if (entityArea == null) {
                        entityArea = new Rectangle2D.Double(transX1 - 2.0, transY1 - 2.0, 4.0, 4.0);
                    }
                    String tip = null;
                    XYToolTipGenerator generator = this.getToolTipGenerator(series, item);
                    if (generator != null) {
                        tip = generator.generateToolTip(dataset, series + 1, item);
                    }
                    String url = null;
                    if (this.getURLGenerator() != null) {
                        url = this.getURLGenerator().generateURL(dataset, series + 1, item);
                    }
                    XYItemEntity entity = new XYItemEntity(entityArea, dataset, series + 1, item, tip, url);
                    entities.add(entity);
                }
            }
            this.updateCrosshairValues(crosshairState, x1, y1, transX1, transY1, orientation);
        }
    }

    protected Shape getPositiveArea(float x0, float y0A, float y0B, float x1, float y1A, float y1B, PlotOrientation orientation) {
        boolean endsNegative;
        GeneralPath result = null;
        boolean startsNegative = y0A >= y0B;
        boolean bl = endsNegative = y1A >= y1B;
        if (orientation == PlotOrientation.HORIZONTAL) {
            startsNegative = y0B >= y0A;
            boolean bl2 = endsNegative = y1B >= y1A;
        }
        if (startsNegative) {
            if (endsNegative) {
                result = null;
            } else {
                float[] p = this.getIntersection(x0, y0A, x1, y1A, x0, y0B, x1, y1B);
                GeneralPath area = new GeneralPath();
                if (orientation == PlotOrientation.HORIZONTAL) {
                    area.moveTo(y1A, x1);
                    area.lineTo(p[1], p[0]);
                    area.lineTo(y1B, x1);
                    area.closePath();
                } else if (orientation == PlotOrientation.VERTICAL) {
                    area.moveTo(x1, y1A);
                    area.lineTo(p[0], p[1]);
                    area.lineTo(x1, y1B);
                    area.closePath();
                }
                result = area;
            }
        } else if (endsNegative) {
            float[] p = this.getIntersection(x0, y0A, x1, y1A, x0, y0B, x1, y1B);
            GeneralPath area = new GeneralPath();
            if (orientation == PlotOrientation.HORIZONTAL) {
                area.moveTo(y0A, x0);
                area.lineTo(p[1], p[0]);
                area.lineTo(y0B, x0);
                area.closePath();
            } else if (orientation == PlotOrientation.VERTICAL) {
                area.moveTo(x0, y0A);
                area.lineTo(p[0], p[1]);
                area.lineTo(x0, y0B);
                area.closePath();
            }
            result = area;
        } else {
            GeneralPath area = new GeneralPath();
            if (orientation == PlotOrientation.HORIZONTAL) {
                area.moveTo(y0A, x0);
                area.lineTo(y1A, x1);
                area.lineTo(y1B, x1);
                area.lineTo(y0B, x0);
                area.closePath();
            } else if (orientation == PlotOrientation.VERTICAL) {
                area.moveTo(x0, y0A);
                area.lineTo(x1, y1A);
                area.lineTo(x1, y1B);
                area.lineTo(x0, y0B);
                area.closePath();
            }
            result = area;
        }
        return result;
    }

    protected Shape getNegativeArea(float x0, float y0A, float y0B, float x1, float y1A, float y1B, PlotOrientation orientation) {
        boolean endsNegative;
        GeneralPath result = null;
        boolean startsNegative = y0A >= y0B;
        boolean bl = endsNegative = y1A >= y1B;
        if (orientation == PlotOrientation.HORIZONTAL) {
            startsNegative = y0B >= y0A;
            boolean bl2 = endsNegative = y1B >= y1A;
        }
        if (startsNegative) {
            if (endsNegative) {
                GeneralPath area = new GeneralPath();
                if (orientation == PlotOrientation.HORIZONTAL) {
                    area.moveTo(y0A, x0);
                    area.lineTo(y1A, x1);
                    area.lineTo(y1B, x1);
                    area.lineTo(y0B, x0);
                    area.closePath();
                } else if (orientation == PlotOrientation.VERTICAL) {
                    area.moveTo(x0, y0A);
                    area.lineTo(x1, y1A);
                    area.lineTo(x1, y1B);
                    area.lineTo(x0, y0B);
                    area.closePath();
                }
                result = area;
            } else {
                float[] p = this.getIntersection(x0, y0A, x1, y1A, x0, y0B, x1, y1B);
                GeneralPath area = new GeneralPath();
                if (orientation == PlotOrientation.HORIZONTAL) {
                    area.moveTo(y0A, x0);
                    area.lineTo(p[1], p[0]);
                    area.lineTo(y0B, x0);
                    area.closePath();
                } else if (orientation == PlotOrientation.VERTICAL) {
                    area.moveTo(x0, y0A);
                    area.lineTo(p[0], p[1]);
                    area.lineTo(x0, y0B);
                    area.closePath();
                }
                result = area;
            }
        } else if (endsNegative) {
            float[] p = this.getIntersection(x0, y0A, x1, y1A, x0, y0B, x1, y1B);
            GeneralPath area = new GeneralPath();
            if (orientation == PlotOrientation.HORIZONTAL) {
                area.moveTo(y1A, x1);
                area.lineTo(p[1], p[0]);
                area.lineTo(y1B, x1);
                area.closePath();
            } else if (orientation == PlotOrientation.VERTICAL) {
                area.moveTo(x1, y1A);
                area.lineTo(p[0], p[1]);
                area.lineTo(x1, y1B);
                area.closePath();
            }
            result = area;
        }
        return result;
    }

    private float[] getIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float n = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        float u = n / d;
        float[] result = new float[]{x1 + u * (x2 - x1), y1 + u * (y2 - y1)};
        return result;
    }

    public LegendItem getLegendItem(int datasetIndex, int series) {
        XYDataset dataset;
        LegendItem result = null;
        XYPlot p = this.getPlot();
        if (p != null && (dataset = p.getDataset(datasetIndex)) != null && this.getItemVisible(series, 0)) {
            String label;
            String description = label = this.getLegendItemLabelGenerator().generateLabel(dataset, series);
            String toolTipText = null;
            if (this.getLegendItemToolTipGenerator() != null) {
                toolTipText = this.getLegendItemToolTipGenerator().generateLabel(dataset, series);
            }
            String urlText = null;
            if (this.getLegendItemURLGenerator() != null) {
                urlText = this.getLegendItemURLGenerator().generateLabel(dataset, series);
            }
            Paint paint = this.getSeriesPaint(series);
            Stroke stroke = this.getSeriesStroke(series);
            Line2D.Double line = new Line2D.Double(-7.0, 0.0, 7.0, 0.0);
            result = new LegendItem(label, description, toolTipText, urlText, (Shape)line, stroke, paint);
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYDifferenceRenderer)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        XYDifferenceRenderer that = (XYDifferenceRenderer)obj;
        if (!PaintUtilities.equal(this.positivePaint, that.positivePaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.negativePaint, that.negativePaint)) {
            return false;
        }
        if (this.shapesVisible != that.shapesVisible) {
            return false;
        }
        if (!ShapeUtilities.equal(this.legendLine, that.legendLine)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.positivePaint, stream);
        SerialUtilities.writePaint(this.negativePaint, stream);
        SerialUtilities.writeShape(this.legendLine, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.positivePaint = SerialUtilities.readPaint(stream);
        this.negativePaint = SerialUtilities.readPaint(stream);
        this.legendLine = SerialUtilities.readShape(stream);
    }
}

