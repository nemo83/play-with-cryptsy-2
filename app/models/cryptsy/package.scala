package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

package object cryptsy {

  sealed trait CryptsyMessage

  case class TradeItem(price: String, quantity: String)

  case class TickerTrade(timestamp: Long, dateTime: String, marketId: String, topSell: TradeItem, topBuy: TradeItem)

  case class Ticker(channel: String, trade: TickerTrade) extends CryptsyMessage

  case class TradeDetails(tradeId: String, timestamp: Long, dateTime: String, marketId: String, marketName: String,
                          quantity: String, price: String, total: String, _type: String)

  case class Trade(channel: String, details: TradeDetails) extends CryptsyMessage

  case class Volume(id: String, volume: Double, volumeBtc: Double)

  case class MarketsVolume(success: Boolean, marketVolumes: List[Volume]) extends CryptsyMessage

  case class TradeHistoryDetail(tradeId: String, timestamp: Long, dateTime: String, initiateOrderType: String,
                                tradePrice: Double, quantity: Double, total: Double)

  case class TradeHistory(success: Boolean, tradeHistoryDetails: List[TradeHistoryDetail]) extends CryptsyMessage

  implicit val tradeHistoryDetailReader: Reads[TradeHistoryDetail] = (
    (JsPath \ "tradeid").read[String] and
      (JsPath \ "timestamp").read[Long] and
      (JsPath \ "datetime").read[String] and
      (JsPath \ "initiate_ordertype").read[String] and
      (JsPath \ "tradeprice").read[Double] and
      (JsPath \ "quantity").read[Double] and
      (JsPath \ "total").read[Double]
    ) (TradeHistoryDetail.apply _)

  implicit val tradeHistoryReader: Reads[TradeHistory] = (
    (JsPath \ "success").read[Boolean] and
      (JsPath \ "data").read[List[TradeHistoryDetail]]
    ) (TradeHistory.apply _)

  implicit val tradeItemReader: Reads[TradeItem] = (
    (JsPath \ "price").read[String] and
      (JsPath \ "quantity").read[String]
    ) (TradeItem.apply _)

  implicit val tradeTickerReader: Reads[TickerTrade] = (
    (JsPath \ "timestamp").read[Long] and
      (JsPath \ "datetime").read[String] and
      (JsPath \ "marketid").read[String] and
      (JsPath \ "topsell").read[TradeItem] and
      (JsPath \ "topbuy").read[TradeItem]
    ) (TickerTrade.apply _)

  implicit val tickerReader: Reads[Ticker] = (
    (JsPath \ "price").read[String] and
      (JsPath \ "trade").read[TickerTrade]
    ) (Ticker.apply _)

  implicit val tradeDetailsReader: Reads[TradeDetails] = (
    (JsPath \ "tradeid").read[String] and
      (JsPath \ "timestamp").read[Long] and
      (JsPath \ "datetime").read[String] and
      (JsPath \ "marketid").read[String] and
      (JsPath \ "marketname").read[String] and
      (JsPath \ "quantity").read[String] and
      (JsPath \ "price").read[String] and
      (JsPath \ "total").read[String] and
      (JsPath \ "type").read[String]
    ) (TradeDetails.apply _)

  implicit val tradeReader: Reads[Trade] = (
    (JsPath \ "channel").read[String] and
      (JsPath \ "trade").read[TradeDetails]
    ) (Trade.apply _)

  implicit val volumeReader: Reads[Volume] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "volume").read[Double] and
      (JsPath \ "volume_btc").read[Double]
    ) (Volume.apply _)

  implicit val marketsVolumeReader: Reads[MarketsVolume] = (
    (JsPath \ "success").read[Boolean] and
      (JsPath \ "data").read[List[Volume]]
    ) (MarketsVolume.apply _)

}
