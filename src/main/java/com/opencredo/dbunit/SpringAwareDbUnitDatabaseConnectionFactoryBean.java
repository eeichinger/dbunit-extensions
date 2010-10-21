package com.opencredo.dbunit;

import org.apache.commons.dbcp.DelegatingConnection;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IStatementFactory;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;

import org.springframework.beans.factory.FactoryBean;

import org.springframework.jdbc.datasource.DataSourceUtils;

import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: Erich Eichinger
 * Date: 19-Apr-2010
 * Time: 19:05:30
 * To change this template use File | Settings | File Templates.
 */
public class SpringAwareDbUnitDatabaseConnectionFactoryBean implements FactoryBean {

    private final DataSource dataSource;
    private final Map<String, Object> configProperties;

    public SpringAwareDbUnitDatabaseConnectionFactoryBean(DataSource dataSource, Map<String, Object> configProperties) {
        Assert.notNull(dataSource);
        Assert.notNull(configProperties);
        this.dataSource = dataSource;
        this.configProperties = configProperties;
    }

    public Object getObject() throws Exception {
        IDatabaseConnection dbUnitCon = new SpringAwareDbUnitDatabaseConnection(dataSource, configProperties);
        return dbUnitCon;
    }

    public Class getObjectType() {
        return DatabaseConnection.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private static class SpringAwareDbUnitDatabaseConnection implements com.opencredo.dbunit.SpringAwareDbUnitDatabaseConnection {
        private final Map<String, Object> configProperties;
        private final DataSource dataSource;
        private final DelegatingConnection connectionProxy;
        private DatabaseConnection inner;

        private DatabaseConnection getDatabaseConnection() {
            if (inner == null) {
                try {
                    String username = connectionProxy.getMetaData().getUserName();
                    inner = new DatabaseConnection(connectionProxy, username); // , schemaName
                    for (Map.Entry<String, Object> entry : configProperties.entrySet()) {
                        inner.getConfig().setProperty(entry.getKey(), entry.getValue());
                    }
                    inner.getConfig().setProperty( DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                                                     new OracleDataTypeFactoryEx(  ) );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return inner;
        }

        private SpringAwareDbUnitDatabaseConnection(DataSource dataSource, Map<String, Object> configProperties) {
            Assert.notNull(dataSource);
            Assert.notNull(configProperties);
            this.dataSource = dataSource;
            this.configProperties = configProperties;
            this.connectionProxy = new DelegatingConnection(null);
        }

        public void obtainConnection() {
            Connection con = DataSourceUtils.getConnection(dataSource);
            connectionProxy.setDelegate(con);
        }

        public void releaseConnection() {
            Connection con = connectionProxy.getDelegate();
            connectionProxy.setDelegate(null);
            DataSourceUtils.releaseConnection(con, dataSource);
        }

        public Connection getConnection()
                throws SQLException {
            return getDatabaseConnection().getConnection();
        }

        public String getSchema() {
            return getDatabaseConnection().getSchema();
        }

        public void close()
                throws SQLException {
            getDatabaseConnection().close();
        }

        public String toString() {
            return getDatabaseConnection().toString();
        }

        public IDataSet createDataSet()
                throws SQLException {
            return getDatabaseConnection().createDataSet();
        }

        public IDataSet createDataSet(String[] tableNames)
                throws DataSetException, SQLException {
            return getDatabaseConnection().createDataSet(tableNames);
        }

        public ITable createQueryTable(String resultName, String sql)
                throws DataSetException, SQLException {
            return getDatabaseConnection().createQueryTable(resultName, sql);
        }

        public ITable createTable(String resultName, PreparedStatement preparedStatement)
                throws DataSetException, SQLException {
            return getDatabaseConnection().createTable(resultName, preparedStatement);
        }

        public ITable createTable(String tableName)
                throws DataSetException, SQLException {
            return getDatabaseConnection().createTable(tableName);
        }

        public int getRowCount(String tableName)
                throws SQLException {
            return getDatabaseConnection().getRowCount(tableName);
        }

        public int getRowCount(String tableName, String whereClause)
                throws SQLException {
            return getDatabaseConnection().getRowCount(tableName, whereClause);
        }

        public DatabaseConfig getConfig() {
            return getDatabaseConnection().getConfig();
        }

        public IStatementFactory getStatementFactory() {
            return getDatabaseConnection().getStatementFactory();
        }
    }
}
