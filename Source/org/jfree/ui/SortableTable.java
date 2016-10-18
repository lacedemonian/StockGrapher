/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.jfree.ui.SortButtonRenderer;
import org.jfree.ui.SortableTableHeaderListener;
import org.jfree.ui.SortableTableModel;

public class SortableTable
extends JTable {
    private SortableTableHeaderListener headerListener;

    public SortableTable(SortableTableModel model) {
        super(model);
        SortButtonRenderer renderer = new SortButtonRenderer();
        TableColumnModel cm = this.getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); ++i) {
            cm.getColumn(i).setHeaderRenderer(renderer);
        }
        JTableHeader header = this.getTableHeader();
        this.headerListener = new SortableTableHeaderListener(model, renderer);
        header.addMouseListener(this.headerListener);
        header.addMouseMotionListener(this.headerListener);
        model.sortByColumn(0, true);
    }

    public void setSortableModel(SortableTableModel model) {
        super.setModel(model);
        this.headerListener.setTableModel(model);
        SortButtonRenderer renderer = new SortButtonRenderer();
        TableColumnModel cm = this.getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); ++i) {
            cm.getColumn(i).setHeaderRenderer(renderer);
        }
        model.sortByColumn(0, true);
    }
}

