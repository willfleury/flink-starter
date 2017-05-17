import org.apache.flink.api.common.time.Time
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.fs.StringWriter
import org.apache.flink.streaming.connectors.fs.bucketing.{BucketingSink, DateTimeBucketer}

object FileSourceS3SinkScala {

  def main(args: Array[String]) : Unit = {

    val params = ParameterTool.fromArgs(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.getConfig.setGlobalJobParameters(params)

    val parsed = env
      .readTextFile(params.getRequired("input"))

    env.enableCheckpointing(Time.minutes(1).toMilliseconds, CheckpointingMode.EXACTLY_ONCE)
//    env.setStateBackend(new FsStateBackend("hdfs://namenode:40010/flink/checkpoints"))
//    env.setStateBackend(new RocksDBStateBackend("hdfs://namenode:40010/flink/checkpoints"))

    val sink = new BucketingSink[String](params.getRequired("output")) //s3://flink-test/TEST
    sink.setBucketer(new DateTimeBucketer[String]("yyyy-MM-dd--HHmm"))
    sink.setWriter(new StringWriter[String]) //new Avro
    sink.setBatchSize(1024*1024)
    sink.setPendingPrefix("file-")
    sink.setPendingSuffix(".txt")

    parsed.addSink(sink).setParallelism(1)

    env.execute("Kafka stream")
  }
}