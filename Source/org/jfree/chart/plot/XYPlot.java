/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.plot;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
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
import java.util.TreeMap;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisCollection;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.plot.Zoomable;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectList;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public class XYPlot
extends Plot
implements ValueAxisPlot,
Zoomable,
RendererChangeListener,
Cloneable,
PublicCloneable,
Serializable {
    private static final long serialVersionUID = 7044148245716569264L;
    public static final Stroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f, 0, 2, 0.0f, new float[]{2.0f, 2.0f}, 0.0f);
    public static final Paint DEFAULT_GRIDLINE_PAINT = Color.lightGray;
    public static final boolean DEFAULT_CROSSHAIR_VISIBLE = false;
    public static final Stroke DEFAULT_CROSSHAIR_STROKE = DEFAULT_GRIDLINE_STROKE;
    public static final Paint DEFAULT_CROSSHAIR_PAINT = Color.blue;
    protected static ResourceBundle localizationResources = ResourceBundle.getBundle("org.jfree.chart.plot.LocalizationBundle");
    private PlotOrientation orientation = PlotOrientation.VERTICAL;
    private RectangleInsets axisOffset = RectangleInsets.ZERO_INSETS;
    private ObjectList domainAxes = new ObjectList();
    private ObjectList domainAxisLocations = new ObjectList();
    private ObjectList rangeAxes = new ObjectList();
    private ObjectList rangeAxisLocations = new ObjectList();
    private ObjectList datasets = new ObjectList();
    private ObjectList renderers = new ObjectList();
    private Map datasetToDomainAxisMap = new TreeMap();
    private Map datasetToRangeAxisMap = new TreeMap();
    private transient Point2D quadrantOrigin = new Point2D.Double(0.0, 0.0);
    private transient Paint[] quadrantPaint = new Paint[]{null, null, null, null};
    private boolean domainGridlinesVisible;
    private transient Stroke domainGridlineStroke;
    private transient Paint domainGridlinePaint;
    private boolean rangeGridlinesVisible;
    private transient Stroke rangeGridlineStroke;
    private transient Paint rangeGridlinePaint;
    private boolean rangeZeroBaselineVisible;
    private transient Stroke rangeZeroBaselineStroke;
    private transient Paint rangeZeroBaselinePaint;
    private boolean domainCrosshairVisible;
    private double domainCrosshairValue;
    private transient Stroke domainCrosshairStroke;
    private transient Paint domainCrosshairPaint;
    private boolean domainCrosshairLockedOnData = true;
    private boolean rangeCrosshairVisible;
    private double rangeCrosshairValue;
    private transient Stroke rangeCrosshairStroke;
    private transient Paint rangeCrosshairPaint;
    private boolean rangeCrosshairLockedOnData = true;
    private Map foregroundDomainMarkers = new HashMap();
    private Map backgroundDomainMarkers = new HashMap();
    private Map foregroundRangeMarkers = new HashMap();
    private Map backgroundRangeMarkers = new HashMap();
    private List annotations;
    private transient Paint domainTickBandPaint;
    private transient Paint rangeTickBandPaint;
    private AxisSpace fixedDomainAxisSpace;
    private AxisSpace fixedRangeAxisSpace;
    private DatasetRenderingOrder datasetRenderingOrder = DatasetRenderingOrder.REVERSE;
    private SeriesRenderingOrder seriesRenderingOrder = SeriesRenderingOrder.REVERSE;
    private int weight = 1;
    private LegendItemCollection fixedLegendItems;

    public XYPlot() {
        this(null, null, null, null);
    }

    public XYPlot(XYDataset dataset, ValueAxis domainAxis, ValueAxis rangeAxis, XYItemRenderer renderer) {
        this.datasets.set(0, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
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
        this.domainAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);
        this.rangeAxes.set(0, rangeAxis);
        this.mapDatasetToRangeAxis(0, 0);
        if (rangeAxis != null) {
            rangeAxis.setPlot(this);
            rangeAxis.addChangeListener(this);
        }
        this.rangeAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);
        this.configureDomainAxes();
        this.configureRangeAxes();
        this.domainGridlinesVisible = true;
        this.domainGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.domainGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.rangeGridlinesVisible = true;
        this.rangeGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.rangeZeroBaselineVisible = false;
        this.rangeZeroBaselinePaint = Color.black;
        this.rangeZeroBaselineStroke = new BasicStroke(0.5f);
        this.domainCrosshairVisible = false;
        this.domainCrosshairValue = 0.0;
        this.domainCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
        this.domainCrosshairPaint = DEFAULT_CROSSHAIR_PAINT;
        this.rangeCrosshairVisible = false;
        this.rangeCrosshairValue = 0.0;
        this.rangeCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
        this.rangeCrosshairPaint = DEFAULT_CROSSHAIR_PAINT;
        this.annotations = new ArrayList();
    }

    public String getPlotType() {
        return localizationResources.getString("XY_Plot");
    }

    public PlotOrientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(PlotOrientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        if (orientation != this.orientation) {
            this.orientation = orientation;
            this.notifyListeners(new PlotChangeEvent(this));
        }
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

    public ValueAxis getDomainAxis() {
        return this.getDomainAxis(0);
    }

    public ValueAxis getDomainAxis(int index) {
        Plot parent;
        ValueAxis result = null;
        if (index < this.domainAxes.size()) {
            result = (ValueAxis)this.domainAxes.get(index);
        }
        if (result == null && (parent = this.getParent()) instanceof XYPlot) {
            XYPlot xy = (XYPlot)parent;
            result = xy.getDomainAxis(index);
        }
        return result;
    }

    public void setDomainAxis(ValueAxis axis) {
        this.setDomainAxis(0, axis);
    }

    public void setDomainAxis(int index, ValueAxis axis) {
        this.setDomainAxis(index, axis, true);
    }

    public void setDomainAxis(int index, ValueAxis axis, boolean notify) {
        ValueAxis existing = this.getDomainAxis(index);
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

    public void setDomainAxes(ValueAxis[] axes) {
        for (int i = 0; i < axes.length; ++i) {
            this.setDomainAxis(i, axes[i], false);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public AxisLocation getDomainAxisLocation() {
        return (AxisLocation)this.domainAxisLocations.get(0);
    }

    public void setDomainAxisLocation(AxisLocation location) {
        this.setDomainAxisLocation(location, true);
    }

    public void setDomainAxisLocation(AxisLocation location, boolean notify) {
        if (location == null) {
            throw new IllegalArgumentException("Null 'location' argument.");
        }
        this.domainAxisLocations.set(0, location);
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public RectangleEdge getDomainAxisEdge() {
        return Plot.resolveDomainAxisLocation(this.getDomainAxisLocation(), this.orientation);
    }

    public int getDomainAxisCount() {
        return this.domainAxes.size();
    }

    public void clearDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            ValueAxis axis = (ValueAxis)this.domainAxes.get(i);
            if (axis == null) continue;
            axis.removeChangeListener(this);
        }
        this.domainAxes.clear();
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void configureDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            ValueAxis axis = (ValueAxis)this.domainAxes.get(i);
            if (axis == null) continue;
            axis.configure();
        }
    }

    public AxisLocation getDomainAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.domainAxisLocations.size()) {
            result = (AxisLocation)this.domainAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(this.getDomainAxisLocation());
        }
        return result;
    }

    public void setDomainAxisLocation(int index, AxisLocation location) {
        this.domainAxisLocations.set(index, location);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public RectangleEdge getDomainAxisEdge(int index) {
        AxisLocation location = this.getDomainAxisLocation(index);
        RectangleEdge result = Plot.resolveDomainAxisLocation(location, this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(this.getDomainAxisEdge());
        }
        return result;
    }

    public ValueAxis getRangeAxis() {
        return this.getRangeAxis(0);
    }

    public void setRangeAxis(ValueAxis axis) {
        ValueAxis existing;
        if (axis != null) {
            axis.setPlot(this);
        }
        if ((existing = this.getRangeAxis()) != null) {
            existing.removeChangeListener(this);
        }
        this.rangeAxes.set(0, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public AxisLocation getRangeAxisLocation() {
        return (AxisLocation)this.rangeAxisLocations.get(0);
    }

    public void setRangeAxisLocation(AxisLocation location) {
        this.setRangeAxisLocation(location, true);
    }

    public void setRangeAxisLocation(AxisLocation location, boolean notify) {
        if (location == null) {
            throw new IllegalArgumentException("Null 'location' argument.");
        }
        this.rangeAxisLocations.set(0, location);
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public RectangleEdge getRangeAxisEdge() {
        return Plot.resolveRangeAxisLocation(this.getRangeAxisLocation(), this.orientation);
    }

    public ValueAxis getRangeAxis(int index) {
        Plot parent;
        ValueAxis result = null;
        if (index < this.rangeAxes.size()) {
            result = (ValueAxis)this.rangeAxes.get(index);
        }
        if (result == null && (parent = this.getParent()) instanceof XYPlot) {
            XYPlot xy = (XYPlot)parent;
            result = xy.getRangeAxis(index);
        }
        return result;
    }

    public void setRangeAxis(int index, ValueAxis axis) {
        this.setRangeAxis(index, axis, true);
    }

    public void setRangeAxis(int index, ValueAxis axis, boolean notify) {
        ValueAxis existing = this.getRangeAxis(index);
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

    public AxisLocation getRangeAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.rangeAxisLocations.size()) {
            result = (AxisLocation)this.rangeAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(this.getRangeAxisLocation());
        }
        return result;
    }

    public void setRangeAxisLocation(int index, AxisLocation location) {
        this.rangeAxisLocations.set(index, location);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public RectangleEdge getRangeAxisEdge(int index) {
        AxisLocation location = this.getRangeAxisLocation(index);
        RectangleEdge result = Plot.resolveRangeAxisLocation(location, this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(this.getRangeAxisEdge());
        }
        return result;
    }

    public XYDataset getDataset() {
        return this.getDataset(0);
    }

    public XYDataset getDataset(int index) {
        XYDataset result = null;
        if (this.datasets.size() > index) {
            result = (XYDataset)this.datasets.get(index);
        }
        return result;
    }

    public void setDataset(XYDataset dataset) {
        this.setDataset(0, dataset);
    }

    public void setDataset(int index, XYDataset dataset) {
        XYDataset existing = this.getDataset(index);
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

    public int getDatasetCount() {
        return this.datasets.size();
    }

    public int indexOf(XYDataset dataset) {
        int result = -1;
        for (int i = 0; i < this.datasets.size(); ++i) {
            if (dataset != this.datasets.get(i)) continue;
            result = i;
            break;
        }
        return result;
    }

    public void mapDatasetToDomainAxis(int index, int axisIndex) {
        this.datasetToDomainAxisMap.put(new Integer(index), new Integer(axisIndex));
        this.datasetChanged(new DatasetChangeEvent(this, this.getDataset(index)));
    }

    public void mapDatasetToRangeAxis(int index, int axisIndex) {
        this.datasetToRangeAxisMap.put(new Integer(index), new Integer(axisIndex));
        this.datasetChanged(new DatasetChangeEvent(this, this.getDataset(index)));
    }

    public XYItemRenderer getRenderer() {
        return this.getRenderer(0);
    }

    public XYItemRenderer getRenderer(int index) {
        XYItemRenderer result = null;
        if (this.renderers.size() > index) {
            result = (XYItemRenderer)this.renderers.get(index);
        }
        return result;
    }

    public void setRenderer(XYItemRenderer renderer) {
        this.setRenderer(0, renderer);
    }

    public void setRenderer(int index, XYItemRenderer renderer) {
        this.setRenderer(index, renderer, true);
    }

    public void setRenderer(int index, XYItemRenderer renderer, boolean notify) {
        XYItemRenderer existing = this.getRenderer(index);
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

    public void setRenderers(XYItemRenderer[] renderers) {
        for (int i = 0; i < renderers.length; ++i) {
            this.setRenderer(i, renderers[i], false);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public DatasetRenderingOrder getDatasetRenderingOrder() {
        return this.datasetRenderingOrder;
    }

    public void setDatasetRenderingOrder(DatasetRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.datasetRenderingOrder = order;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public SeriesRenderingOrder getSeriesRenderingOrder() {
        return this.seriesRenderingOrder;
    }

    public void setSeriesRenderingOrder(SeriesRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.seriesRenderingOrder = order;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public int getIndexOf(XYItemRenderer renderer) {
        return this.renderers.indexOf(renderer);
    }

    public XYItemRenderer getRendererForDataset(XYDataset dataset) {
        XYItemRenderer result = null;
        for (int i = 0; i < this.datasets.size(); ++i) {
            if (this.datasets.get(i) != dataset) continue;
            result = (XYItemRenderer)this.renderers.get(i);
            if (result != null) break;
            result = this.getRenderer();
            break;
        }
        return result;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public Stroke getDomainGridlineStroke() {
        return this.domainGridlineStroke;
    }

    public void setDomainGridlineStroke(Stroke stroke) {
        this.domainGridlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getDomainGridlinePaint() {
        return this.domainGridlinePaint;
    }

    public void setDomainGridlinePaint(Paint paint) {
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
        this.rangeGridlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean isRangeZeroBaselineVisible() {
        return this.rangeZeroBaselineVisible;
    }

    public void setRangeZeroBaselineVisible(boolean visible) {
        this.rangeZeroBaselineVisible = visible;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Stroke getRangeZeroBaselineStroke() {
        return this.rangeZeroBaselineStroke;
    }

    public void setRangeZeroBaselineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.rangeZeroBaselineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getRangeZeroBaselinePaint() {
        return this.rangeZeroBaselinePaint;
    }

    public void setRangeZeroBaselinePaint(Paint paint) {
        this.rangeZeroBaselinePaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getDomainTickBandPaint() {
        return this.domainTickBandPaint;
    }

    public void setDomainTickBandPaint(Paint paint) {
        this.domainTickBandPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getRangeTickBandPaint() {
        return this.rangeTickBandPaint;
    }

    public void setRangeTickBandPaint(Paint paint) {
        this.rangeTickBandPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Point2D getQuadrantOrigin() {
        return this.quadrantOrigin;
    }

    public void setQuadrantOrigin(Point2D origin) {
        if (origin == null) {
            throw new IllegalArgumentException("Null 'origin' argument.");
        }
        this.quadrantOrigin = origin;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getQuadrantPaint(int index) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("The index should be in the range 0 to 3.");
        }
        return this.quadrantPaint[index];
    }

    public void setQuadrantPaint(int index, Paint paint) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("The index should be in the range 0 to 3.");
        }
        this.quadrantPaint[index] = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void addDomainMarker(Marker marker) {
        this.addDomainMarker(marker, Layer.FOREGROUND);
    }

    public void addDomainMarker(Marker marker, Layer layer) {
        this.addDomainMarker(0, marker, layer);
    }

    public void clearDomainMarkers() {
        if (this.foregroundDomainMarkers != null) {
            this.foregroundDomainMarkers.clear();
        }
        if (this.backgroundDomainMarkers != null) {
            this.backgroundDomainMarkers.clear();
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void clearDomainMarkers(int index) {
        Collection markers;
        Integer key = new Integer(index);
        if (this.backgroundDomainMarkers != null && (markers = (Collection)this.backgroundDomainMarkers.get(key)) != null) {
            markers.clear();
        }
        if (this.foregroundRangeMarkers != null && (markers = (Collection)this.foregroundDomainMarkers.get(key)) != null) {
            markers.clear();
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void addDomainMarker(int index, Marker marker, Layer layer) {
        if (layer == Layer.FOREGROUND) {
            ArrayList<Marker> markers = (ArrayList<Marker>)this.foregroundDomainMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList<Marker>();
                this.foregroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        } else if (layer == Layer.BACKGROUND) {
            ArrayList<Marker> markers = (ArrayList<Marker>)this.backgroundDomainMarkers.get(new Integer(index));
            if (markers == null) {
                markers = new ArrayList<Marker>();
                this.backgroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public void addRangeMarker(Marker marker) {
        this.addRangeMarker(marker, Layer.FOREGROUND);
    }

    public void addRangeMarker(Marker marker, Layer layer) {
        this.addRangeMarker(0, marker, layer);
    }

    public void clearRangeMarkers() {
        if (this.foregroundRangeMarkers != null) {
            this.foregroundRangeMarkers.clear();
        }
        if (this.backgroundRangeMarkers != null) {
            this.backgroundRangeMarkers.clear();
        }
        this.notifyListeners(new PlotChangeEvent(this));
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

    public void addAnnotation(XYAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(annotation);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean removeAnnotation(XYAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(annotation);
        if (removed) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
        return removed;
    }

    public List getAnnotations() {
        return new ArrayList(this.annotations);
    }

    public void clearAnnotations() {
        this.annotations.clear();
        this.notifyListeners(new PlotChangeEvent(this));
    }

    protected AxisSpace calculateAxisSpace(Graphics2D g2, Rectangle2D plotArea) {
        AxisSpace space = new AxisSpace();
        space = this.calculateDomainAxisSpace(g2, plotArea, space);
        space = this.calculateRangeAxisSpace(g2, plotArea, space);
        return space;
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
            int i = 0;
            while (i < this.domainAxes.size()) {
                Axis axis = (Axis)this.domainAxes.get(i);
                if (axis != null) {
                    RectangleEdge edge = this.getDomainAxisEdge(i);
                    space = axis.reserveSpace(g2, this, plotArea, edge, space);
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
                Axis axis = (Axis)this.rangeAxes.get(i);
                if (axis != null) {
                    RectangleEdge edge = this.getRangeAxisEdge(i);
                    space = axis.reserveSpace(g2, this, plotArea, edge, space);
                }
                ++i;
            }
            return space;
        }
    }

    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
        ValueAxis domainAxis;
        int rendererCount;
        ValueAxis rangeAxis;
        Paint paint;
        boolean b2;
        XYItemRenderer r;
        int i;
        Stroke stroke;
        int i2;
        AxisState rangeAxisState;
        boolean b1 = area.getWidth() <= 10.0;
        boolean bl = b2 = area.getHeight() <= 10.0;
        if (b1 || b2) {
            return;
        }
        if (info != null) {
            info.setPlotArea(area);
        }
        RectangleInsets insets = this.getInsets();
        insets.trim(area);
        AxisSpace space = this.calculateAxisSpace(g2, area);
        Rectangle2D dataArea = space.shrink(area, null);
        this.axisOffset.trim(dataArea);
        if (info != null) {
            info.setDataArea(dataArea);
        }
        this.drawBackground(g2, dataArea);
        Map axisStateMap = this.drawAxes(g2, area, dataArea, info);
        if (anchor != null && !dataArea.contains(anchor)) {
            anchor = null;
        }
        CrosshairState crosshairState = new CrosshairState();
        crosshairState.setCrosshairDistance(Double.POSITIVE_INFINITY);
        crosshairState.setAnchor(anchor);
        crosshairState.setCrosshairX(this.getDomainCrosshairValue());
        crosshairState.setCrosshairY(this.getRangeCrosshairValue());
        Shape originalClip = g2.getClip();
        Composite originalComposite = g2.getComposite();
        g2.clip(dataArea);
        g2.setComposite(AlphaComposite.getInstance(3, this.getForegroundAlpha()));
        AxisState domainAxisState = (AxisState)axisStateMap.get(this.getDomainAxis());
        if (domainAxisState == null && parentState != null) {
            domainAxisState = (AxisState)parentState.getSharedAxisStates().get(this.getDomainAxis());
        }
        if ((rangeAxisState = (AxisState)axisStateMap.get(this.getRangeAxis())) == null && parentState != null) {
            rangeAxisState = (AxisState)parentState.getSharedAxisStates().get(this.getRangeAxis());
        }
        if (domainAxisState != null) {
            this.drawDomainTickBands(g2, dataArea, domainAxisState.getTicks());
        }
        if (rangeAxisState != null) {
            this.drawRangeTickBands(g2, dataArea, rangeAxisState.getTicks());
        }
        if (domainAxisState != null) {
            this.drawDomainGridlines(g2, dataArea, domainAxisState.getTicks());
        }
        if (rangeAxisState != null) {
            this.drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
            this.drawZeroRangeBaseline(g2, dataArea);
        }
        for (i2 = 0; i2 < this.renderers.size(); ++i2) {
            this.drawDomainMarkers(g2, dataArea, i2, Layer.BACKGROUND);
        }
        for (i2 = 0; i2 < this.renderers.size(); ++i2) {
            this.drawRangeMarkers(g2, dataArea, i2, Layer.BACKGROUND);
        }
        boolean foundData = false;
        DatasetRenderingOrder order = this.getDatasetRenderingOrder();
        if (order == DatasetRenderingOrder.FORWARD) {
            rendererCount = this.renderers.size();
            for (i = 0; i < rendererCount; ++i) {
                r = this.getRenderer(i);
                if (r == null) continue;
                domainAxis = this.getDomainAxisForDataset(i);
                rangeAxis = this.getRangeAxisForDataset(i);
                r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, info);
            }
            for (i = 0; i < this.getDatasetCount(); ++i) {
                foundData = this.render(g2, dataArea, i, info, crosshairState) || foundData;
            }
            for (i = 0; i < rendererCount; ++i) {
                r = this.getRenderer(i);
                if (r == null) continue;
                domainAxis = this.getDomainAxisForDataset(i);
                rangeAxis = this.getRangeAxisForDataset(i);
                r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, info);
            }
        } else if (order == DatasetRenderingOrder.REVERSE) {
            rendererCount = this.renderers.size();
            for (i = rendererCount - 1; i >= 0; --i) {
                r = this.getRenderer(i);
                if (r == null) continue;
                domainAxis = this.getDomainAxisForDataset(i);
                rangeAxis = this.getRangeAxisForDataset(i);
                r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, info);
            }
            for (i = this.getDatasetCount() - 1; i >= 0; --i) {
                foundData = this.render(g2, dataArea, i, info, crosshairState) || foundData;
            }
            for (i = rendererCount - 1; i >= 0; --i) {
                r = this.getRenderer(i);
                if (r == null) continue;
                domainAxis = this.getDomainAxisForDataset(i);
                rangeAxis = this.getRangeAxisForDataset(i);
                r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, info);
            }
        }
        PlotOrientation orient = this.getOrientation();
        if (!this.domainCrosshairLockedOnData && anchor != null) {
            double xx = this.getDomainAxis().java2DToValue(anchor.getX(), dataArea, this.getDomainAxisEdge());
            crosshairState.setCrosshairX(xx);
        }
        this.setDomainCrosshairValue(crosshairState.getCrosshairX(), false);
        if (this.isDomainCrosshairVisible()) {
            double x = this.getDomainCrosshairValue();
            paint = this.getDomainCrosshairPaint();
            stroke = this.getDomainCrosshairStroke();
            if (orient == PlotOrientation.HORIZONTAL) {
                this.drawHorizontalLine(g2, dataArea, x, stroke, paint);
            } else if (orient == PlotOrientation.VERTICAL) {
                this.drawVerticalLine(g2, dataArea, x, stroke, paint);
            }
        }
        if (!this.rangeCrosshairLockedOnData && anchor != null) {
            double yy = this.getRangeAxis().java2DToValue(anchor.getY(), dataArea, this.getRangeAxisEdge());
            crosshairState.setCrosshairY(yy);
        }
        this.setRangeCrosshairValue(crosshairState.getCrosshairY(), false);
        if (this.isRangeCrosshairVisible() && this.getRangeAxis().getRange().contains(this.getRangeCrosshairValue())) {
            double y = this.getRangeCrosshairValue();
            paint = this.getRangeCrosshairPaint();
            stroke = this.getRangeCrosshairStroke();
            if (orient == PlotOrientation.HORIZONTAL) {
                this.drawVerticalLine(g2, dataArea, y, stroke, paint);
            } else if (orient == PlotOrientation.VERTICAL) {
                this.drawHorizontalLine(g2, dataArea, y, stroke, paint);
            }
        }
        if (!foundData) {
            this.drawNoDataMessage(g2, dataArea);
        }
        for (i = 0; i < this.renderers.size(); ++i) {
            this.drawDomainMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }
        for (i = 0; i < this.renderers.size(); ++i) {
            this.drawRangeMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }
        this.drawAnnotations(g2, dataArea, info);
        g2.setClip(originalClip);
        g2.setComposite(originalComposite);
        this.drawOutline(g2, dataArea);
    }

    public void drawBackground(Graphics2D g2, Rectangle2D area) {
        this.fillBackground(g2, area);
        this.drawQuadrants(g2, area);
        this.drawBackgroundImage(g2, area);
    }

    protected void drawQuadrants(Graphics2D g2, Rectangle2D area) {
        boolean somethingToDraw = false;
        ValueAxis xAxis = this.getDomainAxis();
        double x = this.quadrantOrigin.getX();
        double xx = xAxis.valueToJava2D(x, area, this.getDomainAxisEdge());
        ValueAxis yAxis = this.getRangeAxis();
        double y = this.quadrantOrigin.getY();
        double yy = yAxis.valueToJava2D(y, area, this.getRangeAxisEdge());
        double xmin = xAxis.getLowerBound();
        double xxmin = xAxis.valueToJava2D(xmin, area, this.getDomainAxisEdge());
        double xmax = xAxis.getUpperBound();
        double xxmax = xAxis.valueToJava2D(xmax, area, this.getDomainAxisEdge());
        double ymin = yAxis.getLowerBound();
        double yymin = yAxis.valueToJava2D(ymin, area, this.getRangeAxisEdge());
        double ymax = yAxis.getUpperBound();
        double yymax = yAxis.valueToJava2D(ymax, area, this.getRangeAxisEdge());
        Rectangle2D[] r = new Rectangle2D[]{null, null, null, null};
        if (this.quadrantPaint[0] != null && x > xmin && y < ymax) {
            r[0] = this.orientation == PlotOrientation.HORIZONTAL ? new Rectangle2D.Double(Math.min(yymax, yy), Math.min(xxmin, xx), Math.abs(yy - yymax), Math.abs(xx - xxmin)) : new Rectangle2D.Double(Math.min(xxmin, xx), Math.min(yymax, yy), Math.abs(xx - xxmin), Math.abs(yy - yymax));
            somethingToDraw = true;
        }
        if (this.quadrantPaint[1] != null && x < xmax && y < ymax) {
            r[1] = this.orientation == PlotOrientation.HORIZONTAL ? new Rectangle2D.Double(Math.min(yymax, yy), Math.min(xxmax, xx), Math.abs(yy - yymax), Math.abs(xx - xxmax)) : new Rectangle2D.Double(Math.min(xx, xxmax), Math.min(yymax, yy), Math.abs(xx - xxmax), Math.abs(yy - yymax));
            somethingToDraw = true;
        }
        if (this.quadrantPaint[2] != null && x > xmin && y > ymin) {
            r[2] = this.orientation == PlotOrientation.HORIZONTAL ? new Rectangle2D.Double(Math.min(yymin, yy), Math.min(xxmin, xx), Math.abs(yy - yymin), Math.abs(xx - xxmin)) : new Rectangle2D.Double(Math.min(xxmin, xx), Math.min(yymin, yy), Math.abs(xx - xxmin), Math.abs(yy - yymin));
            somethingToDraw = true;
        }
        if (this.quadrantPaint[3] != null && x < xmax && y > ymin) {
            r[3] = this.orientation == PlotOrientation.HORIZONTAL ? new Rectangle2D.Double(Math.min(yymin, yy), Math.min(xxmax, xx), Math.abs(yy - yymin), Math.abs(xx - xxmax)) : new Rectangle2D.Double(Math.min(xx, xxmax), Math.min(yymin, yy), Math.abs(xx - xxmax), Math.abs(yy - yymin));
            somethingToDraw = true;
        }
        if (somethingToDraw) {
            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(3, this.getBackgroundAlpha()));
            for (int i = 0; i < 4; ++i) {
                if (this.quadrantPaint[i] == null || r[i] == null) continue;
                g2.setPaint(this.quadrantPaint[i]);
                g2.fill(r[i]);
            }
            g2.setComposite(originalComposite);
        }
    }

    public void drawDomainTickBands(Graphics2D g2, Rectangle2D dataArea, List ticks) {
        Paint bandPaint = this.getDomainTickBandPaint();
        if (bandPaint != null) {
            boolean fillBand = false;
            ValueAxis xAxis = this.getDomainAxis();
            double previous = xAxis.getLowerBound();
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick)iterator.next();
                double current = tick.getValue();
                if (fillBand) {
                    this.getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            }
            double end = xAxis.getUpperBound();
            if (fillBand) {
                this.getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, end);
            }
        }
    }

    public void drawRangeTickBands(Graphics2D g2, Rectangle2D dataArea, List ticks) {
        Paint bandPaint = this.getRangeTickBandPaint();
        if (bandPaint != null) {
            boolean fillBand = false;
            ValueAxis axis = this.getRangeAxis();
            double previous = axis.getLowerBound();
            Iterator iterator = ticks.iterator();
            while (iterator.hasNext()) {
                ValueTick tick = (ValueTick)iterator.next();
                double current = tick.getValue();
                if (fillBand) {
                    this.getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, current);
                }
                previous = current;
                fillBand = !fillBand;
            }
            double end = axis.getUpperBound();
            if (fillBand) {
                this.getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, end);
            }
        }
    }

    protected Map drawAxes(Graphics2D g2, Rectangle2D plotArea, Rectangle2D dataArea, PlotRenderingInfo plotState) {
        ValueAxis axis;
        AxisState info;
        int index;
        AxisCollection axisCollection = new AxisCollection();
        for (index = 0; index < this.domainAxes.size(); ++index) {
            ValueAxis axis2 = (ValueAxis)this.domainAxes.get(index);
            if (axis2 == null) continue;
            axisCollection.add(axis2, this.getDomainAxisEdge(index));
        }
        for (index = 0; index < this.rangeAxes.size(); ++index) {
            ValueAxis yAxis = (ValueAxis)this.rangeAxes.get(index);
            if (yAxis == null) continue;
            axisCollection.add(yAxis, this.getRangeAxisEdge(index));
        }
        HashMap<ValueAxis, AxisState> axisStateMap = new HashMap<ValueAxis, AxisState>();
        double cursor = dataArea.getMinY() - this.axisOffset.calculateTopOutset(dataArea.getHeight());
        Iterator iterator = axisCollection.getAxesAtTop().iterator();
        while (iterator.hasNext()) {
            axis = (ValueAxis)iterator.next();
            info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.TOP, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }
        cursor = dataArea.getMaxY() + this.axisOffset.calculateBottomOutset(dataArea.getHeight());
        iterator = axisCollection.getAxesAtBottom().iterator();
        while (iterator.hasNext()) {
            axis = (ValueAxis)iterator.next();
            info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.BOTTOM, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }
        cursor = dataArea.getMinX() - this.axisOffset.calculateLeftOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtLeft().iterator();
        while (iterator.hasNext()) {
            axis = (ValueAxis)iterator.next();
            info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.LEFT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }
        cursor = dataArea.getMaxX() + this.axisOffset.calculateRightOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtRight().iterator();
        while (iterator.hasNext()) {
            axis = (ValueAxis)iterator.next();
            info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.RIGHT, plotState);
            cursor = info.getCursor();
            axisStateMap.put(axis, info);
        }
        return axisStateMap;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean render(Graphics2D g2, Rectangle2D dataArea, int index, PlotRenderingInfo info, CrosshairState crosshairState) {
        boolean foundData = false;
        XYDataset dataset = this.getDataset(index);
        if (DatasetUtilities.isEmptyOrNull(dataset)) return foundData;
        foundData = true;
        ValueAxis xAxis = this.getDomainAxisForDataset(index);
        ValueAxis yAxis = this.getRangeAxisForDataset(index);
        XYItemRenderer renderer = this.getRenderer(index);
        if (renderer == null) {
            renderer = this.getRenderer();
        }
        XYItemRendererState state = renderer.initialise(g2, dataArea, this, dataset, info);
        int passCount = renderer.getPassCount();
        SeriesRenderingOrder seriesOrder = this.getSeriesRenderingOrder();
        if (seriesOrder == SeriesRenderingOrder.REVERSE) {
            int pass = 0;
            while (pass < passCount) {
                int seriesCount = dataset.getSeriesCount();
                for (int series = seriesCount - 1; series >= 0; --series) {
                    int itemCount = dataset.getItemCount(series);
                    for (int item = 0; item < itemCount; ++item) {
                        renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);
                    }
                }
                ++pass;
            }
            return foundData;
        }
        int pass = 0;
        while (pass < passCount) {
            int seriesCount = dataset.getSeriesCount();
            for (int series = 0; series < seriesCount; ++series) {
                int itemCount = dataset.getItemCount(series);
                for (int item = 0; item < itemCount; ++item) {
                    renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);
                }
            }
            ++pass;
        }
        return foundData;
    }

    public ValueAxis getDomainAxisForDataset(int index) {
        if (index < 0 || index >= this.getDatasetCount()) {
            throw new IllegalArgumentException("Index 'index' out of bounds.");
        }
        ValueAxis valueAxis = null;
        Integer axisIndex = (Integer)this.datasetToDomainAxisMap.get(new Integer(index));
        valueAxis = axisIndex != null ? this.getDomainAxis(axisIndex) : this.getDomainAxis(0);
        return valueAxis;
    }

    public ValueAxis getRangeAxisForDataset(int index) {
        if (index < 0 || index >= this.getDatasetCount()) {
            throw new IllegalArgumentException("Index 'index' out of bounds.");
        }
        ValueAxis valueAxis = null;
        Integer axisIndex = (Integer)this.datasetToRangeAxisMap.get(new Integer(index));
        valueAxis = axisIndex != null ? this.getRangeAxis(axisIndex) : this.getRangeAxis(0);
        return valueAxis;
    }

    protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea, List ticks) {
        if (this.getRenderer() == null) {
            return;
        }
        if (this.isDomainGridlinesVisible()) {
            Stroke gridStroke = this.getDomainGridlineStroke();
            Paint gridPaint = this.getDomainGridlinePaint();
            if (gridStroke != null && gridPaint != null) {
                Iterator iterator = ticks.iterator();
                while (iterator.hasNext()) {
                    ValueTick tick = (ValueTick)iterator.next();
                    this.getRenderer().drawDomainGridLine(g2, this, this.getDomainAxis(), dataArea, tick.getValue());
                }
            }
        }
    }

    protected void drawRangeGridlines(Graphics2D g2, Rectangle2D area, List ticks) {
        if (this.isRangeGridlinesVisible()) {
            Stroke gridStroke = this.getRangeGridlineStroke();
            Paint gridPaint = this.getRangeGridlinePaint();
            ValueAxis axis = this.getRangeAxis();
            if (axis != null) {
                Iterator iterator = ticks.iterator();
                while (iterator.hasNext()) {
                    ValueTick tick = (ValueTick)iterator.next();
                    if (tick.getValue() == 0.0 && this.isRangeZeroBaselineVisible()) continue;
                    this.getRenderer().drawRangeLine(g2, this, this.getRangeAxis(), area, tick.getValue(), gridPaint, gridStroke);
                }
            }
        }
    }

    protected void drawZeroRangeBaseline(Graphics2D g2, Rectangle2D area) {
        if (this.isRangeZeroBaselineVisible()) {
            this.getRenderer().drawRangeLine(g2, this, this.getRangeAxis(), area, 0.0, this.rangeZeroBaselinePaint, this.rangeZeroBaselineStroke);
        }
    }

    public void drawAnnotations(Graphics2D g2, Rectangle2D dataArea, PlotRenderingInfo info) {
        Iterator iterator = this.annotations.iterator();
        while (iterator.hasNext()) {
            XYAnnotation annotation = (XYAnnotation)iterator.next();
            ValueAxis xAxis = this.getDomainAxis();
            ValueAxis yAxis = this.getRangeAxis();
            annotation.draw(g2, this, dataArea, xAxis, yAxis, 0, info);
        }
    }

    protected void drawDomainMarkers(Graphics2D g2, Rectangle2D dataArea, int index, Layer layer) {
        XYItemRenderer r = this.getRenderer(index);
        if (r == null) {
            return;
        }
        Collection markers = this.getDomainMarkers(index, layer);
        ValueAxis axis = this.getDomainAxisForDataset(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                Marker marker = (Marker)iterator.next();
                r.drawDomainMarker(g2, this, axis, marker, dataArea);
            }
        }
    }

    protected void drawRangeMarkers(Graphics2D g2, Rectangle2D dataArea, int index, Layer layer) {
        XYItemRenderer r = this.getRenderer(index);
        if (r == null) {
            return;
        }
        Collection markers = this.getRangeMarkers(index, layer);
        ValueAxis axis = this.getRangeAxis(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                Marker marker = (Marker)iterator.next();
                r.drawRangeMarker(g2, this, axis, marker, dataArea);
            }
        }
    }

    public Collection getDomainMarkers(Layer layer) {
        return this.getDomainMarkers(0, layer);
    }

    public Collection getRangeMarkers(Layer layer) {
        return this.getRangeMarkers(0, layer);
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

    protected void drawHorizontalLine(Graphics2D g2, Rectangle2D dataArea, double value, Stroke stroke, Paint paint) {
        ValueAxis axis = this.getRangeAxis();
        if (this.getOrientation() == PlotOrientation.HORIZONTAL) {
            axis = this.getDomainAxis();
        }
        if (axis.getRange().contains(value)) {
            double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
            Line2D.Double line = new Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }

    protected void drawVerticalLine(Graphics2D g2, Rectangle2D dataArea, double value, Stroke stroke, Paint paint) {
        ValueAxis axis = this.getDomainAxis();
        if (this.getOrientation() == PlotOrientation.HORIZONTAL) {
            axis = this.getRangeAxis();
        }
        if (axis.getRange().contains(value)) {
            double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
            Line2D.Double line = new Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
            g2.setStroke(stroke);
            g2.setPaint(paint);
            g2.draw(line);
        }
    }

    public void handleClick(int x, int y, PlotRenderingInfo info) {
        Rectangle2D dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            ValueAxis ra;
            ValueAxis da = this.getDomainAxis();
            if (da != null) {
                double hvalue = da.java2DToValue(x, info.getDataArea(), this.getDomainAxisEdge());
                this.setDomainCrosshairValue(hvalue);
            }
            if ((ra = this.getRangeAxis()) != null) {
                double vvalue = ra.java2DToValue(y, info.getDataArea(), this.getRangeAxisEdge());
                this.setRangeCrosshairValue(vvalue);
            }
        }
    }

    private List getDatasetsMappedToDomainAxis(Integer axisIndex) {
        if (axisIndex == null) {
            throw new IllegalArgumentException("Null 'axisIndex' argument.");
        }
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < this.datasets.size(); ++i) {
            Integer mappedAxis = (Integer)this.datasetToDomainAxisMap.get(new Integer(i));
            if (mappedAxis == null) {
                if (!axisIndex.equals(ZERO)) continue;
                result.add(this.datasets.get(i));
                continue;
            }
            if (!mappedAxis.equals(axisIndex)) continue;
            result.add(this.datasets.get(i));
        }
        return result;
    }

    private List getDatasetsMappedToRangeAxis(Integer axisIndex) {
        if (axisIndex == null) {
            throw new IllegalArgumentException("Null 'axisIndex' argument.");
        }
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < this.datasets.size(); ++i) {
            Integer mappedAxis = (Integer)this.datasetToRangeAxisMap.get(new Integer(i));
            if (mappedAxis == null) {
                if (!axisIndex.equals(ZERO)) continue;
                result.add(this.datasets.get(i));
                continue;
            }
            if (!mappedAxis.equals(axisIndex)) continue;
            result.add(this.datasets.get(i));
        }
        return result;
    }

    protected int getDomainAxisIndex(ValueAxis axis) {
        Plot parent;
        int result = this.domainAxes.indexOf(axis);
        if (result < 0 && (parent = this.getParent()) instanceof XYPlot) {
            XYPlot p = (XYPlot)parent;
            result = p.getDomainAxisIndex(axis);
        }
        return result;
    }

    protected int getRangeAxisIndex(ValueAxis axis) {
        Plot parent;
        int result = this.rangeAxes.indexOf(axis);
        if (result < 0 && (parent = this.getParent()) instanceof XYPlot) {
            XYPlot p = (XYPlot)parent;
            result = p.getRangeAxisIndex(axis);
        }
        return result;
    }

    public Range getDataRange(ValueAxis axis) {
        int rangeIndex;
        Range result = null;
        ArrayList mappedDatasets = new ArrayList();
        boolean isDomainAxis = true;
        int domainIndex = this.getDomainAxisIndex(axis);
        if (domainIndex >= 0) {
            isDomainAxis = true;
            mappedDatasets.addAll(this.getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
        }
        if ((rangeIndex = this.getRangeAxisIndex(axis)) >= 0) {
            isDomainAxis = false;
            mappedDatasets.addAll(this.getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        }
        Iterator iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            XYDataset d = (XYDataset)iterator.next();
            if (d == null) continue;
            XYItemRenderer r = this.getRendererForDataset(d);
            if (isDomainAxis) {
                if (r != null) {
                    result = Range.combine(result, r.findDomainBounds(d));
                    continue;
                }
                result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                continue;
            }
            if (r != null) {
                result = Range.combine(result, r.findRangeBounds(d));
                continue;
            }
            result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
        }
        return result;
    }

    public void datasetChanged(DatasetChangeEvent event) {
        this.configureDomainAxes();
        this.configureRangeAxes();
        if (this.getParent() != null) {
            this.getParent().datasetChanged(event);
        } else {
            PlotChangeEvent e = new PlotChangeEvent(this);
            e.setType(ChartChangeEventType.DATASET_UPDATED);
            this.notifyListeners(e);
        }
    }

    public void rendererChanged(RendererChangeEvent event) {
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean isDomainCrosshairVisible() {
        return this.domainCrosshairVisible;
    }

    public void setDomainCrosshairVisible(boolean flag) {
        if (this.domainCrosshairVisible != flag) {
            this.domainCrosshairVisible = flag;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public boolean isDomainCrosshairLockedOnData() {
        return this.domainCrosshairLockedOnData;
    }

    public void setDomainCrosshairLockedOnData(boolean flag) {
        if (this.domainCrosshairLockedOnData != flag) {
            this.domainCrosshairLockedOnData = flag;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public double getDomainCrosshairValue() {
        return this.domainCrosshairValue;
    }

    public void setDomainCrosshairValue(double value) {
        this.setDomainCrosshairValue(value, true);
    }

    public void setDomainCrosshairValue(double value, boolean notify) {
        this.domainCrosshairValue = value;
        if (this.isDomainCrosshairVisible() && notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public Stroke getDomainCrosshairStroke() {
        return this.domainCrosshairStroke;
    }

    public void setDomainCrosshairStroke(Stroke stroke) {
        this.domainCrosshairStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getDomainCrosshairPaint() {
        return this.domainCrosshairPaint;
    }

    public void setDomainCrosshairPaint(Paint paint) {
        this.domainCrosshairPaint = paint;
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

    public void zoomDomainAxes(double factor, PlotRenderingInfo info, Point2D source) {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            ValueAxis domainAxis = (ValueAxis)this.domainAxes.get(i);
            if (domainAxis == null) continue;
            domainAxis.resizeRange(factor);
        }
    }

    public void zoomDomainAxes(double lowerPercent, double upperPercent, PlotRenderingInfo info, Point2D source) {
        for (int i = 0; i < this.domainAxes.size(); ++i) {
            ValueAxis domainAxis = (ValueAxis)this.domainAxes.get(i);
            if (domainAxis == null) continue;
            domainAxis.zoomRange(lowerPercent, upperPercent);
        }
    }

    public void zoomRangeAxes(double factor, PlotRenderingInfo info, Point2D source) {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis rangeAxis = (ValueAxis)this.rangeAxes.get(i);
            if (rangeAxis == null) continue;
            rangeAxis.resizeRange(factor);
        }
    }

    public void zoomRangeAxes(double lowerPercent, double upperPercent, PlotRenderingInfo info, Point2D source) {
        for (int i = 0; i < this.rangeAxes.size(); ++i) {
            ValueAxis rangeAxis = (ValueAxis)this.rangeAxes.get(i);
            if (rangeAxis == null) continue;
            rangeAxis.zoomRange(lowerPercent, upperPercent);
        }
    }

    public boolean isDomainZoomable() {
        return true;
    }

    public boolean isRangeZoomable() {
        return true;
    }

    public int getSeriesCount() {
        int result = 0;
        XYDataset dataset = this.getDataset();
        if (dataset != null) {
            result = dataset.getSeriesCount();
        }
        return result;
    }

    public LegendItemCollection getFixedLegendItems() {
        return this.fixedLegendItems;
    }

    public void setFixedLegendItems(LegendItemCollection items) {
        this.fixedLegendItems = items;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public LegendItemCollection getLegendItems() {
        if (this.fixedLegendItems != null) {
            return this.fixedLegendItems;
        }
        LegendItemCollection result = new LegendItemCollection();
        int count = this.datasets.size();
        for (int datasetIndex = 0; datasetIndex < count; ++datasetIndex) {
            XYDataset dataset = this.getDataset(datasetIndex);
            if (dataset == null) continue;
            XYItemRenderer renderer = this.getRenderer(datasetIndex);
            if (renderer == null) {
                renderer = this.getRenderer(0);
            }
            if (renderer == null) continue;
            int seriesCount = dataset.getSeriesCount();
            for (int i = 0; i < seriesCount; ++i) {
                LegendItem item;
                if (!renderer.isSeriesVisible(i) || !renderer.isSeriesVisibleInLegend(i) || (item = renderer.getLegendItem(datasetIndex, i)) == null) continue;
                result.add(item);
            }
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYPlot)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        XYPlot that = (XYPlot)obj;
        if (this.weight != that.weight) {
            return false;
        }
        if (this.orientation != that.orientation) {
            return false;
        }
        if (!this.domainAxes.equals(that.domainAxes)) {
            return false;
        }
        if (!this.domainAxisLocations.equals(that.domainAxisLocations)) {
            return false;
        }
        if (this.rangeCrosshairLockedOnData != that.rangeCrosshairLockedOnData) {
            return false;
        }
        if (this.domainGridlinesVisible != that.domainGridlinesVisible) {
            return false;
        }
        if (this.rangeGridlinesVisible != that.rangeGridlinesVisible) {
            return false;
        }
        if (this.rangeZeroBaselineVisible != that.rangeZeroBaselineVisible) {
            return false;
        }
        if (this.domainCrosshairVisible != that.domainCrosshairVisible) {
            return false;
        }
        if (this.domainCrosshairValue != that.domainCrosshairValue) {
            return false;
        }
        if (this.domainCrosshairLockedOnData != that.domainCrosshairLockedOnData) {
            return false;
        }
        if (this.rangeCrosshairVisible != that.rangeCrosshairVisible) {
            return false;
        }
        if (this.rangeCrosshairValue != that.rangeCrosshairValue) {
            return false;
        }
        if (!ObjectUtilities.equal(this.axisOffset, that.axisOffset)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.renderers, that.renderers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeAxes, that.rangeAxes)) {
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
        if (!ObjectUtilities.equal(this.domainGridlineStroke, that.domainGridlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainGridlinePaint, that.domainGridlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeGridlineStroke, that.rangeGridlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeGridlinePaint, that.rangeGridlinePaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeZeroBaselinePaint, that.rangeZeroBaselinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeZeroBaselineStroke, that.rangeZeroBaselineStroke)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.domainCrosshairStroke, that.domainCrosshairStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.domainCrosshairPaint, that.domainCrosshairPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.rangeCrosshairStroke, that.rangeCrosshairStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.rangeCrosshairPaint, that.rangeCrosshairPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.foregroundDomainMarkers, that.foregroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundDomainMarkers, that.backgroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.foregroundRangeMarkers, that.foregroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundRangeMarkers, that.backgroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.foregroundDomainMarkers, that.foregroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundDomainMarkers, that.backgroundDomainMarkers)) {
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
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        ValueAxis clonedAxis;
        ValueAxis axis;
        int i;
        XYPlot clone = (XYPlot)super.clone();
        clone.domainAxes = (ObjectList)ObjectUtilities.clone(this.domainAxes);
        for (i = 0; i < this.domainAxes.size(); ++i) {
            axis = (ValueAxis)this.domainAxes.get(i);
            if (axis == null) continue;
            clonedAxis = (ValueAxis)axis.clone();
            clone.domainAxes.set(i, clonedAxis);
            clonedAxis.setPlot(clone);
            clonedAxis.addChangeListener(clone);
        }
        clone.domainAxisLocations = (ObjectList)this.domainAxisLocations.clone();
        clone.rangeAxes = (ObjectList)ObjectUtilities.clone(this.rangeAxes);
        for (i = 0; i < this.rangeAxes.size(); ++i) {
            axis = (ValueAxis)this.rangeAxes.get(i);
            if (axis == null) continue;
            clonedAxis = (ValueAxis)axis.clone();
            clone.rangeAxes.set(i, clonedAxis);
            clonedAxis.setPlot(clone);
            clonedAxis.addChangeListener(clone);
        }
        clone.rangeAxisLocations = (ObjectList)ObjectUtilities.clone(this.rangeAxisLocations);
        clone.datasets = (ObjectList)ObjectUtilities.clone(this.datasets);
        for (i = 0; i < clone.datasets.size(); ++i) {
            XYDataset d = this.getDataset(i);
            if (d == null) continue;
            d.addChangeListener(clone);
        }
        clone.datasetToDomainAxisMap = new TreeMap();
        clone.datasetToDomainAxisMap.putAll(this.datasetToDomainAxisMap);
        clone.datasetToRangeAxisMap = new TreeMap();
        clone.datasetToRangeAxisMap.putAll(this.datasetToRangeAxisMap);
        clone.renderers = (ObjectList)ObjectUtilities.clone(this.renderers);
        for (i = 0; i < this.renderers.size(); ++i) {
            XYItemRenderer renderer2 = (XYItemRenderer)this.renderers.get(i);
            if (!(renderer2 instanceof PublicCloneable)) continue;
            PublicCloneable pc = (PublicCloneable)((Object)renderer2);
            clone.renderers.set(i, pc.clone());
        }
        clone.foregroundDomainMarkers = (Map)ObjectUtilities.clone(this.foregroundDomainMarkers);
        clone.backgroundDomainMarkers = (Map)ObjectUtilities.clone(this.backgroundDomainMarkers);
        clone.foregroundRangeMarkers = (Map)ObjectUtilities.clone(this.foregroundRangeMarkers);
        clone.backgroundRangeMarkers = (Map)ObjectUtilities.clone(this.backgroundRangeMarkers);
        clone.annotations = (List)ObjectUtilities.deepClone(this.annotations);
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
        SerialUtilities.writeStroke(this.rangeZeroBaselineStroke, stream);
        SerialUtilities.writePaint(this.rangeZeroBaselinePaint, stream);
        SerialUtilities.writeStroke(this.domainCrosshairStroke, stream);
        SerialUtilities.writePaint(this.domainCrosshairPaint, stream);
        SerialUtilities.writeStroke(this.rangeCrosshairStroke, stream);
        SerialUtilities.writePaint(this.rangeCrosshairPaint, stream);
        SerialUtilities.writePaint(this.domainTickBandPaint, stream);
        SerialUtilities.writePaint(this.rangeTickBandPaint, stream);
        SerialUtilities.writePoint2D(this.quadrantOrigin, stream);
        for (int i = 0; i < 4; ++i) {
            SerialUtilities.writePaint(this.quadrantPaint[i], stream);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.domainGridlineStroke = SerialUtilities.readStroke(stream);
        this.domainGridlinePaint = SerialUtilities.readPaint(stream);
        this.rangeGridlineStroke = SerialUtilities.readStroke(stream);
        this.rangeGridlinePaint = SerialUtilities.readPaint(stream);
        this.rangeZeroBaselineStroke = SerialUtilities.readStroke(stream);
        this.rangeZeroBaselinePaint = SerialUtilities.readPaint(stream);
        this.domainCrosshairStroke = SerialUtilities.readStroke(stream);
        this.domainCrosshairPaint = SerialUtilities.readPaint(stream);
        this.rangeCrosshairStroke = SerialUtilities.readStroke(stream);
        this.rangeCrosshairPaint = SerialUtilities.readPaint(stream);
        this.domainTickBandPaint = SerialUtilities.readPaint(stream);
        this.rangeTickBandPaint = SerialUtilities.readPaint(stream);
        this.quadrantOrigin = SerialUtilities.readPoint2D(stream);
        this.quadrantPaint = new Paint[4];
        for (int i = 0; i < 4; ++i) {
            this.quadrantPaint[i] = SerialUtilities.readPaint(stream);
        }
        int domainAxisCount = this.domainAxes.size();
        for (int i2 = 0; i2 < domainAxisCount; ++i2) {
            Axis axis = (Axis)this.domainAxes.get(i2);
            if (axis == null) continue;
            axis.setPlot(this);
            axis.addChangeListener(this);
        }
        int rangeAxisCount = this.rangeAxes.size();
        for (int i3 = 0; i3 < rangeAxisCount; ++i3) {
            Axis axis = (Axis)this.rangeAxes.get(i3);
            if (axis == null) continue;
            axis.setPlot(this);
            axis.addChangeListener(this);
        }
        int datasetCount = this.datasets.size();
        for (int i4 = 0; i4 < datasetCount; ++i4) {
            Dataset dataset = (Dataset)this.datasets.get(i4);
            if (dataset == null) continue;
            dataset.addChangeListener(this);
        }
        int rendererCount = this.renderers.size();
        for (int i5 = 0; i5 < rendererCount; ++i5) {
            XYItemRenderer renderer = (XYItemRenderer)this.renderers.get(i5);
            if (renderer == null) continue;
            renderer.addChangeListener(this);
        }
    }
}

