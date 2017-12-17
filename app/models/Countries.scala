package models

import javax.inject.Inject

import anorm.SqlParser._
import anorm._

import play.api.db.DBApi

case class Country(id: Option[Int] = None,
                   code: String,
                   name: String,
                   continent: Option[String],
                   wikipedia_link: Option[String],
                   keywords: Option[String]
                  )


@javax.inject.Singleton
class CountriesService @Inject()(dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple = {
    get[Option[Int]]("countries.id") ~
      get[String]("countries.code") ~
      get[String]("countries.name") ~
      get[Option[String]]("countries.continent") ~
      get[Option[String]]("countries.wikipedia_link") ~
      get[Option[String]]("countries.keywords") map {
      case id ~ code ~ name ~ continent ~ wikipedia_link ~ keywords =>
        Country(id, code, name, continent, wikipedia_link, keywords)
    }
  }

}


