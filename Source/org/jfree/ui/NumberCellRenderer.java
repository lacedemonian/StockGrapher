/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.NumberFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class NumberCellRenderer
extends DefaultTableCellRenderer {
    public NumberCellRenderer() {
        this.setHorizontalAlignment(4);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setFont(null);
        NumberFormat nf = NumberFormat.getNumberInstance();
        this.setText(nf.format(value));
        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(null);
        }
        return this;
    }
}

