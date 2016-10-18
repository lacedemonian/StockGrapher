/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import org.jfree.data.DataUtilities;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.general.AbstractSeriesDataset;

public class DefaultIntervalCategoryDataset
extends AbstractSeriesDataset
implements IntervalCategoryDataset {
    private Comparable[] seriesKeys;
    private Comparable[] categoryKeys;
    private Number[][] startData;
    private Number[][] endData;

    public DefaultIntervalCategoryDataset(double[][] starts, double[][] ends) {
        this(DataUtilities.createNumberArray2D(starts), DataUtilities.createNumberArray2D(ends));
    }

    public DefaultIntervalCategoryDataset(Number[][] starts, Number[][] ends) {
        this(null, null, starts, ends);
    }

    public DefaultIntervalCategoryDataset(String[] seriesNames, Number[][] starts, Number[][] ends) {
        this((Comparable[])seriesNames, null, starts, ends);
    }

    public DefaultIntervalCategoryDataset(Comparable[] seriesKeys, Comparable[] categoryKeys, Number[][] starts, Number[][] ends) {
        this.startData = starts;
        this.endData = ends;
        if (starts != null && ends != null) {
            String baseName = "org.jfree.data.resources.DataPackageResources";
            ResourceBundle resources = ResourceBundle.getBundle(baseName);
            int seriesCount = starts.length;
            if (seriesCount != ends.length) {
                String errMsg = "DefaultIntervalCategoryDataset: the number of series in the start value dataset does not match the number of series in the end value dataset.";
                throw new IllegalArgumentException(errMsg);
            }
            if (seriesCount > 0) {
                if (seriesKeys != null) {
                    if (seriesKeys.length != seriesCount) {
                        throw new IllegalArgumentException("The number of series keys does not match the number of series in the data.");
                    }
                    this.seriesKeys = seriesKeys;
                } else {
                    String prefix = resources.getString("series.default-prefix") + " ";
                    this.seriesKeys = this.generateKeys(seriesCount, prefix);
                }
                int categoryCount = starts[0].length;
                if (categoryCount != ends[0].length) {
                    String errMsg = "DefaultIntervalCategoryDataset: the number of categories in the start value dataset does not match the number of categories in the end value dataset.";
                    throw new IllegalArgumentException(errMsg);
                }
                if (categoryKeys != null) {
                    if (categoryKeys.length != categoryCount) {
                        throw new IllegalArgumentException("The number of category keys does not match the number of categories in the data.");
                    }
                    this.categoryKeys = categoryKeys;
                } else {
                    String prefix = resources.getString("categories.default-prefix") + " ";
                    this.categoryKeys = this.generateKeys(categoryCount, prefix);
                }
            } else {
                this.seriesKeys = null;
                this.categoryKeys = null;
            }
        }
    }

    public int getSeriesCount() {
        int result = 0;
        if (this.startData != null) {
            result = this.startData.length;
        }
        return result;
    }

    public int getItemCount() {
        return this.categoryKeys.length;
    }

    public int getSeriesIndex(Comparable series) {
        List seriesKeys = this.getSeries();
        return seriesKeys.indexOf(series);
    }

    public Comparable getSeriesKey(int series) {
        if (series >= this.getSeriesCount() || series < 0) {
            throw new IllegalArgumentException("No such series : " + series);
        }
        return this.seriesKeys[series];
    }

    public void setSeriesKeys(Comparable[] seriesKeys) {
        if (seriesKeys == null) {
            throw new IllegalArgumentException("Null 'seriesKeys' argument.");
        }
        if (seriesKeys.length != this.getSeriesCount()) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.setSeriesKeys(): the number of series keys does not match the data.");
        }
        this.seriesKeys = seriesKeys;
        this.fireDatasetChanged();
    }

    public int getCategoryCount() {
        int result = 0;
        if (this.startData != null && this.getSeriesCount() > 0) {
            result = this.startData[0].length;
        }
        return result;
    }

    public List getSeries() {
        if (this.seriesKeys == null) {
            return new ArrayList();
        }
        return Collections.unmodifiableList(Arrays.asList(this.seriesKeys));
    }

    public List getCategories() {
        return this.getColumnKeys();
    }

    public List getColumnKeys() {
        if (this.categoryKeys == null) {
            return new ArrayList();
        }
        return Collections.unmodifiableList(Arrays.asList(this.categoryKeys));
    }

    public void setCategoryKeys(Comparable[] categoryKeys) {
        if (categoryKeys == null) {
            throw new IllegalArgumentException("Null 'categoryKeys' argument.");
        }
        if (categoryKeys.length != this.startData[0].length) {
            throw new IllegalArgumentException("The number of categories does not match the data.");
        }
        for (int i = 0; i < categoryKeys.length; ++i) {
            if (categoryKeys[i] != null) continue;
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.setCategoryKeys(): null category not permitted.");
        }
        this.categoryKeys = categoryKeys;
        this.fireDatasetChanged();
    }

    public Number getValue(Comparable series, Comparable category) {
        int seriesIndex = this.getSeriesIndex(series);
        int itemIndex = this.getColumnIndex(category);
        return this.getValue(seriesIndex, itemIndex);
    }

    public Number getValue(int series, int category) {
        return this.getEndValue(series, category);
    }

    public Number getStartValue(Comparable series, Comparable category) {
        int seriesIndex = this.getSeriesIndex(series);
        int itemIndex = this.getColumnIndex(category);
        return this.getStartValue(seriesIndex, itemIndex);
    }

    public Number getStartValue(int series, int category) {
        if (series < 0 || series >= this.getSeriesCount()) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.getValue(): series index out of range.");
        }
        if (category < 0 || category >= this.getCategoryCount()) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.getValue(): category index out of range.");
        }
        return this.startData[series][category];
    }

    public Number getEndValue(Comparable series, Comparable category) {
        int seriesIndex = this.getSeriesIndex(series);
        int itemIndex = this.getColumnIndex(category);
        return this.getEndValue(seriesIndex, itemIndex);
    }

    public Number getEndValue(int series, int category) {
        if (series < 0 || series >= this.getSeriesCount()) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.getValue(): series index out of range.");
        }
        if (category < 0 || category >= this.getCategoryCount()) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.getValue(): category index out of range.");
        }
        return this.endData[series][category];
    }

    public void setStartValue(int series, Comparable category, Number value) {
        if (series < 0 || series > this.getSeriesCount()) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.setValue: series outside valid range.");
        }
        int categoryIndex = this.getCategoryIndex(category);
        if (categoryIndex < 0) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.setValue: unrecognised category.");
        }
        this.startData[series][categoryIndex] = value;
        this.fireDatasetChanged();
    }

    public void setEndValue(int series, Comparable category, Number value) {
        if (series < 0 || series > this.getSeriesCount()) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.setValue: series outside valid range.");
        }
        int categoryIndex = this.getCategoryIndex(category);
        if (categoryIndex < 0) {
            throw new IllegalArgumentException("DefaultIntervalCategoryDataset.setValue: unrecognised category.");
        }
        this.endData[series][categoryIndex] = value;
        this.fireDatasetChanged();
    }

    private int getCategoryIndex(Comparable category) {
        int result = -1;
        for (int i = 0; i < this.categoryKeys.length; ++i) {
            if (!category.equals(this.categoryKeys[i])) continue;
            result = i;
            break;
        }
        return result;
    }

    private Comparable[] generateKeys(int count, String prefix) {
        Comparable[] result = new Comparable[count];
        for (int i = 0; i < count; ++i) {
            String name;
            result[i] = name = prefix + (i + 1);
        }
        return result;
    }

    public Comparable getColumnKey(int column) {
        return this.categoryKeys[column];
    }

    public int getColumnIndex(Comparable columnKey) {
        List categories = this.getCategories();
        return categories.indexOf(columnKey);
    }

    public int getRowIndex(Comparable rowKey) {
        List seriesKeys = this.getSeries();
        return seriesKeys.indexOf(rowKey);
    }

    public List getRowKeys() {
        if (this.seriesKeys == null) {
            return new ArrayList();
        }
        return Collections.unmodifiableList(Arrays.asList(this.seriesKeys));
    }

    public Comparable getRowKey(int row) {
        if (row >= this.getRowCount() || row < 0) {
            throw new IllegalArgumentException("The 'row' argument is out of bounds.");
        }
        return this.seriesKeys[row];
    }

    public int getColumnCount() {
        int result = 0;
        if (this.startData != null && this.getSeriesCount() > 0) {
            result = this.startData[0].length;
        }
        return result;
    }

    public int getRowCount() {
        int result = 0;
        if (this.startData != null) {
            result = this.startData.length;
        }
        return result;
    }
}

