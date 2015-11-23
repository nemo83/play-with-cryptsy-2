import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.connection.{ConnectionEventListener, ConnectionState, ConnectionStateChange}
import com.pusher.client.{Pusher, PusherOptions}

/**
  * Created by giovanni on 17/11/2015.
  */
object Main {

  def main(args: Array[String]) {

    val options = new PusherOptions
    options.setEncrypted(true)


    val pusher = new Pusher("cb65d0a7a72cd94adf1f", options)

    pusher.connect(new ConnectionEventListener {
      override def onError(message: String, code: String, e: Exception): Unit = {
        println(s"Error, message: $message, code: $code")
        e.printStackTrace
      }

      override def onConnectionStateChange(connectionStateChange: ConnectionStateChange): Unit = {
        println(s"State changed to ${connectionStateChange.getCurrentState}, from ${connectionStateChange.getPreviousState}")
      }
    }, ConnectionState.ALL);

    val trades = pusher.subscribe("trade.3")
    val ticker = pusher.subscribe("ticker.3")

    trades.bind("message", new SubscriptionEventListener {
      override def onEvent(channel: String, event: String, data: String): Unit = {
        println(s"Trades: event with data: channel: $channel, event: $event, data: $data")
      }
    })

    ticker.bind("message", new SubscriptionEventListener {
      override def onEvent(channel: String, event: String, data: String): Unit = {
        println(s"Ticker: event with data: channel: $channel, event: $event, data: $data")
      }
    })

    var i = 0

    pusher.disconnect

    Thread sleep 2000

    pusher.connect

    while (i < 60) {
      Thread sleep 1000
      i += 1
    }

    pusher.disconnect
  }

}
