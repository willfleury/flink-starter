package flink.batch;

import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;

public class LargePartition {

    public static void main(String[] args) throws Exception {
        ParameterTool params = ParameterTool.fromArgs(args);

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        new LargePartition(env).run(params);
    }

    private final ExecutionEnvironment env;

    public LargePartition(ExecutionEnvironment env) {
        this.env = env;
    }

    public void run(ParameterTool params) throws Exception {
        DataSet<String> text = env.readTextFile(params.getRequired("input"));

        text.map(new Rich()).writeAsText(params.getRequired("output"));

        env.execute("Large File Job");
    }

    private static class Rich extends RichMapFunction<String, String> {

        private IntCounter c = new IntCounter();

        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            getRuntimeContext().addAccumulator("int", c);
        }

        @Override
        public String map(String value) throws Exception {
            c.add(1);
            return value;
        }
    }
}
