package services.poloniex.actors

import akka.actor.Actor
import com.google.inject.Inject
import services.poloniex.MarketService
import services.poloniex.MarketService.TradeHistoryQuery
import services.poloniex.actors.TradeHistoryActor.PoloniexTradeHistoryMessage

class TradeHistoryActor @Inject()(marketService: MarketService) extends Actor {

  override def receive: Receive = {
    case message: PoloniexTradeHistoryMessage =>
      val tradeHistory = marketService.getTradeHistory(message.currencyFrom,
        message.currencyTo,
        message.tradeHistoryQueryOpt)

  }

}

object TradeHistoryActor {

  case class PoloniexTradeHistoryMessage(currencyFrom: String, currencyTo: String, tradeHistoryQueryOpt: Option[TradeHistoryQuery] = None)

}
