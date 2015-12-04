package controllers

import com.google.inject.Inject
import play.api.mvc._
import services.cryptsy.AccountService
import services.poloniex.MarketService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject()(accountService: AccountService,
                            poloniexMarketService: MarketService) extends Controller {

  val cointFormatter = new java.text.DecimalFormat("#0.0#######")

  def index = Action {

    //    accountService.getAccountInfo().map(response => println(response.body))

    //    val tradeHistory = poloniexMarketService.getTradeHistory("BTC", "ETH")

    //    tradeHistory.map(_.foreach(println))

    poloniexMarketService.get24HoursVolume().map { markets =>

      val (highVolumeMarkets, lowVolumeMarkets) = markets.toList.filter(_.primary == "BTC").sortBy(-_.volumePrimary).partition(_.volumePrimary >= 0.01)

      println("Hi Volume Mkts")
      highVolumeMarkets.foreach(println)

      println

      println("Low Volume Mkts")
      lowVolumeMarkets.foreach(println)

      println()
      println("--------")

      val marketsVolumesFutList = highVolumeMarkets.map { market =>
        poloniexMarketService.getTradeHistory(market.secondary)
      }

      println("a")

      val marketsTradesFut = Future.sequence(marketsVolumesFutList)

      println("b")

      marketsTradesFut.map { marketsTrades =>

        val tradesStatistics = marketsTrades.map(poloniexMarketService.getTradesStatistics)

        println(s"${tradesStatistics.size} === ${highVolumeMarkets.size} ??")

        val suggestedCoins = (highVolumeMarkets zip tradesStatistics)
          .filter(_._2.transactionPerMinute > (30d / 60d))
          .filter(_._2.stdPct > 0.2d)
          .sortBy(-_._2.transactionPerMinute)


        suggestedCoins.foreach(currency => println(s"Coin: ${currency._1.secondary} - ${currency._2}"))

        println("========")


        suggestedCoins.foreach(currency =>
          println(s"Coin: ${currency._1.secondary}, buy price: ${cointFormatter.format(currency._2.avg - currency._2.std)}, sell price: ${cointFormatter.format(currency._2.avg + currency._2.std)}"))

      }


    }


    Ok(views.html.index("Your new application is ready."))
  }

}
