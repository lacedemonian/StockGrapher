/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.Serializable;
import org.jfree.chart.Effect3D;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLine3DRenderer
extends XYLineAndShapeRenderer
implements Effect3D,
Serializable {
    private static final long serialVersionUID = 588933208243446087L;
    public static final double DEFAULT_X_OFFSET = 12.0;
    public static final double DEFAULT_Y_OFFSET = 8.0;
    public static final Paint DEFAULT_WALL_PAINT = new Color(221, 221, 221);
    private double xOffset = 12.0;
    private double yOffset = 8.0;
    private transient Paint wallPaint = DEFAULT_WALL_PAINT;

    public double getXOffset() {
        return this.xOffset;
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public Paint getWallPaint() {
        return this.wallPaint;
    }

    public void setWallPaint(Paint paint) {
        this.wallPaint = paint;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public int getPassCount() {
        return 3;
    }

    protected boolean isLinePass(int pass) {
        return pass == 0 || pass == 1;
    }

    protected boolean isItemPass(int pass) {
        return pass == 2;
    }

    protected boolean isShadowPass(int pass) {
        return pass == 0;
    }

    protected void drawFirstPassShape(Graphics2D g2, int pass, int series, int item, Shape shape) {
        if (this.isShadowPass(pass)) {
            if (this.getWallPaint() != null) {
                g2.setStroke(this.getItemStroke(series, item));
                g2.setPaint(this.getWallPaint());
                g2.translate(this.getXOffset(), this.getYOffset());
                g2.draw(shape);
                g2.translate(- this.getXOffset(), - this.getYOffset());
            }
        } else {
            super.drawFirstPassShape(g2, pass, series, item, shape);
        }
    }
}

