package services.poloniex

import com.google.inject.{Inject, Singleton}
import models.poloniex.TradeHistory
import org.joda.time.DateTime
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MarketService @Inject()(ws: WSClient) extends PoloniexClient {

  //  Call: https://poloniex.com/public?command=returnTradeHistory&currencyPair=BTC_NXT&start=1410158341&end=1410499372

  case class TradeHistoryQuery(from: DateTime, to: DateTime)

  def getTradeHistory(currencyFrom: String, currencyTo: String, tradeHistoryQueryOpt: Option[TradeHistoryQuery] = None): Future[List[TradeHistory]] = {
    val currencyPair = s"${currencyFrom}_${currencyTo}"
    val foo = ws.url(s"$basePoloniexApiUrl")
      .withQueryString(("command", "returnTradeHistory"), ("currencyPair", currencyPair))
    println(s"foo: $foo")
    foo.get.map(_.json.as[List[TradeHistory]])
  }


}
