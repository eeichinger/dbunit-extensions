package com.opencredo.dbunit.sample;

import oracle.jdbc.OracleDriver;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

public class ImportSample {

    public static void main(String[] args) throws Exception
    {
        DriverManager.registerDriver(new OracleDriver());

        String[] tables = { "TABLE_NAME" };
        String jdbcUrl = "jdbc:oracle:thin:USERNAME/PASSWORD@localhost:1521/ORCL";

        // database import
        importTables(tables, jdbcUrl);
    }

    private static void importTables(String[] tables, String jdbcUrl) {
        for(String table:tables) {
            Connection jdbcConnection = null;
            IDatabaseConnection connection = null;
            try {
                jdbcConnection = DriverManager.getConnection(jdbcUrl);
                connection = DbUnitHelper.createDbUnitConnection(jdbcConnection, "SCHEMA_NAME");

                try {
                    IDataSet dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(table + ".xml"));
                    DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
                } finally {
                    if (connection!=null) connection.close();
                    if (jdbcConnection!=null) jdbcConnection.close();
                }
            } catch (Exception e) {
                System.out.println("Cant import table " + table + ": " + e);
            }
        }
    }

}
