package services.math

class Regression {

  // Normalize
  //  def normalizeValues(values):
  //  def normalize(value, min, scalingFactor):
  //  return (value - min) / scalingFactor if scalingFactor != 0 else (value - min)
  //
  //  minValue = min(values)
  //  scalingFactor = max(values) - minValue
  //  normalizedValues = [normalize(value, minValue, scalingFactor) for value in values]
  //  return normalizedValues, minValue, scalingFactor


  case class ScaledMeasure[T](scaledValues: List[T], initialValue: T, scalingFactor: Double)

  /**
    * Normalization process assumes all values are positive!
    * @param times
    * @param amounts
    */
  def normalize(times: List[Long], amounts: List[Double]) = {

    val initialTime = times.min
    val timeScalingFactor = times.max - initialTime
    val scaledTimes = times.map(time => if (time == initialTime) 0 else (time - initialTime) / timeScalingFactor)


    // return normalisedTimes, normalisedAmounts
    // return baseNormalisedTime, scalingFactor
    // return baseNormalisedAmount, scalingFactor

  }

  def linearRegression(times: List[Double], amounts: List[Double]) = {

    val n = times.size

    val timeAvg = times.sum / n.toDouble
    val amountAvg = amounts.sum / n.toDouble

    (0 until n).foreach(i =>
    )



  }


}

object Regression {

  def main(args: Array[String]) {
    (0 until 5).foreach(println)
  }

}
