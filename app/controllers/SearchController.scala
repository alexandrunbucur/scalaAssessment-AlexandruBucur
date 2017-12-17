package controllers

import javax.inject.Inject

import play.api.i18n._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import views.html


class SearchController @Inject() (reportService: ReportService,countriesService: CountriesService,
                                airportsService: AirportsService,
                                runwaysService: RunwaysService,
                                val messagesApi: MessagesApi)
  extends Controller with I18nSupport{


  val Home = Redirect(routes.SearchController.list(0, 2, ""))

  val countryForm = Form(
    mapping(
      "id" -> ignored(None:Option[Int]),
      "code" -> nonEmptyText,
      "name" -> nonEmptyText,
      "continent" -> optional(text),
      "wikipedia_link" -> optional(text),
      "keywords" -> optional(text)
    )(Country.apply)(Country.unapply)
  )


  def index = Action { Home }


  def list(page: Int, orderBy: Int, filter: String) = Action { implicit request =>
    Ok(html.countries(
      reportService.list(page = page, orderBy = orderBy, filter = (filter+"%")),
      orderBy, filter
    ))
  }

  def listASC(page: Int, orderBy: Int) = Action { implicit request =>
    Ok(html.reportAS(
      reportService.listASC(page = page, orderBy = orderBy),
      orderBy
    ))
  }

  def listDESC(page: Int, orderBy: Int) = Action { implicit request =>
    Ok(html.reportAS(
      reportService.listDESC(page = page, orderBy = orderBy),
      orderBy
    ))
  }

  def listCommon(page: Int, orderBy: Int) = Action { implicit request =>
    Ok(html.runways(
      reportService.listCommon(page = page, orderBy = orderBy),
      orderBy
    ))
  }
  def listRunwayTypesByCountry(page: Int, orderBy: Int) = Action { implicit request =>
    Ok(html.countryRw(
      reportService.listRwTypesByCountry(page = page, orderBy = orderBy),
      orderBy
    ))
  }


}
