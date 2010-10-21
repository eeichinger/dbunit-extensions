package com.opencredo.dbunit;

import org.dbunit.database.IDatabaseConnection;

/**
 * Created by IntelliJ IDEA.
 * User: Erich Eichinger
 * Date: 19-Apr-2010
 * Time: 19:48:51
 * To change this template use File | Settings | File Templates.
 */
public interface SpringAwareDbUnitDatabaseConnection extends IDatabaseConnection {
    void obtainConnection();
    void releaseConnection();
}
