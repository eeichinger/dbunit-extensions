package com.opencredo.dbunit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration
public class SpringAwareDbUnitDatabaseConnectionTest {

    @Autowired
    TestDataSetupHelper testDataSetupHelper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void testData_should_be_loaded_correctly() {
        testDataSetupHelper.setUpTestData(this);

        int count = jdbcTemplate.queryForInt("SELECT count(*) FROM BEROAV01 WHERE ORD_ID=2928158");
        Assert.assertEquals(4, count);
    }
}
