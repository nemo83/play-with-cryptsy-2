package services.cryptsy

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import org.joda.time.DateTime
import play.api.libs.Codecs

trait CryptsyClient {

  protected def apiV2BaseUrl = "http://api.cryptsy.com/api"

  def nonce: String = (DateTime.now.getMillis / 1000).toString

  def baseQueryParams = Map("nonce" -> this.nonce)

  def signRequest(requestParameters: String, privateKey: String): String = {

    val sha512_HMAC = Mac.getInstance("HmacSHA512")

    val secretkey = new SecretKeySpec(privateKey.getBytes("UTF-8"), "HmacSHA512")

    sha512_HMAC.init(secretkey);

    val mac_data = sha512_HMAC.doFinal(requestParameters.getBytes("UTF-8"));

    Codecs.toHex(mac_data).mkString

  }

}
