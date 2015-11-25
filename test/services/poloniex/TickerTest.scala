package services.poloniex

import com.pusher.client.channel.ChannelEventListener
import org.jfarcand.wcs
import org.jfarcand.wcs.MessageListener
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class TickerTest extends Specification {

//  https://github.com/backchatio/hookup/blob/master/src/main/scala/io/backchat/hookup/examples/PubSubClient.scala
//  https://github.com/backchatio/hookup/blob/master/src/main/scala/io/backchat/hookup/examples/PubSubClient.scala

  "Bla" >> {

    //    "something" >> {
    //
    //      val pusherOptions = new PusherOptions().setHost("api.poloniex.com")
    //
    //      val pusher = new Pusher("cb65d0a7a72cd94adf1f", pusherOptions)
    //
    //      pusher.connect(new ConnectionEventListener {
    //        override def onError(message: String, code: String, e: Exception): Unit = {
    //          println(s"Error, message: $message, code: $code")
    //          e.printStackTrace
    //        }
    //
    //        override def onConnectionStateChange(connectionStateChange: ConnectionStateChange): Unit = {
    //          println(s"State changed to ${connectionStateChange.getCurrentState}, from ${connectionStateChange.getPreviousState}")
    //        }
    //      }, ConnectionState.ALL);
    //
    //      val cryptsyListener = new CryptsyListener
    //
    //      pusher.subscribe("BTC_ETH", cryptsyListener, "message")
    //
    //      pusher.connect
    //
    //      Thread sleep 120000
    //
    //      pusher.disconnect
    //
    //      0 must_== 0
    //    }

    "something else" >> {

      wcs.WebSocket().open("ws://api.poloniex.com")
        .listener(new MessageListener {
          override def onMessage(message: String) {
            println(message)
          }
        })

      Thread sleep 30000

      0 should_== 0
    }

  }

  class CryptsyListener extends ChannelEventListener {

    override def onSubscriptionSucceeded(s: String): Unit = {
      println(s"Subscription SUCCEEDED: $s")
    }

    override def onEvent(channel: String, event: String, data: String): Unit = {
      println(s"channel: $channel, event: $event, data: $data")
    }

  }

}
