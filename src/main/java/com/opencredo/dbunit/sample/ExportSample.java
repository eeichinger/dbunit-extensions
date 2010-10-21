package com.opencredo.dbunit.sample;/* Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import oracle.jdbc.OracleDriver;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Code demoing how to export dbunit files from SQL statement results
 *
 * @author Erich Eichinger
 * @since Mar 25, 2010
 */
public class ExportSample
{
    public static void main(String[] args) throws Exception
    {
        DriverManager.registerDriver(new OracleDriver());

        String[] tables = { "BEROAT01" };
        String jdbcUrl = "jdbc:oracle:thin:BEADMIN/BEADMIN@172.16.1.208:1521:DEVDB10";

        // database export
        exportTables(tables, jdbcUrl);
    }

    public static void exportTables(String[] tables, String url) {
        for(String table :tables) {
            Connection jdbcConnection = null;
            IDatabaseConnection connection = null;
            // partial database export
            try {
                try {
                    jdbcConnection = DriverManager.getConnection(url);
                    connection = DbUnitHelper.createDbUnitConnection(jdbcConnection, "BEADMIN");

                    QueryDataSet partialDataSet = new QueryDataSet(connection);
                    // partialDataSet.addTable(table, "select * from NCRCPT01 where smp_id='EX10' and cou_iso_id='GB'");

//                    String qualifiedTableName = connection.getSchema() + "." + table.replace("T01", "V01");
                    String qualifiedTableName = connection.getSchema() + "." + table;
                    partialDataSet.addTable(table, "SELECT * FROM " + qualifiedTableName);
                    FlatXmlDataSet.write(partialDataSet, new FileOutputStream(table + ".xml"));
                    System.out.println("exported table " + table);
                } finally {
                    if (connection!=null) connection.close();
                    if (jdbcConnection!=null) jdbcConnection.close();
                }
            } catch (Exception e) {
                System.out.println("Cant export table " + table + ": " + e);
            }
        }
    }
}
