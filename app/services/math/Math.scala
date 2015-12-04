package services.math

object Math {

  def mean[T](values: Traversable[T])(implicit n: Numeric[T]): Double = {
    n.toDouble(values.sum) / values.size.toDouble
  }

  def variance[T](items: Traversable[T])(implicit n: Numeric[T]): Double = {
    val itemMean = mean(items)
    val count = items.size
    val sumOfSquares = items.foldLeft(0.0d)((total, item) => {
      val itemDbl = n.toDouble(item)
      val square = math.pow(itemDbl - itemMean, 2)
      total + square
    })
    sumOfSquares / count.toDouble
  }

  def standardDeviation[T](items: Traversable[T])(implicit n: Numeric[T]): Double = {
    math.sqrt(variance(items))
  }

}
