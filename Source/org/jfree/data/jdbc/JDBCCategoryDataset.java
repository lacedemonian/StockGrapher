/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.jfree.data.category.DefaultCategoryDataset;

public class JDBCCategoryDataset
extends DefaultCategoryDataset {
    private transient Connection connection;
    private boolean transpose = true;

    public JDBCCategoryDataset(String url, String driverName, String user, String passwd) throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        this.connection = DriverManager.getConnection(url, user, passwd);
    }

    public JDBCCategoryDataset(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("A connection must be supplied.");
        }
        this.connection = connection;
    }

    public JDBCCategoryDataset(Connection connection, String query) throws SQLException {
        this(connection);
        this.executeQuery(query);
    }

    public boolean getTranspose() {
        return this.transpose;
    }

    public void setTranspose(boolean transpose) {
        this.transpose = transpose;
    }

    public void executeQuery(String query) throws SQLException {
        this.executeQuery(this.connection, query);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeQuery(Connection con, String query) throws SQLException {
        Statement statement;
        block25 : {
            statement = null;
            ResultSet resultSet = null;
            try {
                statement = con.createStatement();
                resultSet = statement.executeQuery(query);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                if (columnCount < 2) {
                    throw new SQLException("JDBCCategoryDataset.executeQuery() : insufficient columns returned from the database.");
                }
                for (int i = this.getRowCount(); i > 0; --i) {
                    this.removeRow(i);
                }
                while (resultSet.next()) {
                    String rowKey = resultSet.getString(1);
                    block17 : for (int column = 2; column <= columnCount; ++column) {
                        String columnKey = metaData.getColumnName(column);
                        int columnType = metaData.getColumnType(column);
                        switch (columnType) {
                            case -6: 
                            case -5: 
                            case 2: 
                            case 3: 
                            case 4: 
                            case 5: 
                            case 6: 
                            case 7: 
                            case 8: {
                                Number value2 = (Number)resultSet.getObject(column);
                                if (this.transpose) {
                                    this.setValue(value2, (Comparable)((Object)columnKey), (Comparable)((Object)rowKey));
                                    continue block17;
                                }
                                this.setValue(value2, (Comparable)((Object)rowKey), (Comparable)((Object)columnKey));
                                continue block17;
                            }
                            case 91: 
                            case 92: 
                            case 93: {
                                Date date = (Date)resultSet.getObject(column);
                                Long value = new Long(date.getTime());
                                if (this.transpose) {
                                    this.setValue(value, (Comparable)((Object)columnKey), (Comparable)((Object)rowKey));
                                    continue block17;
                                }
                                this.setValue(value, (Comparable)((Object)rowKey), (Comparable)((Object)columnKey));
                                continue block17;
                            }
                            case -1: 
                            case 1: 
                            case 12: {
                                String string = (String)resultSet.getObject(column);
                                try {
                                    Double value = Double.valueOf(string);
                                    if (this.transpose) {
                                        this.setValue(value, (Comparable)((Object)columnKey), (Comparable)((Object)rowKey));
                                        continue block17;
                                    }
                                    this.setValue(value, (Comparable)((Object)rowKey), (Comparable)((Object)columnKey));
                                    continue block17;
                                }
                                catch (NumberFormatException e) {
                                    break;
                                }
                            }
                        }
                    }
                }
                this.fireDatasetChanged();
                Object var15_17 = null;
                if (resultSet == null) break block25;
            }
            catch (Throwable var14_21) {
                Object var15_18 = null;
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    }
                    catch (Exception e2) {
                        // empty catch block
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    }
                    catch (Exception e) {
                        // empty catch block
                    }
                }
                throw var14_21;
            }
            try {
                resultSet.close();
            }
            catch (Exception e2) {
                // empty catch block
            }
        }
        if (statement != null) {
            try {
                statement.close();
            }
            catch (Exception e) {}
        }
    }
}

