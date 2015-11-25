package services.poloniex

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class MarketServiceTest extends Specification {

  "Market Service" should {

    "get list of trade history" in new WithApplication {

      val marketService = new MarketService(WsTestClient.)

    }

  }

}
