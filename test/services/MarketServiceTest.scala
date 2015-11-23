package services

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

@RunWith(classOf[JUnitRunner])
class MarketServiceTest extends Specification {

  val cryptsyApiUrl = "http://pubapi2.cryptsy.com"

  "MarketService" should {

    "get all markets and sort them" in new WithApplication {

      val marketService = new MarketService

      val marketsVolumeFut = marketService.getMarketsVolume()

      val marketsVolume = Await.result(marketsVolumeFut, Duration.Inf)

      val sortedMarketsVolume = marketsVolume.sortBy(-_.btcVolume)

      marketsVolume must_== sortedMarketsVolume

      marketsVolume.foreach(volume => println(s"Market Id: ${volume.marketId}, Volume in BTC: ${volume.btcVolume}"))

    }

    "get market trade history" in new WithApplication {

      val marketService = new MarketService

      val fooFut = marketService.getMarketTrades(3L, 1.hour)

      val foo = Await.result(fooFut, Duration.Inf)

      val stats = marketService.getTradesStatistics(foo, Some(1.hour))

    }

    "get markets statistics" in new WithApplication {

      val marketService = new MarketService

      val marketsVolumeFut = marketService.getMarketsVolume()

      val marketsVolume = Await.result(marketsVolumeFut, Duration.Inf)

      val sortedMarketsVolume = marketsVolume.sortBy(-_.btcVolume)

      marketsVolume must_== sortedMarketsVolume

      val topMarkets = marketsVolume.take(10)

      topMarkets.foreach(volume => println(s"Market Id: ${volume.marketId}, Volume in BTC: ${volume.btcVolume}"))

      val marketTradesListFut = Future.sequence(topMarkets.map(market => marketService.getMarketTrades(market.marketId, 10.minutes)))

      val marketTrades = Await.result(marketTradesListFut, Duration.Inf)

      val marketStats = (topMarkets zip marketTrades.map(marketService.getTradesStatistics(_, Some(10.minutes)))).sortBy(-_._2.std)

      println("Markets and Stats")

      marketStats.foreach { case (market, stats) => println(s"Market id: ${market.marketId}, stats: $stats") }

    }

  }

}
