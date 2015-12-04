package services.cryptsy

import models._
import models.cryptsy.{MarketsVolume, TradeHistory, TradeHistoryDetail}
import org.joda.time.DateTime
import play.api.Play.current
import play.api.libs.ws.WS
import services.math.Math

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class MarketService extends CryptsyClient {

  def getMarketsVolume(): Future[List[MarketVolume]] = {

    val marketsVolumeExtFut = WS.url(s"${apiV2BaseUrl}/v2/markets/volume").get()

    val marketsVolumeFut = marketsVolumeExtFut.map(response =>
      response.json.as[MarketsVolume].marketVolumes.map(volume => MarketVolume(volume.id.toLong, volume.volume, volume.volumeBtc))
    )

    marketsVolumeFut.map(_.sortBy(-_.btcVolume))
  }

  def getMarketTrades(marketId: Long, inLast: Duration): Future[List[TradeHistoryDetail]] = {

    val stopMillis: Long = DateTime.now().getMillis
    val startMillis: Long = stopMillis - inLast.toMillis

    val stop: Long = stopMillis / 1000L
    val start: Long = startMillis / 1000L

    val marketsVolumeExtFut = WS.url(s"$apiV2BaseUrl/v2/markets/$marketId/tradehistory")
      //      .withQueryString(("start", start.toString), ("stop", stop.toString))
      .get()

    marketsVolumeExtFut.map(response => response.json.as[TradeHistory].tradeHistoryDetails)

  }

  def getTradesStatistics(marketTrades: List[TradeHistoryDetail], duration: Option[Duration] = None) = {

    val tradePrices = marketTrades.map(trade => BigDecimal(trade.tradePrice))
    val btcVolume = marketTrades.map(trade => BigDecimal(trade.total)).sum

    val timestamps = marketTrades.map(_.timestamp)
    val minTimestamp = timestamps.min
    val maxTimestamp = timestamps.max

    val transactionPerSecond = marketTrades.size.toDouble / (maxTimestamp - minTimestamp).toDouble

    TradesStatistic(tradePrices.min,
      tradePrices.max,
      Math.mean(tradePrices),
      Math.standardDeviation(tradePrices),
      marketTrades.size.toLong,
      transactionPerSecond * 60,
      btcVolume,
      duration)

  }


}
