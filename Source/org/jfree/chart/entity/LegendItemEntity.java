/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.entity;

import java.awt.Shape;
import java.io.Serializable;
import org.jfree.chart.entity.ChartEntity;

public class LegendItemEntity
extends ChartEntity
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -7435683933545666702L;
    private int seriesIndex;

    public LegendItemEntity(Shape area) {
        super(area);
    }

    public int getSeriesIndex() {
        return this.seriesIndex;
    }

    public void setSeriesIndex(int index) {
        this.seriesIndex = index;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof LegendItemEntity && super.equals(obj)) {
            LegendItemEntity e = (LegendItemEntity)obj;
            if (this.seriesIndex != e.seriesIndex) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

