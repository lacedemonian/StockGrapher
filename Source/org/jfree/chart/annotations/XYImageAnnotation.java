/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
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
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;

public class XYImageAnnotation
extends AbstractXYAnnotation
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -4364694501921559958L;
    private double x;
    private double y;
    private transient Image image;

    public XYImageAnnotation(double x, double y, Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Null 'image' argument.");
        }
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea, ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex, PlotRenderingInfo info) {
        PlotOrientation orientation = plot.getOrientation();
        AxisLocation domainAxisLocation = plot.getDomainAxisLocation();
        AxisLocation rangeAxisLocation = plot.getRangeAxisLocation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(domainAxisLocation, orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(rangeAxisLocation, orientation);
        float j2DX = (float)domainAxis.valueToJava2D(this.x, dataArea, domainEdge);
        float j2DY = (float)rangeAxis.valueToJava2D(this.y, dataArea, rangeEdge);
        float xx = 0.0f;
        float yy = 0.0f;
        if (orientation == PlotOrientation.HORIZONTAL) {
            xx = j2DY;
            yy = j2DX;
        } else if (orientation == PlotOrientation.VERTICAL) {
            xx = j2DX;
            yy = j2DY;
        }
        int w = this.image.getWidth(null);
        int h = this.image.getHeight(null);
        g2.drawImage(this.image, (int)(xx -= (float)w / 2.0f), (int)(yy -= (float)h / 2.0f), null);
        String toolTip = this.getToolTipText();
        String url = this.getURL();
        if (toolTip != null || url != null) {
            this.addEntity(info, new Rectangle2D.Float(xx, yy, w, h), rendererIndex, toolTip, url);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof XYImageAnnotation)) {
            return false;
        }
        XYImageAnnotation that = (XYImageAnnotation)obj;
        if (this.x != that.x) {
            return false;
        }
        if (this.y != that.y) {
            return false;
        }
        if (!ObjectUtilities.equal(this.image, that.image)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.image.hashCode();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}

