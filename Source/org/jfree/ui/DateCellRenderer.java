/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateCellRenderer
extends DefaultTableCellRenderer {
    private DateFormat formatter;

    public DateCellRenderer() {
        this(DateFormat.getDateTimeInstance());
    }

    public DateCellRenderer(DateFormat formatter) {
        this.formatter = formatter;
        this.setHorizontalAlignment(0);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setFont(null);
        this.setText(this.formatter.format(value));
        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
        } else {
            this.setBackground(null);
        }
        return this;
    }
}

