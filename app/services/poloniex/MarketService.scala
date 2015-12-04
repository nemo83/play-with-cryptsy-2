package services.poloniex

import com.google.inject.{Inject, Singleton}
import models.TradesStatistic
import models.poloniex.TradeHistory
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.ws.WSClient
import services.math.Math
import services.poloniex.MarketService.{CurrencyVolume, TradeHistoryQuery}

import scala.collection.Set
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MarketService @Inject()(ws: WSClient) extends PoloniexClient {

  /**
    * Poloniex Fee = 0.2%
    */

  private val command: String = "command"

  def getTradeHistory(currencyTo: String, currencyFrom: String = "BTC", tradeHistoryQueryOpt: Option[TradeHistoryQuery] = None): Future[List[TradeHistory]] = {
    ws.url(s"$basePoloniexApiUrl")
      .withQueryString((command, "returnTradeHistory"), ("currencyPair", s"${currencyFrom}_${currencyTo}"))
      .get
      .map(_.json.as[List[TradeHistory]])
  }

  def get24HoursVolume(): Future[Set[CurrencyVolume]] = {
    ws.url(s"$basePoloniexApiUrl")
      .withQueryString((command, "return24hVolume"))
      .get
      .map(response => getCurrencyVolume(response.json))
  }

  def getTradesStatistics(tradeHistory: Seq[TradeHistory]): TradesStatistic = {

      val dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

      case class TypedTradeHistory(rate: Double, amount: Double, timestamp: DateTime)

      def toDateTime(date: String): DateTime = {

        DateTime.parse(date, dateFormatter)

      }

      val trades = tradeHistory.map(trade => TypedTradeHistory(trade.rate.toDouble, trade.amount.toDouble, toDateTime(trade.date))).toList

      val tradeTimestamps = trades.map(_.timestamp.getMillis / 1000L)

      val tradeRates = trades.map(_.rate)
      val tradeAmounts = trades.map(trade => trade.rate * trade.amount)

      val transactionPerSecond = trades.size.toDouble / (tradeTimestamps.max - tradeTimestamps.min).toDouble

      TradesStatistic(
        tradeRates.min,
        tradeRates.max,
        Math.mean(tradeRates),
        Math.standardDeviation(tradeRates),
        tradeHistory.size,
        transactionPerSecond * 60,
        tradeAmounts.sum,
        None
      )

  }

  private def getCurrencyVolume(json: JsValue): Set[CurrencyVolume] = {
    val volumes = json.as[JsObject]
    val markets = volumes.keys
    val realMarkets = markets.filter(_.contains("_"))
    val marketVolumePayload = realMarkets.map(marketName => (json \ marketName).as[Map[String, String]])
    marketVolumePayload.map { map =>
      val keys = map.keys.toList
      val values = map.values.toList
      CurrencyVolume(keys(0), keys(1), values(0).toDouble, values(1).toDouble)
    }
  }

}

object MarketService {

  case class TradeHistoryQuery(from: DateTime, to: DateTime)

  case class CurrencyVolume(primary: String, secondary: String, volumePrimary: Double, volumeSecondary: Double) {
    override def toString: String = s"primary: $primary, secondary: $secondary, volumePrimary: $volumePrimary, volumeSecondary: $volumeSecondary"
  }

}
