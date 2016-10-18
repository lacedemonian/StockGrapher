/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.plot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.plot.PieLabelRecord;

public class PieLabelDistributor {
    private List labels;
    private double minGap = 4.0;

    public PieLabelDistributor(int labelCount) {
        this.labels = new ArrayList(labelCount);
    }

    public PieLabelRecord getPieLabelRecord(int index) {
        return (PieLabelRecord)this.labels.get(index);
    }

    public void addPieLabelRecord(PieLabelRecord record) {
        this.labels.add(record);
    }

    public int getItemCount() {
        return this.labels.size();
    }

    public void distributeLabels(double minY, double height) {
        this.sort();
        if (this.isOverlap()) {
            this.adjustInwards();
        }
        if (this.isOverlap()) {
            this.adjustDownwards(minY, height);
        }
        if (this.isOverlap()) {
            this.adjustUpwards(minY, height);
        }
        if (this.isOverlap()) {
            this.spreadEvenly(minY, height);
        }
    }

    private boolean isOverlap() {
        double y = 0.0;
        for (int i = 0; i < this.labels.size(); ++i) {
            PieLabelRecord plr = this.getPieLabelRecord(i);
            if (y > plr.getLowerY()) {
                return true;
            }
            y = plr.getUpperY();
        }
        return false;
    }

    protected void adjustInwards() {
        int lower = 0;
        for (int upper = this.labels.size() - 1; upper > lower; ++lower, --upper) {
            double adjust;
            if (lower < upper - 1) {
                PieLabelRecord r0 = this.getPieLabelRecord(lower);
                PieLabelRecord r1 = this.getPieLabelRecord(lower + 1);
                if (r1.getLowerY() < r0.getUpperY()) {
                    adjust = r0.getUpperY() - r1.getLowerY() + this.minGap;
                    r1.setAllocatedY(r1.getAllocatedY() + adjust);
                }
            }
            PieLabelRecord r2 = this.getPieLabelRecord(upper - 1);
            PieLabelRecord r3 = this.getPieLabelRecord(upper);
            if (r2.getUpperY() <= r3.getLowerY()) continue;
            adjust = r2.getUpperY() - r3.getLowerY() + this.minGap;
            r2.setAllocatedY(r2.getAllocatedY() - adjust);
        }
    }

    protected void adjustDownwards(double minY, double height) {
        for (int i = 0; i < this.labels.size() - 1; ++i) {
            PieLabelRecord record0 = this.getPieLabelRecord(i);
            PieLabelRecord record1 = this.getPieLabelRecord(i + 1);
            if (record1.getLowerY() >= record0.getUpperY()) continue;
            record1.setAllocatedY(Math.min(minY + height, record0.getUpperY() + this.minGap + record1.getLabelHeight() / 2.0));
        }
    }

    protected void adjustUpwards(double minY, double height) {
        for (int i = this.labels.size() - 1; i > 0; --i) {
            PieLabelRecord record0 = this.getPieLabelRecord(i);
            PieLabelRecord record1 = this.getPieLabelRecord(i - 1);
            if (record1.getUpperY() <= record0.getLowerY()) continue;
            record1.setAllocatedY(Math.max(minY, record0.getLowerY() - this.minGap - record1.getLabelHeight() / 2.0));
        }
    }

    protected void spreadEvenly(double minY, double height) {
        double y = minY;
        double sumOfLabelHeights = 0.0;
        for (int i = 0; i < this.labels.size(); ++i) {
            sumOfLabelHeights += this.getPieLabelRecord(i).getLabelHeight();
        }
        double gap = height - sumOfLabelHeights;
        if (this.labels.size() > 1) {
            gap /= (double)(this.labels.size() - 1);
        }
        for (int i2 = 0; i2 < this.labels.size(); ++i2) {
            PieLabelRecord record = this.getPieLabelRecord(i2);
            record.setAllocatedY(y += record.getLabelHeight() / 2.0);
            y = y + record.getLabelHeight() / 2.0 + gap;
        }
    }

    public void sort() {
        Collections.sort(this.labels);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < this.labels.size(); ++i) {
            result.append(this.getPieLabelRecord(i).toString()).append("\n");
        }
        return result.toString();
    }
}

