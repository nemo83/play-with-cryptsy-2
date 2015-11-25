package controllers

import com.google.inject.Inject
import play.api.mvc._
import services.cryptsy.AccountService
import services.poloniex.MarketService

import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(accountService: AccountService,
                            poloniexMarketService: MarketService) extends Controller {

  def index = Action {

    accountService.getAccountInfo().map(response => println(response.body))

    val tradeHistory = poloniexMarketService.getTradeHistory("BTC", "ETH")

    tradeHistory.map(_.foreach(println))

    Ok(views.html.index("Your new application is ready."))
  }

}
