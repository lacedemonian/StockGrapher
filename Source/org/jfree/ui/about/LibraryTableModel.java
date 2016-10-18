/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui.about;

import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import org.jfree.base.Library;

public class LibraryTableModel
extends AbstractTableModel {
    private List libraries;
    private String nameColumnLabel;
    private String versionColumnLabel;
    private String licenceColumnLabel;
    private String infoColumnLabel;

    public LibraryTableModel(List libraries) {
        this.libraries = libraries;
        String baseName = "org.jfree.ui.about.resources.AboutResources";
        ResourceBundle resources = ResourceBundle.getBundle("org.jfree.ui.about.resources.AboutResources");
        this.nameColumnLabel = resources.getString("libraries-table.column.name");
        this.versionColumnLabel = resources.getString("libraries-table.column.version");
        this.licenceColumnLabel = resources.getString("libraries-table.column.licence");
        this.infoColumnLabel = resources.getString("libraries-table.column.info");
    }

    public int getRowCount() {
        return this.libraries.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public String getColumnName(int column) {
        String result = null;
        switch (column) {
            case 0: {
                result = this.nameColumnLabel;
                break;
            }
            case 1: {
                result = this.versionColumnLabel;
                break;
            }
            case 2: {
                result = this.licenceColumnLabel;
                break;
            }
            case 3: {
                result = this.infoColumnLabel;
            }
        }
        return result;
    }

    public Object getValueAt(int row, int column) {
        String result = null;
        Library library = (Library)this.libraries.get(row);
        if (column == 0) {
            result = library.getName();
        } else if (column == 1) {
            result = library.getVersion();
        } else if (column == 2) {
            result = library.getLicenceName();
        } else if (column == 3) {
            result = library.getInfo();
        }
        return result;
    }
}

