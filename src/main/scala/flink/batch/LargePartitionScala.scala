package flink.batch

import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.configuration.Configuration

object LargePartitionScala {

  def main(args: Array[String]) : Unit = {

    val params = ParameterTool.fromArgs(args)
    val env = ExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(10)

    val text = env.readTextFile(params.getRequired("input"))

    text
      .map(new Rich)
      .writeAsText(params.getRequired("output"))

    val res = env.execute("Large File Job")
  }

  private class Rich extends RichMapFunction[String, String] {
    private val c = new IntCounter

    @throws[Exception]
    override def open(parameters: Configuration) {
      super.open(parameters)
      getRuntimeContext.addAccumulator("int", c)
      getRuntimeContext.getExecutionConfig.getGlobalJobParameters
    }

    @throws[Exception]
    def map(value: String): String = {
      val name = getRuntimeContext.getTaskName
      val attempt = getRuntimeContext.getAttemptNumber
      c.add(1)
      value
    }
  }
}