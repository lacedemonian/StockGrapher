/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.axis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.text.TextBlock;
import org.jfree.text.TextFragment;
import org.jfree.text.TextLine;
import org.jfree.ui.RectangleEdge;

public class ExtendedCategoryAxis
extends CategoryAxis {
    private Map sublabels = new HashMap();
    private Font sublabelFont = new Font("SansSerif", 0, 10);
    private Paint sublabelPaint = Color.black;

    public ExtendedCategoryAxis(String label) {
        super(label);
    }

    public Font getSubLabelFont() {
        return this.sublabelFont;
    }

    public void setSubLabelFont(Font font) {
        this.sublabelFont = font;
    }

    public Paint getSubLabelPaint() {
        return this.sublabelPaint;
    }

    public void setSubLabelPaint(Paint paint) {
        this.sublabelPaint = paint;
    }

    public void addSubLabel(Comparable category, String label) {
        this.sublabels.put(category, label);
    }

    protected TextBlock createLabel(Comparable category, float width, RectangleEdge edge, Graphics2D g2) {
        TextBlock label = super.createLabel(category, width, edge, g2);
        String s = (String)this.sublabels.get(category);
        if (s != null) {
            TextLine line;
            if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
                TextLine line2 = new TextLine(s, this.sublabelFont, this.sublabelPaint);
                label.addLine(line2);
            } else if ((edge == RectangleEdge.LEFT || edge == RectangleEdge.RIGHT) && (line = label.getLastLine()) != null) {
                line.addFragment(new TextFragment("  " + s, this.sublabelFont, this.sublabelPaint));
            }
        }
        return label;
    }
}

