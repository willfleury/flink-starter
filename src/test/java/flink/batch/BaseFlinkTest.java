package flink.batch;


import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.test.util.AbstractTestBase;
import org.junit.After;
import org.junit.Before;

public abstract class BaseFlinkTest extends AbstractTestBase {

    private ExecutionEnvironment env;

    public BaseFlinkTest() {
        super(new Configuration());
    }

    @Before
    public void setupBase() throws Exception {
        startCluster();

        // if we want to re-use with more control either do
        // createLocalEnvironment() or
        // extend MultipleProgramsTestBase
        env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
    }

    @After
    public void stopBase() throws Exception {
        stopCluster();
    }

    protected ExecutionEnvironment getEnv() {
        return env;
    }
}
