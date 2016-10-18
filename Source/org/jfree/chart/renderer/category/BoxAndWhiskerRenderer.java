/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.renderer.category;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.Outlier;
import org.jfree.chart.renderer.OutlierList;
import org.jfree.chart.renderer.OutlierListCollection;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public class BoxAndWhiskerRenderer
extends AbstractCategoryItemRenderer
implements Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 632027470694481177L;
    private transient Paint artifactPaint = Color.black;
    private boolean fillBox = true;
    private double itemMargin = 0.2;

    public Paint getArtifactPaint() {
        return this.artifactPaint;
    }

    public void setArtifactPaint(Paint paint) {
        this.artifactPaint = paint;
    }

    public boolean getFillBox() {
        return this.fillBox;
    }

    public void setFillBox(boolean flag) {
        this.fillBox = flag;
        this.notifyListeners(new RendererChangeEvent(this));
    }

    public double getItemMargin() {
        return this.itemMargin;
    }

    public void setItemMargin(double margin) {
        this.itemMargin = margin;
    }

    public LegendItem getLegendItem(int datasetIndex, int series) {
        String label;
        CategoryPlot cp = this.getPlot();
        if (cp == null) {
            return null;
        }
        CategoryDataset dataset = cp.getDataset(datasetIndex);
        String description = label = this.getLegendItemLabelGenerator().generateLabel(dataset, series);
        String toolTipText = null;
        if (this.getLegendItemToolTipGenerator() != null) {
            toolTipText = this.getLegendItemToolTipGenerator().generateLabel(dataset, series);
        }
        String urlText = null;
        if (this.getLegendItemURLGenerator() != null) {
            urlText = this.getLegendItemURLGenerator().generateLabel(dataset, series);
        }
        Rectangle2D.Double shape = new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0);
        Paint paint = this.getSeriesPaint(series);
        Paint outlinePaint = this.getSeriesOutlinePaint(series);
        Stroke outlineStroke = this.getSeriesOutlineStroke(series);
        return new LegendItem(label, description, toolTipText, urlText, (Shape)shape, paint, outlineStroke, outlinePaint);
    }

    public CategoryItemRendererState initialise(Graphics2D g2, Rectangle2D dataArea, CategoryPlot plot, int rendererIndex, PlotRenderingInfo info) {
        CategoryItemRendererState state = super.initialise(g2, dataArea, plot, rendererIndex, info);
        CategoryAxis domainAxis = this.getDomainAxis(plot, rendererIndex);
        CategoryDataset dataset = plot.getDataset(rendererIndex);
        if (dataset != null) {
            int columns = dataset.getColumnCount();
            int rows = dataset.getRowCount();
            double space = 0.0;
            PlotOrientation orientation = plot.getOrientation();
            if (orientation == PlotOrientation.HORIZONTAL) {
                space = dataArea.getHeight();
            } else if (orientation == PlotOrientation.VERTICAL) {
                space = dataArea.getWidth();
            }
            double categoryMargin = 0.0;
            double currentItemMargin = 0.0;
            if (columns > 1) {
                categoryMargin = domainAxis.getCategoryMargin();
            }
            if (rows > 1) {
                currentItemMargin = this.getItemMargin();
            }
            double used = space * (1.0 - domainAxis.getLowerMargin() - domainAxis.getUpperMargin() - categoryMargin - currentItemMargin);
            if (rows * columns > 0) {
                state.setBarWidth(used / (double)(dataset.getColumnCount() * dataset.getRowCount()));
            } else {
                state.setBarWidth(used);
            }
        }
        return state;
    }

    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
        if (!(dataset instanceof BoxAndWhiskerCategoryDataset)) {
            throw new IllegalArgumentException("BoxAndWhiskerRenderer.drawItem() : the data should be of type BoxAndWhiskerCategoryDataset only.");
        }
        PlotOrientation orientation = plot.getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            this.drawHorizontalItem(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column);
        } else if (orientation == PlotOrientation.VERTICAL) {
            this.drawVerticalItem(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column);
        }
    }

    public void drawHorizontalItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column) {
        EntityCollection entities;
        Number xMedian;
        BoxAndWhiskerCategoryDataset bawDataset = (BoxAndWhiskerCategoryDataset)dataset;
        double categoryEnd = domainAxis.getCategoryEnd(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double categoryStart = domainAxis.getCategoryStart(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double categoryWidth = Math.abs(categoryEnd - categoryStart);
        double yy = categoryStart;
        int seriesCount = this.getRowCount();
        int categoryCount = this.getColumnCount();
        if (seriesCount > 1) {
            double seriesGap = dataArea.getWidth() * this.getItemMargin() / (double)(categoryCount * (seriesCount - 1));
            double usedWidth = state.getBarWidth() * (double)seriesCount + seriesGap * (double)(seriesCount - 1);
            double offset = (categoryWidth - usedWidth) / 2.0;
            yy = yy + offset + (double)row * (state.getBarWidth() + seriesGap);
        } else {
            double offset = (categoryWidth - state.getBarWidth()) / 2.0;
            yy += offset;
        }
        Paint p = this.getItemPaint(row, column);
        if (p != null) {
            g2.setPaint(p);
        }
        Stroke s = this.getItemStroke(row, column);
        g2.setStroke(s);
        RectangleEdge location = plot.getRangeAxisEdge();
        Number xQ1 = bawDataset.getQ1Value(row, column);
        Number xQ3 = bawDataset.getQ3Value(row, column);
        Number xMax = bawDataset.getMaxRegularValue(row, column);
        Number xMin = bawDataset.getMinRegularValue(row, column);
        Rectangle2D.Double box = null;
        if (xQ1 != null && xQ3 != null && xMax != null && xMin != null) {
            double xxQ1 = rangeAxis.valueToJava2D(xQ1.doubleValue(), dataArea, location);
            double xxQ3 = rangeAxis.valueToJava2D(xQ3.doubleValue(), dataArea, location);
            double xxMax = rangeAxis.valueToJava2D(xMax.doubleValue(), dataArea, location);
            double xxMin = rangeAxis.valueToJava2D(xMin.doubleValue(), dataArea, location);
            double yymid = yy + state.getBarWidth() / 2.0;
            g2.draw(new Line2D.Double(xxMax, yymid, xxQ3, yymid));
            g2.draw(new Line2D.Double(xxMax, yy, xxMax, yy + state.getBarWidth()));
            g2.draw(new Line2D.Double(xxMin, yymid, xxQ1, yymid));
            g2.draw(new Line2D.Double(xxMin, yy, xxMin, yy + state.getBarWidth()));
            box = new Rectangle2D.Double(Math.min(xxQ1, xxQ3), yy, Math.abs(xxQ1 - xxQ3), state.getBarWidth());
            if (this.fillBox) {
                g2.fill(box);
            }
            g2.draw(box);
        }
        g2.setPaint(this.artifactPaint);
        double aRadius = 0.0;
        Number xMean = bawDataset.getMeanValue(row, column);
        if (xMean != null) {
            double xxMean = rangeAxis.valueToJava2D(xMean.doubleValue(), dataArea, location);
            aRadius = state.getBarWidth() / 4.0;
            Ellipse2D.Double avgEllipse = new Ellipse2D.Double(xxMean - aRadius, yy + aRadius, aRadius * 2.0, aRadius * 2.0);
            g2.fill(avgEllipse);
            g2.draw(avgEllipse);
        }
        if ((xMedian = bawDataset.getMedianValue(row, column)) != null) {
            double xxMedian = rangeAxis.valueToJava2D(xMedian.doubleValue(), dataArea, location);
            g2.draw(new Line2D.Double(xxMedian, yy, xxMedian, yy + state.getBarWidth()));
        }
        if (state.getInfo() != null && (entities = state.getEntityCollection()) != null) {
            String tip = null;
            CategoryToolTipGenerator tipster = this.getToolTipGenerator(row, column);
            if (tipster != null) {
                tip = tipster.generateToolTip(dataset, row, column);
            }
            String url = null;
            if (this.getItemURLGenerator(row, column) != null) {
                url = this.getItemURLGenerator(row, column).generateURL(dataset, row, column);
            }
            CategoryItemEntity entity = new CategoryItemEntity(box, tip, url, dataset, row, dataset.getColumnKey(column), column);
            entities.add(entity);
        }
    }

    public void drawVerticalItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column) {
        EntityCollection entities;
        Number yMedian;
        BoxAndWhiskerCategoryDataset bawDataset = (BoxAndWhiskerCategoryDataset)dataset;
        double categoryEnd = domainAxis.getCategoryEnd(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double categoryStart = domainAxis.getCategoryStart(column, this.getColumnCount(), dataArea, plot.getDomainAxisEdge());
        double categoryWidth = categoryEnd - categoryStart;
        double xx = categoryStart;
        int seriesCount = this.getRowCount();
        int categoryCount = this.getColumnCount();
        if (seriesCount > 1) {
            double seriesGap = dataArea.getWidth() * this.getItemMargin() / (double)(categoryCount * (seriesCount - 1));
            double usedWidth = state.getBarWidth() * (double)seriesCount + seriesGap * (double)(seriesCount - 1);
            double offset = (categoryWidth - usedWidth) / 2.0;
            xx = xx + offset + (double)row * (state.getBarWidth() + seriesGap);
        } else {
            double offset = (categoryWidth - state.getBarWidth()) / 2.0;
            xx += offset;
        }
        double yyAverage = 0.0;
        Paint p = this.getItemPaint(row, column);
        if (p != null) {
            g2.setPaint(p);
        }
        Stroke s = this.getItemStroke(row, column);
        g2.setStroke(s);
        double aRadius = 0.0;
        RectangleEdge location = plot.getRangeAxisEdge();
        Number yQ1 = bawDataset.getQ1Value(row, column);
        Number yQ3 = bawDataset.getQ3Value(row, column);
        Number yMax = bawDataset.getMaxRegularValue(row, column);
        Number yMin = bawDataset.getMinRegularValue(row, column);
        Rectangle2D.Double box = null;
        if (yQ1 != null && yQ3 != null && yMax != null && yMin != null) {
            double yyQ1 = rangeAxis.valueToJava2D(yQ1.doubleValue(), dataArea, location);
            double yyQ3 = rangeAxis.valueToJava2D(yQ3.doubleValue(), dataArea, location);
            double yyMax = rangeAxis.valueToJava2D(yMax.doubleValue(), dataArea, location);
            double yyMin = rangeAxis.valueToJava2D(yMin.doubleValue(), dataArea, location);
            double xxmid = xx + state.getBarWidth() / 2.0;
            g2.draw(new Line2D.Double(xxmid, yyMax, xxmid, yyQ3));
            g2.draw(new Line2D.Double(xx, yyMax, xx + state.getBarWidth(), yyMax));
            g2.draw(new Line2D.Double(xxmid, yyMin, xxmid, yyQ1));
            g2.draw(new Line2D.Double(xx, yyMin, xx + state.getBarWidth(), yyMin));
            box = new Rectangle2D.Double(xx, Math.min(yyQ1, yyQ3), state.getBarWidth(), Math.abs(yyQ1 - yyQ3));
            if (this.fillBox) {
                g2.fill(box);
            }
            g2.draw(box);
        }
        g2.setPaint(this.artifactPaint);
        Number yMean = bawDataset.getMeanValue(row, column);
        if (yMean != null) {
            yyAverage = rangeAxis.valueToJava2D(yMean.doubleValue(), dataArea, location);
            aRadius = state.getBarWidth() / 4.0;
            Ellipse2D.Double avgEllipse = new Ellipse2D.Double(xx + aRadius, yyAverage - aRadius, aRadius * 2.0, aRadius * 2.0);
            g2.fill(avgEllipse);
            g2.draw(avgEllipse);
        }
        if ((yMedian = bawDataset.getMedianValue(row, column)) != null) {
            double yyMedian = rangeAxis.valueToJava2D(yMedian.doubleValue(), dataArea, location);
            g2.draw(new Line2D.Double(xx, yyMedian, xx + state.getBarWidth(), yyMedian));
        }
        double maxAxisValue = rangeAxis.valueToJava2D(rangeAxis.getUpperBound(), dataArea, location) + aRadius;
        double minAxisValue = rangeAxis.valueToJava2D(rangeAxis.getLowerBound(), dataArea, location) - aRadius;
        g2.setPaint(p);
        double oRadius = state.getBarWidth() / 3.0;
        ArrayList<Outlier> outliers = new ArrayList<Outlier>();
        OutlierListCollection outlierListCollection = new OutlierListCollection();
        List yOutliers = bawDataset.getOutliers(row, column);
        if (yOutliers != null) {
            for (int i = 0; i < yOutliers.size(); ++i) {
                double yyOutlier;
                double outlier = ((Number)yOutliers.get(i)).doubleValue();
                Number minOutlier = bawDataset.getMinOutlier(row, column);
                Number maxOutlier = bawDataset.getMaxOutlier(row, column);
                Number minRegular = bawDataset.getMinRegularValue(row, column);
                Number maxRegular = bawDataset.getMaxRegularValue(row, column);
                if (outlier > maxOutlier.doubleValue()) {
                    outlierListCollection.setHighFarOut(true);
                } else if (outlier < minOutlier.doubleValue()) {
                    outlierListCollection.setLowFarOut(true);
                } else if (outlier > maxRegular.doubleValue()) {
                    yyOutlier = rangeAxis.valueToJava2D(outlier, dataArea, location);
                    outliers.add(new Outlier(xx + state.getBarWidth() / 2.0, yyOutlier, oRadius));
                } else if (outlier < minRegular.doubleValue()) {
                    yyOutlier = rangeAxis.valueToJava2D(outlier, dataArea, location);
                    outliers.add(new Outlier(xx + state.getBarWidth() / 2.0, yyOutlier, oRadius));
                }
                Collections.sort(outliers);
            }
            Iterator iterator = outliers.iterator();
            while (iterator.hasNext()) {
                Outlier outlier = (Outlier)iterator.next();
                outlierListCollection.add(outlier);
            }
            iterator = outlierListCollection.iterator();
            while (iterator.hasNext()) {
                OutlierList list = (OutlierList)iterator.next();
                Outlier outlier = list.getAveragedOutlier();
                Point2D point = outlier.getPoint();
                if (list.isMultiple()) {
                    this.drawMultipleEllipse(point, state.getBarWidth(), oRadius, g2);
                    continue;
                }
                this.drawEllipse(point, oRadius, g2);
            }
            if (outlierListCollection.isHighFarOut()) {
                this.drawHighFarOut(aRadius / 2.0, g2, xx + state.getBarWidth() / 2.0, maxAxisValue);
            }
            if (outlierListCollection.isLowFarOut()) {
                this.drawLowFarOut(aRadius / 2.0, g2, xx + state.getBarWidth() / 2.0, minAxisValue);
            }
        }
        if (state.getInfo() != null && (entities = state.getEntityCollection()) != null) {
            String tip = null;
            CategoryToolTipGenerator tipster = this.getToolTipGenerator(row, column);
            if (tipster != null) {
                tip = tipster.generateToolTip(dataset, row, column);
            }
            String url = null;
            if (this.getItemURLGenerator(row, column) != null) {
                url = this.getItemURLGenerator(row, column).generateURL(dataset, row, column);
            }
            CategoryItemEntity entity = new CategoryItemEntity(box, tip, url, dataset, row, dataset.getColumnKey(column), column);
            entities.add(entity);
        }
    }

    private void drawEllipse(Point2D point, double oRadius, Graphics2D g2) {
        Ellipse2D.Double dot = new Ellipse2D.Double(point.getX() + oRadius / 2.0, point.getY(), oRadius, oRadius);
        g2.draw(dot);
    }

    private void drawMultipleEllipse(Point2D point, double boxWidth, double oRadius, Graphics2D g2) {
        Ellipse2D.Double dot1 = new Ellipse2D.Double(point.getX() - boxWidth / 2.0 + oRadius, point.getY(), oRadius, oRadius);
        Ellipse2D.Double dot2 = new Ellipse2D.Double(point.getX() + boxWidth / 2.0, point.getY(), oRadius, oRadius);
        g2.draw(dot1);
        g2.draw(dot2);
    }

    private void drawHighFarOut(double aRadius, Graphics2D g2, double xx, double m) {
        double side = aRadius * 2.0;
        g2.draw(new Line2D.Double(xx - side, m + side, xx + side, m + side));
        g2.draw(new Line2D.Double(xx - side, m + side, xx, m));
        g2.draw(new Line2D.Double(xx + side, m + side, xx, m));
    }

    private void drawLowFarOut(double aRadius, Graphics2D g2, double xx, double m) {
        double side = aRadius * 2.0;
        g2.draw(new Line2D.Double(xx - side, m - side, xx + side, m - side));
        g2.draw(new Line2D.Double(xx - side, m - side, xx, m));
        g2.draw(new Line2D.Double(xx + side, m - side, xx, m));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BoxAndWhiskerRenderer)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        BoxAndWhiskerRenderer that = (BoxAndWhiskerRenderer)obj;
        if (!PaintUtilities.equal(this.artifactPaint, that.artifactPaint)) {
            return false;
        }
        if (this.fillBox != that.fillBox) {
            return false;
        }
        if (this.itemMargin != that.itemMargin) {
            return false;
        }
        return true;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.artifactPaint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.artifactPaint = SerialUtilities.readPaint(stream);
    }
}

