/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.xy;

import java.io.Serializable;
import org.jfree.data.general.Series;

public class MatrixSeries
extends Series
implements Serializable {
    private static final long serialVersionUID = 7934188527308315704L;
    protected double[][] data;

    public MatrixSeries(String name, int rows, int columns) {
        super((Comparable)((Object)name));
        this.data = new double[rows][columns];
        this.zeroAll();
    }

    public int getColumnsCount() {
        return this.data[0].length;
    }

    public Number getItem(int itemIndex) {
        int i = this.getItemRow(itemIndex);
        int j = this.getItemColumn(itemIndex);
        Double n = new Double(this.get(i, j));
        return n;
    }

    public int getItemColumn(int itemIndex) {
        return itemIndex % this.getColumnsCount();
    }

    public int getItemCount() {
        return this.getRowCount() * this.getColumnsCount();
    }

    public int getItemRow(int itemIndex) {
        return itemIndex / this.getColumnsCount();
    }

    public int getRowCount() {
        return this.data.length;
    }

    public double get(int i, int j) {
        return this.data[i][j];
    }

    public void update(int i, int j, double mij) {
        this.data[i][j] = mij;
        this.fireSeriesChanged();
    }

    public void zeroAll() {
        int rows = this.getRowCount();
        int columns = this.getColumnsCount();
        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                this.data[row][column] = 0.0;
            }
        }
        this.fireSeriesChanged();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof MatrixSeries && super.equals(obj)) {
            MatrixSeries m = (MatrixSeries)obj;
            if (this.getRowCount() != m.getRowCount()) {
                return false;
            }
            if (this.getColumnsCount() != m.getColumnsCount()) {
                return false;
            }
            return true;
        }
        return false;
    }
}

