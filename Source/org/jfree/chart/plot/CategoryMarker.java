/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.Serializable;
import org.jfree.chart.plot.Marker;
import org.jfree.ui.LengthAdjustmentType;

public class CategoryMarker
extends Marker
implements Cloneable,
Serializable {
    private Comparable key;
    private boolean drawAsLine = false;

    public CategoryMarker(Comparable key) {
        this(key, Color.gray, new BasicStroke(1.0f));
    }

    public CategoryMarker(Comparable key, Paint paint, Stroke stroke) {
        this(key, paint, stroke, paint, stroke, 0.5f);
    }

    public CategoryMarker(Comparable key, Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
        super(paint, stroke, outlinePaint, outlineStroke, alpha);
        this.key = key;
        this.setLabelOffsetType(LengthAdjustmentType.EXPAND);
    }

    public Comparable getKey() {
        return this.key;
    }

    public boolean getDrawAsLine() {
        return this.drawAsLine;
    }

    public void setDrawAsLine(boolean drawAsLine) {
        this.drawAsLine = drawAsLine;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CategoryMarker)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        CategoryMarker that = (CategoryMarker)obj;
        if (!this.key.equals(that.key)) {
            return false;
        }
        if (this.drawAsLine != that.drawAsLine) {
            return false;
        }
        return true;
    }
}

