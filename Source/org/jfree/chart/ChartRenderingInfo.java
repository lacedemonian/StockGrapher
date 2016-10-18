/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.io.SerialUtilities;
import org.jfree.util.ObjectUtilities;

public class ChartRenderingInfo
implements Cloneable,
Serializable {
    private static final long serialVersionUID = 2751952018173406822L;
    private transient Rectangle2D chartArea = new Rectangle2D.Double();
    private PlotRenderingInfo plotInfo;
    private EntityCollection entities;

    public ChartRenderingInfo() {
        this(new StandardEntityCollection());
    }

    public ChartRenderingInfo(EntityCollection entities) {
        this.plotInfo = new PlotRenderingInfo(this);
        this.entities = entities;
    }

    public Rectangle2D getChartArea() {
        return this.chartArea;
    }

    public void setChartArea(Rectangle2D area) {
        this.chartArea.setRect(area);
    }

    public EntityCollection getEntityCollection() {
        return this.entities;
    }

    public void setEntityCollection(EntityCollection entities) {
        this.entities = entities;
    }

    public void clear() {
        this.chartArea.setRect(0.0, 0.0, 0.0, 0.0);
        this.plotInfo = new PlotRenderingInfo(this);
        if (this.entities != null) {
            this.entities.clear();
        }
    }

    public PlotRenderingInfo getPlotInfo() {
        return this.plotInfo;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChartRenderingInfo)) {
            return false;
        }
        ChartRenderingInfo that = (ChartRenderingInfo)obj;
        if (!ObjectUtilities.equal(this.chartArea, that.chartArea)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.plotInfo, that.plotInfo)) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writeShape(this.chartArea, stream);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.chartArea = (Rectangle2D)SerialUtilities.readShape(stream);
    }
}

