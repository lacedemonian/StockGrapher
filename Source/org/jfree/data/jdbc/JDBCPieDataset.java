/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.data.jdbc;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.jfree.data.general.DefaultPieDataset;

public class JDBCPieDataset
extends DefaultPieDataset {
    private transient Connection connection;

    public JDBCPieDataset(String url, String driverName, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driverName);
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public JDBCPieDataset(Connection con) {
        if (con == null) {
            throw new NullPointerException("A connection must be supplied.");
        }
        this.connection = con;
    }

    public JDBCPieDataset(Connection con, String query) throws SQLException {
        this(con);
        this.executeQuery(query);
    }

    public void executeQuery(String query) throws SQLException {
        this.executeQuery(this.connection, query);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void executeQuery(Connection con, String query) throws SQLException {
        Statement statement;
        Exception e2;
        block17 : {
            statement = null;
            ResultSet resultSet = null;
            try {
                statement = con.createStatement();
                resultSet = statement.executeQuery(query);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                if (columnCount != 2) {
                    throw new SQLException("Invalid sql generated.  PieDataSet requires 2 columns only");
                }
                int columnType = metaData.getColumnType(2);
                double value = Double.NaN;
                block12 : while (resultSet.next()) {
                    String key = resultSet.getString(1);
                    switch (columnType) {
                        case -5: 
                        case 2: 
                        case 3: 
                        case 4: 
                        case 6: 
                        case 7: 
                        case 8: {
                            value = resultSet.getDouble(2);
                            this.setValue((Comparable)((Object)key), value);
                            continue block12;
                        }
                        case 91: 
                        case 92: 
                        case 93: {
                            Timestamp date = resultSet.getTimestamp(2);
                            value = date.getTime();
                            this.setValue((Comparable)((Object)key), value);
                            continue block12;
                        }
                    }
                    System.err.println("JDBCPieDataset - unknown data type");
                }
                this.fireDatasetChanged();
                Object var13_11 = null;
                if (resultSet == null) break block17;
            }
            catch (Throwable var12_15) {
                Exception e2;
                Object var13_12 = null;
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    }
                    catch (Exception e2) {
                        System.err.println("JDBCPieDataset: swallowing exception.");
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    }
                    catch (Exception e2) {
                        System.err.println("JDBCPieDataset: swallowing exception.");
                    }
                }
                throw var12_15;
            }
            try {
                resultSet.close();
            }
            catch (Exception e2) {
                System.err.println("JDBCPieDataset: swallowing exception.");
            }
        }
        if (statement != null) {
            try {
                statement.close();
            }
            catch (Exception e2) {
                System.err.println("JDBCPieDataset: swallowing exception.");
            }
        }
    }

    public void close() {
        try {
            this.connection.close();
        }
        catch (Exception e) {
            System.err.println("JdbcXYDataset: swallowing exception.");
        }
    }
}

