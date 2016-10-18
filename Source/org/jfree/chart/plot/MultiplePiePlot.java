/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.plot;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.TableOrder;

public class MultiplePiePlot
extends Plot
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -355377800470807389L;
    private JFreeChart pieChart;
    private CategoryDataset dataset;
    private TableOrder dataExtractOrder;
    private double limit = 0.0;

    public MultiplePiePlot() {
        this(null);
    }

    public MultiplePiePlot(CategoryDataset dataset) {
        this.dataset = dataset;
        PiePlot piePlot = new PiePlot(null);
        this.pieChart = new JFreeChart(piePlot);
        this.pieChart.removeLegend();
        this.dataExtractOrder = TableOrder.BY_COLUMN;
        this.pieChart.setBackgroundPaint(null);
        TextTitle seriesTitle = new TextTitle("Series Title", new Font("SansSerif", 1, 12));
        seriesTitle.setPosition(RectangleEdge.BOTTOM);
        this.pieChart.setTitle(seriesTitle);
    }

    public CategoryDataset getDataset() {
        return this.dataset;
    }

    public void setDataset(CategoryDataset dataset) {
        if (this.dataset != null) {
            this.dataset.removeChangeListener(this);
        }
        this.dataset = dataset;
        if (dataset != null) {
            this.setDatasetGroup(dataset.getGroup());
            dataset.addChangeListener(this);
        }
        this.datasetChanged(new DatasetChangeEvent(this, dataset));
    }

    public JFreeChart getPieChart() {
        return this.pieChart;
    }

    public void setPieChart(JFreeChart pieChart) {
        this.pieChart = pieChart;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public TableOrder getDataExtractOrder() {
        return this.dataExtractOrder;
    }

    public void setDataExtractOrder(TableOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument");
        }
        this.dataExtractOrder = order;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getLimit() {
        return this.limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public String getPlotType() {
        return "Multiple Pie Plot";
    }

    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
        int displayRows;
        RectangleInsets insets = this.getInsets();
        insets.trim(area);
        this.drawBackground(g2, area);
        this.drawOutline(g2, area);
        if (DatasetUtilities.isEmptyOrNull(this.dataset)) {
            this.drawNoDataMessage(g2, area);
            return;
        }
        int pieCount = 0;
        pieCount = this.dataExtractOrder == TableOrder.BY_ROW ? this.dataset.getRowCount() : this.dataset.getColumnCount();
        int displayCols = (int)Math.ceil(Math.sqrt(pieCount));
        if (displayCols > (displayRows = (int)Math.ceil((double)pieCount / (double)displayCols)) && area.getWidth() < area.getHeight()) {
            int temp = displayCols;
            displayCols = displayRows;
            displayRows = temp;
        }
        int x = (int)area.getX();
        int y = (int)area.getY();
        int width = (int)area.getWidth() / displayCols;
        int height = (int)area.getHeight() / displayRows;
        int row = 0;
        int column = 0;
        int diff = displayRows * displayCols - pieCount;
        int xoffset = 0;
        Rectangle rect = new Rectangle();
        for (int pieIndex = 0; pieIndex < pieCount; ++pieIndex) {
            rect.setBounds(x + xoffset + width * column, y + height * row, width, height);
            String title = null;
            title = this.dataExtractOrder == TableOrder.BY_ROW ? this.dataset.getRowKey(pieIndex).toString() : this.dataset.getColumnKey(pieIndex).toString();
            this.pieChart.setTitle(title);
            PieDataset piedataset = null;
            PieDataset dd = new CategoryToPieDataset(this.dataset, this.dataExtractOrder, pieIndex);
            piedataset = this.limit > 0.0 ? DatasetUtilities.createConsolidatedPieDataset(dd, (Comparable)((Object)"Other"), this.limit) : dd;
            PiePlot piePlot = (PiePlot)this.pieChart.getPlot();
            piePlot.setDataset(piedataset);
            piePlot.setPieIndex(pieIndex);
            ChartRenderingInfo subinfo = null;
            if (info != null) {
                subinfo = new ChartRenderingInfo();
            }
            this.pieChart.draw(g2, rect, subinfo);
            if (info != null) {
                info.getOwner().getEntityCollection().addAll(subinfo.getEntityCollection());
                info.addSubplotInfo(subinfo.getPlotInfo());
            }
            if (++column != displayCols) continue;
            column = 0;
            if (++row != displayRows - 1 || diff == 0) continue;
            xoffset = diff * width / 2;
        }
    }

    public LegendItemCollection getLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        if (this.dataset != null) {
            List keys = null;
            if (this.dataExtractOrder == TableOrder.BY_ROW) {
                keys = this.dataset.getColumnKeys();
            } else if (this.dataExtractOrder == TableOrder.BY_COLUMN) {
                keys = this.dataset.getRowKeys();
            }
            if (keys != null) {
                int section = 0;
                Iterator iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String label;
                    String description = label = iterator.next().toString();
                    PiePlot plot = (PiePlot)this.pieChart.getPlot();
                    Paint paint = plot.getSectionPaint(section);
                    Paint outlinePaint = plot.getSectionOutlinePaint(section);
                    Stroke outlineStroke = plot.getSectionOutlineStroke(section);
                    LegendItem item = new LegendItem(label, description, null, null, Plot.DEFAULT_LEGEND_ITEM_CIRCLE, paint, outlineStroke, outlinePaint);
                    result.add(item);
                    ++section;
                }
            }
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MultiplePiePlot)) {
            return false;
        }
        MultiplePiePlot that = (MultiplePiePlot)obj;
        if (this.dataExtractOrder != that.dataExtractOrder) {
            return false;
        }
        if (this.limit != that.limit) {
            return false;
        }
        if (!ObjectUtilities.equal(this.pieChart, that.pieChart)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }
}

