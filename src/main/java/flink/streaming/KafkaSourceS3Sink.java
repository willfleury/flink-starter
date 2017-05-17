package flink.streaming;

import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.fs.StringWriter;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

public class KafkaSourceS3Sink {

    public static void main(String[] args) throws Exception {

        final ParameterTool parameterTool = ParameterTool.fromArgs(args);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.getConfig().disableSysoutLogging();
        env.getConfig().setGlobalJobParameters(parameterTool);
//        HadoopUtils.getHadoopConfiguration()

        DataStream<String> parsed = env
                .addSource(new FlinkKafkaConsumer09<>(
                        parameterTool.getRequired("kafka.topic"),
                        new SimpleStringSchema(),
                        parameterTool.getProperties()));

        env.enableCheckpointing(Time.minutes(1).toMilliseconds(), CheckpointingMode.EXACTLY_ONCE);

        BucketingSink<String> sink = new BucketingSink<>("s3://flink-test/TEST");
        sink.setBucketer(new DateTimeBucketer("yyyy-MM-dd--HHmm"));
        sink.setWriter(new StringWriter<>()); //new Avro
        sink.setBatchSize(200);
        sink.setPendingPrefix("file-");
        sink.setPendingSuffix(".txt");
        parsed.print();
        parsed.addSink(sink).setParallelism(1);

        env.execute();

    }
}
