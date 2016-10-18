/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.category;

import java.util.List;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;

public class CategoryToPieDataset
extends AbstractDataset
implements PieDataset,
DatasetChangeListener {
    private CategoryDataset source;
    private TableOrder extract;
    private int index;

    public CategoryToPieDataset(CategoryDataset source, TableOrder extract, int index) {
        if (extract == null) {
            throw new IllegalArgumentException("Null 'extract' argument.");
        }
        this.source = source;
        this.source.addChangeListener(this);
        this.extract = extract;
        this.index = index;
    }

    public int getItemCount() {
        int result = 0;
        if (this.source != null) {
            if (this.extract == TableOrder.BY_ROW) {
                result = this.source.getColumnCount();
            } else if (this.extract == TableOrder.BY_COLUMN) {
                result = this.source.getRowCount();
            }
        }
        return result;
    }

    public Number getValue(int item) {
        Number result = null;
        if (this.source != null) {
            if (this.extract == TableOrder.BY_ROW) {
                result = this.source.getValue(this.index, item);
            } else if (this.extract == TableOrder.BY_COLUMN) {
                result = this.source.getValue(item, this.index);
            }
        }
        return result;
    }

    public Comparable getKey(int index) {
        Comparable result = null;
        if (this.extract == TableOrder.BY_ROW) {
            result = this.source.getColumnKey(index);
        } else if (this.extract == TableOrder.BY_COLUMN) {
            result = this.source.getRowKey(index);
        }
        return result;
    }

    public int getIndex(Comparable key) {
        int result = -1;
        if (this.extract == TableOrder.BY_ROW) {
            result = this.source.getColumnIndex(key);
        } else if (this.extract == TableOrder.BY_COLUMN) {
            result = this.source.getRowIndex(key);
        }
        return result;
    }

    public List getKeys() {
        List result = null;
        if (this.extract == TableOrder.BY_ROW) {
            result = this.source.getColumnKeys();
        } else if (this.extract == TableOrder.BY_COLUMN) {
            result = this.source.getRowKeys();
        }
        return result;
    }

    public Number getValue(Comparable key) {
        Number result = null;
        int keyIndex = this.getIndex(key);
        if (this.extract == TableOrder.BY_ROW) {
            result = this.source.getValue(this.index, keyIndex);
        } else if (this.extract == TableOrder.BY_COLUMN) {
            result = this.source.getValue(keyIndex, this.index);
        }
        return result;
    }

    public void datasetChanged(DatasetChangeEvent event) {
        this.fireDatasetChanged();
    }
}

