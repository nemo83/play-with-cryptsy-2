package controllers

import com.google.inject.Inject
import play.api.mvc._
import services.AccountService

import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(accountService: AccountService) extends Controller {

  def index = Action {

    accountService.getAccountInfo().map(response => println(response.body))

    Ok(views.html.index("Your new application is ready."))
  }

}
