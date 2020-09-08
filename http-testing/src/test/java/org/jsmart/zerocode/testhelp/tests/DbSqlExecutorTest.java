package org.jsmart.zerocode.testhelp.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsmart.zerocode.core.domain.Scenario;
import org.jsmart.zerocode.core.domain.TargetEnv;
import org.jsmart.zerocode.core.runner.ZeroCodePackageRunner;
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner;
import org.jsmart.zerocode.zerocodejavaexec.pojo.DbResult;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@TargetEnv("application_host.properties")
@RunWith(ZeroCodeUnitRunner.class)
public class DbSqlExecutorTest {

    @Test
    @Scenario("db_tests/postgres_db_get_parkrunners_test.json")
    public void tetPostGresDB_using_postGresDB() throws Exception{
        System.out.println("test is starting");

    }

    /*@Test
    public void testJavaMethod_exec() throws JsonProcessingException {
        DbSqlExecutor executor = new DbSqlExecutor();

        // ---------------------------------------------------------------------------
        // Call to the setters - Only needed during Unit testing
        // not needed while running via `@RunWith(ZeroCodeUnitRunner.class)`, because
        // the framework sets the values from the `@TargetEnv("my_web_app.properties")`
        // via Guice injection
        // ---------------------------------------------------------------------------
        executor.setDbUserName("localappuser");
        executor.setDbPassword("pass00rd");

        List<Map<String, Object>> resultMap = executor.fetchDbRecords("select id, name from customers");

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(resultMap);

        assertThat(json, is("{\"results\":[{\"id\":1,\"name\":\"Elon Musk\"},{\"id\":2,\"name\":\"Jeff Bezos\"}]}"));
    }*/
}