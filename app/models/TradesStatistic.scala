package models

import scala.concurrent.duration.Duration

case class TradesStatistic(min: BigDecimal,
                           max: BigDecimal,
                           avg: BigDecimal,
                           std: BigDecimal,
                           numTransactions: Long,
                           transactionPerMinute: Double,
                           btcVolume: BigDecimal,
                           duration: Option[Duration]) {

  val stdPct: BigDecimal = std / avg * BigDecimal(100)

  override def toString: String = s"min: $min, max: $max, avg: $avg, std: $std, stdPct: ${stdPct}, numTransactions: $numTransactions, TPM: $transactionPerMinute, btcVolume: $btcVolume, duration: $duration"

}