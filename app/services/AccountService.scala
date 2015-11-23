package services

import com.google.inject.Inject
import play.api.Configuration
import play.api.Play.current
import play.api.libs.ws.WS

class AccountService @Inject()(configuration: Configuration) extends CryptsyClient {

  val publicKey = configuration.getString("cryptsy.key.public").get

  val secretKey = configuration.getString("cryptsy.key.secret").get

  def getAccountInfo() = {

    val queryParams = Map("method" -> "getinfo") ++ baseQueryParams

    val body = queryParams.map { case (param, value) => s"$param=$value" }.mkString("&")
    val signature = signRequest(body, secretKey)

    val request = WS.url(s"$apiV2BaseUrl")
      .withBody(body)
      .withHeaders(("Key", publicKey), ("Sign", signature))
      .post(body)

  }

}
