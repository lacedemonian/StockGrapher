/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.xy;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer2;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PublicCloneable;

public class StackedXYAreaRenderer2
extends XYAreaRenderer2
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 7752676509764539182L;

    public StackedXYAreaRenderer2() {
        this(null, null);
    }

    public StackedXYAreaRenderer2(XYToolTipGenerator labelGenerator, XYURLGenerator urlGenerator) {
        super(labelGenerator, urlGenerator);
    }

    public Range findRangeBounds(XYDataset dataset) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        TableXYDataset d = (TableXYDataset)dataset;
        int itemCount = d.getItemCount();
        for (int i = 0; i < itemCount; ++i) {
            double[] stackValues = this.getStackValues((TableXYDataset)dataset, d.getSeriesCount(), i);
            min = Math.min(min, stackValues[0]);
            max = Math.max(max, stackValues[1]);
        }
        return new Range(min, max);
    }

    public int getPassCount() {
        return 1;
    }

    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        float transStack1;
        float transStackLeft;
        float transY1;
        GeneralPath entityArea = null;
        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }
        TableXYDataset tdataset = (TableXYDataset)dataset;
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);
        if (Double.isNaN(y1)) {
            y1 = 0.0;
        }
        double[] stack1 = this.getStackValues(tdataset, series, item);
        double x0 = dataset.getXValue(series, Math.max(item - 1, 0));
        double y0 = dataset.getYValue(series, Math.max(item - 1, 0));
        if (Double.isNaN(y0)) {
            y0 = 0.0;
        }
        double[] stack0 = this.getStackValues(tdataset, series, Math.max(item - 1, 0));
        int itemCount = dataset.getItemCount(series);
        double x2 = dataset.getXValue(series, Math.min(item + 1, itemCount - 1));
        double y2 = dataset.getYValue(series, Math.min(item + 1, itemCount - 1));
        if (Double.isNaN(y2)) {
            y2 = 0.0;
        }
        double[] stack2 = this.getStackValues(tdataset, series, Math.min(item + 1, itemCount - 1));
        double xleft = (x0 + x1) / 2.0;
        double xright = (x1 + x2) / 2.0;
        double[] stackLeft = this.averageStackValues(stack0, stack1);
        double[] stackRight = this.averageStackValues(stack1, stack2);
        double[] adjStackLeft = this.adjustedStackValues(stack0, stack1);
        double[] adjStackRight = this.adjustedStackValues(stack1, stack2);
        RectangleEdge edge0 = plot.getDomainAxisEdge();
        float transX1 = (float)domainAxis.valueToJava2D(x1, dataArea, edge0);
        float transXLeft = (float)domainAxis.valueToJava2D(xleft, dataArea, edge0);
        float transXRight = (float)domainAxis.valueToJava2D(xright, dataArea, edge0);
        RectangleEdge edge1 = plot.getRangeAxisEdge();
        GeneralPath left = new GeneralPath();
        GeneralPath right = new GeneralPath();
        if (y1 >= 0.0) {
            transY1 = (float)rangeAxis.valueToJava2D(y1 + stack1[1], dataArea, edge1);
            transStack1 = (float)rangeAxis.valueToJava2D(stack1[1], dataArea, edge1);
            transStackLeft = (float)rangeAxis.valueToJava2D(adjStackLeft[1], dataArea, edge1);
            if (y0 >= 0.0) {
                double yleft = (y0 + y1) / 2.0 + stackLeft[1];
                float transYLeft = (float)rangeAxis.valueToJava2D(yleft, dataArea, edge1);
                left.moveTo(transX1, transY1);
                left.lineTo(transX1, transStack1);
                left.lineTo(transXLeft, transStackLeft);
                left.lineTo(transXLeft, transYLeft);
                left.closePath();
            } else {
                left.moveTo(transX1, transStack1);
                left.lineTo(transX1, transY1);
                left.lineTo(transXLeft, transStackLeft);
                left.closePath();
            }
            float transStackRight = (float)rangeAxis.valueToJava2D(adjStackRight[1], dataArea, edge1);
            if (y2 >= 0.0) {
                double yright = (y1 + y2) / 2.0 + stackRight[1];
                float transYRight = (float)rangeAxis.valueToJava2D(yright, dataArea, edge1);
                right.moveTo(transX1, transStack1);
                right.lineTo(transX1, transY1);
                right.lineTo(transXRight, transYRight);
                right.lineTo(transXRight, transStackRight);
                right.closePath();
            } else {
                right.moveTo(transX1, transStack1);
                right.lineTo(transX1, transY1);
                right.lineTo(transXRight, transStackRight);
                right.closePath();
            }
        } else {
            transY1 = (float)rangeAxis.valueToJava2D(y1 + stack1[0], dataArea, edge1);
            transStack1 = (float)rangeAxis.valueToJava2D(stack1[0], dataArea, edge1);
            transStackLeft = (float)rangeAxis.valueToJava2D(adjStackLeft[0], dataArea, edge1);
            if (y0 >= 0.0) {
                left.moveTo(transX1, transStack1);
                left.lineTo(transX1, transY1);
                left.lineTo(transXLeft, transStackLeft);
                left.clone();
            } else {
                double yleft = (y0 + y1) / 2.0 + stackLeft[0];
                float transYLeft = (float)rangeAxis.valueToJava2D(yleft, dataArea, edge1);
                left.moveTo(transX1, transY1);
                left.lineTo(transX1, transStack1);
                left.lineTo(transXLeft, transStackLeft);
                left.lineTo(transXLeft, transYLeft);
                left.closePath();
            }
            float transStackRight = (float)rangeAxis.valueToJava2D(adjStackRight[0], dataArea, edge1);
            if (y2 >= 0.0) {
                right.moveTo(transX1, transStack1);
                right.lineTo(transX1, transY1);
                right.lineTo(transXRight, transStackRight);
                right.closePath();
            } else {
                double yright = (y1 + y2) / 2.0 + stackRight[0];
                float transYRight = (float)rangeAxis.valueToJava2D(yright, dataArea, edge1);
                right.moveTo(transX1, transStack1);
                right.lineTo(transX1, transY1);
                right.lineTo(transXRight, transYRight);
                right.lineTo(transXRight, transStackRight);
                right.closePath();
            }
        }
        Paint itemPaint = this.getItemPaint(series, item);
        if (pass == 0) {
            g2.setPaint(itemPaint);
            g2.fill(left);
            g2.fill(right);
        }
        if (entities != null) {
            GeneralPath gp = new GeneralPath(left);
            gp.append(right, false);
            entityArea = gp;
            this.addEntity(entities, entityArea, dataset, series, item, transX1, transY1);
        }
    }

    private double[] getStackValues(TableXYDataset dataset, int series, int index) {
        double[] result = new double[2];
        for (int i = 0; i < series; ++i) {
            double v = dataset.getYValue(i, index);
            if (Double.isNaN(v)) continue;
            if (v >= 0.0) {
                double[] arrd = result;
                arrd[1] = arrd[1] + v;
                continue;
            }
            double[] arrd = result;
            arrd[0] = arrd[0] + v;
        }
        return result;
    }

    private double[] averageStackValues(double[] stack1, double[] stack2) {
        double[] result = new double[]{(stack1[0] + stack2[0]) / 2.0, (stack1[1] + stack2[1]) / 2.0};
        return result;
    }

    private double[] adjustedStackValues(double[] stack1, double[] stack2) {
        double[] result = new double[2];
        result[0] = stack1[0] == 0.0 || stack2[0] == 0.0 ? 0.0 : (stack1[0] + stack2[0]) / 2.0;
        result[1] = stack1[1] == 0.0 || stack2[1] == 0.0 ? 0.0 : (stack1[1] + stack2[1]) / 2.0;
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
