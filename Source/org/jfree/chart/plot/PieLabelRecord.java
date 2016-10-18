/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.plot;

import org.jfree.text.TextBox;

public class PieLabelRecord
implements Comparable {
    private Comparable key;
    private double angle;
    private double baseY;
    private double allocatedY;
    private TextBox label;
    private double labelHeight;
    private double gap;
    private double linkPercent;

    public PieLabelRecord(Comparable key, double angle, double baseY, TextBox label, double labelHeight, double gap, double linkPercent) {
        this.key = key;
        this.angle = angle;
        this.baseY = baseY;
        this.allocatedY = baseY;
        this.label = label;
        this.labelHeight = labelHeight;
        this.gap = gap;
        this.linkPercent = linkPercent;
    }

    public double getBaseY() {
        return this.baseY;
    }

    public void setBaseY(double base) {
        this.baseY = base;
    }

    public double getLowerY() {
        return this.allocatedY - this.labelHeight / 2.0;
    }

    public double getUpperY() {
        return this.allocatedY + this.labelHeight / 2.0;
    }

    public double getAngle() {
        return this.angle;
    }

    public Comparable getKey() {
        return this.key;
    }

    public TextBox getLabel() {
        return this.label;
    }

    public double getLabelHeight() {
        return this.labelHeight;
    }

    public double getAllocatedY() {
        return this.allocatedY;
    }

    public void setAllocatedY(double y) {
        this.allocatedY = y;
    }

    public double getGap() {
        return this.gap;
    }

    public double getLinkPercent() {
        return this.linkPercent;
    }

    public int compareTo(Object obj) {
        int result = 0;
        if (obj instanceof PieLabelRecord) {
            PieLabelRecord plr = (PieLabelRecord)obj;
            if (this.baseY < plr.baseY) {
                result = -1;
            } else if (this.baseY > plr.baseY) {
                result = 1;
            }
        }
        return result;
    }

    public String toString() {
        return "" + this.baseY + ", " + this.key.toString();
    }
}

