/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.plot;

import java.awt.geom.Point2D;
import org.jfree.chart.plot.PlotOrientation;

public class CrosshairState {
    private boolean calculateDistanceInDataSpace = false;
    private double anchorX;
    private double anchorY;
    private Point2D anchor;
    private double crosshairX;
    private double crosshairY;
    private double distance;

    public CrosshairState() {
        this(false);
    }

    public CrosshairState(boolean calculateDistanceInDataSpace) {
        this.calculateDistanceInDataSpace = calculateDistanceInDataSpace;
    }

    public void setCrosshairDistance(double distance) {
        this.distance = distance;
    }

    public void updateCrosshairPoint(double x, double y, double transX, double transY, PlotOrientation orientation) {
        if (this.anchor != null) {
            double d = 0.0;
            if (this.calculateDistanceInDataSpace) {
                d = (x - this.anchorX) * (x - this.anchorX) + (y - this.anchorY) * (y - this.anchorY);
            } else {
                double xx = this.anchor.getX();
                double yy = this.anchor.getY();
                if (orientation == PlotOrientation.HORIZONTAL) {
                    double temp = yy;
                    yy = xx;
                    xx = temp;
                }
                d = (transX - xx) * (transX - xx) + (transY - yy) * (transY - yy);
            }
            if (d < this.distance) {
                this.crosshairX = x;
                this.crosshairY = y;
                this.distance = d;
            }
        }
    }

    public void updateCrosshairX(double candidateX) {
        double d = Math.abs(candidateX - this.anchorX);
        if (d < this.distance) {
            this.crosshairX = candidateX;
            this.distance = d;
        }
    }

    public void updateCrosshairY(double candidateY) {
        double d = Math.abs(candidateY - this.anchorY);
        if (d < this.distance) {
            this.crosshairY = candidateY;
            this.distance = d;
        }
    }

    public void setAnchor(Point2D anchor) {
        this.anchor = anchor;
    }

    public double getCrosshairX() {
        return this.crosshairX;
    }

    public void setCrosshairX(double x) {
        this.crosshairX = x;
    }

    public double getCrosshairY() {
        return this.crosshairY;
    }

    public void setCrosshairY(double y) {
        this.crosshairY = y;
    }
}

