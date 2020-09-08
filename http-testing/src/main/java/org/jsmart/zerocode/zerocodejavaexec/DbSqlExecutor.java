package org.jsmart.zerocode.zerocodejavaexec;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.util.Arrays;

import org.springframework.util.ClassUtils;
import org.jsmart.zerocode.zerocodejavaexec.pojo.DbResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbSqlExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbSqlExecutor.class);
    Map<String, List<Map<String, Object>>> dbRecordsMap = new HashMap<>();

    @Inject(optional = true)
    @Named("db.conn.host.url")
    private String dbHostUrl;

    @Inject(optional = true)
    @Named("db.conn.user.name")
    private String dbUserName;

    @Inject(optional = true)
    @Named("db.conn.password")
    private String dbPassword;

    public static final String RESULTS_KEY = "results";

    public Map<String, List<Map<String, Object>>> executeSelectSql(String sqlStatement) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        LOGGER.info("\n\nDB Connection user:{}, password:{}\n\n", dbUserName, dbPassword);

        /**
         * ----------------------------------------------------------------------------------
         * // Your code goes here. //
         * e.g.
         * - You can use JDBC-connection/spring JDBC template and fetch the results using
         * the above 'userName and password'
         * ----------------------------------------------------------------------------------
         */

        /**
         * Once you finished the DB execution and you will get the list of results from DB in
         * the 'results' list. Values hard coded below for the example understanding only.
         * In reality you get these results from the DB.
         */

        LOGGER.info("DB - Executing SQL query: {}", sqlStatement);

        List<Map<String, Object>> recordsList = fetchDbRecords(sqlStatement);

        // -------------------------------------------------------
        // Put all the fetched rows into nice JSON key and return.
        // -- This make it better to assert SIZE etc in the steps.
        // -- You can choose any key.
        // -------------------------------------------------------
        dbRecordsMap.put(RESULTS_KEY, recordsList);

        return dbRecordsMap;

/*        List<DbResult> results = new ArrayList<>();
        results.add(new DbResult(1, "Elon Musk"));
        results.add(new DbResult(2, "Jeff Bezos"));

        Map<String, List<DbResult>> resultsMap = new HashMap<>();
        resultsMap.put(RESULTS_KEY, results);

        return resultsMap;*/

    }

    private List<Map<String, Object>> fetchDbRecords(String simpleSql) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return getJdbcTemplate().query(simpleSql, (resultSet, i) -> {
            Map<String, Object> aRowColumnValue = new HashMap<>();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int j = 1; j <= columnCount; j++) {
                String columnName = metaData.getColumnName(j);
                Object columnValue = resultSet.getObject(columnName);

                aRowColumnValue.put(columnName, columnValue);
            }

            return aRowColumnValue;
        });
    }

    private JdbcTemplate getJdbcTemplate() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final String driverClassName = "org.postgresql.Driver";

        // Build dataSource & JDBC template from the host properties file
        final Class<?> driverClass = ClassUtils.resolveClassName(driverClassName, this.getClass().getClassLoader());
        org.postgresql.Driver driver = (org.postgresql.Driver) ClassUtils.getConstructorIfAvailable(driverClass).newInstance();
        final DataSource dataSource = new SimpleDriverDataSource(driver, dbHostUrl, dbUserName, dbPassword);

        return new JdbcTemplate(dataSource);
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
