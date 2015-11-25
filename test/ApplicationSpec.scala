import com.pusher.client.channel.ChannelEventListener
import models.cryptsy.MarketsVolume
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.ws.WS
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  val cryptsyApiUrl = "http://pubapi2.cryptsy.com"

  "Application" should {

    //    "send 404 on a bad request" in new WithApplication {

    //      val marketsFut = WS.url(s"$cryptsyApiUrl/api/v2/markets").get()
    //      val marketsFut = WS.url(s"http://pubapi.cryptsy.com/api.php?method=marketdatav2").get()
    //
    //      val markets = Await.result(marketsFut, Duration.Inf)
    //
    //      println(markets.body)

    //      route(FakeRequest(GET, "/boum")) must beSome.which(status(_) == NOT_FOUND)
    //    }


    "get all markets" in new WithApplication {

      case class Foo(marketId: Long, volume: Double, btcVolume: Double)

      //            val marketsFut = WS.url(s"http://pubapi2.cryptsy.com/api.php?method=marketdatav2").get()
      val marketsFut = WS.url(s"http://api.cryptsy.com/api/v2/markets/volume").get()
      //      val marketsFut = WS.url("http://pubapi2.cryptsy.com/api.php?method=singleorderdata&marketid=3")
      //        .withHeaders(
      //          "Accept-Encoding" -> "gzip, deflate, sdch"
      //        ).get()

      val markets = Await.result(marketsFut, Duration.Inf)

      val marketsVolume = markets.json.as[MarketsVolume]

      val volumes = marketsVolume.marketVolumes.map(marketVolume => Foo(marketVolume.id.toLong, marketVolume.volume, marketVolume.volumeBtc))

      volumes.sortBy(_.btcVolume).foreach(volume => println(s"Market id: ${volume.marketId}, BTC Volume: ${volume.btcVolume}"))

    }

  }


  //  "Bla" should {
  //
  //    "something" in new WithApplication {

  //      val pusher = new Pusher("cb65d0a7a72cd94adf1f")
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
  //      pusher.subscribe("trade.2", cryptsyListener, "message")
  //      pusher.subscribe("trade.3", cryptsyListener, "message")
  //      pusher.subscribe("ticker.2", cryptsyListener, "message")
  //      pusher.subscribe("ticker.3", cryptsyListener, "message")
  //
  //      pusher.connect
  //
  //      Thread sleep 120000
  //
  //      pusher.disconnect


  //    }

  //  }

  class CryptsyListener extends ChannelEventListener {

    override def onSubscriptionSucceeded(s: String): Unit = {
      println(s"Subscription SUCCEEDED: $s")
    }

    override def onEvent(channel: String, event: String, data: String): Unit = {
      println(s"channel: $channel, event: $event, data: $data")
    }

  }

}
