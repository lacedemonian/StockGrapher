/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.Drawable;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;

public class XYDrawableAnnotation
extends AbstractXYAnnotation
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -6540812859722691020L;
    private double x;
    private double y;
    private double width;
    private double height;
    private Drawable drawable;

    public XYDrawableAnnotation(double x, double y, double width, double height, Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Null 'drawable' argument.");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.drawable = drawable;
    }

    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea, ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex, PlotRenderingInfo info) {
        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(plot.getDomainAxisLocation(), orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(), orientation);
        float j2DX = (float)domainAxis.valueToJava2D(this.x, dataArea, domainEdge);
        float j2DY = (float)rangeAxis.valueToJava2D(this.y, dataArea, rangeEdge);
        Rectangle2D.Double area = new Rectangle2D.Double((double)j2DX - this.width / 2.0, (double)j2DY - this.height / 2.0, this.width, this.height);
        this.drawable.draw(g2, area);
        String toolTip = this.getToolTipText();
        String url = this.getURL();
        if (toolTip != null || url != null) {
            this.addEntity(info, area, rendererIndex, toolTip, url);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof XYDrawableAnnotation)) {
            return false;
        }
        XYDrawableAnnotation that = (XYDrawableAnnotation)obj;
        if (this.x != that.x) {
            return false;
        }
        if (this.y != that.y) {
            return false;
        }
        if (this.width != that.width) {
            return false;
        }
        if (this.height != that.height) {
            return false;
        }
        if (!ObjectUtilities.equal(this.drawable, that.drawable)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.x);
        int result = (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 29 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.width);
        result = 29 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.height);
        result = 29 * result + (int)(temp ^ temp >>> 32);
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

