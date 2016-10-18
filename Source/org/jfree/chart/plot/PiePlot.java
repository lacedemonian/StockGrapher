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
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PieLabelDistributor;
import org.jfree.chart.plot.PieLabelRecord;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.text.G2TextMeasurer;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBox;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectList;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintList;
import org.jfree.util.PaintUtilities;
import org.jfree.util.Rotation;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.StrokeList;

public class PiePlot
extends Plot
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -795612466005590431L;
    public static final double DEFAULT_INTERIOR_GAP = 0.25;
    public static final double MAX_INTERIOR_GAP = 0.4;
    public static final double DEFAULT_START_ANGLE = 90.0;
    public static final Font DEFAULT_LABEL_FONT = new Font("SansSerif", 0, 10);
    public static final Paint DEFAULT_LABEL_PAINT = Color.black;
    public static final Paint DEFAULT_LABEL_BACKGROUND_PAINT = new Color(255, 255, 192);
    public static final Paint DEFAULT_LABEL_OUTLINE_PAINT = Color.black;
    public static final Stroke DEFAULT_LABEL_OUTLINE_STROKE = new BasicStroke(0.5f);
    public static final Paint DEFAULT_LABEL_SHADOW_PAINT = Color.lightGray;
    public static final double DEFAULT_MINIMUM_ARC_ANGLE_TO_DRAW = 1.0E-5;
    private PieDataset dataset;
    private int pieIndex;
    private double interiorGap;
    private boolean circular;
    private double startAngle;
    private Rotation direction;
    private transient Paint sectionPaint;
    private PaintList sectionPaintList;
    private transient Paint baseSectionPaint;
    private boolean sectionOutlinesVisible;
    private transient Paint sectionOutlinePaint;
    private PaintList sectionOutlinePaintList;
    private transient Paint baseSectionOutlinePaint;
    private transient Stroke sectionOutlineStroke;
    private StrokeList sectionOutlineStrokeList;
    private transient Stroke baseSectionOutlineStroke;
    private transient Paint shadowPaint = Color.gray;
    private double shadowXOffset = 4.0;
    private double shadowYOffset = 4.0;
    private ObjectList explodePercentages;
    private PieSectionLabelGenerator labelGenerator;
    private Font labelFont;
    private transient Paint labelPaint;
    private transient Paint labelBackgroundPaint;
    private transient Paint labelOutlinePaint;
    private transient Stroke labelOutlineStroke;
    private transient Paint labelShadowPaint;
    private double maximumLabelWidth = 0.2;
    private double labelGap = 0.05;
    private boolean labelLinksVisible;
    private double labelLinkMargin = 0.05;
    private transient Paint labelLinkPaint = Color.black;
    private transient Stroke labelLinkStroke = new BasicStroke(0.5f);
    private PieToolTipGenerator toolTipGenerator;
    private PieURLGenerator urlGenerator;
    private PieSectionLabelGenerator legendLabelGenerator;
    private PieSectionLabelGenerator legendLabelToolTipGenerator;
    private boolean ignoreNullValues;
    private boolean ignoreZeroValues;
    private transient Shape legendItemShape;
    private double minimumArcAngleToDraw;
    protected static ResourceBundle localizationResources = ResourceBundle.getBundle("org.jfree.chart.plot.LocalizationBundle");

    public PiePlot() {
        this(null);
    }

    public PiePlot(PieDataset dataset) {
        this.dataset = dataset;
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        this.pieIndex = 0;
        this.interiorGap = 0.25;
        this.circular = true;
        this.startAngle = 90.0;
        this.direction = Rotation.CLOCKWISE;
        this.minimumArcAngleToDraw = 1.0E-5;
        this.sectionPaint = null;
        this.sectionPaintList = new PaintList();
        this.baseSectionPaint = null;
        this.sectionOutlinesVisible = true;
        this.sectionOutlinePaint = null;
        this.sectionOutlinePaintList = new PaintList();
        this.baseSectionOutlinePaint = DEFAULT_OUTLINE_PAINT;
        this.sectionOutlineStroke = null;
        this.sectionOutlineStrokeList = new StrokeList();
        this.baseSectionOutlineStroke = DEFAULT_OUTLINE_STROKE;
        this.explodePercentages = new ObjectList();
        this.labelGenerator = new StandardPieSectionLabelGenerator();
        this.labelFont = DEFAULT_LABEL_FONT;
        this.labelPaint = DEFAULT_LABEL_PAINT;
        this.labelBackgroundPaint = DEFAULT_LABEL_BACKGROUND_PAINT;
        this.labelOutlinePaint = DEFAULT_LABEL_OUTLINE_PAINT;
        this.labelOutlineStroke = DEFAULT_LABEL_OUTLINE_STROKE;
        this.labelShadowPaint = DEFAULT_LABEL_SHADOW_PAINT;
        this.labelLinksVisible = true;
        this.toolTipGenerator = null;
        this.urlGenerator = null;
        this.legendLabelGenerator = new StandardPieSectionLabelGenerator();
        this.legendLabelToolTipGenerator = null;
        this.legendItemShape = Plot.DEFAULT_LEGEND_ITEM_CIRCLE;
        this.ignoreNullValues = false;
        this.ignoreZeroValues = false;
    }

    public PieDataset getDataset() {
        return this.dataset;
    }

    public void setDataset(PieDataset dataset) {
        PieDataset existing = this.dataset;
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.dataset = dataset;
        if (dataset != null) {
            this.setDatasetGroup(dataset.getGroup());
            dataset.addChangeListener(this);
        }
        DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);
        this.datasetChanged(event);
    }

    public int getPieIndex() {
        return this.pieIndex;
    }

    public void setPieIndex(int index) {
        this.pieIndex = index;
    }

    public double getStartAngle() {
        return this.startAngle;
    }

    public void setStartAngle(double angle) {
        this.startAngle = angle;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Rotation getDirection() {
        return this.direction;
    }

    public void setDirection(Rotation direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Null 'direction' argument.");
        }
        this.direction = direction;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getInteriorGap() {
        return this.interiorGap;
    }

    public void setInteriorGap(double percent) {
        if (percent < 0.0 || percent > 0.4) {
            throw new IllegalArgumentException("Invalid 'percent' (" + percent + ") argument.");
        }
        if (this.interiorGap != percent) {
            this.interiorGap = percent;
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public boolean isCircular() {
        return this.circular;
    }

    public void setCircular(boolean flag) {
        this.setCircular(flag, true);
    }

    public void setCircular(boolean circular, boolean notify) {
        this.circular = circular;
        if (notify) {
            this.notifyListeners(new PlotChangeEvent(this));
        }
    }

    public boolean getIgnoreNullValues() {
        return this.ignoreNullValues;
    }

    public void setIgnoreNullValues(boolean flag) {
        this.ignoreNullValues = flag;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean getIgnoreZeroValues() {
        return this.ignoreZeroValues;
    }

    public void setIgnoreZeroValues(boolean flag) {
        this.ignoreZeroValues = flag;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getSectionPaint() {
        return this.sectionPaint;
    }

    public void setSectionPaint(Paint paint) {
        this.sectionPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getSectionPaint(int section) {
        if (this.sectionPaint != null) {
            return this.sectionPaint;
        }
        Paint result = this.sectionPaintList.getPaint(section);
        if (result == null) {
            DrawingSupplier supplier = this.getDrawingSupplier();
            if (supplier != null) {
                Paint p = supplier.getNextPaint();
                this.sectionPaintList.setPaint(section, p);
                result = p;
            } else {
                result = this.baseSectionPaint;
            }
        }
        return result;
    }

    public void setSectionPaint(int section, Paint paint) {
        this.sectionPaintList.setPaint(section, paint);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getBaseSectionPaint() {
        return this.baseSectionPaint;
    }

    public void setBaseSectionPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.baseSectionPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean getSectionOutlinesVisible() {
        return this.sectionOutlinesVisible;
    }

    public void setSectionOutlinesVisible(boolean visible) {
        this.sectionOutlinesVisible = visible;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getSectionOutlinePaint() {
        return this.sectionOutlinePaint;
    }

    public void setSectionOutlinePaint(Paint paint) {
        this.sectionOutlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getSectionOutlinePaint(int section) {
        if (this.sectionOutlinePaint != null) {
            return this.sectionOutlinePaint;
        }
        Paint result = this.sectionOutlinePaintList.getPaint(section);
        if (result == null) {
            result = this.baseSectionOutlinePaint;
        }
        return result;
    }

    public void setSectionOutlinePaint(int section, Paint paint) {
        this.sectionOutlinePaintList.setPaint(section, paint);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getBaseSectionOutlinePaint() {
        return this.baseSectionOutlinePaint;
    }

    public void setBaseSectionOutlinePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.baseSectionOutlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Stroke getSectionOutlineStroke() {
        return this.sectionOutlineStroke;
    }

    public void setSectionOutlineStroke(Stroke stroke) {
        this.sectionOutlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Stroke getSectionOutlineStroke(int section) {
        if (this.sectionOutlineStroke != null) {
            return this.sectionOutlineStroke;
        }
        Stroke result = this.sectionOutlineStrokeList.getStroke(section);
        if (result == null) {
            result = this.baseSectionOutlineStroke;
        }
        return result;
    }

    public void setSectionOutlineStroke(int section, Stroke stroke) {
        this.sectionOutlineStrokeList.setStroke(section, stroke);
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Stroke getBaseSectionOutlineStroke() {
        return this.baseSectionOutlineStroke;
    }

    public void setBaseSectionOutlineStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.baseSectionOutlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getShadowPaint() {
        return this.shadowPaint;
    }

    public void setShadowPaint(Paint paint) {
        this.shadowPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getShadowXOffset() {
        return this.shadowXOffset;
    }

    public void setShadowXOffset(double offset) {
        this.shadowXOffset = offset;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getShadowYOffset() {
        return this.shadowYOffset;
    }

    public void setShadowYOffset(double offset) {
        this.shadowYOffset = offset;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getExplodePercent(int section) {
        Number percent;
        double result = 0.0;
        if (this.explodePercentages != null && (percent = (Number)this.explodePercentages.get(section)) != null) {
            result = percent.doubleValue();
        }
        return result;
    }

    public void setExplodePercent(int section, double percent) {
        if (this.explodePercentages == null) {
            this.explodePercentages = new ObjectList();
        }
        this.explodePercentages.set(section, new Double(percent));
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getMaximumExplodePercent() {
        double result = 0.0;
        for (int i = 0; i < this.explodePercentages.size(); ++i) {
            Number explode = (Number)this.explodePercentages.get(i);
            if (explode == null) continue;
            result = Math.max(result, explode.doubleValue());
        }
        return result;
    }

    public PieSectionLabelGenerator getLabelGenerator() {
        return this.labelGenerator;
    }

    public void setLabelGenerator(PieSectionLabelGenerator generator) {
        this.labelGenerator = generator;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getLabelGap() {
        return this.labelGap;
    }

    public void setLabelGap(double gap) {
        this.labelGap = gap;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getMaximumLabelWidth() {
        return this.maximumLabelWidth;
    }

    public void setMaximumLabelWidth(double width) {
        this.maximumLabelWidth = width;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public boolean getLabelLinksVisible() {
        return this.labelLinksVisible;
    }

    public void setLabelLinksVisible(boolean visible) {
        this.labelLinksVisible = visible;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getLabelLinkMargin() {
        return this.labelLinkMargin;
    }

    public void setLabelLinkMargin(double margin) {
        this.labelLinkMargin = margin;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getLabelLinkPaint() {
        return this.labelLinkPaint;
    }

    public void setLabelLinkPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.labelLinkPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Stroke getLabelLinkStroke() {
        return this.labelLinkStroke;
    }

    public void setLabelLinkStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Null 'stroke' argument.");
        }
        this.labelLinkStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Font getLabelFont() {
        return this.labelFont;
    }

    public void setLabelFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("Null 'font' argument.");
        }
        this.labelFont = font;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getLabelPaint() {
        return this.labelPaint;
    }

    public void setLabelPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.labelPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getLabelBackgroundPaint() {
        return this.labelBackgroundPaint;
    }

    public void setLabelBackgroundPaint(Paint paint) {
        this.labelBackgroundPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getLabelOutlinePaint() {
        return this.labelOutlinePaint;
    }

    public void setLabelOutlinePaint(Paint paint) {
        this.labelOutlinePaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Stroke getLabelOutlineStroke() {
        return this.labelOutlineStroke;
    }

    public void setLabelOutlineStroke(Stroke stroke) {
        this.labelOutlineStroke = stroke;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public Paint getLabelShadowPaint() {
        return this.labelShadowPaint;
    }

    public void setLabelShadowPaint(Paint paint) {
        this.labelShadowPaint = paint;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public PieToolTipGenerator getToolTipGenerator() {
        return this.toolTipGenerator;
    }

    public void setToolTipGenerator(PieToolTipGenerator generator) {
        this.toolTipGenerator = generator;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public PieURLGenerator getURLGenerator() {
        return this.urlGenerator;
    }

    public void setURLGenerator(PieURLGenerator generator) {
        this.urlGenerator = generator;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public double getMinimumArcAngleToDraw() {
        return this.minimumArcAngleToDraw;
    }

    public void setMinimumArcAngleToDraw(double angle) {
        this.minimumArcAngleToDraw = angle;
    }

    public Shape getLegendItemShape() {
        return this.legendItemShape;
    }

    public void setLegendItemShape(Shape shape) {
        if (shape == null) {
            throw new IllegalArgumentException("Null 'shape' argument.");
        }
        this.legendItemShape = shape;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public PieSectionLabelGenerator getLegendLabelToolTipGenerator() {
        return this.legendLabelToolTipGenerator;
    }

    public void setLegendLabelToolTipGenerator(PieSectionLabelGenerator generator) {
        this.legendLabelToolTipGenerator = generator;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public PieSectionLabelGenerator getLegendLabelGenerator() {
        return this.legendLabelGenerator;
    }

    public void setLegendLabelGenerator(PieSectionLabelGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("Null 'generator' argument.");
        }
        this.legendLabelGenerator = generator;
        this.notifyListeners(new PlotChangeEvent(this));
    }

    public PiePlotState initialise(Graphics2D g2, Rectangle2D plotArea, PiePlot plot, Integer index, PlotRenderingInfo info) {
        PiePlotState state = new PiePlotState(info);
        state.setPassesRequired(2);
        state.setTotal(DatasetUtilities.calculatePieDatasetTotal(plot.getDataset()));
        state.setLatestAngle(plot.getStartAngle());
        return state;
    }

    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
        RectangleInsets insets = this.getInsets();
        insets.trim(area);
        if (info != null) {
            info.setPlotArea(area);
            info.setDataArea(area);
        }
        this.drawBackground(g2, area);
        this.drawOutline(g2, area);
        Shape savedClip = g2.getClip();
        g2.clip(area);
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(3, this.getForegroundAlpha()));
        if (!DatasetUtilities.isEmptyOrNull(this.dataset)) {
            this.drawPie(g2, area, info);
        } else {
            this.drawNoDataMessage(g2, area);
        }
        g2.setClip(savedClip);
        g2.setComposite(originalComposite);
        this.drawOutline(g2, area);
    }

    protected void drawPie(Graphics2D g2, Rectangle2D plotArea, PlotRenderingInfo info) {
        PiePlotState state = this.initialise(g2, plotArea, this, null, info);
        double labelWidth = 0.0;
        if (this.labelGenerator != null) {
            labelWidth = this.labelGap + this.maximumLabelWidth + this.labelLinkMargin;
        }
        double gapHorizontal = plotArea.getWidth() * (this.interiorGap + labelWidth);
        double gapVertical = plotArea.getHeight() * this.interiorGap;
        double linkX = plotArea.getX() + gapHorizontal / 2.0;
        double linkY = plotArea.getY() + gapVertical / 2.0;
        double linkW = plotArea.getWidth() - gapHorizontal;
        double linkH = plotArea.getHeight() - gapVertical;
        if (this.circular) {
            double min = Math.min(linkW, linkH) / 2.0;
            linkX = (linkX + linkX + linkW) / 2.0 - min;
            linkY = (linkY + linkY + linkH) / 2.0 - min;
            linkW = 2.0 * min;
            linkH = 2.0 * min;
        }
        Rectangle2D.Double linkArea = new Rectangle2D.Double(linkX, linkY, linkW, linkH);
        state.setLinkArea(linkArea);
        double hh = linkArea.getWidth() * this.labelLinkMargin;
        double vv = linkArea.getHeight() * this.labelLinkMargin;
        Rectangle2D.Double explodeArea = new Rectangle2D.Double(linkX + hh / 2.0, linkY + vv / 2.0, linkW - hh, linkH - vv);
        state.setExplodedPieArea(explodeArea);
        double maximumExplodePercent = this.getMaximumExplodePercent();
        double percent = maximumExplodePercent / (1.0 + maximumExplodePercent);
        double h1 = explodeArea.getWidth() * percent;
        double v1 = explodeArea.getHeight() * percent;
        Rectangle2D.Double pieArea = new Rectangle2D.Double(explodeArea.getX() + h1 / 2.0, explodeArea.getY() + v1 / 2.0, explodeArea.getWidth() - h1, explodeArea.getHeight() - v1);
        state.setPieArea(pieArea);
        state.setPieCenterX(pieArea.getCenterX());
        state.setPieCenterY(pieArea.getCenterY());
        state.setPieWRadius(pieArea.getWidth() / 2.0);
        state.setPieHRadius(pieArea.getHeight() / 2.0);
        if (this.dataset != null && this.dataset.getKeys().size() > 0) {
            List keys = this.dataset.getKeys();
            double totalValue = DatasetUtilities.calculatePieDatasetTotal(this.dataset);
            int passesRequired = state.getPassesRequired();
            for (int pass = 0; pass < passesRequired; ++pass) {
                double runningTotal = 0.0;
                for (int section = 0; section < keys.size(); ++section) {
                    double value;
                    Number n = this.dataset.getValue(section);
                    if (n == null || (value = n.doubleValue()) <= 0.0) continue;
                    runningTotal += value;
                    this.drawItem(g2, section, explodeArea, state, pass);
                }
            }
            this.drawLabels(g2, keys, totalValue, plotArea, linkArea, state);
        } else {
            this.drawNoDataMessage(g2, plotArea);
        }
    }

    protected void drawItem(Graphics2D g2, int section, Rectangle2D dataArea, PiePlotState state, int currentPass) {
        Number n = this.dataset.getValue(section);
        if (n == null) {
            return;
        }
        double value = n.doubleValue();
        double angle1 = 0.0;
        double angle2 = 0.0;
        if (this.direction == Rotation.CLOCKWISE) {
            angle1 = state.getLatestAngle();
            angle2 = angle1 - value / state.getTotal() * 360.0;
        } else if (this.direction == Rotation.ANTICLOCKWISE) {
            angle1 = state.getLatestAngle();
            angle2 = angle1 + value / state.getTotal() * 360.0;
        } else {
            throw new IllegalStateException("Rotation type not recognised.");
        }
        double angle = angle2 - angle1;
        if (Math.abs(angle) > this.getMinimumArcAngleToDraw()) {
            double ep = 0.0;
            double mep = this.getMaximumExplodePercent();
            if (mep > 0.0) {
                ep = this.getExplodePercent(section) / mep;
            }
            Rectangle2D arcBounds = this.getArcBounds(state.getPieArea(), state.getExplodedPieArea(), angle1, angle, ep);
            Arc2D.Double arc = new Arc2D.Double(arcBounds, angle1, angle, 2);
            if (currentPass == 0) {
                if (this.shadowPaint != null) {
                    Shape shadowArc = ShapeUtilities.createTranslatedShape(arc, (float)this.shadowXOffset, (float)this.shadowYOffset);
                    g2.setPaint(this.shadowPaint);
                    g2.fill(shadowArc);
                }
            } else if (currentPass == 1) {
                EntityCollection entities;
                Paint paint = this.getSectionPaint(section);
                g2.setPaint(paint);
                g2.fill(arc);
                Paint outlinePaint = this.getSectionOutlinePaint(section);
                Stroke outlineStroke = this.getSectionOutlineStroke(section);
                if (this.sectionOutlinesVisible) {
                    g2.setPaint(outlinePaint);
                    g2.setStroke(outlineStroke);
                    g2.draw(arc);
                }
                if (state.getInfo() != null && (entities = state.getEntityCollection()) != null) {
                    Comparable key = this.dataset.getKey(section);
                    String tip = null;
                    if (this.toolTipGenerator != null) {
                        tip = this.toolTipGenerator.generateToolTip(this.dataset, key);
                    }
                    String url = null;
                    if (this.urlGenerator != null) {
                        url = this.urlGenerator.generateURL(this.dataset, key, this.pieIndex);
                    }
                    PieSectionEntity entity = new PieSectionEntity(arc, this.dataset, this.pieIndex, section, key, tip, url);
                    entities.add(entity);
                }
            }
        }
        state.setLatestAngle(angle2);
    }

    protected void drawLabels(Graphics2D g2, List keys, double totalValue, Rectangle2D plotArea, Rectangle2D linkArea, PiePlotState state) {
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(3, 1.0f));
        DefaultKeyedValues leftKeys = new DefaultKeyedValues();
        DefaultKeyedValues rightKeys = new DefaultKeyedValues();
        double runningTotal1 = 0.0;
        Iterator iterator1 = keys.iterator();
        while (iterator1.hasNext()) {
            Comparable key = (Comparable)iterator1.next();
            boolean include = true;
            double v = 0.0;
            Number n = this.dataset.getValue(key);
            if (n == null) {
                include = !this.ignoreNullValues;
            } else {
                v = n.doubleValue();
                boolean bl = this.ignoreZeroValues ? v > 0.0 : (include = v >= 0.0);
            }
            if (!include) continue;
            double mid = this.startAngle + this.direction.getFactor() * (((runningTotal1 += v) - v / 2.0) * 360.0) / totalValue;
            if (Math.cos(Math.toRadians(mid)) < 0.0) {
                leftKeys.addValue(key, new Double(mid));
                continue;
            }
            rightKeys.addValue(key, new Double(mid));
        }
        g2.setFont(this.getLabelFont());
        float maxLabelWidth = (float)(this.getMaximumLabelWidth() * plotArea.getWidth());
        if (this.labelGenerator != null) {
            this.drawLeftLabels(leftKeys, g2, plotArea, linkArea, maxLabelWidth, state);
            this.drawRightLabels(rightKeys, g2, plotArea, linkArea, maxLabelWidth, state);
        }
        g2.setComposite(originalComposite);
    }

    protected void drawLeftLabels(KeyedValues leftKeys, Graphics2D g2, Rectangle2D plotArea, Rectangle2D linkArea, float maxLabelWidth, PiePlotState state) {
        int i;
        PieLabelDistributor distributor1 = new PieLabelDistributor(leftKeys.getItemCount());
        double lGap = plotArea.getWidth() * this.labelGap;
        double verticalLinkRadius = state.getLinkArea().getHeight() / 2.0;
        for (i = 0; i < leftKeys.getItemCount(); ++i) {
            String label = this.labelGenerator.generateSectionLabel(this.dataset, leftKeys.getKey(i));
            if (label == null) continue;
            TextBlock block = TextUtilities.createTextBlock(label, this.labelFont, this.labelPaint, maxLabelWidth, new G2TextMeasurer(g2));
            TextBox labelBox = new TextBox(block);
            labelBox.setBackgroundPaint(this.labelBackgroundPaint);
            labelBox.setOutlinePaint(this.labelOutlinePaint);
            labelBox.setOutlineStroke(this.labelOutlineStroke);
            labelBox.setShadowPaint(this.labelShadowPaint);
            double theta = Math.toRadians(leftKeys.getValue(i).doubleValue());
            double baseY = state.getPieCenterY() - Math.sin(theta) * verticalLinkRadius;
            double hh = labelBox.getHeight(g2);
            distributor1.addPieLabelRecord(new PieLabelRecord(leftKeys.getKey(i), theta, baseY, labelBox, hh, lGap / 2.0 + lGap / 2.0 * (- Math.cos(theta)), 0.9 + this.getExplodePercent(this.dataset.getIndex(leftKeys.getKey(i)))));
        }
        distributor1.distributeLabels(plotArea.getMinY(), plotArea.getHeight());
        for (i = 0; i < distributor1.getItemCount(); ++i) {
            this.drawLeftLabel(g2, state, distributor1.getPieLabelRecord(i));
        }
    }

    protected void drawRightLabels(KeyedValues keys, Graphics2D g2, Rectangle2D plotArea, Rectangle2D linkArea, float maxLabelWidth, PiePlotState state) {
        int i;
        PieLabelDistributor distributor2 = new PieLabelDistributor(keys.getItemCount());
        double lGap = plotArea.getWidth() * this.labelGap;
        double verticalLinkRadius = state.getLinkArea().getHeight() / 2.0;
        for (i = 0; i < keys.getItemCount(); ++i) {
            String label = this.labelGenerator.generateSectionLabel(this.dataset, keys.getKey(i));
            if (label == null) continue;
            TextBlock block = TextUtilities.createTextBlock(label, this.labelFont, this.labelPaint, maxLabelWidth, new G2TextMeasurer(g2));
            TextBox labelBox = new TextBox(block);
            labelBox.setBackgroundPaint(this.labelBackgroundPaint);
            labelBox.setOutlinePaint(this.labelOutlinePaint);
            labelBox.setOutlineStroke(this.labelOutlineStroke);
            labelBox.setShadowPaint(this.labelShadowPaint);
            double theta = Math.toRadians(keys.getValue(i).doubleValue());
            double baseY = state.getPieCenterY() - Math.sin(theta) * verticalLinkRadius;
            double hh = labelBox.getHeight(g2);
            distributor2.addPieLabelRecord(new PieLabelRecord(keys.getKey(i), theta, baseY, labelBox, hh, lGap / 2.0 + lGap / 2.0 * Math.cos(theta), 0.9 + this.getExplodePercent(this.dataset.getIndex(keys.getKey(i)))));
        }
        distributor2.distributeLabels(linkArea.getMinY(), linkArea.getHeight());
        for (i = 0; i < distributor2.getItemCount(); ++i) {
            this.drawRightLabel(g2, state, distributor2.getPieLabelRecord(i));
        }
    }

    public LegendItemCollection getLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        if (this.dataset == null) {
            return result;
        }
        List keys = this.dataset.getKeys();
        int section = 0;
        Shape shape = this.getLegendItemShape();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Comparable key = (Comparable)iterator.next();
            Number n = this.dataset.getValue(key);
            boolean include = true;
            if (n == null) {
                include = !this.ignoreNullValues;
            } else {
                double v = n.doubleValue();
                if (v == 0.0) {
                    include = !this.ignoreZeroValues;
                } else {
                    boolean bl = include = v > 0.0;
                }
            }
            if (include) {
                String label;
                String description = label = this.legendLabelGenerator.generateSectionLabel(this.dataset, key);
                String toolTipText = null;
                if (this.legendLabelToolTipGenerator != null) {
                    toolTipText = this.legendLabelToolTipGenerator.generateSectionLabel(this.dataset, key);
                }
                String urlText = null;
                Paint paint = this.getSectionPaint(section);
                Paint outlinePaint = this.getSectionOutlinePaint(section);
                Stroke outlineStroke = this.getSectionOutlineStroke(section);
                LegendItem item = new LegendItem(label, description, toolTipText, urlText, true, shape, true, paint, true, outlinePaint, outlineStroke, false, (Shape)new Line2D.Float(), (Stroke)new BasicStroke(), (Paint)Color.black);
                result.add(item);
                ++section;
                continue;
            }
            ++section;
        }
        return result;
    }

    public String getPlotType() {
        return localizationResources.getString("Pie_Plot");
    }

    public void zoom(double percent) {
    }

    protected Rectangle2D getArcBounds(Rectangle2D unexploded, Rectangle2D exploded, double angle, double extent, double explodePercent) {
        if (explodePercent == 0.0) {
            return unexploded;
        }
        Arc2D.Double arc1 = new Arc2D.Double(unexploded, angle, extent / 2.0, 0);
        Point2D point1 = arc1.getEndPoint();
        Arc2D.Double arc2 = new Arc2D.Double(exploded, angle, extent / 2.0, 0);
        Point2D point2 = arc2.getEndPoint();
        double deltaX = (point1.getX() - point2.getX()) * explodePercent;
        double deltaY = (point1.getY() - point2.getY()) * explodePercent;
        return new Rectangle2D.Double(unexploded.getX() - deltaX, unexploded.getY() - deltaY, unexploded.getWidth(), unexploded.getHeight());
    }

    protected void drawLeftLabel(Graphics2D g2, PiePlotState state, PieLabelRecord record) {
        double anchorX = state.getLinkArea().getMinX();
        double targetX = anchorX - record.getGap();
        double targetY = record.getAllocatedY();
        if (this.labelLinksVisible) {
            double elbowY;
            double theta = record.getAngle();
            double linkX = state.getPieCenterX() + Math.cos(theta) * state.getPieWRadius() * record.getLinkPercent();
            double linkY = state.getPieCenterY() - Math.sin(theta) * state.getPieHRadius() * record.getLinkPercent();
            double elbowX = state.getPieCenterX() + Math.cos(theta) * state.getLinkArea().getWidth() / 2.0;
            double anchorY = elbowY = state.getPieCenterY() - Math.sin(theta) * state.getLinkArea().getHeight() / 2.0;
            g2.setPaint(this.labelLinkPaint);
            g2.setStroke(this.labelLinkStroke);
            g2.draw(new Line2D.Double(linkX, linkY, elbowX, elbowY));
            g2.draw(new Line2D.Double(anchorX, anchorY, elbowX, elbowY));
            g2.draw(new Line2D.Double(anchorX, anchorY, targetX, targetY));
        }
        TextBox tb = record.getLabel();
        tb.draw(g2, (float)targetX, (float)targetY, RectangleAnchor.RIGHT);
    }

    protected void drawRightLabel(Graphics2D g2, PiePlotState state, PieLabelRecord record) {
        double anchorX = state.getLinkArea().getMaxX();
        double targetX = anchorX + record.getGap();
        double targetY = record.getAllocatedY();
        if (this.labelLinksVisible) {
            double elbowY;
            double theta = record.getAngle();
            double linkX = state.getPieCenterX() + Math.cos(theta) * state.getPieWRadius() * record.getLinkPercent();
            double linkY = state.getPieCenterY() - Math.sin(theta) * state.getPieHRadius() * record.getLinkPercent();
            double elbowX = state.getPieCenterX() + Math.cos(theta) * state.getLinkArea().getWidth() / 2.0;
            double anchorY = elbowY = state.getPieCenterY() - Math.sin(theta) * state.getLinkArea().getHeight() / 2.0;
            g2.setPaint(this.labelLinkPaint);
            g2.setStroke(this.labelLinkStroke);
            g2.draw(new Line2D.Double(linkX, linkY, elbowX, elbowY));
            g2.draw(new Line2D.Double(anchorX, anchorY, elbowX, elbowY));
            g2.draw(new Line2D.Double(anchorX, anchorY, targetX, targetY));
        }
        TextBox tb = record.getLabel();
        tb.draw(g2, (float)targetX, (float)targetY, RectangleAnchor.LEFT);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PiePlot)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        PiePlot that = (PiePlot)obj;
        if (this.pieIndex != that.pieIndex) {
            return false;
        }
        if (this.interiorGap != that.interiorGap) {
            return false;
        }
        if (this.circular != that.circular) {
            return false;
        }
        if (this.startAngle != that.startAngle) {
            return false;
        }
        if (this.direction != that.direction) {
            return false;
        }
        if (this.ignoreZeroValues != that.ignoreZeroValues) {
            return false;
        }
        if (this.ignoreNullValues != that.ignoreNullValues) {
            return false;
        }
        if (!PaintUtilities.equal(this.sectionPaint, that.sectionPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.sectionPaintList, that.sectionPaintList)) {
            return false;
        }
        if (!PaintUtilities.equal(this.baseSectionPaint, that.baseSectionPaint)) {
            return false;
        }
        if (this.sectionOutlinesVisible != that.sectionOutlinesVisible) {
            return false;
        }
        if (!PaintUtilities.equal(this.sectionOutlinePaint, that.sectionOutlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.sectionOutlinePaintList, that.sectionOutlinePaintList)) {
            return false;
        }
        if (!PaintUtilities.equal(this.baseSectionOutlinePaint, that.baseSectionOutlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.sectionOutlineStroke, that.sectionOutlineStroke)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.sectionOutlineStrokeList, that.sectionOutlineStrokeList)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.baseSectionOutlineStroke, that.baseSectionOutlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.shadowPaint, that.shadowPaint)) {
            return false;
        }
        if (this.shadowXOffset != that.shadowXOffset) {
            return false;
        }
        if (this.shadowYOffset != that.shadowYOffset) {
            return false;
        }
        if (!ObjectUtilities.equal(this.explodePercentages, that.explodePercentages)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.labelGenerator, that.labelGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.labelFont, that.labelFont)) {
            return false;
        }
        if (!PaintUtilities.equal(this.labelPaint, that.labelPaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.labelBackgroundPaint, that.labelBackgroundPaint)) {
            return false;
        }
        if (!PaintUtilities.equal(this.labelOutlinePaint, that.labelOutlinePaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.labelOutlineStroke, that.labelOutlineStroke)) {
            return false;
        }
        if (!PaintUtilities.equal(this.labelShadowPaint, that.labelShadowPaint)) {
            return false;
        }
        if (this.maximumLabelWidth != that.maximumLabelWidth) {
            return false;
        }
        if (this.labelGap != that.labelGap) {
            return false;
        }
        if (this.labelLinkMargin != that.labelLinkMargin) {
            return false;
        }
        if (this.labelLinksVisible != that.labelLinksVisible) {
            return false;
        }
        if (!PaintUtilities.equal(this.labelLinkPaint, that.labelLinkPaint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.labelLinkStroke, that.labelLinkStroke)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.toolTipGenerator, that.toolTipGenerator)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.urlGenerator, that.urlGenerator)) {
            return false;
        }
        if (this.minimumArcAngleToDraw != that.minimumArcAngleToDraw) {
            return false;
        }
        if (!ShapeUtilities.equal(this.legendItemShape, that.legendItemShape)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        PiePlot clone = (PiePlot)super.clone();
        if (clone.dataset != null) {
            clone.dataset.addChangeListener(clone);
        }
        return clone;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.sectionPaint, stream);
        SerialUtilities.writePaint(this.baseSectionPaint, stream);
        SerialUtilities.writePaint(this.sectionOutlinePaint, stream);
        SerialUtilities.writePaint(this.baseSectionOutlinePaint, stream);
        SerialUtilities.writeStroke(this.sectionOutlineStroke, stream);
        SerialUtilities.writeStroke(this.baseSectionOutlineStroke, stream);
        SerialUtilities.writePaint(this.shadowPaint, stream);
        SerialUtilities.writePaint(this.labelPaint, stream);
        SerialUtilities.writePaint(this.labelBackgroundPaint, stream);
        SerialUtilities.writePaint(this.labelOutlinePaint, stream);
        SerialUtilities.writeStroke(this.labelOutlineStroke, stream);
        SerialUtilities.writePaint(this.labelShadowPaint, stream);
        SerialUtilities.writePaint(this.labelLinkPaint, stream);
        SerialUtilities.writeStroke(this.labelLinkStroke, stream);
        SerialUtilities.writeShape(this.legendItemShape, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.sectionPaint = SerialUtilities.readPaint(stream);
        this.baseSectionPaint = SerialUtilities.readPaint(stream);
        this.sectionOutlinePaint = SerialUtilities.readPaint(stream);
        this.baseSectionOutlinePaint = SerialUtilities.readPaint(stream);
        this.sectionOutlineStroke = SerialUtilities.readStroke(stream);
        this.baseSectionOutlineStroke = SerialUtilities.readStroke(stream);
        this.shadowPaint = SerialUtilities.readPaint(stream);
        this.labelPaint = SerialUtilities.readPaint(stream);
        this.labelBackgroundPaint = SerialUtilities.readPaint(stream);
        this.labelOutlinePaint = SerialUtilities.readPaint(stream);
        this.labelOutlineStroke = SerialUtilities.readStroke(stream);
        this.labelShadowPaint = SerialUtilities.readPaint(stream);
        this.labelLinkPaint = SerialUtilities.readPaint(stream);
        this.labelLinkStroke = SerialUtilities.readStroke(stream);
        this.legendItemShape = SerialUtilities.readShape(stream);
    }
}

