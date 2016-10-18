/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.block;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.block.AbstractBlock;
import org.jfree.chart.block.Block;
import org.jfree.chart.block.BlockResult;
import org.jfree.chart.block.EntityBlockParams;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.text.TextUtilities;
import org.jfree.ui.Size2D;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

public class LabelBlock
extends AbstractBlock
implements Block,
PublicCloneable {
    private String text;
    private TextBlock label;
    private Font font;
    private String toolTipText;
    private String urlText;
    public static final Paint DEFAULT_PAINT = Color.black;
    private Paint paint;

    public LabelBlock(String label) {
        this(label, new Font("SansSerif", 0, 10), DEFAULT_PAINT);
    }

    public LabelBlock(String text, Font font) {
        this(text, font, DEFAULT_PAINT);
    }

    public LabelBlock(String text, Font font, Paint paint) {
        this.text = text;
        this.paint = paint;
        this.label = TextUtilities.createTextBlock(text, font, this.paint);
        this.font = font;
        this.toolTipText = null;
        this.urlText = null;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("Null 'font' argument.");
        }
        this.font = font;
        this.label = TextUtilities.createTextBlock(this.text, font, this.paint);
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.paint = paint;
        this.label = TextUtilities.createTextBlock(this.text, this.font, this.paint);
    }

    public String getToolTipText() {
        return this.toolTipText;
    }

    public void setToolTipText(String text) {
        this.toolTipText = text;
    }

    public String getURLText() {
        return this.urlText;
    }

    public void setURLText(String text) {
        this.urlText = text;
    }

    public Size2D arrange(Graphics2D g2, RectangleConstraint constraint) {
        RectangleConstraint contentConstraint = this.toContentConstraint(constraint);
        g2.setFont(this.font);
        Size2D s = this.label.calculateDimensions(g2);
        return new Size2D(this.calculateTotalWidth(s.getWidth()), this.calculateTotalHeight(s.getHeight()));
    }

    public void draw(Graphics2D g2, Rectangle2D area) {
        this.draw(g2, area, null);
    }

    public Object draw(Graphics2D g2, Rectangle2D area, Object params) {
        area = this.trimMargin(area);
        this.drawBorder(g2, area);
        area = this.trimBorder(area);
        area = this.trimPadding(area);
        EntityBlockParams ebp = null;
        StandardEntityCollection sec = null;
        Shape entityArea = null;
        if (params instanceof EntityBlockParams && (ebp = (EntityBlockParams)params).getGenerateEntities()) {
            sec = new StandardEntityCollection();
            entityArea = g2.getTransform().createTransformedShape(area);
        }
        g2.setPaint(this.paint);
        g2.setFont(this.font);
        this.label.draw(g2, (float)area.getX(), (float)area.getY(), TextBlockAnchor.TOP_LEFT);
        BlockResult result = null;
        if (ebp != null && sec != null && (this.toolTipText != null || this.urlText != null)) {
            ChartEntity entity = new ChartEntity(entityArea, this.toolTipText, this.urlText);
            sec.add(entity);
            result = new BlockResult();
            result.setEntityCollection(sec);
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LabelBlock)) {
            return false;
        }
        LabelBlock that = (LabelBlock)obj;
        if (!this.text.equals(that.text)) {
            return false;
        }
        if (!this.font.equals(that.font)) {
            return false;
        }
        if (!PaintUtilities.equal(this.paint, that.paint)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.toolTipText, that.toolTipText)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.urlText, that.urlText)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

