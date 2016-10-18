/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.annotations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.io.SerialUtilities;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public class XYTextAnnotation
extends AbstractXYAnnotation
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -2946063342782506328L;
    public static final Font DEFAULT_FONT = new Font("SansSerif", 0, 10);
    public static final Paint DEFAULT_PAINT = Color.black;
    public static final TextAnchor DEFAULT_TEXT_ANCHOR = TextAnchor.CENTER;
    public static final TextAnchor DEFAULT_ROTATION_ANCHOR = TextAnchor.CENTER;
    public static final double DEFAULT_ROTATION_ANGLE = 0.0;
    private String text;
    private Font font;
    private transient Paint paint;
    private double x;
    private double y;
    private TextAnchor textAnchor;
    private TextAnchor rotationAnchor;
    private double rotationAngle;

    public XYTextAnnotation(String text, double x, double y) {
        if (text == null) {
            throw new IllegalArgumentException("Null 'text' argument.");
        }
        this.text = text;
        this.font = DEFAULT_FONT;
        this.paint = DEFAULT_PAINT;
        this.x = x;
        this.y = y;
        this.textAnchor = DEFAULT_TEXT_ANCHOR;
        this.rotationAnchor = DEFAULT_ROTATION_ANCHOR;
        this.rotationAngle = 0.0;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public TextAnchor getTextAnchor() {
        return this.textAnchor;
    }

    public void setTextAnchor(TextAnchor anchor) {
        this.textAnchor = anchor;
    }

    public TextAnchor getRotationAnchor() {
        return this.rotationAnchor;
    }

    public void setRotationAnchor(TextAnchor anchor) {
        this.rotationAnchor = anchor;
    }

    public double getRotationAngle() {
        return this.rotationAngle;
    }

    public void setRotationAngle(double angle) {
        this.rotationAngle = angle;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea, ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex, PlotRenderingInfo info) {
        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(plot.getDomainAxisLocation(), orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(), orientation);
        float anchorX = (float)domainAxis.valueToJava2D(this.x, dataArea, domainEdge);
        float anchorY = (float)rangeAxis.valueToJava2D(this.y, dataArea, rangeEdge);
        if (orientation == PlotOrientation.HORIZONTAL) {
            float tempAnchor = anchorX;
            anchorX = anchorY;
            anchorY = tempAnchor;
        }
        g2.setFont(this.getFont());
        g2.setPaint(this.getPaint());
        TextUtilities.drawRotatedString(this.getText(), g2, anchorX, anchorY, this.getTextAnchor(), this.getRotationAngle(), this.getRotationAnchor());
        Shape hotspot = TextUtilities.calculateRotatedStringBounds(this.getText(), g2, anchorX, anchorY, this.getTextAnchor(), this.getRotationAngle(), this.getRotationAnchor());
        String toolTip = this.getToolTipText();
        String url = this.getURL();
        if (toolTip != null || url != null) {
            this.addEntity(info, hotspot, rendererIndex, toolTip, url);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYTextAnnotation)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        XYTextAnnotation that = (XYTextAnnotation)obj;
        if (!this.text.equals(that.text)) {
            return false;
        }
        if (this.x != that.x) {
            return false;
        }
        if (this.y != that.y) {
            return false;
        }
        if (!this.font.equals(that.font)) {
            return false;
        }
        if (!PaintUtilities.equal(this.paint, that.paint)) {
            return false;
        }
        if (!this.rotationAnchor.equals(that.rotationAnchor)) {
            return false;
        }
        if (this.rotationAngle != that.rotationAngle) {
            return false;
        }
        if (!this.textAnchor.equals(that.textAnchor)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.text.hashCode();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.paint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.paint = SerialUtilities.readPaint(stream);
    }
}

