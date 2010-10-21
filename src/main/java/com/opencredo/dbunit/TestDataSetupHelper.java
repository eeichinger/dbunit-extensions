package com.opencredo.dbunit;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public class TestDataSetupHelper {

    private SpringAwareDbUnitDatabaseConnection dbUnitDatabase;

    public TestDataSetupHelper(SpringAwareDbUnitDatabaseConnection dbUnitDatabase) {
        this.dbUnitDatabase = dbUnitDatabase;
    }

    /**
     * Load DBUnit data set from specified resource, using the caller's resource loader
     * and the default name <simple classname>-dbunit.xml
     *
     * @throws java.sql.SQLException
     */
    public final void setUpTestData(Object caller) {
        setUpTestData(caller, caller.getClass().getSimpleName() + "-dbunit.xml");
    }

    /**
     * Load DBUnit data set from specified resource, using the caller's resource loader
     *
     * @throws java.sql.SQLException
     */
    public final void setUpTestData(Object caller, String dbunitResourceName) {
        try {
            dbUnitDatabase.obtainConnection();
            onSetupTestData(caller, dbUnitDatabase, dbunitResourceName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbUnitDatabase.releaseConnection();
        }
    }

    protected void onSetupTestData(Object caller, IDatabaseConnection databaseConnection, String dbunitResourceName) throws Exception {
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(caller.getClass().getResourceAsStream(dbunitResourceName));
        dataSet = configureTestData(dataSet);
        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
    }

    protected IDataSet configureTestData(IDataSet dataSet) {
//		 ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
//		 replacementDataSet.addReplacementObject("{DATE}", (new Date(System.currentTimeMillis())));
//       replacementDataSet.addReplacementObject("{NULL}", null);
//       return replacementDataSet;
        return dataSet;
    }

}
