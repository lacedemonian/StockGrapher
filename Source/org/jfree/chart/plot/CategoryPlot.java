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
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisCollection;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.Zoomable;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectList;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.SortOrder;

public class CategoryPlot
extends Plot
implements ValueAxisPlot,
Zoomable,
RendererChangeListener,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = -3537691700434728188L;
    public static final boolean DEFAULT_DOMAIN_GRIDLINES_VISIBLE = false;
    public static final boolean DEFAULT_RANGE_GRIDLINES_VISIBLE = true;
    public static final Stroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f, 0, 2, 0.0f, new float[]{2.0f, 2.0f}, 0.0f);
    public static final Paint DEFAULT_GRIDLINE_PAINT = Color.lightGray;
    public static final Font DEFAULT_VALUE_LABEL_FONT = new Font("SansSerif", 0, 10);
    protected static ResourceBundle localizationResources = ResourceBundle.getBundle("org.jfree.chart.plot.LocalizationBundle");
    private PlotOrientation orientation = PlotOrientation.VERTICAL;
    private RectangleInsets axisOffset;
    private ObjectList domainAxes = new ObjectList();
    private ObjectList domainAxisLocations = new ObjectList();
    private boolean drawSharedDomainAxis;
    private ObjectList rangeAxes = new ObjectList();
    private ObjectList rangeAxisLocations = new ObjectList();
    private ObjectList datasets = new ObjectList();
    private ObjectList datasetToDomainAxisMap = new ObjectList();
    private ObjectList datasetToRangeAxisMap = new ObjectList();
    private ObjectList renderers = new ObjectList();
    private DatasetRenderingOrder renderingOrder = DatasetRenderingOrder.REVERSE;
    private SortOrder columnRenderingOrder = SortOrder.ASCENDING;
    private SortOrder rowRenderingOrder = SortOrder.ASCENDING;
    private boolean domainGridlinesVisible;
    private CategoryAnchor domainGridlinePosition;
    private transient Stroke domainGridlineStroke;
    private transient Paint domainGridlinePaint;
    private boolean rangeGridlinesVisible;
    private transient Stroke rangeGridlineStroke;
    private transient Paint rangeGridlinePaint;
    private double anchorValue;
    private boolean rangeCrosshairVisible;
    private double rangeCrosshairValue;
    private transient Stroke rangeCrosshairStroke;
    private transient Paint rangeCrosshairPaint;
    private boolean rangeCrosshairLockedOnData = true;
    private Map foregroundDomainMarkers;
    private Map backgroundDomainMarkers;
    private Map foregroundRangeMarkers;
    private Map backgroundRangeMarkers;
    private List annotations;
    private int weight;
    private AxisSpace fixedDomainAxisSpace;
    private AxisSpace fixedRangeAxisSpace;
    private LegendItemCollection fixedLegendItems;

    public CategoryPlot() {
        this(null, null, null, null);
    }

    public CategoryPlot(CategoryDataset dataset, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryItemRenderer renderer) {
        this.datasets.set(0, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        this.axisOffset = RectangleInsets.ZERO_INSETS;
        this.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT, false);
        this.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT, false);
        this.renderers.set(0, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }
        this.domainAxes.set(0, domainAxis);
        this.mapDatasetToDomainAxis(0, 0);
        if (domainAxis != null) {
            domainAxis.setPlot(this);
            domainAxis.addChangeListener(this);
        }
        this.drawSharedDomainAxis = false;
        this.rangeAxes.set(0, rangeAxis);
        this.mapDatasetToRangeAxis(0, 0);
        if (rangeAxis != null) {
            rangeAxis.setPlot(this);
            rangeAxis.addChangeListener(this);
        }
        this.configureDomainAxes();
        this.configureRangeAxes();
        this.domainGridlinesVisible = false;
        this.domainGridlinePosition = CategoryAnchor.MIDDLE;
        this.domainGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.domainGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.rangeGridlinesVisible = true;
        this.rangeGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.foregroundDomainMarkers = new HashMap();
        this.backgroundDomainMarkers = new HashMap();
        this.foregroundRangeMarkers = new HashMap();
        this.backgroundRangeMarkers = new HashMap();
        ValueMarker baseline = new ValueMarker(0.0, new Color(0.8f, 0.8f, 0.8f, 0.5f), new BasicStroke(1.0f), new Color(0.85f, 0.85f, 0.95f, 0.5f), new BasicStroke(1.0f), 0.6f);
        this.addRangeMarker(baseline, Layer.BACKGROUND);
        this.anchorValue = 0.0;
        this.annotations = new ArrayList();
    }

    public String getPlotType() {
        return localizationResources.getString("Category_Plot");
    }

    public PlotOrientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(PlotOrientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        this.orientation = orientation;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public RectangleInsets getAxisOffset() {
        return this.axisOffset;
    }

    public void setAxisOffset(RectangleInsets offset) {
        if (offset == null) {
            throw new IllegalArgumentException("Null 'offset' argument.");
        }
        this.axisOffset = offset;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public CategoryAxis getDomainAxis() {
        return this.getDomainAxis(0);
    }

    public CategoryAxis getDomainAxis(int index) {
        Plot parent;
        CategoryAxis result = null;
        if (index < this.domainAxes.size()) {
            result = (CategoryAxis)this.domainAxes.get(index);
        }
        if (result == null && (parent = this.getParent()) instanceof CategoryPlot) {
            CategoryPlot cp = (CategoryPlot)parent;
            result = cp.getDomainAxis(index);
        }
        return result;
    }

    public void setDomainAxis(CategoryAxis axis) {
        this.setDomainAxis(0, axis);
    }

    public void setDomainAxis(int index, CategoryAxis axis) {
        this.setDomainAxis(index, axis, true);
    }

    public void setDomainAxis(int index, CategoryAxis axis, boolean notify) {
        CategoryAxis existing = (CategoryAxis)this.domainAxes.get(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        this.domainAxes.set(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public void setDomainAxes(CategoryAxis[] axes) {
        for (int i = 0; i < axes.length; ++i) {
            this.setDomainAxis(i, axes[i], false);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public AxisLocation getDomainAxisLocation() {
        return this.getDomainAxisLocation(0);
    }

    public AxisLocation getDomainAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.domainAxisLocations.size()) {
            result = (AxisLocation)this.domainAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(this.getDomainAxisLocation(0));
        }
        return result;
    }

    public void setDomainAxisLocation(AxisLocation location) {
        this.setDomainAxisLocation(location, true);
    }

    public void setDomainAxisLocation(AxisLocation location, boolean notify) {
        if (location == null) {
            throw new IllegalArgumentException("Null 'location' argument.");
        }
        this.setDomainAxisLocation(0, location);
    }

    public void setDomainAxisLocation(int index, AxisLocation location) {
        this.domainAxisLocations.set(index, location);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public RectangleEdge getDomainAxisEdge() {
        return this.getDomainAxisEdge(0);
    }

    public RectangleEdge getDomainAxisEdge(int index) {
        RectangleEdge result = null;
        AxisLocation location = this.getDomainAxisLocation(index);
        result = location != null ? Plot.resolveDomainAxisLocation(location, this.orientation) : RectangleEdge.opposite(this.getDomainAxisEdge(0));
        return result;
    }

    public int getDomainAxisCount() {
        return this.domainAxes.size();
    }

    public void clearDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            CategoryAxis axis = (CategoryAxis)this.domainAxes.get(i);
            if (axis == null) continue;
            axis.removeChangeListener(this);
        }
        this.domainAxes.clear();
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void configureDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            CategoryAxis axis = (CategoryAxis)this.domainAxes.get(i);
            if (axis == null) continue;
            axis.configure();
        }
    }

    public ValueAxis getRangeAxis() {
        return this.getRangeAxis(0);
    }

    public ValueAxis getRangeAxis(int index) {
        Plot parent;
        ValueAxis result = null;
        if (index < this.rangeAxes.size()) {
            result = (ValueAxis)this.rangeAxes.get(index);
        }
        if (result == null && (parent = this.getParent()) instanceof CategoryPlot) {
            CategoryPlot cp = (CategoryPlot)parent;
            result = cp.getRangeAxis(index);
        }
        return result;
    }

    public void setRangeAxis(ValueAxis axis) {
        this.setRangeAxis(0, axis);
    }

    public void setRangeAxis(int index, ValueAxis axis) {
        this.setRangeAxis(index, axis, true);
    }

    public void setRangeAxis(int index, ValueAxis axis, boolean notify) {
        ValueAxis existing = (ValueAxis)this.rangeAxes.get(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        this.rangeAxes.set(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public void setRangeAxes(ValueAxis[] axes) {
        for (int i = 0; i < axes.length; ++i) {
            this.setRangeAxis(i, axes[i], false);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public AxisLocation getRangeAxisLocation() {
        return this.getRangeAxisLocation(0);
    }

    public AxisLocation getRangeAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.rangeAxisLocations.size()) {
            result = (AxisLocation)this.rangeAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(this.getRangeAxisLocation(0));
        }
        return result;
    }

    public void setRangeAxisLocation(AxisLocation location) {
        this.setRangeAxisLocation(location, true);
    }

    public void setRangeAxisLocation(AxisLocation location, boolean notify) {
        this.setRangeAxisLocation(0, location, notify);
    }

    public void setRangeAxisLocation(int index, AxisLocation location) {
        this.setRangeAxisLocation(index, location, true);
    }

    public void setRangeAxisLocation(int index, AxisLocation location, boolean notify) {
        this.rangeAxisLocations.set(index, location);
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public RectangleEdge getRangeAxisEdge() {
        return this.getRangeAxisEdge(0);
    }

    public RectangleEdge getRangeAxisEdge(int index) {
        AxisLocation location = this.getRangeAxisLocation(index);
        RectangleEdge result = Plot.resolveRangeAxisLocation(location, this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(this.getRangeAxisEdge(0));
        }
        return result;
    }

    public int getRangeAxisCount() {
        return this.rangeAxes.size();
    }

    public void clearRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis axis = (ValueAxis)this.rangeAxes.get(i);
            if (axis == null) continue;
            axis.removeChangeListener(this);
        }
        this.rangeAxes.clear();
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void configureRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis axis = (ValueAxis)this.rangeAxes.get(i);
            if (axis == null) continue;
            axis.configure();
        }
    }

    public CategoryDataset getDataset() {
        return this.getDataset(0);
    }

    public CategoryDataset getDataset(int index) {
        CategoryDataset result = null;
        if (this.datasets.size() > index) {
            result = (CategoryDataset)this.datasets.get(index);
        }
        return result;
    }

    public void setDataset(CategoryDataset dataset) {
        this.setDataset(0, dataset);
    }

    public void setDataset(int index, CategoryDataset dataset) {
        CategoryDataset existing = (CategoryDataset)this.datasets.get(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.datasets.set(index, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);
        this.datasetChanged(event);
    }

    public void mapDatasetToDomainAxis(int index, int axisIndex) {
        this.datasetToDomainAxisMap.set(index, new Integer(axisIndex));
        this.datasetChanged(new DatasetChangeEvent(this, this.getDataset(index)));
    }

    public CategoryAxis getDomainAxisForDataset(int index) {
        CategoryAxis result = this.getDomainAxis();
        Integer axisIndex = (Integer)this.datasetToDomainAxisMap.get(index);
        if (axisIndex != null) {
            result = this.getDomainAxis(axisIndex);
        }
        return result;
    }

    public void mapDatasetToRangeAxis(int index, int axisIndex) {
        this.datasetToRangeAxisMap.set(index, new Integer(axisIndex));
        this.datasetChanged(new DatasetChangeEvent(this, this.getDataset(index)));
    }

    public ValueAxis getRangeAxisForDataset(int index) {
        ValueAxis result = this.getRangeAxis();
        Integer axisIndex = (Integer)this.datasetToRangeAxisMap.get(index);
        if (axisIndex != null) {
            result = this.getRangeAxis(axisIndex);
        }
        return result;
    }

    public CategoryItemRenderer getRenderer() {
        return this.getRenderer(0);
    }

    public CategoryItemRenderer getRenderer(int index) {
        CategoryItemRenderer result = null;
        if (this.renderers.size() > index) {
            result = (CategoryItemRenderer)this.renderers.get(index);
        }
        return result;
    }

    public void setRenderer(CategoryItemRenderer renderer) {
        this.setRenderer(0, renderer, true);
    }

    public void setRenderer(CategoryItemRenderer renderer, boolean notify) {
        this.setRenderer(0, renderer, notify);
    }

    public void setRenderer(int index, CategoryItemRenderer renderer) {
        this.setRenderer(index, renderer, true);
    }

    public void setRenderer(int index, CategoryItemRenderer renderer, boolean notify) {
        CategoryItemRenderer existing = (CategoryItemRenderer)this.renderers.get(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.renderers.set(index, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }
        this.configureDomainAxes();
        this.configureRangeAxes();
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public void setRenderers(CategoryItemRenderer[] renderers) {
        for (int i = 0; i < renderers.length; ++i) {
            this.setRenderer(i, renderers[i], false);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public CategoryItemRenderer getRendererForDataset(CategoryDataset dataset) {
        CategoryItemRenderer result = null;
        for (int i = 0; i < this.datasets.size(); ++i) {
            if (this.datasets.get(i) != dataset) continue;
            result = (CategoryItemRenderer)this.renderers.get(i);
            break;
        }
        return result;
    }

    public int getIndexOf(CategoryItemRenderer renderer) {
        return this.renderers.indexOf(renderer);
    }

    public DatasetRenderingOrder getDatasetRenderingOrder() {
        return this.renderingOrder;
    }

    public void setDatasetRenderingOrder(DatasetRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.renderingOrder = order;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public SortOrder getColumnRenderingOrder() {
        return this.columnRenderingOrder;
    }

    public void setColumnRenderingOrder(SortOrder order) {
        this.columnRenderingOrder = order;
    }

    public SortOrder getRowRenderingOrder() {
        return this.rowRenderingOrder;
    }

    public void setRowRenderingOrder(SortOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.rowRenderingOrder = order;
    }

    public boolean isDomainGridlinesVisible() {
        return this.domainGridlinesVisible;
    }

    public void setDomainGridlinesVisible(boolean visible) {
        if (this.domainGridlinesVisible != visible) {
            this.domainGridlinesVisible = visible;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public CategoryAnchor getDomainGridlinePosition() {
        return this.domainGridlinePosition;
    }

    public void setDomainGridlinePosition(CategoryAnchor position) {
        if (position == null) {
            throw new IllegalArgumentException("Null 'position' argument.");
        }
        this.domainGridlinePosition = position;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    public void setDomainGridlineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' not permitted.");
        }
        this.domainGridlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }

    public void setDomainGridlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.domainGridlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean isRangeGridlinesVisible() {
        return this.rangeGridlinesVisible;
    }

    public void setRangeGridlinesVisible(boolean visible) {
        if (this.rangeGridlinesVisible != visible) {
            this.rangeGridlinesVisible = visible;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public Stroke getRangeGridlineStroke() {
        return this.rangeGridlineStroke;
    }

    public void setRangeGridlineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeGridlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getRangeGridlinePaint() {
        return this.rangeGridlinePaint;
    }

    public void setRangeGridlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.rangeGridlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public LegendItemCollection getFixedLegendItems() {
        return this.fixedLegendItems;
    }

    public void setFixedLegendItems(LegendItemCollection items) {
        this.fixedLegendItems = items;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public LegendItemCollection getLegendItems() {
        LegendItemCollection result = this.fixedLegendItems;
        if (result == null) {
            result = new LegendItemCollection();
            int count = this.datasets.size();
            for (int datasetIndex = 0; datasetIndex < count; ++datasetIndex) {
                CategoryItemRenderer renderer;
                CategoryDataset dataset = this.getDataset(datasetIndex);
                if (dataset == null || (renderer = this.getRenderer(datasetIndex)) == null) continue;
                int seriesCount = dataset.getRowCount();
                for (int i = 0; i < seriesCount; ++i) {
                    LegendItem item = renderer.getLegendItem(datasetIndex, i);
                    if (item == null) continue;
                    result.add(item);
                }
            }
        }
        return result;
    }

    public void handleClick(int x, int y, PlotRenderingInfo info) {
        Rectangle2D dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            double java2D = 0.0;
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                java2D = x;
            } else if (this.orientation == PlotOrientation.VERTICAL) {
                java2D = y;
            }
            RectangleEdge edge = Plot.resolveRangeAxisLocation(this.getRangeAxisLocation(), this.orientation);
            double value = this.getRangeAxis().java2DToValue(java2D, info.getDataArea(), edge);
            this.setAnchorValue(value);
            this.setRangeCrosshairValue(value);
        }
    }

    public void zoom(double percent) {
        if (percent > 0.0) {
            double range = this.getRangeAxis().getRange().getLength();
            double scaledRange = range * percent;
            this.getRangeAxis().setRange(this.anchorValue - scaledRange / 2.0, this.anchorValue + scaledRange / 2.0);
        } else {
            this.getRangeAxis().setAutoRange(true);
        }
    }

    public void datasetChanged(DatasetChangeEvent event) {
        int count = this.rangeAxes.size();
        for (int axisIndex = 0; axisIndex < count; ++axisIndex) {
            ValueAxis yAxis = this.getRangeAxis(axisIndex);
            if (yAxis == null) continue;
            yAxis.configure();
        }
        if (this.getParent() != null) {
            this.getParent().datasetChanged(event);
        } else {
            PlotChangeEvent e = new PlotChangeEvent(this);
            e.setType(ChartChangeEventType.DATASET_UPDATED);
            this.notifyListeners(e);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void rendererChanged(RendererChangeEvent event) {
        Plot parent = this.getParent();
        if (parent != null) {
            if (!(parent instanceof RendererChangeListener)) throw new RuntimeException("The renderer has changed and I don't know what to do!");
            RendererChangeListener rcl = (RendererChangeListener)((Object)parent);
            rcl.rendererChanged(event);
            return;
        } else {
            this.configureRangeAxes();
            PlotChangeEvent e = new PlotChangeEvent(this);
            this.notifyListeners(e);
        }
    }

    public void addDomainMarker(CategoryMarker marker) {
        this.addDomainMarker(marker, Layer.FOREGROUND);
    }

    public void addDomainMarker(CategoryMarker marker, Layer layer) {
        this.addDomainMarker(0, marker, layer);
    }

    public void addDomainMarker(int index, CategoryMarker marker, Layer layer) {
        if (layer == Layer.FOREGROUND) {
            ArrayList<CategoryMarker> markers = (ArrayList<CategoryMarker>)this.foregroundDomainMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList<CategoryMarker>();
                this.foregroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        } else if (layer == Layer.BACKGROUND) {
            ArrayList<CategoryMarker> markers = (ArrayList<CategoryMarker>)this.backgroundDomainMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList<CategoryMarker>();
                this.backgroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void clearDomainMarkers() {
        if (this.backgroundDomainMarkers != null) {
            this.backgroundDomainMarkers.clear();
        }
        if (this.foregroundDomainMarkers != null) {
            this.foregroundDomainMarkers.clear();
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Collection getDomainMarkers(Layer layer) {
        return this.getDomainMarkers(0, layer);
    }

    public Collection getDomainMarkers(int index, Layer layer) {
        Collection result = null;
        Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = (Collection)this.foregroundDomainMarkers.get(key);
        } else if (layer == Layer.BACKGROUND) {
            result = (Collection)this.backgroundDomainMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection(result);
        }
        return result;
    }

    public void clearDomainMarkers(int index) {
        Collection markers;
        Integer key = new Integer(index);
        if (this.backgroundDomainMarkers != null && (markers = (Collection)this.backgroundDomainMarkers.get(key)) != null) {
            markers.clear();
        }
        if (this.foregroundDomainMarkers != null && (markers = (Collection)this.foregroundDomainMarkers.get(key)) != null) {
            markers.clear();
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void addRangeMarker(Marker marker) {
        this.addRangeMarker(marker, Layer.FOREGROUND);
    }

    public void addRangeMarker(Marker marker, Layer layer) {
        this.addRangeMarker(0, marker, layer);
    }

    public void addRangeMarker(int index, Marker marker, Layer layer) {
        if (layer == Layer.FOREGROUND) {
            ArrayList<Marker> markers = (ArrayList<Marker>)this.foregroundRangeMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList<Marker>();
                this.foregroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        } else if (layer == Layer.BACKGROUND) {
            ArrayList<Marker> markers = (ArrayList<Marker>)this.backgroundRangeMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList<Marker>();
                this.backgroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void clearRangeMarkers() {
        if (this.backgroundRangeMarkers != null) {
            this.backgroundRangeMarkers.clear();
        }
        if (this.foregroundRangeMarkers != null) {
            this.foregroundRangeMarkers.clear();
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Collection getRangeMarkers(Layer layer) {
        return this.getRangeMarkers(0, layer);
    }

    public Collection getRangeMarkers(int index, Layer layer) {
        Collection result = null;
        Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = (Collection)this.foregroundRangeMarkers.get(key);
        } else if (layer == Layer.BACKGROUND) {
            result = (Collection)this.backgroundRangeMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection(result);
        }
        return result;
    }

    public void clearRangeMarkers(int index) {
        Collection markers;
        Integer key = new Integer(index);
        if (this.backgroundRangeMarkers != null && (markers = (Collection)this.backgroundRangeMarkers.get(key)) != null) {
            markers.clear();
        }
        if (this.foregroundRangeMarkers != null && (markers = (Collection)this.foregroundRangeMarkers.get(key)) != null) {
            markers.clear();
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean isRangeCrosshairVisible() {
        return this.rangeCrosshairVisible;
    }

    public void setRangeCrosshairVisible(boolean flag) {
        if (this.rangeCrosshairVisible != flag) {
            this.rangeCrosshairVisible = flag;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public boolean isRangeCrosshairLockedOnData() {
        return this.rangeCrosshairLockedOnData;
    }

    public void setRangeCrosshairLockedOnData(boolean flag) {
        if (this.rangeCrosshairLockedOnData != flag) {
            this.rangeCrosshairLockedOnData = flag;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public double getRangeCrosshairValue() {
        return this.rangeCrosshairValue;
    }

    public void setRangeCrosshairValue(double value) {
        this.setRangeCrosshairValue(value, true);
    }

    public void setRangeCrosshairValue(double value, boolean notify) {
        this.rangeCrosshairValue = value;
        if (this.isRangeCrosshairVisible() && notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public Stroke getRangeCrosshairStroke() {
        return this.rangeCrosshairStroke;
    }

    public void setRangeCrosshairStroke(Stroke stroke) {
        this.rangeCrosshairStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getRangeCrosshairPaint() {
        return this.rangeCrosshairPaint;
    }

    public void setRangeCrosshairPaint(Paint paint) {
        this.rangeCrosshairPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public List getAnnotations() {
        return this.annotations;
    }

    public void addAnnotation(CategoryAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean removeAnnotation(CategoryAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }

    public void clearAnnotations() {
        this.annotations.clear();
        this.notifyListeners(new PlotChangeEvent(this));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected AxisSpace calculateDomainAxisSpace(Graphics2D g2, Rectangle2D plotArea, AxisSpace space) {
        if (space == null) {
            space = new AxisSpace();
        }
        if (this.fixedDomainAxisSpace != null) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(this.fixedDomainAxisSpace.getLeft(), RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getRight(), RectangleEdge.RIGHT);
                return space;
            } else {
                if (this.orientation != PlotOrientation.VERTICAL) return space;
                space.ensureAtLeast(this.fixedDomainAxisSpace.getTop(), RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getBottom(), RectangleEdge.BOTTOM);
            }
            return space;
        } else {
            RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(this.getDomainAxisLocation(), this.orientation);
            if (this.drawSharedDomainAxis) {
                space = this.getDomainAxis().reserveSpace(g2, this, plotArea, domainEdge, space);
            }
            int i = 0;
            while (i < this.domainAxes.size()) {
                Axis xAxis = (Axis)this.domainAxes.get(i);
                if (xAxis != null) {
                    RectangleEdge edge = this.getDomainAxisEdge(i);
                    space = xAxis.reserveSpace(g2, this, plotArea, edge, space);
                }
                ++i;
            }
            return space;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected AxisSpace calculateRangeAxisSpace(Graphics2D g2, Rectangle2D plotArea, AxisSpace space) {
        if (space == null) {
            space = new AxisSpace();
        }
        if (this.fixedRangeAxisSpace != null) {
            if (this.orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getTop(), RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getBottom(), RectangleEdge.BOTTOM);
                return space;
            } else {
                if (this.orientation != PlotOrientation.VERTICAL) return space;
                space.ensureAtLeast(this.fixedRangeAxisSpace.getLeft(), RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getRight(), RectangleEdge.RIGHT);
            }
            return space;
        } else {
            int i = 0;
            while (i < this.rangeAxes.size()) {
                Axis yAxis = (Axis)this.rangeAxes.get(i);
                if (yAxis != null) {
                    RectangleEdge edge = this.getRangeAxisEdge(i);
                    space = yAxis.reserveSpace(g2, this, plotArea, edge, space);
                }
                ++i;
            }
            return space;
        }
    }

    protected AxisSpace calculateAxisSpace(Graphics2D g2, Rectangle2D plotArea) {
        AxisSpace space = new AxisSpace();
        space = this.calculateRangeAxisSpace(g2, plotArea, space);
        space = this.calculateDomainAxisSpace(g2, plotArea, space);
        return space;
    }

    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo state) {
        int i;
        int i2;
        boolean b2;
        boolean b1 = area.getWidth() <= 10.0;
        boolean bl = b2 = area.getHeight() <= 10.0;
        if (b1 || b2) {
            return;
        }
        if (state == null) {
            state = new PlotRenderingInfo(null);
        }
        state.setPlotArea(area);
        RectangleInsets insets = this.getInsets();
        insets.trim(area);
        AxisSpace space = this.calculateAxisSpace(g2, area);
        Rectangle2D dataArea = space.shrink(area, null);
        this.axisOffset.trim(dataArea);
        if (state != null) {
            state.setDataArea(dataArea);
        }
        if (this.getRenderer() != null) {
            this.getRenderer().drawBackground(g2, this, dataArea);
        } else {
            this.drawBackground(g2, dataArea);
        }
        Map axisStateMap = this.drawAxes(g2, area, dataArea, state);
        this.drawDomainGridlines(g2, dataArea);
        AxisState rangeAxisState = (AxisState)axisStateMap.get(this.getRangeAxis());
        if (rangeAxisState == null && parentState != null) {
            rangeAxisState = (AxisState)parentState.getSharedAxisStates().get(this.getRangeAxis());
        }
        if (rangeAxisState != null) {
            this.drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
        }
        for (i = 0; i < this.renderers.size(); ++i) {
            this.drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        for (i = 0; i < this.renderers.size(); ++i) {
            this.drawRangeMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        boolean foundData = false;
        Shape savedClip = g2.getClip();
        g2.clip(dataArea);
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(3, this.getForegroundAlpha()));
        DatasetRenderingOrder order = this.getDatasetRenderingOrder();
        if (order == DatasetRenderingOrder.FORWARD) {
            for (i2 = 0; i2 < this.datasets.size(); ++i2) {
                foundData = this.render(g2, dataArea, i2, state) || foundData;
            }
        } else {
            for (i2 = this.datasets.size() - 1; i2 >= 0; --i2) {
                foundData = this.render(g2, dataArea, i2, state) || foundData;
            }
        }
        g2.setClip(savedClip);
        g2.setComposite(originalComposite);
        if (!foundData) {
            this.drawNoDataMessage(g2, dataArea);
        }
        if (this.isRangeCrosshairVisible()) {
            this.drawRangeLine(g2, dataArea, this.getRangeCrosshairValue(), this.getRangeCrosshairStroke(), this.getRangeCrosshairPaint());
        }
        for (i2 = 0; i2 < this.renderers.size(); ++i2) {
            this.drawDomainMarkers(g2, dataArea, i2, Layer.FOREGROUND);
        }
        for (i2 = 0; i2 < this.renderers.size(); ++i2) {
            this.drawRangeMarkers(g2, dataArea, i2, Layer.FOREGROUND);
        }
        this.drawAnnotations(g2, dataArea);
        if (this.getRenderer() != null) {
            this.getRenderer().drawOutline(g2, this, dataArea);
        } else {
            this.drawOutline(g2, dataArea);
        }
    }

    protected Map drawAxes(Graphics2D g2, Rectangle2D plotArea, Rectangle2D dataArea, PlotRenderingInfo plotState) {
        Axis axis;
        AxisState axisState;
        int index;
        AxisCollection axisCollection = new AxisCollection();
        for (index = 0; index < this.domainAxes.size(); ++index) {
            CategoryAxis xAxis = (CategoryAxis)this.domainAxes.get(index);
            if (xAxis == null) continue;
            axisCollection.add(xAxis, this.getDomainAxisEdge(index));
        }
        for (index = 0; index < this.rangeAxes.size(); ++index) {
            ValueAxis yAxis = (ValueAxis)this.rangeAxes.get(index);
            if (yAxis == null) continue;
            axisCollection.add(yAxis, this.getRangeAxisEdge(index));
        }
        HashMap<Axis, AxisState> axisStateMap = new HashMap<Axis, AxisState>();
        double cursor = dataArea.getMinY() - this.axisOffset.calculateTopOutset(dataArea.getHeight());
        Iterator iterator = axisCollection.getAxesAtTop().iterator();
        while (iterator.hasNext()) {
            axis = (Axis)iterator.next();
            if (axis == null) continue;
            axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.TOP, plotState);
            cursor = axisState.getCursor();
            axisStateMap.put(axis, axisState);
        }
        cursor = dataArea.getMaxY() + this.axisOffset.calculateBottomOutset(dataArea.getHeight());
        iterator = axisCollection.getAxesAtBottom().iterator();
        while (iterator.hasNext()) {
            axis = (Axis)iterator.next();
            if (axis == null) continue;
            axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.BOTTOM, plotState);
            cursor = axisState.getCursor();
            axisStateMap.put(axis, axisState);
        }
        cursor = dataArea.getMinX() - this.axisOffset.calculateLeftOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtLeft().iterator();
        while (iterator.hasNext()) {
            axis = (Axis)iterator.next();
            if (axis == null) continue;
            axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.LEFT, plotState);
            cursor = axisState.getCursor();
            axisStateMap.put(axis, axisState);
        }
        cursor = dataArea.getMaxX() + this.axisOffset.calculateRightOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtRight().iterator();
        while (iterator.hasNext()) {
            axis = (Axis)iterator.next();
            if (axis == null) continue;
            axisState = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.RIGHT, plotState);
            cursor = axisState.getCursor();
            axisStateMap.put(axis, axisState);
        }
        return axisStateMap;
    }

    public boolean render(Graphics2D g2, Rectangle2D dataArea, int index, PlotRenderingInfo info) {
        boolean hasData;
        boolean foundData = false;
        CategoryDataset currentDataset = this.getDataset(index);
        CategoryItemRenderer renderer = this.getRenderer(index);
        CategoryAxis domainAxis = this.getDomainAxisForDataset(index);
        ValueAxis rangeAxis = this.getRangeAxisForDataset(index);
        boolean bl = hasData = !DatasetUtilities.isEmptyOrNull(currentDataset);
        if (hasData && renderer != null) {
            foundData = true;
            CategoryItemRendererState state = renderer.initialise(g2, dataArea, this, index, info);
            int columnCount = currentDataset.getColumnCount();
            int rowCount = currentDataset.getRowCount();
            int passCount = renderer.getPassCount();
            for (int pass = 0; pass < passCount; ++pass) {
                int column;
                int row;
                if (this.columnRenderingOrder == SortOrder.ASCENDING) {
                    for (column = 0; column < columnCount; ++column) {
                        if (this.rowRenderingOrder == SortOrder.ASCENDING) {
                            for (row = 0; row < rowCount; ++row) {
                                renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                            }
                            continue;
                        }
                        for (row = rowCount - 1; row >= 0; --row) {
                            renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                        }
                    }
                    continue;
                }
                for (column = columnCount - 1; column >= 0; --column) {
                    if (this.rowRenderingOrder == SortOrder.ASCENDING) {
                        for (row = 0; row < rowCount; ++row) {
                            renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                        }
                        continue;
                    }
                    for (row = rowCount - 1; row >= 0; --row) {
                        renderer.drawItem(g2, state, dataArea, this, domainAxis, rangeAxis, currentDataset, row, column, pass);
                    }
                }
            }
        }
        return foundData;
    }

    protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea) {
        if (this.isDomainGridlinesVisible()) {
            CategoryDataset data;
            CategoryAxis axis;
            CategoryAnchor anchor = this.getDomainGridlinePosition();
            RectangleEdge domainAxisEdge = this.getDomainAxisEdge();
            Stroke gridStroke = this.getDomainGridlineStroke();
            Paint gridPaint = this.getDomainGridlinePaint();
            if (gridStroke != null && gridPaint != null && (data = this.getDataset()) != null && (axis = this.getDomainAxis()) != null) {
                int columnCount = data.getColumnCount();
                for (int c = 0; c < columnCount; ++c) {
                    double xx = axis.getCategoryJava2DCoordinate(anchor, c, columnCount, dataArea, domainAxisEdge);
                    CategoryItemRenderer renderer1 = this.getRenderer();
                    if (renderer1 == null) continue;
                    renderer1.drawDomainGridline(g2, this, dataArea, xx);
                }
            }
        }
    }

    protected void drawRangeGridlines(Graphics2D g2, Rectangle2D dataArea, List ticks) {
        if (this.isRangeGridlinesVisible()) {
            ValueAxis axis;
            Stroke gridStroke = this.getRangeGridlineStroke();
            Paint gridPaint = this.getRangeGridlinePaint();
            if (gridStroke != null && gridPaint != null && (axis = this.getRangeAxis()) != null) {
                Iterator iterator = ticks.iterator();
                while (iterator.hasNext()) {
                    ValueTick tick = (ValueTick)iterator.next();
                    CategoryItemRenderer renderer1 = this.getRenderer();
                    if (renderer1 == null) continue;
                    renderer1.drawRangeGridline(g2, this, this.getRangeAxis(), dataArea, tick.getValue());
                }
            }
        }
    }

    protected void drawAnnotations(Graphics2D g2, Rectangle2D dataArea) {
        if (this.getAnnotations() != null) {
            Iterator iterator = this.getAnnotations().iterator();
            while (iterator.hasNext()) {
                CategoryAnnotation annotation = (CategoryAnnotation)iterator.next();
                annotation.draw(g2, this, dataArea, this.getDomainAxis(), this.getRangeAxis());
            }
        }
    }

    protected void drawDomainMarkers(Graphics2D g2, Rectangle2D dataArea, int index, Layer layer) {
        CategoryItemRenderer r = this.getRenderer(index);
        if (r == null) {
            return;
        }
        Collection markers = this.getDomainMarkers(index, layer);
        CategoryAxis axis = this.getDomainAxisForDataset(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                CategoryMarker marker = (CategoryMarker)iterator.next();
                r.drawDomainMarker(g2, this, axis, marker, dataArea);
            }
        }
    }

    protected void drawRangeMarkers(Graphics2D g2, Rectangle2D dataArea, int index, Layer layer) {
        CategoryItemRenderer r = this.getRenderer(index);
        if (r == null) {
            return;
        }
        Collection markers = this.getRangeMarkers(index, layer);
        ValueAxis axis = this.getRangeAxisForDataset(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                Marker marker = (Marker)iterator.next();
                r.drawRangeMarker(g2, this, axis, marker, dataArea);
            }
        }
    }

    protected void drawRangeLine(Graphics2D g2, Rectangle2D dataArea, double value, Stroke stroke, Paint paint) {
        double java2D = this.getRangeAxis().valueToJava2D(value, dataArea, this.getRangeAxisEdge());
        Line2D.Double line = null;
        if (this.orientation == PlotOrientation.HORIZONTAL) {
            line = new Line2D.Double(java2D, dataArea.getMinY(), java2D, dataArea.getMaxY());
        } else if (this.orientation == PlotOrientation.VERTICAL) {
            line = new Line2D.Double(dataArea.getMinX(), java2D, dataArea.getMaxX(), java2D);
        }
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(line);
    }

    public Range getDataRange(ValueAxis axis) {
        Range result = null;
        ArrayList mappedDatasets = new ArrayList();
        int rangeIndex = this.rangeAxes.indexOf(axis);
        if (rangeIndex >= 0) {
            mappedDatasets.addAll(this.datasetsMappedToRangeAxis(rangeIndex));
        } else if (axis == this.getRangeAxis()) {
            mappedDatasets.addAll(this.datasetsMappedToRangeAxis(0));
        }
        Iterator iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            CategoryDataset d = (CategoryDataset)iterator.next();
            CategoryItemRenderer r = this.getRendererForDataset(d);
            if (r == null) continue;
            result = Range.combine(result, r.findRangeBounds(d));
        }
        return result;
    }

    private List datasetsMappedToRangeAxis(int index) {
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < this.datasets.size(); ++i) {
            Object dataset = this.datasets.get(i);
            if (dataset == null) continue;
            Integer m = (Integer)this.datasetToRangeAxisMap.get(i);
            if (m == null) {
                if (index != 0) continue;
                result.add(dataset);
                continue;
            }
            if (m != index) continue;
            result.add(dataset);
        }
        return result;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public AxisSpace getFixedDomainAxisSpace() {
        return this.fixedDomainAxisSpace;
    }

    public void setFixedDomainAxisSpace(AxisSpace space) {
        this.fixedDomainAxisSpace = space;
    }

    public AxisSpace getFixedRangeAxisSpace() {
        return this.fixedRangeAxisSpace;
    }

    public void setFixedRangeAxisSpace(AxisSpace space) {
        this.fixedRangeAxisSpace = space;
    }

    public List getCategories() {
        List result = null;
        if (this.getDataset() != null) {
            result = Collections.unmodifiableList(this.getDataset().getColumnKeys());
        }
        return result;
    }

    public boolean getDrawSharedDomainAxis() {
        return this.drawSharedDomainAxis;
    }

    public void setDrawSharedDomainAxis(boolean draw) {
        this.drawSharedDomainAxis = draw;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean isDomainZoomable() {
        return false;
    }

    public boolean isRangeZoomable() {
        return true;
    }

    public void zoomDomainAxes(double factor, PlotRenderingInfo state, Point2D source) {
    }

    public void zoomDomainAxes(double lowerPercent, double upperPercent, PlotRenderingInfo state, Point2D source) {
    }

    public void zoomRangeAxes(double factor, PlotRenderingInfo state, Point2D source) {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis rangeAxis = (ValueAxis)this.rangeAxes.get(i);
            if (rangeAxis == null) continue;
            rangeAxis.resizeRange(factor);
        }
    }

    public void zoomRangeAxes(double lowerPercent, double upperPercent, PlotRenderingInfo state, Point2D source) {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis rangeAxis = (ValueAxis)this.rangeAxes.get(i);
            if (rangeAxis == null) continue;
            rangeAxis.zoomRange(lowerPercent, upperPercent);
        }
    }

    public double getAnchorValue() {
        return this.anchorValue;
    }

    public void setAnchorValue(double value) {
        this.setAnchorValue(value, true);
    }

    public void setAnchorValue(double value, boolean notify) {
        this.anchorValue = value;
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CategoryPlot)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        CategoryPlot that = (CategoryPlot)obj;
        if (this.orientation != that.orientation) {
            return false;
        }
        if (!ObjectUtilities.equal(this.axisOffset, that.axisOffset)) {
            return false;
        }
        if (!this.domainAxes.equals(that.domainAxes)) {
            return false;
        }
        if (!this.domainAxisLocations.equals(that.domainAxisLocations)) {
            return false;
        }
        if (this.drawSharedDomainAxis != that.drawSharedDomainAxis) {
            return false;
        }
        if (!this.rangeAxes.equals(that.rangeAxes)) {
            return false;
        }
        if (!this.rangeAxisLocations.equals(that.rangeAxisLocations)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.datasetToDomainAxisMap, that.datasetToDomainAxisMap)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.datasetToRangeAxisMap, that.datasetToRangeAxisMap)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.renderers, that.renderers)) {
            return false;
        }
        if (this.renderingOrder != that.renderingOrder) {
            return false;
        }
        if (this.columnRenderingOrder != that.columnRenderingOrder) {
            return false;
        }
        if (this.rowRenderingOrder != that.rowRenderingOrder) {
            return false;
        }
        if (this.domainGridlinesVisible != that.domainGridlinesVisible) {
            return false;
        }
        if (this.domainGridlinePosition != that.domainGridlinePosition) {
            return false;
        }
        if (!ObjectUtilities.equal(this.domainGridlineStroke, that.domainGridlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainGridlinePaint, that.domainGridlinePaint)) {
            return false;
        }
        if (this.rangeGridlinesVisible != that.rangeGridlinesVisible) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeGridlineStroke, that.rangeGridlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeGridlinePaint, that.rangeGridlinePaint)) {
            return false;
        }
        if (this.anchorValue != that.anchorValue) {
            return false;
        }
        if (this.rangeCrosshairVisible != that.rangeCrosshairVisible) {
            return false;
        }
        if (this.rangeCrosshairValue != that.rangeCrosshairValue) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeCrosshairStroke, that.rangeCrosshairStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeCrosshairPaint, that.rangeCrosshairPaint)) {
            return false;
        }
        if (this.rangeCrosshairLockedOnData != that.rangeCrosshairLockedOnData) {
            return false;
        }
        if (!ObjectUtilities.equal(this.foregroundRangeMarkers, that.foregroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundRangeMarkers, that.backgroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.annotations, that.annotations)) {
            return false;
        }
        if (this.weight != that.weight) {
            return false;
        }
        if (!ObjectUtilities.equal(this.fixedDomainAxisSpace, that.fixedDomainAxisSpace)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.fixedRangeAxisSpace, that.fixedRangeAxisSpace)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        int i;
        CategoryPlot clone = (CategoryPlot)super.clone();
        clone.domainAxes = new ObjectList();
        for (i = 0; i < this.domainAxes.size(); ++i) {
            CategoryAxis xAxis = (CategoryAxis)this.domainAxes.get(i);
            if (xAxis == null) continue;
            CategoryAxis clonedAxis = (CategoryAxis)xAxis.clone();
            clone.setDomainAxis(i, clonedAxis);
        }
        clone.domainAxisLocations = (ObjectList)this.domainAxisLocations.clone();
        clone.rangeAxes = new ObjectList();
        for (i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis yAxis = (ValueAxis)this.rangeAxes.get(i);
            if (yAxis == null) continue;
            ValueAxis clonedAxis = (ValueAxis)yAxis.clone();
            clone.setRangeAxis(i, clonedAxis);
        }
        clone.rangeAxisLocations = (ObjectList)this.rangeAxisLocations.clone();
        clone.datasets = (ObjectList)this.datasets.clone();
        for (i = 0; i < clone.datasets.size(); ++i) {
            CategoryDataset dataset = clone.getDataset(i);
            if (dataset == null) continue;
            dataset.addChangeListener(clone);
        }
        clone.datasetToDomainAxisMap = (ObjectList)this.datasetToDomainAxisMap.clone();
        clone.datasetToRangeAxisMap = (ObjectList)this.datasetToRangeAxisMap.clone();
        clone.renderers = (ObjectList)this.renderers.clone();
        if (this.fixedDomainAxisSpace != null) {
            clone.fixedDomainAxisSpace = (AxisSpace)ObjectUtilities.clone(this.fixedDomainAxisSpace);
        }
        if (this.fixedRangeAxisSpace != null) {
            clone.fixedRangeAxisSpace = (AxisSpace)ObjectUtilities.clone(this.fixedRangeAxisSpace);
        }
        return clone;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeStroke(this.domainGridlineStroke, stream);
        SerialUtilities.writePaint(this.domainGridlinePaint, stream);
        SerialUtilities.writeStroke(this.rangeGridlineStroke, stream);
        SerialUtilities.writePaint(this.rangeGridlinePaint, stream);
        SerialUtilities.writeStroke(this.rangeCrosshairStroke, stream);
        SerialUtilities.writePaint(this.rangeCrosshairPaint, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        int i;
        stream.defaultReadObject();
        this.domainGridlineStroke = SerialUtilities.readStroke(stream);
        this.domainGridlinePaint = SerialUtilities.readPaint(stream);
        this.rangeGridlineStroke = SerialUtilities.readStroke(stream);
        this.rangeGridlinePaint = SerialUtilities.readPaint(stream);
        this.rangeCrosshairStroke = SerialUtilities.readStroke(stream);
        this.rangeCrosshairPaint = SerialUtilities.readPaint(stream);
        for (i = 0; i < this.domainAxes.size(); ++i) {
            CategoryAxis xAxis = (CategoryAxis)this.domainAxes.get(i);
            if (xAxis == null) continue;
            xAxis.setPlot(this);
            xAxis.addChangeListener(this);
        }
        for (i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis yAxis = (ValueAxis)this.rangeAxes.get(i);
            if (yAxis == null) continue;
            yAxis.setPlot(this);
            yAxis.addChangeListener(this);
        }
        int datasetCount = this.datasets.size();
        for (int i2 = 0; i2 < datasetCount; ++i2) {
            Dataset dataset = (Dataset)this.datasets.get(i2);
            if (dataset == null) continue;
            dataset.addChangeListener(this);
        }
        int rendererCount = this.renderers.size();
        for (int i3 = 0; i3 < rendererCount; ++i3) {
            CategoryItemRenderer renderer = (CategoryItemRenderer)this.renderers.get(i3);
            if (renderer == null) continue;
            renderer.addChangeListener(this);
        }
    }
}

