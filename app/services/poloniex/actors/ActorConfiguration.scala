package services.poloniex.actors

import akka.actor.{ActorRef, Props}
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides}
import play.api.Play.current
import play.api.libs.concurrent.Akka

class ActorConfiguration extends AbstractModule {

  override def configure(): Unit = {

  }

  @Provides
  @Named("poloniexTradeHistoryActor")
  def dbOneRedisClient(): ActorRef = {
    Akka.system.actorOf(Props[TradeHistoryActor], "poloniex-trade-history-actor")
  }

}
