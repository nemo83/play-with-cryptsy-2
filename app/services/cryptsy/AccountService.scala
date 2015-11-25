package services.cryptsy

import com.google.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.libs.ws.WSClient

@Singleton
class AccountService @Inject()(configuration: Configuration,
                               ws: WSClient) extends CryptsyClient {

  val publicKey = configuration.getString("cryptsy.key.public").get

  val secretKey = configuration.getString("cryptsy.key.secret").get

  def getAccountInfo() = {

    val queryParams = Map("method" -> "getinfo") ++ baseQueryParams

    val body = queryParams.map { case (param, value) => s"$param=$value" }.mkString("&")
    val signature = signRequest(body, secretKey)

    ws.url(s"$apiV2BaseUrl")
      .withBody(body)
      .withHeaders(("Key", publicKey), ("Sign", signature))
      .post(body)

  }

}
