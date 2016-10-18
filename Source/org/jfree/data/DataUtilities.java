/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data;

import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.Values2D;

public abstract class DataUtilities {
    public static double calculateColumnTotal(Values2D data, int column) {
        double total = 0.0;
        int rowCount = data.getRowCount();
        for (int r = 0; r < rowCount; ++r) {
            Number n = data.getValue(r, column);
            if (n == null) continue;
            total += n.doubleValue();
        }
        return total;
    }

    public static double calculateRowTotal(Values2D data, int row) {
        double total = 0.0;
        int columnCount = data.getColumnCount();
        for (int c = 0; c < columnCount; ++c) {
            Number n = data.getValue(row, c);
            if (n == null) continue;
            total += n.doubleValue();
        }
        return total;
    }

    public static Number[] createNumberArray(double[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Null 'data' argument.");
        }
        Number[] result = new Number[data.length];
        for (int i = 0; i < data.length; ++i) {
            result[i] = new Double(data[i]);
        }
        return result;
    }

    public static Number[][] createNumberArray2D(double[][] data) {
        if (data == null) {
            throw new IllegalArgumentException("Null 'data' argument.");
        }
        int l1 = data.length;
        Number[][] result = new Number[l1][];
        for (int i = 0; i < l1; ++i) {
            result[i] = DataUtilities.createNumberArray(data[i]);
        }
        return result;
    }

    public static KeyedValues getCumulativePercentages(KeyedValues data) {
        if (data == null) {
            throw new IllegalArgumentException("Null 'data' argument.");
        }
        DefaultKeyedValues result = new DefaultKeyedValues();
        double total = 0.0;
        for (int i = 0; i < data.getItemCount(); ++i) {
            Number v = data.getValue(i);
            if (v == null) continue;
            total += v.doubleValue();
        }
        double runningTotal = 0.0;
        for (int i2 = 0; i2 < data.getItemCount(); ++i2) {
            Number v = data.getValue(i2);
            if (v != null) {
                runningTotal += v.doubleValue();
            }
            result.addValue(data.getKey(i2), new Double(runningTotal / total));
        }
        return result;
    }
}

