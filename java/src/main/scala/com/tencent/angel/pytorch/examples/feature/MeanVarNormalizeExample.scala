package com.tencent.angel.pytorch.examples.feature

import com.tencent.angel.pytorch.feature.normalize.MeanVarNormalize
import com.tencent.angel.spark.ml.core.ArgsUtil
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{SaveMode, SparkSession}

object MeanVarNormalizeExample {
  def main(args: Array[String]): Unit = {
    val params = ArgsUtil.parse(args)
    val featureInput = params.getOrElse("featureInput", "")
    val featureOutput = params.getOrElse("featureOutput", "")
    val format = params.getOrElse("format", "dense")
    val featureDim = params.getOrElse("featureDim", "-1").toInt

    assert(featureDim > 0)

    val ss = SparkSession.builder().getOrCreate()
    val schema = StructType(Seq(
      StructField("node", LongType),
      StructField("feature", StringType)))

    val input = ss.read
      .option("header", "false")
      .option("sep", "\t")
      .schema(schema)
      .csv(featureInput)

    val mv = new MeanVarNormalize()
    mv.setDataFormat(format)
    mv.setFeatureDim(featureDim)

    val df = mv.transform(input)
    df.write
      .mode(SaveMode.Overwrite)
      .option("header", "false")
      .option("delimiter", "\t")
      .csv(featureOutput)
  }


}