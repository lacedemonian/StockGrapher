/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.plot;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.io.SerialUtilities;
import org.jfree.text.G2TextMeasurer;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.text.TextUtilities;
import org.jfree.ui.Align;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public abstract class Plot
implements AxisChangeListener,
DatasetChangeListener,
LegendItemSource,
PublicCloneable,
Cloneable,
Serializable {
    private static final long serialVersionUID = -8831571430103671324L;
    public static final Number ZERO = new Integer(0);
    public static final RectangleInsets DEFAULT_INSETS = new RectangleInsets(4.0, 8.0, 4.0, 8.0);
    public static final Stroke DEFAULT_OUTLINE_STROKE = new BasicStroke(0.5f);
    public static final Paint DEFAULT_OUTLINE_PAINT = Color.gray;
    public static final float DEFAULT_FOREGROUND_ALPHA = 1.0f;
    public static final float DEFAULT_BACKGROUND_ALPHA = 1.0f;
    public static final Paint DEFAULT_BACKGROUND_PAINT = Color.white;
    public static final int MINIMUM_WIDTH_TO_DRAW = 10;
    public static final int MINIMUM_HEIGHT_TO_DRAW = 10;
    public static final Shape DEFAULT_LEGEND_ITEM_BOX = new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0);
    public static final Shape DEFAULT_LEGEND_ITEM_CIRCLE = new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0);
    private Plot parent = null;
    private DatasetGroup datasetGroup;
    private String noDataMessage = null;
    private Font noDataMessageFont = new Font("SansSerif", 0, 12);
    private transient Paint noDataMessagePaint = Color.black;
    private RectangleInsets insets = DEFAULT_INSETS;
    private transient Stroke outlineStroke = DEFAULT_OUTLINE_STROKE;
    private transient Paint outlinePaint = DEFAULT_OUTLINE_PAINT;
    private transient Paint backgroundPaint = DEFAULT_BACKGROUND_PAINT;
    private transient Image backgroundImage = null;
    private int backgroundImageAlignment = 15;
    private float foregroundAlpha = 1.0f;
    private float backgroundAlpha = 1.0f;
    private DrawingSupplier drawingSupplier = new DefaultDrawingSupplier();
    private transient EventListenerList listenerList = new EventListenerList();
    static /* synthetic */ Class class$org$jfree$chart$event$PlotChangeListener;

    protected Plot() {
    }

    public DatasetGroup getDatasetGroup() {
        return this.datasetGroup;
    }

    protected void setDatasetGroup(DatasetGroup group) {
        this.datasetGroup = group;
    }

    public String getNoDataMessage() {
        return this.noDataMessage;
    }

    public void setNoDataMessage(String message) {
        this.noDataMessage = message;
    }

    public Font getNoDataMessageFont() {
        return this.noDataMessageFont;
    }

    public void setNoDataMessageFont(Font font) {
        this.noDataMessageFont = font;
    }

    public Paint getNoDataMessagePaint() {
        return this.noDataMessagePaint;
    }

    public void setNoDataMessagePaint(Paint paint) {
        this.noDataMessagePaint = paint;
    }

    public abstract String getPlotType();

    public Plot getParent() {
        return this.parent;
    }

    public void setParent(Plot parent) {
        this.parent = parent;
    }

    public Plot getRootPlot() {
        Plot p = this.getParent();
        if (p == null) {
            return this;
        }
        return p.getRootPlot();
    }

    public boolean isSubplot() {
        return this.getParent() != null;
    }

    public RectangleInsets getInsets() {
        return this.insets;
    }

    public void setInsets(RectangleInsets insets) {
        this.setInsets(insets, true);
    }

    public void setInsets(RectangleInsets insets, boolean notify) {
        if (insets == null) {
            throw new IllegalArgumentException("Null 'insets' argument.");
        }
        if (!this.insets.equals(insets)) {
            this.insets = insets;
            if (notify) {
                this.notifyListeners(new PlotChangeEvent(this));
            }
        }
    }

    public Paint getBackgroundPaint() {
        return this.backgroundPaint;
    }

    public void setBackgroundPaint(Paint paint) {
        if (paint == null) {
            if (this.backgroundPaint != null) {
                this.backgroundPaint = null;
                this.notifyListeners(new PlotChangeEvent(this));
            }
        } else {
            if (this.backgroundPaint != null && this.backgroundPaint.equals(paint)) {
                return;
            }
            this.backgroundPaint = paint;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public float getBackgroundAlpha() {
        return this.backgroundAlpha;
    }

    public void setBackgroundAlpha(float alpha) {
        if (this.backgroundAlpha != alpha) {
            this.backgroundAlpha = alpha;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public DrawingSupplier getDrawingSupplier() {
        DrawingSupplier result = null;
        Plot p = this.getParent();
        result = p != null ? p.getDrawingSupplier() : this.drawingSupplier;
        return result;
    }

    public void setDrawingSupplier(DrawingSupplier supplier) {
        this.drawingSupplier = supplier;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Image getBackgroundImage() {
        return this.backgroundImage;
    }

    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public int getBackgroundImageAlignment() {
        return this.backgroundImageAlignment;
    }

    public void setBackgroundImageAlignment(int alignment) {
        if (this.backgroundImageAlignment != alignment) {
            this.backgroundImageAlignment = alignment;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public Stroke getOutlineStroke() {
        return this.outlineStroke;
    }

    public void setOutlineStroke(Stroke stroke) {
        if (stroke == null) {
            if (this.outlineStroke != null) {
                this.outlineStroke = null;
                this.notifyListeners(new PlotChangeEvent(this));
            }
        } else {
            if (this.outlineStroke != null && this.outlineStroke.equals(stroke)) {
                return;
            }
            this.outlineStroke = stroke;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public Paint getOutlinePaint() {
        return this.outlinePaint;
    }

    public void setOutlinePaint(Paint paint) {
        if (paint == null) {
            if (this.outlinePaint != null) {
                this.outlinePaint = null;
                this.notifyListeners(new PlotChangeEvent(this));
            }
        } else {
            if (this.outlinePaint != null && this.outlinePaint.equals(paint)) {
                return;
            }
            this.outlinePaint = paint;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public float getForegroundAlpha() {
        return this.foregroundAlpha;
    }

    public void setForegroundAlpha(float alpha) {
        if (this.foregroundAlpha != alpha) {
            this.foregroundAlpha = alpha;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public LegendItemCollection getLegendItems() {
        return null;
    }

    public void addChangeListener(PlotChangeListener listener) {
        Class class_ = class$org$jfree$chart$event$PlotChangeListener == null ? (Plot.class$org$jfree$chart$event$PlotChangeListener = Plot.class$("org.jfree.chart.event.PlotChangeListener")) : class$org$jfree$chart$event$PlotChangeListener;
        this.listenerList.add(class_, listener);
    }

    public void removeChangeListener(PlotChangeListener listener) {
        Class class_ = class$org$jfree$chart$event$PlotChangeListener == null ? (Plot.class$org$jfree$chart$event$PlotChangeListener = Plot.class$("org.jfree.chart.event.PlotChangeListener")) : class$org$jfree$chart$event$PlotChangeListener;
        this.listenerList.remove(class_, listener);
    }

    public void notifyListeners(PlotChangeEvent event) {
        Object[] listeners = this.listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] != (class$org$jfree$chart$event$PlotChangeListener == null ? Plot.class$("org.jfree.chart.event.PlotChangeListener") : class$org$jfree$chart$event$PlotChangeListener)) continue;
            ((PlotChangeListener)listeners[i + 1]).plotChanged(event);
        }
    }

    public abstract void draw(Graphics2D var1, Rectangle2D var2, Point2D var3, PlotState var4, PlotRenderingInfo var5);

    public void drawBackground(Graphics2D g2, Rectangle2D area) {
        this.fillBackground(g2, area);
        this.drawBackgroundImage(g2, area);
    }

    protected void fillBackground(Graphics2D g2, Rectangle2D area) {
        if (this.backgroundPaint != null) {
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(3, this.backgroundAlpha));
            g2.setPaint(this.backgroundPaint);
            g2.fill(area);
            g2.setComposite(originalComposite);
        }
    }

    protected void drawBackgroundImage(Graphics2D g2, Rectangle2D area) {
        if (this.backgroundImage != null) {
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(2, this.backgroundAlpha));
            Rectangle2D.Double dest = new Rectangle2D.Double(0.0, 0.0, this.backgroundImage.getWidth(null), this.backgroundImage.getHeight(null));
            Align.align(dest, area, this.backgroundImageAlignment);
            g2.drawImage(this.backgroundImage, (int)dest.getX(), (int)dest.getY(), (int)dest.getWidth() + 1, (int)dest.getHeight() + 1, null);
            g2.setComposite(originalComposite);
        }
    }

    public void drawOutline(Graphics2D g2, Rectangle2D area) {
        if (this.outlineStroke != null && this.outlinePaint != null) {
            g2.setStroke(this.outlineStroke);
            g2.setPaint(this.outlinePaint);
            g2.draw(area);
        }
    }

    protected void drawNoDataMessage(Graphics2D g2, Rectangle2D area) {
        Shape savedClip = g2.getClip();
        g2.clip(area);
        String message = this.noDataMessage;
        if (message != null) {
            g2.setFont(this.noDataMessageFont);
            g2.setPaint(this.noDataMessagePaint);
            TextBlock block = TextUtilities.createTextBlock(this.noDataMessage, this.noDataMessageFont, this.noDataMessagePaint, 0.9f * (float)area.getWidth(), new G2TextMeasurer(g2));
            block.draw(g2, (float)area.getCenterX(), (float)area.getCenterY(), TextBlockAnchor.CENTER);
        }
        g2.setClip(savedClip);
    }

    public void handleClick(int x, int y, PlotRenderingInfo info) {
    }

    public void zoom(double percent) {
    }

    public void axisChanged(AxisChangeEvent event) {
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void datasetChanged(DatasetChangeEvent event) {
        PlotChangeEvent newEvent = new PlotChangeEvent(this);
        newEvent.setType(ChartChangeEventType.DATASET_UPDATED);
        this.notifyListeners(newEvent);
    }

    protected double getRectX(double x, double w1, double w2, RectangleEdge edge) {
        double result = x;
        if (edge == RectangleEdge.LEFT) {
            result += w1;
        } else if (edge == RectangleEdge.RIGHT) {
            result += w2;
        }
        return result;
    }

    protected double getRectY(double y, double h1, double h2, RectangleEdge edge) {
        double result = y;
        if (edge == RectangleEdge.TOP) {
            result += h1;
        } else if (edge == RectangleEdge.BOTTOM) {
            result += h2;
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Plot)) {
            return false;
        }
        Plot that = (Plot)obj;
        if (!ObjectUtilities.equal(this.noDataMessage, that.noDataMessage)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.noDataMessageFont, that.noDataMessageFont)) {
            return false;
        }
        if (!PaintUtilities.equal(this.noDataMessagePaint, that.noDataMessagePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.insets, that.insets)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.outlineStroke, that.outlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.outlinePaint, that.outlinePaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.backgroundPaint, that.backgroundPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundImage, that.backgroundImage)) {
            return false;
        }
        if (this.backgroundImageAlignment != that.backgroundImageAlignment) {
            return false;
        }
        if (this.foregroundAlpha != that.foregroundAlpha) {
            return false;
        }
        if (this.backgroundAlpha != that.backgroundAlpha) {
            return false;
        }
        if (!this.drawingSupplier.equals(that.drawingSupplier)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        Plot clone = (Plot)super.clone();
        if (this.datasetGroup != null) {
            clone.datasetGroup = (DatasetGroup)ObjectUtilities.clone(this.datasetGroup);
        }
        clone.drawingSupplier = (DrawingSupplier)ObjectUtilities.clone(this.drawingSupplier);
        clone.listenerList = new EventListenerList();
        return clone;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.noDataMessagePaint, stream);
        SerialUtilities.writeStroke(this.outlineStroke, stream);
        SerialUtilities.writePaint(this.outlinePaint, stream);
        SerialUtilities.writePaint(this.backgroundPaint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.noDataMessagePaint = SerialUtilities.readPaint(stream);
        this.outlineStroke = SerialUtilities.readStroke(stream);
        this.outlinePaint = SerialUtilities.readPaint(stream);
        this.backgroundPaint = SerialUtilities.readPaint(stream);
        this.listenerList = new EventListenerList();
    }

    public static RectangleEdge resolveDomainAxisLocation(AxisLocation location, PlotOrientation orientation) {
        if (location == null) {
            throw new IllegalArgumentException("Null 'location' argument.");
        }
        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        RectangleEdge result = null;
        if (location == AxisLocation.TOP_OR_RIGHT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.RIGHT;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.TOP;
            }
        } else if (location == AxisLocation.TOP_OR_LEFT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.LEFT;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.TOP;
            }
        } else if (location == AxisLocation.BOTTOM_OR_RIGHT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.RIGHT;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.BOTTOM;
            }
        } else if (location == AxisLocation.BOTTOM_OR_LEFT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.LEFT;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.BOTTOM;
            }
        }
        if (result == null) {
            throw new IllegalStateException("resolveDomainAxisLocation()");
        }
        return result;
    }

    public static RectangleEdge resolveRangeAxisLocation(AxisLocation location, PlotOrientation orientation) {
        if (location == null) {
            throw new IllegalArgumentException("Null 'location' argument.");
        }
        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        RectangleEdge result = null;
        if (location == AxisLocation.TOP_OR_RIGHT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.TOP;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.RIGHT;
            }
        } else if (location == AxisLocation.TOP_OR_LEFT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.TOP;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.LEFT;
            }
        } else if (location == AxisLocation.BOTTOM_OR_RIGHT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.BOTTOM;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.RIGHT;
            }
        } else if (location == AxisLocation.BOTTOM_OR_LEFT) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                result = RectangleEdge.BOTTOM;
            } else if (orientation == PlotOrientation.VERTICAL) {
                result = RectangleEdge.LEFT;
            }
        }
        if (result == null) {
            throw new IllegalStateException("resolveRangeAxisLocation()");
        }
        return result;
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

