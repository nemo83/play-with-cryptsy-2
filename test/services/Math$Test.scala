package services

import java.lang.Math.sqrt

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import services.math

@RunWith(classOf[JUnitRunner])
class Math$Test extends Specification {

  "Math$Test" should {

    "mean" in {
      math.Math.mean(List(1, 2, 3)) should_== 2
    }

    "variance" in {
      math.Math.variance(List(1, 2, 3)) should_== 2.toDouble / 3.toDouble
    }

    "standardDeviation" in {
      math.Math.standardDeviation(List(1, 2, 3)) should_== sqrt(2.toDouble / 3.toDouble)
    }

  }

}
