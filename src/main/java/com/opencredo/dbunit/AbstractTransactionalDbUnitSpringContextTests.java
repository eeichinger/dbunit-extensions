package com.opencredo.dbunit;

import org.dbunit.database.IDatabaseConnection;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import org.dbunit.operation.DatabaseOperation;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: Erich Eichinger
 * Date: 19-Apr-2010
 * Time: 21:04:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractTransactionalDbUnitSpringContextTests extends AbstractTransactionalDataSourceSpringContextTests {
    private SpringAwareDbUnitDatabaseConnection dbUnitDatabase;

    public void setDbUnitDatabaseConnection(SpringAwareDbUnitDatabaseConnection dbUnitDatabase) {
        this.dbUnitDatabase = dbUnitDatabase;
    }

    /**
     * Load DBUnit data set from specified resource. DBUnit resource data-set
     * file expected to be in same package as invoking class.
     *
     * @param dbunitResourceName
     * @throws org.dbunit.DatabaseUnitException
     *
     * @throws java.sql.SQLException
     */
    protected final void setUpTestData(String dbunitResourceName) {
        try {
            dbUnitDatabase.obtainConnection();
            onSetupTestData(dbUnitDatabase, dbunitResourceName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbUnitDatabase.releaseConnection();
        }
    }

    protected void onSetupTestData(IDatabaseConnection databaseConnection, String dbunitResourceName)
            throws Exception {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getResourceAsStream(dbunitResourceName));
        dataSet = configureTestData(dataSet);
        DatabaseOperation.INSERT.execute(databaseConnection, dataSet);
    }

    protected IDataSet configureTestData(IDataSet dataSet) {
//		 ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
//		 replacementDataSet.addReplacementObject("{DATE}", (new Date(System.currentTimeMillis())));
//       replacementDataSet.addReplacementObject("{NULL}", null);
//       return replacementDataSet;
        return dataSet;
    }
}
