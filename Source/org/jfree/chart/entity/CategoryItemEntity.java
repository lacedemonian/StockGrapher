/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.chart.entity;

import java.awt.Shape;
import java.io.Serializable;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.data.category.CategoryDataset;
import org.jfree.util.ObjectUtilities;

public class CategoryItemEntity
extends ChartEntity
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -8657249457902337349L;
    private transient CategoryDataset dataset;
    private int series;
    private Object category;
    private int categoryIndex;

    public CategoryItemEntity(Shape area, String toolTipText, String urlText, CategoryDataset dataset, int series, Object category, int categoryIndex) {
        super(area, toolTipText, urlText);
        this.dataset = dataset;
        this.series = series;
        this.category = category;
        this.categoryIndex = categoryIndex;
    }

    public CategoryDataset getDataset() {
        return this.dataset;
    }

    public void setDataset(CategoryDataset dataset) {
        this.dataset = dataset;
    }

    public int getSeries() {
        return this.series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public Object getCategory() {
        return this.category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public int getCategoryIndex() {
        return this.categoryIndex;
    }

    public void setCategoryIndex(int index) {
        this.categoryIndex = index;
    }

    public String toString() {
        return "Category Item: series=" + this.series + ", category=" + this.category.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CategoryItemEntity && super.equals(obj)) {
            CategoryItemEntity cie = (CategoryItemEntity)obj;
            if (this.categoryIndex != cie.categoryIndex) {
                return false;
            }
            if (this.series != cie.series) {
                return false;
            }
            if (!ObjectUtilities.equal(this.category, cie.category)) {
                return false;
            }
            return true;
        }
        return false;
    }
}

