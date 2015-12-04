package services.poloniex.actors

import akka.actor.ActorRef
import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Scheduler @Inject()(configuration: Configuration,
                          @Named("poloniexTradeHistoryActor") poloniexTradeHistoryActor: ActorRef) {

//  Akka.system.scheduler.schedule(0.microsecond, 1.hour, ResultingActor.resultingActor, ResultEvents)

}
