package services

import java.lang.Math.sqrt

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class Math$Test extends Specification {

  "Math$Test" should {

    "mean" in {
      Math.mean(List(1, 2, 3)) should_== 2
    }

    "variance" in {
      Math.variance(List(1, 2, 3)) should_== 2.toDouble / 3.toDouble
    }

    "standardDeviation" in {
      Math.standardDeviation(List(1, 2, 3)) should_== sqrt(2.toDouble / 3.toDouble)
    }

  }

}
