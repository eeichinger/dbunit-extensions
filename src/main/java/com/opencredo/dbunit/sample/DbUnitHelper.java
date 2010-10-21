package com.opencredo.dbunit.sample;

import com.opencredo.dbunit.OracleDataTypeFactoryEx;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.database.IDatabaseConnection;

import java.sql.Connection;

public class DbUnitHelper {

    /**
     * These settings are vital for most efficient batch access
     * @param jdbcConnection
     * @param schema
     * @return
     * @throws DatabaseUnitException
     */
    public static IDatabaseConnection createDbUnitConnection(Connection jdbcConnection, String schema) throws DatabaseUnitException {
        IDatabaseConnection connection;
        connection = new DatabaseConnection(jdbcConnection, schema);
        connection.getConfig().setProperty(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
        connection.getConfig().setProperty(DatabaseConfig.PROPERTY_BATCH_SIZE, new Integer(500));
        connection.getConfig().setProperty(DatabaseConfig.PROPERTY_FETCH_SIZE, new Integer(500));
//        connection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
        connection.getConfig().setProperty(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, true);
        connection.getConfig().setProperty(DatabaseConfig.PROPERTY_RESULTSET_TABLE_FACTORY, new ForwardOnlyResultSetTableFactory());
        connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactoryEx());
        return connection;
    }
}
