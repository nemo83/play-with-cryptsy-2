package models

import scala.concurrent.duration.Duration

case class TradesStatistic(min: BigDecimal,
                           max: BigDecimal,
                           avg: BigDecimal,
                           std: BigDecimal,
                           numTransactions: Long,
                           btcVolume: BigDecimal,
                           duration: Option[Duration])
