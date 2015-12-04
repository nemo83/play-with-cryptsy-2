package models

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

package object poloniex {

  sealed trait PoloniexMessage

  sealed trait PoloniexTickerMessage extends PoloniexMessage

  case class Data(rate: Double, dataType: String)

  case class Ticker(data: Data, messageType: String) extends PoloniexTickerMessage

  case class TradeHistory(globalTradeId: Long, tradeId: Long, date: String, tradeType: String, rate: String, amount: String, total: String) extends PoloniexMessage

//  case class CurrencyVolume(volumes: Map[String, Map[String, Double]], totalBTC: String, totalUSDT: String, totalXMR: String, totalXUSD: String) extends PoloniexMessage
//
//  implicit val currencyVolumeReader: Reads[CurrencyVolume] = (
//    (JsPath \ "globalTradeID").read[Long] and
//      (JsPath \ "tradeID").read[Long] and
//      (JsPath \ "date").read[String] and
//      (JsPath \ "type").read[String]
//    ) (CurrencyVolume.apply _)


  implicit val tradeHistory: Reads[TradeHistory] = (
    (JsPath \ "globalTradeID").read[Long] and
      (JsPath \ "tradeID").read[Long] and
      (JsPath \ "date").read[String] and
      (JsPath \ "type").read[String] and
      (JsPath \ "rate").read[String] and
      (JsPath \ "amount").read[String] and
      (JsPath \ "total").read[String]
    ) (TradeHistory.apply _)


  implicit val dataReader: Reads[Data] = (
    (JsPath \ "rate").read[Double] and
      (JsPath \ "type").read[String]
    ) (Data.apply _)

  implicit val tickerReader: Reads[Ticker] = (
    (JsPath \ "data").read[Data] and
      (JsPath \ "type").read[String]
    ) (Ticker.apply _)

}
