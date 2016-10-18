/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.contour;

import org.jfree.data.Range;
import org.jfree.data.contour.DefaultContourDataset;

public class NonGridContourDataset
extends DefaultContourDataset {
    static final int DEFAULT_NUM_X = 50;
    static final int DEFAULT_NUM_Y = 50;
    static final int DEFAULT_POWER = 4;

    public NonGridContourDataset() {
    }

    public NonGridContourDataset(String seriesName, Object[] xData, Object[] yData, Object[] zData) {
        super((Comparable)((Object)seriesName), xData, yData, zData);
        this.buildGrid(50, 50, 4);
    }

    public NonGridContourDataset(String seriesName, Object[] xData, Object[] yData, Object[] zData, int numX, int numY, int power) {
        super((Comparable)((Object)seriesName), xData, yData, zData);
        this.buildGrid(numX, numY, power);
    }

    protected void buildGrid(int numX, int numY, int power) {
        int numValues = numX * numY;
        double[] xGrid = new double[numValues];
        double[] yGrid = new double[numValues];
        double[] zGrid = new double[numValues];
        double xMin = 1.0E20;
        for (int k = 0; k < this.xValues.length; ++k) {
            xMin = Math.min(xMin, this.xValues[k].doubleValue());
        }
        double xMax = -1.0E20;
        for (int k2 = 0; k2 < this.xValues.length; ++k2) {
            xMax = Math.max(xMax, this.xValues[k2].doubleValue());
        }
        double yMin = 1.0E20;
        for (int k3 = 0; k3 < this.yValues.length; ++k3) {
            yMin = Math.min(yMin, this.yValues[k3].doubleValue());
        }
        double yMax = -1.0E20;
        for (int k4 = 0; k4 < this.yValues.length; ++k4) {
            yMax = Math.max(yMax, this.yValues[k4].doubleValue());
        }
        Range xRange = new Range(xMin, xMax);
        Range yRange = new Range(yMin, yMax);
        xRange.getLength();
        yRange.getLength();
        double dxGrid = xRange.getLength() / (double)(numX - 1);
        double dyGrid = yRange.getLength() / (double)(numY - 1);
        double x = 0.0;
        for (int i = 0; i < numX; ++i) {
            x = i == 0 ? xMin : (x += dxGrid);
            double y = 0.0;
            for (int j = 0; j < numY; ++j) {
                int k5 = numY * i + j;
                xGrid[k5] = x;
                y = j == 0 ? yMin : (y += dyGrid);
                yGrid[k5] = y;
            }
        }
        for (int kGrid = 0; kGrid < xGrid.length; ++kGrid) {
            double dTotal = 0.0;
            zGrid[kGrid] = 0.0;
            for (int k6 = 0; k6 < this.xValues.length; ++k6) {
                double xPt = this.xValues[k6].doubleValue();
                double yPt = this.yValues[k6].doubleValue();
                double d = this.distance(xPt, yPt, xGrid[kGrid], yGrid[kGrid]);
                if (power != 1) {
                    d = Math.pow(d, power);
                }
                d = (d = Math.sqrt(d)) > 0.0 ? 1.0 / d : 1.0E20;
                if (this.zValues[k6] != null) {
                    double[] arrd = zGrid;
                    int n = kGrid;
                    arrd[n] = arrd[n] + this.zValues[k6].doubleValue() * d;
                }
                dTotal += d;
            }
            zGrid[kGrid] = zGrid[kGrid] / dTotal;
        }
        this.initialize(NonGridContourDataset.formObjectArray(xGrid), NonGridContourDataset.formObjectArray(yGrid), NonGridContourDataset.formObjectArray(zGrid));
    }

    protected double distance(double xDataPt, double yDataPt, double xGrdPt, double yGrdPt) {
        double dx = xDataPt - xGrdPt;
        double dy = yDataPt - yGrdPt;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

