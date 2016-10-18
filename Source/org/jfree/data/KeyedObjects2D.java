/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.data.KeyedObjects;

public class KeyedObjects2D
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -1015873563138522374L;
    private List rowKeys = new ArrayList();
    private List columnKeys = new ArrayList();
    private List rows = new ArrayList();

    public int getRowCount() {
        return this.rowKeys.size();
    }

    public int getColumnCount() {
        return this.columnKeys.size();
    }

    public Object getObject(int row, int column) {
        Comparable columnKey;
        Object result = null;
        KeyedObjects rowData = (KeyedObjects)this.rows.get(row);
        if (rowData != null && (columnKey = (Comparable)this.columnKeys.get(column)) != null) {
            result = rowData.getObject(columnKey);
        }
        return result;
    }

    public Comparable getRowKey(int row) {
        return (Comparable)this.rowKeys.get(row);
    }

    public int getRowIndex(Comparable key) {
        return this.rowKeys.indexOf(key);
    }

    public List getRowKeys() {
        return Collections.unmodifiableList(this.rowKeys);
    }

    public Comparable getColumnKey(int column) {
        return (Comparable)this.columnKeys.get(column);
    }

    public int getColumnIndex(Comparable key) {
        return this.columnKeys.indexOf(key);
    }

    public List getColumnKeys() {
        return Collections.unmodifiableList(this.columnKeys);
    }

    public Object getObject(Comparable rowKey, Comparable columnKey) {
        Object result = null;
        int row = this.rowKeys.indexOf(rowKey);
        if (row >= 0) {
            KeyedObjects rowData = (KeyedObjects)this.rows.get(row);
            result = rowData.getObject(columnKey);
        }
        return result;
    }

    public void addObject(Object object, Comparable rowKey, Comparable columnKey) {
        this.setObject(object, rowKey, columnKey);
    }

    public void setObject(Object object, Comparable rowKey, Comparable columnKey) {
        KeyedObjects row;
        int rowIndex = this.rowKeys.indexOf(rowKey);
        if (rowIndex >= 0) {
            row = (KeyedObjects)this.rows.get(rowIndex);
        } else {
            this.rowKeys.add(rowKey);
            row = new KeyedObjects();
            this.rows.add(row);
        }
        row.setObject(columnKey, object);
        int columnIndex = this.columnKeys.indexOf(columnKey);
        if (columnIndex < 0) {
            this.columnKeys.add(columnKey);
        }
    }

    public void removeObject(Comparable rowKey, Comparable columnKey) {
        this.setObject(null, rowKey, columnKey);
    }

    public void removeRow(int rowIndex) {
        this.rowKeys.remove(rowIndex);
        this.rows.remove(rowIndex);
    }

    public void removeRow(Comparable rowKey) {
        this.removeRow(this.getRowIndex(rowKey));
    }

    public void removeColumn(int columnIndex) {
        Comparable columnKey = this.getColumnKey(columnIndex);
        this.removeColumn(columnKey);
    }

    public void removeColumn(Comparable columnKey) {
        Iterator iterator = this.rows.iterator();
        while (iterator.hasNext()) {
            KeyedObjects rowData = (KeyedObjects)iterator.next();
            rowData.removeValue(columnKey);
        }
        this.columnKeys.remove(columnKey);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KeyedObjects2D)) {
            return false;
        }
        KeyedObjects2D ko2D = (KeyedObjects2D)obj;
        if (!this.getRowKeys().equals(ko2D.getRowKeys())) {
            return false;
        }
        if (!this.getColumnKeys().equals(ko2D.getColumnKeys())) {
            return false;
        }
        int rowCount = this.getRowCount();
        if (rowCount != ko2D.getRowCount()) {
            return false;
        }
        int colCount = this.getColumnCount();
        if (colCount != ko2D.getColumnCount()) {
            return false;
        }
        for (int r = 0; r < rowCount; ++r) {
            for (int c = 0; c < colCount; ++c) {
                Object v1 = this.getObject(r, c);
                Object v2 = ko2D.getObject(r, c);
                if (!(v1 == null ? v2 != null : !v1.equals(v2))) continue;
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = this.rowKeys.hashCode();
        result = 29 * result + this.columnKeys.hashCode();
        result = 29 * result + this.rows.hashCode();
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

