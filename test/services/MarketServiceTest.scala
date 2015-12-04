package services

import org.joda.time.DateTime
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test._
import services.cryptsy.MarketService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

@RunWith(classOf[JUnitRunner])
class MarketServiceTest extends Specification {

  val cryptsyApiUrl = "http://pubapi2.cryptsy.com"

  "MarketService" should {

    //    "get all markets and sort them" in new WithApplication {
    //
    //      val marketService = new MarketService
    //
    //      val marketsVolumeFut = marketService.getMarketsVolume()
    //
    //      val marketsVolume = Await.result(marketsVolumeFut, Duration.Inf)
    //
    //      val sortedMarketsVolume = marketsVolume.sortBy(-_.btcVolume)
    //
    //      marketsVolume must_== sortedMarketsVolume
    //
    //      marketsVolume.foreach(volume => println(s"Market Id: ${volume.marketId}, Volume in BTC: ${volume.btcVolume}"))
    //
    //    }
    //
    //    "get market trade history" in new WithApplication {
    //
    //      val marketService = new MarketService
    //
    //      val fooFut = marketService.getMarketTrades(3L, 1.hour)
    //
    //      val foo = Await.result(fooFut, Duration.Inf)
    //
    //      val stats = marketService.getTradesStatistics(foo, Some(1.hour))
    //
    //    }

    "get markets statistics" in new WithApplication {

      val marketService = new MarketService

      val marketsVolumeFut = marketService.getMarketsVolume()

      val marketsVolume = Await.result(marketsVolumeFut, Duration.Inf)

      val sortedMarketsVolume = marketsVolume.sortBy(-_.btcVolume)

      marketsVolume must_== sortedMarketsVolume

      val topMarkets = marketsVolume.filter(_.btcVolume > 0.001)

      topMarkets.foreach(volume => println(s"Market Id: ${volume.marketId}, Volume in BTC: ${volume.btcVolume}"))

      val marketTradesListFut = Future.sequence(topMarkets.map(market => marketService.getMarketTrades(market.marketId, 20.minutes)))

      val marketTrades = Await.result(marketTradesListFut, Duration.Inf)

      val tradesTimestamps = marketTrades.map(_.map(_.timestamp))
      val minTradeTimestamp = tradesTimestamps.map(_.min)
      val latestFirstTrade = minTradeTimestamp.max

      println("Earliest: " + new DateTime(latestFirstTrade * 1000))

      val fuckingTrades = marketTrades.map(marketService.getTradesStatistics(_, Some(20.minutes)))

      val marketStats = (topMarkets zip fuckingTrades).sortBy(-_._2.std)

      println("Markets and Stats")

      val (frequentMarket, lessFrequentMarket) = marketStats.partition(_._2.transactionPerMinute * 15 > 1)

      frequentMarket.foreach { case (market, stats) => println(s"Market id: ${market.marketId}, stats: $stats") }

      println()
      println("LFM")
      println()

      lessFrequentMarket.foreach { case (market, stats) => println(s"Market id: ${market.marketId}, stats: $stats") }

    }

  }

}

//object Mannaia {
//
//  def main(args: Array[String]) {
//    val foo = List(
//      List(4, 5, 6),
//      List(3),
//      List(5, 6)
//    ).map(_.min).max
//    println(s"Min: $foo")
//  }
//
//}